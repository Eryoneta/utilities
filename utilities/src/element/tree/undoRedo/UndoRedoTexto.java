package element.tree.undoRedo;
import java.util.List;
import element.tree.objeto.Objeto;
public class UndoRedoTexto extends UndoRedoNodo{
//TEXTO
	private List<String>texto;
		public List<String>getTexto(){return texto;}
//MÓDULO/CONEXÃO
	private Objeto obj;
		public Objeto getObjeto(){return obj;}
//MAIN
	public UndoRedoTexto(Objeto obj,List<String>texto){
		super(UndoRedoNodo.Tipo.TEXTO);
		this.texto=texto;
		this.obj=obj;
	}
//INVERT
	public UndoRedoTexto invert(List<String>texto){
		this.texto=texto;
		return this;
	}
}