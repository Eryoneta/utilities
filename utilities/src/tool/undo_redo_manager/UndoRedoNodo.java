package tool.undo_redo_manager;

public interface UndoRedoNodo{
	public Runnable getAction();
	public Runnable getOpositeAction();
}