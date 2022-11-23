package component.text.java_based.mindmarkdown;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoManager;
import component.text.java_based.mindmarkdown.attribute.variable.MMLinkAtributo;
import component.text.java_based.plain.PlainTexto;
@SuppressWarnings("serial")
public class MindMarkdownTexto extends PlainTexto{
//FORMAT_VIEWMODE
	public static final int SHOW_ALL_FORMAT=0,SHOW_FORMAT_ON_LINE=1,SHOW_FORMAT_ON_WORD=2,SHOW_FORMAT_ON_CHAR=3,SHOW_NO_FORMAT=4;
	private int formatViewmode=SHOW_FORMAT_ON_WORD;
		public void setFormatViewMode(int viewMode){formatViewmode=viewMode;}
		public int getFormatViewMode(){return formatViewmode;}
//DOCUMENTO
	private MindMarkdownDocumento documento;
//MAIN
	public MindMarkdownTexto(){
		setEditorKit(new MindMarkdownEditor(this));
		setUndoManager(new UndoManager());
		MMLinkAtributo.setListeners(this);
		setVisible(true);
	}
	public void setEditorKit(MindMarkdownEditor editor){
		this.editor=editor;
		documento=editor.getMindMarkDocumento();
		super.setEditorKit(editor);
		editor.build();
		editor.setTabSize(20);
	}
//FUNCS
@Override
	public void setFont(Font font){
		super.setFont(font);
		if(font==null||documento==null)return;
		documento.setFont(font);
	}
@Override
	public Point getToolTipLocation(MouseEvent m){
		final int tooltipHeight=18;
		if(getToolTipText()!=null){
			final int y=getParent().getY()+getParent().getHeight()-getY();	//SCROLLPANE.Y + SCROLLPANE.HEIGHT + (-TEXTPANE.Y)
			return new Point(getParent().getX(),y-(tooltipHeight+8));
		}
		return null;
	}
@Override
	public void setText(String text){
		/*setText() CHAMA getEditorKit().read() E ELE CHAMA getDocument().insertString() VÁRIAS VEZES, UMA PARA CADA LINHA(ERRADO!).
			ISSO CHAMA insertUpdate(), QUE CHAMA updateAllMarkdown() VÁRIAS VEZES EM 1 setText()!*/
//		super.setText(text);
		try{
			documento.remove(0,documento.getLength());
			if(text==null||text.equals(""))return;
			if(text.endsWith("\n"))text+="\n";	//ADICIONA PARA NÃO PERDER A ÚLTIMA LINHA SE ESTIVER VAZIA
			getEditorKit().read(new StringReader(text),documento,0);
		}catch(BadLocationException|IOException erro){}//TODO
	}
}