package utilitarios.visual.text.java.mindmarkdown;
import java.awt.Font;
import utilitarios.visual.text.java.SimpleTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLinkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMSizeModifierAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MindMarkTexto extends SimpleTexto{
//EDITING
	private boolean editing=false;
		public boolean isEditing(){return editing;}
//MAIN
	public MindMarkTexto(){
		setEditorKit(new MindMarkEditor(this));
		setUndoManager(new MMUndoManager());
		MMLinkAtributo.setListeners(this);
		setVisible(true);
	}
	public void setEditorKit(MindMarkEditor editor){
		this.editor=editor;
		super.setEditorKit(editor);
		editor.build();
		editor.setTabSize(20);
	}
//FUNCS
@Override
	public void setFont(Font font){
		super.setFont(font);
		if(font==null)return;
		MindMarkAtributo.setDefaultFont(font);
		MMSizeModifierAtributo.setDefaultFontSize(font.getSize());
	}
}