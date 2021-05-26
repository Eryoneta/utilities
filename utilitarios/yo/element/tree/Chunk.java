package element.tree;
import java.awt.Point;
import java.awt.Rectangle;

import element.tree.objeto.ListaObjeto;
import element.tree.objeto.Objeto;
public class Chunk{
//VAR GLOBAIS
	public final static int SIZE=16;
//OBJETOS
	private ListaObjeto objetos=new ListaObjeto();
		public ListaObjeto getObjetos(){return objetos;}
		public void add(Objeto obj){objetos.add(obj);}
		public void del(Objeto obj){objetos.del(obj);}
		public boolean isEmpty(){return objetos.isEmpty();}
//LOCAL
	private int xIndex;
		public int getXIndex(){return xIndex;}
		public int getX(){return (Tree.getLocalX()+xIndex)*Tree.UNIT;}
	private int yIndex;
		public int getYIndex(){return yIndex;}
		public int getY(){return (Tree.getLocalY()+yIndex)*Tree.UNIT;}
	public Point getLocationIndex(){return new Point(getXIndex(),getYIndex());}
//FORMA
	public Rectangle getForm(){
		final int size=Tree.UNIT*Chunk.SIZE;
		final int borda=1;	//PARA INCLUIR COXS RETOS
		return new Rectangle(
				(xIndex*size)+(Tree.getLocalX()*Tree.UNIT)-borda,
				(yIndex*size)+(Tree.getLocalY()*Tree.UNIT)-borda,
				borda+size+borda,
				borda+size+borda
		);
	}
//MAIN
	public Chunk(int xIndex,int yIndex){
		this.xIndex=xIndex;
		this.yIndex=yIndex;
	}
}