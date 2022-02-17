package utilitarios.visual.text.java.mindmarkdown;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMEscapeAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMHighlightAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMImageAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMItalicAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMLinkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMSizeModifierAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMStrikeThroughAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMStyleModifierAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSubscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSuperscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMUnderlineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMAnchorAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMBoldAtributo;
@SuppressWarnings("serial")
public class MindMarkDocumento extends DefaultStyledDocument{
//ATRIBUTO BY WORD
	public static MMEscapeAtributo ESCAPE=new MMEscapeAtributo();
	public static MMBoldAtributo BOLD=new MMBoldAtributo();
	public static MMItalicAtributo ITALIC=new MMItalicAtributo();
	public static MMStrikeThroughAtributo STRIKE_THROUGH=new MMStrikeThroughAtributo();
	public static MMUnderlineAtributo UNDERLINE=new MMUnderlineAtributo();
	public static MMHighlightAtributo HIGHLIGHT=new MMHighlightAtributo();
	public static MMSuperscriptAtributo SUPERSCRIPT=new MMSuperscriptAtributo();
	public static MMSubscriptAtributo SUBSCRIPT=new MMSubscriptAtributo();
	public static MMLinkAtributo LINK=new MMLinkAtributo();
	public static MMStyleModifierAtributo STYLE_CHANGE=new MMStyleModifierAtributo();
//ATRIBUTO BY LINE
	public static MMSizeModifierAtributo SIZE_CHANGE=new MMSizeModifierAtributo();
//ATRIBUTO BY PARAGRAPH
	public static MMCitationAtributo CITATION=new MMCitationAtributo();
	public static MMLineAtributo LINE=new MMLineAtributo();
	public static MMListAtributo LIST=new MMListAtributo();
	public static MMAnchorAtributo ANCHOR=new MMAnchorAtributo();
	public static MMImageAtributo IMAGE=new MMImageAtributo();
//MAIN
	public MindMarkDocumento(){}
//FUNCS
	public void updateMarkdown(){
		long timeIni=System.currentTimeMillis();
	//ESCAPE
		ESCAPE.applyStyle(this);			//\\
	//BY PARAGRAPH
		CITATION.applyStyle(this);			//>TEXTO,|>TEXTO
		LINE.applyStyle(this);				//===,=0=,=1=,=2=,...,=9=
		LIST.applyStyle(this);				//* TEXTO,+ TEXTO,- TEXTO,. TEXTO,  TEXTO	//ANTES DE BOLD
		ANCHOR.applyStyle(this);			//:-TEXTO,:-TEXTO-:,TEXTO-:
		IMAGE.applyStyle(this);				//![TEXTO](LINK)				//ANTES DE LINK, UNDERSCORE
	//BY WORD
		STYLE_CHANGE.applyStyle(this);		//:[TEXTO]{:FONTE#COR##COR}		//ANTES DE LINK, SIZE_MODIFIER, UNDERSCORE
		LINK.applyStyle(this);				//[TEXTO](LINK)					//ANTES DE UNDERSCORE
		BOLD.applyStyle(this);				//*TEXTO*
		ITALIC.applyStyle(this);			//´TEXTO´
		STRIKE_THROUGH.applyStyle(this);	//~TEXTO~
		UNDERLINE.applyStyle(this);			//_TEXTO_
		HIGHLIGHT.applyStyle(this);			//`TEXTO`
		SUPERSCRIPT.applyStyle(this);		//$TEXTO$
		SUBSCRIPT.applyStyle(this);			//%TEXTO%
	//BY LINE
		SIZE_CHANGE.applyStyle(this);		//#TEXTO,^TEXTO
		System.out.println(System.currentTimeMillis()-timeIni);
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