package utilitarios.visual.text.java.mindmarkdown;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
@SuppressWarnings("serial")
public class MMUndoManager extends UndoManager{
//DISABLED
	private boolean disabled=false;
		public void setDisabled(boolean disabled){this.disabled=disabled;}
		public boolean isDisabled(){return disabled;}
//MAIN
	public MMUndoManager(){}//TODO: DELETAR TUDO?
//FUNCS
@Override
	public synchronized boolean addEdit(UndoableEdit anEdit){
		if(isDisabled())return false;
		return super.addEdit(anEdit);
	}
}