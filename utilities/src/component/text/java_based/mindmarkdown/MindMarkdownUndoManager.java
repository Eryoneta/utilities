package component.text.java_based.mindmarkdown;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
@SuppressWarnings("serial")
public class MindMarkdownUndoManager extends UndoManager{
//DISABLED
	private boolean disabled=false;
		public void setDisabled(boolean disabled){this.disabled=disabled;}
		public boolean isDisabled(){return disabled;}
//MAIN
	public MindMarkdownUndoManager(){}//TODO: DELETAR TUDO?
//FUNCS
@Override
	public synchronized boolean addEdit(UndoableEdit anEdit){
		if(isDisabled())return false;
		return super.addEdit(anEdit);
	}
}