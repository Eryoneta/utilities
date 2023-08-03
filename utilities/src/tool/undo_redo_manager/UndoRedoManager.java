package tool.undo_redo_manager;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

public class UndoRedoManager {
//VARS GLOBAIS
	public static final int NO_LIMIT=-1;
//LIMITE DE UNDO/REDO
	private int doLimite=100;
		public int getDoLimite(){return doLimite;}
		public void setDoLimite(int limite){this.doLimite=limite;}
//LISTENER: DISPARA QUANDO UM UNDO/REDO É FEITO
	private List<UndoRedoListener>undoRedoListeners=new ArrayList<UndoRedoListener>();
		public void addUndoRedoListener(UndoRedoListener undoRedoListener){undoRedoListeners.add(undoRedoListener);}
		public void triggerUndoListener(){
			for(UndoRedoListener undoRedoListener:undoRedoListeners)undoRedoListener.actionUndone();
		}
		public void triggerRedoListener(){
			for(UndoRedoListener undoRedoListener:undoRedoListeners)undoRedoListener.actionRedone();
		}
		public void triggerRecordedListener(){
			for(UndoRedoListener undoRedoListener:undoRedoListeners)undoRedoListener.actionRecorded();
		}
//UNDO
	private List<UndoRedoNodo>undo=new ArrayList<UndoRedoNodo>();
		public void undo(){
			if(!canUndo()){
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			final UndoRedoNodo nodo=undo.remove(undo.size()-1);
			nodo.getOpositeAction().run();
			triggerUndoListener();
		}
		public boolean canUndo() {return !undo.isEmpty();}
//REDO
	private List<UndoRedoNodo>redo=new ArrayList<UndoRedoNodo>();
		public void redo(){
			if(!canRedo()){
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			final UndoRedoNodo nodo=redo.remove(redo.size()-1);
			nodo.getAction().run();
			triggerRedoListener();
		}
		public boolean canRedo() {return !redo.isEmpty();}
//MAIN
	public UndoRedoManager() {}
//FUNCS
	public void clear(){
		undo.clear();
		redo.clear();
	}
	public boolean recordAction(UndoRedoNodo nodo) {
		redo.clear();
		undo.add(nodo);
		if(doLimite!=UndoRedoManager.NO_LIMIT&&undo.size()>doLimite) {
			undo.remove(0);		//REMOVE O UNDO MAIS ANTIGO
		}
		triggerRecordedListener();
		return true;
	}
}