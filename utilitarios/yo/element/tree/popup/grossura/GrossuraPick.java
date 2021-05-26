package element.tree.popup.grossura;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import element.tree.Grossura;
import element.tree.Tree;
import element.tree.objeto.conexao.Conexao;
import element.tree.popup.Item;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class GrossuraPick extends Item{
//CONEXÕES SELECIONADAS
	private List<Conexao>selectedCoxs;
		public void setSelectedCoxs(List<Conexao>selectedCoxs){this.selectedCoxs=selectedCoxs;}
		public List<Conexao>getSelectedCoxs(){return selectedCoxs;}
//GROSSURAS
	private List<GrossuraBloco>grossurasBlocos=new ArrayList<GrossuraBloco>();
//LISTENER: DISPARA QUANDO COXS TEM SUA GROSSURA MUDADA
	private List<GrossuraPickListener>grossuraPickListeners=new ArrayList<GrossuraPickListener>();
		public List<GrossuraPickListener>getGrossuraPickListeners(){return grossuraPickListeners;}
		public void addGrossuraPickListener(GrossuraPickListener grossuraPickListener){grossuraPickListeners.add(grossuraPickListener);}
		public void triggerGrossuraPickListener(Grossura[]oldGrossuras,Grossura newGrossura){
			for(GrossuraPickListener grossuraPickListener:getGrossuraPickListeners()){
				final Conexao[]savedCoxs=getSelectedCoxs().toArray(new Conexao[0]);
				grossuraPickListener.grossuraModified(savedCoxs,oldGrossuras,newGrossura);
			}
		}
//TOOLTIP
	private GrossuraBloco toolTipBloco;
		public void setToolTipBloco(GrossuraBloco bloco){
			if(bloco==null){
				if(toolTipBloco==null||!toolTipBloco.isFocused()){
					setToolTipText(null);
				}
			}else{
				toolTipBloco=bloco;
				setToolTipText(toolTipBloco.getGrossura().getNome());
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
	public GrossuraPick(Tree tree,List<Conexao>selectedCoxs){
		super("",tree);
		setSelectedCoxs(selectedCoxs);
		setEnabled(false);	//IMPEDE BRILHO
		final Grossura[]grossuras=Grossura.getAllGrossuras();
		final int height=grossuras.length;
		final int width=3;
		setPreferredSize(new Dimension(
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*width)+Popup.GRID_BORDA,
				Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*height)+Popup.GRID_BORDA)
		);
		for(int y=0,i=0;y<height;y++){
			grossurasBlocos.add(new GrossuraBloco(this,
					Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*0),
					Popup.GRID_BORDA+((Popup.BLOCO_SIZE+Popup.BLOCO_SPACE)*y),
					Popup.BLOCO_SIZE+Popup.BLOCO_SPACE,
					grossuras[i++]
			));
		}
	}
//FUNCS
@Override
	public void unfocusAll(){
		for(GrossuraBloco bloco:grossurasBlocos)bloco.setFocus(false);
	}
//DRAW
@Override
	public void paint(Graphics imagemEdit){
		super.paint(imagemEdit);
		if(getSelectedCoxs().isEmpty())return;
		for(GrossuraBloco bloco:grossurasBlocos)bloco.draw(imagemEdit);
	}
}
