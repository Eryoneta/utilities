package element.tree.popup.borda;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import element.tree.propriedades.Borda;
import element.tree.Tree;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.modulo.Modulo;
import element.tree.popup.Item;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class BordaPick extends Item{
//MÓDULOS/CONEXÕES SELECIONADOS
	private List<Modulo>selectedMods;
		public void setSelectedMods(List<Modulo>selectedMods){this.selectedMods=selectedMods;}
		public List<Modulo>getSelectedMods(){return selectedMods;}
	private List<Conexao>selectedCoxs;
		public void setSelectedCoxs(List<Conexao>selectedCoxs){this.selectedCoxs=selectedCoxs;}
		public List<Conexao>getSelectedCoxs(){return selectedCoxs;}
//BORDAS
	private List<List<BordaBloco>>bordasBlocos=new ArrayList<List<BordaBloco>>();
//LISTENER: DISPARA QUANDO MODS/COXS TEM SUA BORDA MUDADA
	private List<BordaPickListener>bordaPickListeners=new ArrayList<BordaPickListener>();
		public List<BordaPickListener>getBordaPickListeners(){return bordaPickListeners;}
		public void addBordaPickListener(BordaPickListener bordaPickListener){bordaPickListeners.add(bordaPickListener);}
		public void triggerBordaPickListener(Borda[]oldBordas,Borda newBorda){
			for(BordaPickListener bordaPickListener:getBordaPickListeners()){
				final Objeto[]savedMods=getSelectedMods().toArray(new Objeto[0]);
				final Objeto[]savedCoxs=getSelectedCoxs().toArray(new Objeto[0]);
				final Objeto[]savedObjs=Arrays.copyOf(savedMods,savedMods.length+savedCoxs.length);
				System.arraycopy(savedCoxs,0,savedObjs,savedMods.length,savedCoxs.length);
				bordaPickListener.bordaModified(savedObjs,oldBordas,newBorda);
			}
		}
//TOOLTIP
	private BordaBloco toolTipBloco;
		public BordaBloco getTooltipBloco(){return toolTipBloco;}
		public void setToolTipBloco(BordaBloco bloco){
			if(bloco==null){
				if(toolTipBloco==null||!toolTipBloco.isFocused()){
					setToolTipText(null);
				}
			}else{
				toolTipBloco=bloco;
				setToolTipText(toolTipBloco.getBorda().getNome());
			}
		}
	@Override
		public Point getToolTipLocation(MouseEvent m){
			if(getToolTipText()!=null){
				return new Point(0,-Popup.BLOCO_SIZE-5);	//O -5 EVITA QUE ELE ROUBE O MOUSE_EXIT DO POPUP
			}
			return null;
		}
//MAIN
	public BordaPick(Tree tree,List<Modulo>selectedMods,List<Conexao>selectedCoxs){
		super("",tree);
		setSelectedMods(selectedMods);
		setSelectedCoxs(selectedCoxs);
		setEnabled(false);	//IMPEDE BRILHO
		final Borda[]bordas=Borda.getAllBordas();
		final double largura=Math.sqrt(bordas.length);
		int height=(int)Math.round(largura);
		int width=(largura<height?height:height+1);
		setPreferredSize(new Dimension(
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*width)+Popup.GRID_BORDA,
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*height)+Popup.GRID_BORDA)
		);
		for(int y=0,i=0;y<height;y++){
			bordasBlocos.add(new ArrayList<BordaBloco>());
			for(int x=0;x<width&&i<bordas.length;x++){
				bordasBlocos.get(y).add(new BordaBloco(this,
						Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*x),
						Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*y),
						Popup.BLOCO_SIZE+Popup.BLOCO_SPACE,
						bordas[i++]
				));
			}
		}
	}
//FUNCS
@Override
	public void unfocusAll(){
		for(List<BordaBloco>linha:bordasBlocos)for(BordaBloco bloco:linha)bloco.setFocus(false);
	}
//DRAW
@Override
	public void paint(Graphics imagemEdit){
		super.paint(imagemEdit);
		if(getSelectedMods().isEmpty()&&getSelectedCoxs().isEmpty())return;
		for(List<BordaBloco>linha:bordasBlocos)for(BordaBloco bloco:linha)bloco.draw(imagemEdit);
	}
}