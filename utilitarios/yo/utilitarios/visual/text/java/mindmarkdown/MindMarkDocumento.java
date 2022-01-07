package utilitarios.visual.text.java.mindmarkdown;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMHighlightAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMItalicAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLinkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMSizeModifierAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMStrikeThroughAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMStyleModifierAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMUnderlineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMAnchorAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMBoldAtributo;
@SuppressWarnings("serial")
public class MindMarkDocumento extends DefaultStyledDocument{
//ATRIBUTO BY WORD
	public static MMBoldAtributo BOLD=new MMBoldAtributo();
	public static MMItalicAtributo ITALIC=new MMItalicAtributo();
	public static MMStrikeThroughAtributo STRIKE_THROUGH=new MMStrikeThroughAtributo();
	public static MMUnderlineAtributo UNDERLINE=new MMUnderlineAtributo();
	public static MMHighlightAtributo HIGHLIGHT=new MMHighlightAtributo();
	public static MMLinkAtributo LINK=new MMLinkAtributo();
	public static MMStyleModifierAtributo STYLE_CHANGE=new MMStyleModifierAtributo();
//ATRIBUTO BY LINE
	public static MMSizeModifierAtributo SIZE_CHANGE=new MMSizeModifierAtributo();
//ATRIBUTO BY PARAGRAPH
	public static MMCitationAtributo CITATION=new MMCitationAtributo();
	public static MMLineAtributo LINE=new MMLineAtributo();
	public static MMListAtributo LIST=new MMListAtributo();
	public static MMAnchorAtributo ANCHOR=new MMAnchorAtributo();
//MAIN
	public MindMarkDocumento(){}
//FUNCS
	public void updateMarkdown(){
		clearMarkdown();
	//BY WORD
		MMBoldAtributo.applyStyle(this);
		MMItalicAtributo.applyStyle(this);
		MMStrikeThroughAtributo.applyStyle(this);
		MMUnderlineAtributo.applyStyle(this);
		MMHighlightAtributo.applyStyle(this);
		MMLinkAtributo.applyStyle(this);
		MMStyleModifierAtributo.applyStyle(this);
	//BY LINE
		MMSizeModifierAtributo.applyStyle(this);
	//BY PARAGRAPH
		MMCitationAtributo.applyStyle(this);
		MMLineAtributo.applyStyle(this);
		MMListAtributo.applyStyle(this);
		MMAnchorAtributo.applyStyle(this);
	}
	public void clearMarkdown(){
		setCharacterAttributes(0,getLength(),MindMarkAtributo.DEFAULT,true);
		setParagraphAttributes(0,getLength()+1,MindMarkAtributo.DEFAULT,true);	//O '+1' LIMPA A ÚLTIMA LINHA
	}
	public String getText(){
		try{
			return getText(0,getLength());
		}catch(BadLocationException error){
			return "";
		}
	}
}