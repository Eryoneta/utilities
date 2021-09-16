package element.tree;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import element.tree.propriedades.Cor;
import element.tree.objeto.modulo.ModuloUI;
public class Selecao{
//STATES
	public enum State{
		TO_UNSELECT,
		TO_SELECT,
		TO_CREATE,
		TO_CREATE_SON,
		TO_CREATE_PAI,
		TO_DELETE;
		public boolean is(Selecao.State... states){
			for(Selecao.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
	private State state=Selecao.State.TO_SELECT;
		public Selecao.State getState(){return state;}
		public void setState(Selecao.State state){this.state=state;}
//LOCAL
	private int ancoraXIndex=0;
		public int getAncoraXIndex(){return Math.min(ancoraXIndex,areaXIndex);}
		public int getAncoraX(){return (Math.min(ancoraXIndex,areaXIndex)+Tree.getLocalX())*Tree.UNIT;}
	private int ancoraYIndex=0;
		public int getAncoraYIndex(){return Math.min(ancoraYIndex,areaYIndex);}
		public int getAncoraY(){return (Math.min(ancoraYIndex,areaYIndex)+Tree.getLocalY())*Tree.UNIT;}
	public void setAncoraIndex(int x,int y){
		ancoraXIndex=x;
		ancoraYIndex=y;
	}
//FORM
	private int areaXIndex=0;
		public int getAreaXIndex(){return Math.max(ancoraXIndex,areaXIndex)-Math.min(ancoraXIndex,areaXIndex);}
		public int getAreaX(){return (Math.max(ancoraXIndex,areaXIndex)-Math.min(ancoraXIndex,areaXIndex))*Tree.UNIT;}
	private int areaYIndex=0;
		public int getAreaYIndex(){return Math.max(ancoraYIndex,areaYIndex)-Math.min(ancoraYIndex,areaYIndex);}
		public int getAreaY(){return (Math.max(ancoraYIndex,areaYIndex)-Math.min(ancoraYIndex,areaYIndex))*Tree.UNIT;}
	public void setAreaIndex(int x,int y){
		areaXIndex=x;
		areaYIndex=y;
	}
	public Rectangle getFormIndex(){return new Rectangle(getAncoraXIndex(),getAncoraYIndex(),Math.max(getAreaXIndex(),1),Math.max(getAreaYIndex(),1));}
	public Rectangle getForm(){return new Rectangle(getAncoraX(),getAncoraY(),Math.max(getAreaX(),1),Math.max(getAreaY(),1));}
	public boolean intersects(Rectangle forma){return getForm().intersects(forma);}
	public boolean intersects(Path2D.Float forma){return forma.intersects(getForm());}
	public void setEmpty(){
		ancoraXIndex=0;
		ancoraYIndex=0;
		areaXIndex=0;
		areaYIndex=0;
	}
	public boolean isEmpty(){return(getAreaX()==0&&getAreaY()==0);}
//MAIN
	public Selecao(){}
//DRAW
	public void draw(Graphics2D imagemEdit){
		imagemEdit.setStroke(new BasicStroke(1));
		switch(getState()){
			case TO_SELECT:default:	imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.FUNDO,0.6f));	break;
			case TO_CREATE:			imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.CREATE,0.6f));	break;
			case TO_CREATE_SON:		imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.SON,0.6f));		break;
			case TO_CREATE_PAI:		imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.PAI,0.6f));		break;
			case TO_DELETE:			imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.DELETE,0.6f));	break;
		}
		imagemEdit.fillRect(getAncoraX(),getAncoraY(),getAreaX(),getAreaY());
		switch(getState()){
			case TO_SELECT:default:	imagemEdit.setColor(ModuloUI.Cores.FUNDO);	break;
			case TO_CREATE:			imagemEdit.setColor(ModuloUI.Cores.CREATE);	break;
			case TO_CREATE_SON:		imagemEdit.setColor(ModuloUI.Cores.SON);		break;
			case TO_CREATE_PAI:		imagemEdit.setColor(ModuloUI.Cores.PAI);		break;
			case TO_DELETE:			imagemEdit.setColor(ModuloUI.Cores.DELETE);	break;
		}
		imagemEdit.drawRect(getAncoraX(),getAncoraY(),getAreaX(),getAreaY());
	}
}