package element.tree.undoRedo;
import java.awt.Point;
import element.tree.objeto.Objeto;
public class UndoRedoLocal extends UndoRedoNodo{
//LOCAL
	private Point diferenca;
		public Point getDiferenca(){return diferenca;}
//MÓDULOS/NÓDULOS
	private Objeto[]objs;
		public Objeto[]getObjetos(){return objs;}
		public Objeto getObjeto(int index){return objs[index];}
		public int getObjetosCount(){return objs.length;}
//MAIN
	public UndoRedoLocal(Objeto[]objs,Point diferenca){
		super(UndoRedoNodo.Tipo.LOCAL);
		this.diferenca=diferenca;
		this.objs=objs;
	}
//INVERT
	public UndoRedoLocal invert(){
		diferenca.x=-diferenca.x;
		diferenca.y=-diferenca.y;
		return this;
	}
}