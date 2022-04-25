package element.tree.objeto.nodulo;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import element.tree.objeto.conexao.ConexaoST;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Grossura;
public class NoduloUI{
//NODULO
	private Nodulo nod;
//MAIN
	public NoduloUI(Nodulo nod){this.nod=nod;}
//DRAW
	public void draw(Graphics2D imagemEdit,int unit){
		final float width=nod.getSize(unit);
		final float round=nod.getRoundValue(unit);
		float borda=nod.getSize(unit)/2;
		switch(nod.getConexao().getGrossura().getIndex()){
			case Grossura.THIN:				borda=nod.getSize(unit)/1;break;
			case Grossura.MEDIUM:default:	borda=nod.getSize(unit)/2;break;
			case Grossura.WIDE:				borda=nod.getSize(unit)/3;break;
			case Grossura.ULTRA_WIDE:		borda=nod.getSize(unit)/4;break;
		}
		drawBrilho(imagemEdit,unit,width,round,borda);
		drawPonto(imagemEdit,unit,width,round);
	}
		private void drawBrilho(Graphics2D imagemEdit,int unit,float size,float round,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(nod.getState().is(NoduloST.State.UNSELECTED,NoduloST.State.HIGHLIGHTED,NoduloST.State.TO_BE_CREATOR))return;
			imagemEdit.setColor(getBrilhoCor(nod.getState()));
			final float x=nod.getX(unit)-(size/2)-borda;
			final float y=nod.getY(unit)-(size/2)-borda;
			size+=borda*2;
			imagemEdit.fill(new RoundRectangle2D.Float(x,y,size,size,round,round));
		}
		private void drawPonto(Graphics2D imagemEdit,int unit,float size,float round){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			imagemEdit.setColor(getPontoCor(nod.getState()));
			final float x=nod.getX(unit)-(size/2);
			final float y=nod.getY(unit)-(size/2);
			imagemEdit.fill(new RoundRectangle2D.Float(x,y,size,size,round,round));
		}
	public Cor getBrilhoCor(NoduloST.State state){
		switch(nod.getState()){
			case SELECTED:default:	return nod.getConexao().getUI().getBrilhoCor(ConexaoST.State.SELECTED);
			case TO_BE_DELETED:		return nod.getConexao().getUI().getBrilhoCor(ConexaoST.State.TO_BE_DELETED);	
		}
	}
	public Cor getPontoCor(NoduloST.State state){
		switch(nod.getState()){
			case UNSELECTED:default:	return nod.getConexao().getUI().getLinhaCor(ConexaoST.State.UNSELECTED);
			case SELECTED:				return nod.getConexao().getUI().getLinhaCor(ConexaoST.State.SELECTED);
			case HIGHLIGHTED:			return nod.getConexao().getUI().getLinhaCor(ConexaoST.State.HIGHLIGHTED);
			case TO_BE_CREATOR:			return nod.getConexao().getUI().getLinhaCor(ConexaoST.State.TO_BE_CREATOR);
			case TO_BE_DELETED:			return nod.getConexao().getUI().getLinhaCor(ConexaoST.State.TO_BE_DELETED);
		}
	}
}