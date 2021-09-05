package element.tree.popup.color;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import element.tree.propriedades.Cor;
import element.tree.popup.Item;
import element.tree.popup.Popup;
import element.tree.objeto.modulo.Modulo;
import element.tree.Tree;
@SuppressWarnings("serial")
public class CorPick extends Item{
//MÓDULOS SELECIONADOS
	private List<Modulo>selectedMods;
		public void setSelectedMods(List<Modulo>selectedMods){this.selectedMods=selectedMods;}
		public List<Modulo>getSelectedMods(){return selectedMods;}
//PALETA DE CORES
	public static Cor[][]PALETA_DEFAULT=new Cor[][]{
		new Cor[]{new Cor(153,000,204),	new Cor(255,000,255),	new Cor(255,000,000),	new Cor(000,255,000),	new Cor(000,000,255),	new Cor(000,000,000)},
		new Cor[]{new Cor(153,102,000),	new Cor(255,214,205),	new Cor(255,100,000),	new Cor(000,200,027),	new Cor(000,153,204),	new Cor(100,100,100)},
		new Cor[]{null,					null,					new Cor(255,170,000),	new Cor(000,153,000),	new Cor(051,204,255),	new Cor(220,220,220)},
		new Cor[]{null,					null,					new Cor(255,255,000),	new Cor(000,102,000),	new Cor(000,255,255),	new Cor(255,255,255)}
	};
//BLOCOS DE CORES
	private List<List<CorBloco>>coresBlocos=new ArrayList<List<CorBloco>>();
	private CorBloco corBloco;
		public CorBloco getBlocoPrincipal(){return corBloco;}
//BLOCO DO COLORPICK
	private CorPickBloco corPickBloco;
//LISTENER: DISPARA QUANDO MODS TEM SUAS CORES MUDADAS
	private List<CorPickListener>corPickListeners=new ArrayList<CorPickListener>();
		public List<CorPickListener>getCorPickListeners(){return corPickListeners;}
		public void addCorPickListener(CorPickListener corPickListener){corPickListeners.add(corPickListener);}
		public void triggerCorPickListener(Cor[]oldCores,Cor newCor){
			for(CorPickListener colorPickListener:getCorPickListeners()){
				colorPickListener.colorModified(getSelectedMods().toArray(new Modulo[0]),oldCores,newCor);
			}
		}
//MAIN
	public CorPick(Tree tree,List<Modulo>selectedMods){
		super("",tree);
		setSelectedMods(selectedMods);
		setEnabled(false);	//IMPEDE BRILHO
		final Cor[][]paleta=CorPick.PALETA_DEFAULT;
		final int height=paleta.length;
		final int width=paleta[0].length;
		setPreferredSize(new Dimension(
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*width)+Popup.GRID_BORDA,
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*height)+Popup.GRID_BORDA+Popup.BLOCO_SIZE
		));
		for(int y=0;y<paleta.length;y++){
			coresBlocos.add(new ArrayList<CorBloco>());
			for(int x=0;x<paleta[y].length;x++){
				if(paleta[y][x]==null){
					if(corBloco==null){
						corBloco=new CorBloco(this,
								Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*x),
								Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*y),
								(Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*2,		//4X4
								new Cor(Color.WHITE)
						);
					}
					coresBlocos.get(y).add(corBloco);	//O BLOCO PRINCIPAL OCUPA TODOS OS ESPAÇOS NULL(MAS É DESENHADO VÁRIAS VEZES)
					continue;
				}
				coresBlocos.get(y).add(new CorBloco(this,
						Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*x),
						Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*y),
						Popup.BLOCO_SIZE+Popup.BLOCO_SPACE,
						paleta[y][x]
				));
			}
		}
		corPickBloco=new CorPickBloco(this,
				Popup.GRID_BORDA,
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*coresBlocos.size()),
				(Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*6,
				Popup.BLOCO_SIZE);
	}
@Override
	public void unfocusAll(){
		for(List<CorBloco>linha:coresBlocos)for(CorBloco bloco:linha)bloco.setFocus(false);
	}
//DRAW
@Override
	public void paint(Graphics imagemEdit){
		super.paint(imagemEdit);
		if(getSelectedMods().isEmpty())return;
		corBloco.setCor(getSelectedMods().get(getSelectedMods().size()-1).getCor());
		for(List<CorBloco>linha:coresBlocos)for(CorBloco bloco:linha)bloco.draw(imagemEdit);
		corPickBloco.draw(imagemEdit);
	}
}