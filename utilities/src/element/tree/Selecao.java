package element.tree;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import element.tree.main.Tree;
public class Selecao{
//ST
	private final SelecaoST ST=new SelecaoST(this);
		public SelecaoST getST(){return ST;}
		public SelecaoST.State getState(){return getST().getState();}
		public void setState(SelecaoST.State state){getST().setState(state);}
//UI
	private final SelecaoUI UI=new SelecaoUI(this);
		public SelecaoUI getUI(){return UI;}
		public void draw(Graphics2D imagemEdit){
			getUI().draw(imagemEdit);
		}
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
}