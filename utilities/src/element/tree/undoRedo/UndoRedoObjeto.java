package element.tree.undoRedo;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import element.tree.objeto.Objeto;
public class UndoRedoObjeto extends UndoRedoNodo{
//MODO
	public enum Modo{
		ADD,
		DEL;
	}
	private Modo modo;
		public Modo getModo(){return modo;}
//MÓDULOS/CONEXÕES/NÓDULOS
	private Objeto[]toRepObjs;
		public Objeto[]getToReplaceObjetos(){return toRepObjs;}
		public Objeto getToReplaceObjeto(int index){return toRepObjs[index];}
		public int getToReplaceObjetosCount(){return toRepObjs.length;}
	private Objeto[]objs;
		public Objeto[]getObjetos(){return objs;}
		public Objeto getObjeto(int index){return objs[index];}
		public int getObjetosCount(){return objs.length;}
//MAIN
	public UndoRedoObjeto(Objeto[]objs,Modo modo){
		super(UndoRedoNodo.Tipo.OBJETO);
		this.modo=modo;
		this.objs=objs;
	}
	public UndoRedoObjeto(Objeto[]oldObjs,Objeto[]newObjs,Modo modo){
		super(UndoRedoNodo.Tipo.OBJETO);
		this.modo=modo;
		this.toRepObjs=oldObjs;
		this.objs=newObjs;
	}
//INVERT
	public UndoRedoObjeto invert(){
		final List<Objeto>objsLista=Arrays.asList(objs);	//INVERTER A ORDEM É IMPORTANTE PARA OS NODS
		Collections.reverse(objsLista);
		switch(getModo()){
			case ADD:
				modo=Modo.DEL;
				objs=objsLista.toArray(new Objeto[0]);
			break;
			case DEL:
				modo=Modo.ADD;
				objs=objsLista.toArray(new Objeto[0]);
			break;
//			case REPLACE:
//				toRepObjs=ListaObjeto.toList(objs).getAllOrdenedInverted().toArray(new Objeto[0]);
//				objs=ListaObjeto.toList(toRepObjs).getAllOrdened().toArray(new Objeto[0]);
//			break;
		}
		return this;
	}
}