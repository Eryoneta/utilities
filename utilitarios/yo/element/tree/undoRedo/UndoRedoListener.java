package element.tree.undoRedo;
public interface UndoRedoListener{
	public void actionUndone();
	public void actionRedone();
	public void actionSaved();
}