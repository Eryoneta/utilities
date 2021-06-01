package element.tree.undoRedo;
import element.tree.propriedades.Cor;
import element.tree.objeto.modulo.Modulo;
public class UndoRedoCor extends UndoRedoNodo{
//CORES
	private Cor[]cores;
		public Cor getCor(int index){return cores[index];}
//MÓDULOS
	private Modulo[]mods;
		public Modulo[]getModulos(){return mods;}
		public Modulo getModulo(int index){return mods[index];}
		public int getModulosCount(){return mods.length;}
//MAIN
	public UndoRedoCor(Modulo[]mods,Cor[]cores){
		super(UndoRedoNodo.Tipo.COR);
		this.cores=cores;
		this.mods=mods;
	}
//INVERT
	public UndoRedoCor invert(Cor[]cores){
		this.cores=cores;
		return this;
	}
}