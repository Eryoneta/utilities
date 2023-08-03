package tool.undo_redo_manager;

public interface UndoRedoListener{
	public void actionUndone();
	public void actionRedone();
	public void actionRecorded();
}