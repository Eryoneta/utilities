package element.tree.undoRedo;
import element.tree.propriedades.Borda;
import element.tree.objeto.Objeto;
public class UndoRedoBorda extends UndoRedoNodo{
//BORDA
	private Borda[]bordas;
		public Borda getBorda(int index){return bordas[index];}
//MÓDULOS/CONEXÕES
	private Objeto[]objs;
		public Objeto[]getObjetos(){return objs;}
		public Objeto getObjeto(int index){return objs[index];}
		public int getObjetosCount(){return objs.length;}
//MAIN
	public UndoRedoBorda(Objeto[]objs,Borda[]bordas){
		super(UndoRedoNodo.Tipo.BORDA);
		this.bordas=bordas;
		this.objs=objs;
	}
//INVERT
	public UndoRedoBorda invert(Borda[]bordas){
		this.bordas=bordas;
		return this;
	}
}