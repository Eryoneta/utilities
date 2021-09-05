package element.tree.popup.icon;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import element.tree.propriedades.Icone;
import element.tree.objeto.modulo.Modulo;
import element.tree.Tree;
import element.tree.popup.Item;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class IconePick extends Item{
//MÓDULOS
	private List<Modulo>selectedMods;
		public void setModSelec(List<Modulo>modSelec){this.selectedMods=modSelec;}
		public List<Modulo>getSelectedMods(){return selectedMods;}
//ICONES
	private List<List<IconeBloco>>iconesBlocos=new ArrayList<List<IconeBloco>>();
//LISTENER: DISPARA QUANDO MODS TEM SEUS ICONES MUDADOS
	private List<IconePickListener>iconePickListeners=new ArrayList<IconePickListener>();
		public List<IconePickListener>getIconePickListeners(){return iconePickListeners;}
		public void addIconePickListener(IconePickListener iconePickListener){iconePickListeners.add(iconePickListener);}
		public void triggerIconePickListener(Icone icone,boolean isAdding){
			if(isAdding){
				for(IconePickListener iconPickListener:getIconePickListeners()){
					iconPickListener.iconAdded(getSelectedMods().toArray(new Modulo[0]),icone);
				}
			}else{
				for(IconePickListener iconPickListener:getIconePickListeners()){
					iconPickListener.iconRemoved(getSelectedMods().toArray(new Modulo[0]),icone);
				}
			}
		}
//TOOLTIP
	private IconeBloco toolTipBloco;
		public void setToolTipBloco(IconeBloco bloco){
			if(bloco==null){
				if(toolTipBloco==null||!toolTipBloco.isFocused()){
					setToolTipText(null);
				}
			}else{
				toolTipBloco=bloco;
				setToolTipText(toolTipBloco.getIcone().getNome());
			}
		}
	@Override
		public Point getToolTipLocation(MouseEvent m){
			if(getToolTipText()!=null){
				return new Point(0,-Popup.BLOCO_SIZE-5);	//O -5 EVITA QUE ELE ROUBE O MOUSE_EXIT DO POPUP
			}
			return null;
		}
//VARS GLOBAIS
	public static List<Icone>ICONES=new ArrayList<Icone>();
//MAIN
	public IconePick(Tree tree,File iconesPasta,String iconeSubPasta,List<Modulo>modSelec,double minWidth){
		super("",tree);
		setModSelec(modSelec);
		setEnabled(false);	//IMPEDE BRILHO
		final List<Icone>icones=new ArrayList<Icone>();
		final File pasta=new File(iconesPasta.toString()+"/"+iconeSubPasta);
		for(File iconLink:pasta.listFiles()){
			if(iconLink.getName().endsWith(".png")){
				final Image imagem=Toolkit.getDefaultToolkit().getImage(iconLink.getPath());
				final String relativeLink=iconeSubPasta+"/"+iconLink.getName();
				final Icone icone=new Icone(imagem,iconesPasta,relativeLink);
				ICONES.add(icone);
				icones.add(icone);
			}
		}
		final double largura=Math.sqrt(icones.size());
		int height=(int)Math.round(largura);
		int width=(largura<height?height:height+1);
		minWidth/=Popup.BLOCO_SIZE;
		if(width<minWidth-1){
			width=(int)minWidth;
			height=(int)Math.ceil((double)icones.size()/width);
		}
		setPreferredSize(new Dimension(
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*width)+Popup.GRID_BORDA,
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*height)+Popup.GRID_BORDA)
		);
		for(int y=0,i=0;y<height;y++){
			iconesBlocos.add(new ArrayList<IconeBloco>());
			for(int x=0;x<width&&i<icones.size();x++){
				iconesBlocos.get(y).add(new IconeBloco(this,
						Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*x),
						Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*y),
						Popup.BLOCO_SIZE+Popup.BLOCO_SPACE,
						icones.get(i++)
				));
			}
		}
		final Graphics imagemEdit=new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).getGraphics();
		for(List<IconeBloco>linha:iconesBlocos)for(IconeBloco bloco:linha)bloco.draw(imagemEdit);	//DRAW PRIMEIRA APARIÇÃO, CARREGANDO AS IMAGENS
	}
//FUNCS
@Override
	public void unfocusAll(){
		for(List<IconeBloco>linha:iconesBlocos)for(IconeBloco bloco:linha)bloco.setFocus(false);
	}
//DRAW
@Override
	public void paint(Graphics imagemEdit){
		super.paint(imagemEdit);
		if(getSelectedMods().isEmpty())return;
		for(List<IconeBloco>linha:iconesBlocos)for(IconeBloco bloco:linha)bloco.draw(imagemEdit);
	}
}