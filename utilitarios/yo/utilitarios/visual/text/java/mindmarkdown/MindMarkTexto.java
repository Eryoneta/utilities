package utilitarios.visual.text.java.mindmarkdown;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import utilitarios.visual.text.java.SimpleTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMLinkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMSizeModifierAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MindMarkTexto extends SimpleTexto{
//FORMAT_VIEWMODE
	public static final int SHOW_ALL_FORMAT=0,SHOW_FORMAT_ON_LINE=1,SHOW_FORMAT_ON_WORD=2,SHOW_FORMAT_ON_CHAR=3,SHOW_NO_FORMAT=4;
	private int formatViewmode=SHOW_FORMAT_ON_LINE;	//TODO
		public void setFormatViewMode(int viewMode){formatViewmode=viewMode;}
		public int getFormatViewMode(){return formatViewmode;}
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
@Override
	public Point getToolTipLocation(MouseEvent m){
		if(getToolTipText()!=null){
			return new Point(getX(),getY()+getHeight()-20);
		}
		return null;
	}
}