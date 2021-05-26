package element.tree.undoRedo;
import element.tree.Icone;
import element.tree.objeto.modulo.Modulo;
public class UndoRedoIcone extends UndoRedoNodo{
//MODO
	public enum Modo{
		ADD,
		DEL;
	}
	private Modo modo;
		public Modo getModo(){return modo;}
//ICONE
	private Icone icone;
		public Icone getIcone(){return icone;}
//MÓDULOS
	private Modulo[]mods;
		public Modulo[]getModulos(){return mods;}
		public Modulo getModulo(int index){return mods[index];}
		public int getModulosCount(){return mods.length;}
//MAIN
	public UndoRedoIcone(Modulo[]mods,Icone icone,Modo modo){
		super(UndoRedoNodo.Tipo.ICONE);
		this.modo=modo;
		this.icone=icone;
		this.mods=mods;
	}
//INVERT
	public UndoRedoIcone invert(){
		switch(getModo()){
			case ADD:	modo=Modo.DEL;	break;
			case DEL:	modo=Modo.ADD;	break;
		}
		return this;
	}
}