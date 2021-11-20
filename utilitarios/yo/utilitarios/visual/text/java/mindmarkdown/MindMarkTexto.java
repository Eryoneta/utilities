package utilitarios.visual.text.java.mindmarkdown;
import java.awt.Font;
import utilitarios.visual.text.java.SimpleTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkSizeModifierAtributo;
@SuppressWarnings("serial")
public class MindMarkTexto extends SimpleTexto{
//EDITING
	private boolean editing=false;
		public boolean isEditing(){return editing;}
//MAIN
	public MindMarkTexto(){
		setEditorKit(new MindMarkEditor(this));
		setUndoManager(new MMUndoManager());
//		addKeyListener(new KeyAdapter(){	//TESTES
//			public void keyPressed(KeyEvent k){	//TODO: REMOVER
//				switch(k.getKeyCode()){
//					case KeyEvent.VK_T:
//						((MindMarkDocumento)getDocument()).insertTable(0, 2, new int[] {200, 100, 150});
//					break;
//				}
//				
//			}
//		});
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
		MindMarkSizeModifierAtributo.setDefaultFontSize(font.getSize());
	}
}