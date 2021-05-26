package element.tree.undoRedo;
public class UndoRedoNodo{
//TIPO
	public enum Tipo{
		LOCAL,
		TITULO,
		TEXTO,
		COR,
		BORDA,
		GROSSURA,
		ICONE,
		OBJETO;
		public boolean is(UndoRedoNodo.Tipo... tipos){
			for(UndoRedoNodo.Tipo tipo:tipos)if(this.equals(tipo))return true;
			return false;
		}
	}
	private Tipo tipo;
		public Tipo getTipo(){return tipo;}
//MAIN
	public UndoRedoNodo(Tipo tipo){
		this.tipo=tipo;
	}
}