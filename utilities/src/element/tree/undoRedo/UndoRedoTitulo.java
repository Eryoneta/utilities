package element.tree.undoRedo;
import element.tree.objeto.modulo.Modulo;
public class UndoRedoTitulo extends UndoRedoNodo{
//TÍTULO
	private String titulo;
		public String getTitulo(){return titulo;}
//MÓDULO
	private Modulo mod;
		public Modulo getModulo(){return mod;}
//MAIN
	public UndoRedoTitulo(Modulo mod,String titulo){
		super(UndoRedoNodo.Tipo.TITULO);
		this.titulo=titulo;
		this.mod=mod;
	}
//INVERT
	public UndoRedoTitulo invert(String titulo){
		this.titulo=titulo;
		return this;
	}
}