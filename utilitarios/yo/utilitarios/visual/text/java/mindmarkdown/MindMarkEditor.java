package utilitarios.visual.text.java.mindmarkdown;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.ViewFactory;
import utilitarios.visual.text.java.SimpleEditor;
import utilitarios.visual.text.java.SimpleTexto;
import utilitarios.visual.text.java.mindmarkdown.view.MMViewFactory;
@SuppressWarnings("serial")
public class MindMarkEditor extends SimpleEditor{
//INTERFACE
	private final MMViewFactory viewFactory=new MMViewFactory(this);
	@Override
		public ViewFactory getViewFactory(){return viewFactory;}
//TEXTO
	private JTextPane editorTexto;
//DOCUMENTO
	private MindMarkDocumento documento=new MindMarkDocumento();
		public MindMarkDocumento getMindMarkDocumento(){return documento;}
//MAIN
	public MindMarkEditor(JTextPane editorTexto){
		this.editorTexto=editorTexto;
	}
	public void build(){
		editorTexto.setDocument(documento);
		editorTexto.getDocument().addDocumentListener(new DocumentListener(){
		@Override public void removeUpdate(DocumentEvent d){update();}
		@Override public void insertUpdate(DocumentEvent d){update();}
		@Override public void changedUpdate(DocumentEvent d){update();}
			private boolean updating=false;
			private void update(){
				if(updating)return;
				updating=true;
				SwingUtilities.invokeLater(new Runnable(){
				@Override
					public void run(){
						updateMarkdown();
						updating=false;
					}
				});
			}
		});
	}
//FUNCS
	private void updateMarkdown(){
		final MMUndoManager undoRedo=(MMUndoManager)((SimpleTexto)editorTexto).getUndoManager();
		undoRedo.setDisabled(true);
		
//		final int selecStart=editorTexto.getSelectionStart();
//		final int selecEnd=editorTexto.getSelectionEnd();
		final MindMarkDocumento documento=(MindMarkDocumento)editorTexto.getDocument();
		documento.updateMarkdown();
//		editorTexto.setSelectionStart(selecStart);
//		editorTexto.setSelectionEnd(selecEnd);

		undoRedo.setDisabled(false);
		
		System.out.println(editorTexto.getText());
	}
}