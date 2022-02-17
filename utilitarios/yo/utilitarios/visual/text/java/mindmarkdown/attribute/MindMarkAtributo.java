package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Font;
import java.util.regex.Pattern;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import utilitarios.ferramenta.regex.RegexBuilder;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.mindmarkdown.MindMarkTexto;
@SuppressWarnings("serial")
public class MindMarkAtributo extends SimpleAttributeSet implements RegexBuilder{
//ESCAPE
	protected final static String ESCAPE_TAG=Pattern.quote("\\");
//VAR STATICS
	public static MindMarkAtributo DEFAULT=new MindMarkAtributo();
		public static void setDefaultFont(Font fonte){
			DEFAULT=new MindMarkAtributo(){{
				StyleConstants.setFontFamily(this,fonte.getFamily());
				StyleConstants.setFontSize(this,fonte.getSize());
			}};
		}
	public final static String SHOW_FORMAT="show-format";
	public static void setShowFormat(MindMarkAtributo atributo,MindMarkEditor.Section section){
		atributo.addAttribute(SHOW_FORMAT,section);
	}
	public final static String SPECIAL_FORMAT="special-format";
	public final static MindMarkAtributo INVISIBLE=new MindMarkAtributo(){{
		this.addAttribute(SPECIAL_FORMAT,true);
		StyleConstants.setFontSize(this,0);
	}};
	public final static MindMarkAtributo SPECIAL=new MindMarkAtributo(){{
		this.addAttribute(SPECIAL_FORMAT,true);
		StyleConstants.setFontFamily(this,MindMarkEditor.DEFAULT_FONT.getFamily());
		StyleConstants.setForeground(this,MindMarkEditor.SPECIAL_CHARACTERS_COLOR);
	}};
//FUNCS STATICS
//SPECIAL
	public String notPrecededByEscape(){
		return	//(?<!(?<!\\)\\(?:\\\\){0,9})
				notPrecededBy(
						notPrecededBy(ESCAPE_TAG)+
						ESCAPE_TAG+
						pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+occursBetween(0,9)
				);
	}
	public String precededByEscape(){
		return	//(?<!\\)\\(?:\\\\)*
				notPrecededBy(ESCAPE_TAG)+
				ESCAPE_TAG+
				pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount();
	}
//MAIN
	public MindMarkAtributo(){}
//FUNCS
	protected static MindMarkAtributo getSpecialAtributo_TwoTags(MindMarkDocumento doc,int formatStart,int formatStartLength,int formatEnd,int formatEndLength){
		MindMarkEditor.Section section=(MindMarkEditor.Section)doc.getParagraphElement(formatStart).getAttributes().getAttribute(MindMarkAtributo.SHOW_FORMAT);
		if(section==null)section=(MindMarkEditor.Section)doc.getParagraphElement(formatEnd).getAttributes().getAttribute(MindMarkAtributo.SHOW_FORMAT);
		if(section==null)return MindMarkAtributo.INVISIBLE;
		final int selecStart=section.getIndex1();
		final int selecEnd=section.getIndex2();
		final int formatRealEnd=formatEnd+formatEndLength;
		switch(section.getShowMode()){
			case MindMarkTexto.SHOW_ALL_FORMAT:			return MindMarkAtributo.SPECIAL;	//TUDO É VISÍVEL
			case MindMarkTexto.SHOW_FORMAT_ON_LINE:		return MindMarkAtributo.SPECIAL;	//LINHA ESTÁ SELECIONADA
			case MindMarkTexto.SHOW_FORMAT_ON_WORD:
				if(formatStart<selecStart&&selecEnd<formatRealEnd)return MindMarkAtributo.SPECIAL;								//F1 S1-S2 F2: O TEXTO É SELECIONADO
				if(formatStart<selecStart&&selecStart<formatRealEnd&&formatRealEnd<selecEnd)return MindMarkAtributo.SPECIAL;	//F1 S1-F2-S2: F2 É SELECIONADO
				if(selecStart<formatStart&&formatStart<selecEnd&&selecEnd<formatRealEnd)return MindMarkAtributo.SPECIAL;		//S1-F1-S2 F2: F1 É SELECIONADO
				if(selecStart<formatStart&&formatRealEnd<selecEnd)return MindMarkAtributo.SPECIAL;								//S1-F1-F2-S2: F1 E F2 SÃO SELECIONADOS
				return MindMarkAtributo.INVISIBLE;
			case MindMarkTexto.SHOW_FORMAT_ON_CHAR:
				if(selecStart>=formatStart&&formatStart+formatStartLength>=selecStart)return MindMarkAtributo.SPECIAL;			//F1|S1-S2 F2: A SELEÇÃO COMEÇA EM F1
				if(selecEnd>=formatStart&&formatStart+formatStartLength>=selecEnd)return MindMarkAtributo.SPECIAL;				//S1-S2|F1 F2: A SELEÇÃO TERMINA EM F1
				if(selecEnd>=formatEnd&&formatEnd+formatEndLength>=selecEnd)return MindMarkAtributo.SPECIAL;					//F1 S1-S2|F2: A SELEÇÃO TERMINA EM F2
				if(selecStart>=formatEnd&&formatEnd+formatEndLength>=selecStart)return MindMarkAtributo.SPECIAL;				//F1 F2|S1-S2: A SELEÇÃO COMEÇA EM F2
				if(formatStart<selecStart&&selecStart<formatRealEnd&&formatRealEnd<selecEnd)return MindMarkAtributo.SPECIAL;	//F1 S1-F2-S2: F2 É SELECIONADO
				if(selecStart<formatStart&&formatStart<selecEnd&&selecEnd<formatRealEnd)return MindMarkAtributo.SPECIAL;		//S1-F1-S2 F2: F1 É SELECIONADO
				if(selecStart<formatStart&&formatRealEnd<selecEnd)return MindMarkAtributo.SPECIAL;								//S1-F1-F2-S2: F1 E F2 SÃO SELECIONADOS
				return MindMarkAtributo.INVISIBLE;
			case MindMarkTexto.SHOW_NO_FORMAT:default:	return MindMarkAtributo.INVISIBLE;
		}
	}
	protected static MindMarkAtributo getSpecialAtributo_OneTagOnLeft(MindMarkDocumento doc,int formatIndex,int formatLength,int textLength){
		final MindMarkEditor.Section section=(MindMarkEditor.Section)doc.getParagraphElement(formatIndex).getAttributes().getAttribute(MindMarkAtributo.SHOW_FORMAT);
		if(section==null)return MindMarkAtributo.INVISIBLE;
		final int selecStart=section.getIndex1();
		final int selecEnd=section.getIndex2();
		switch(section.getShowMode()){
			case MindMarkTexto.SHOW_FORMAT_ON_CHAR:
				if(selecStart>=formatIndex&&formatIndex+formatLength>=selecStart)return MindMarkAtributo.SPECIAL;				//F|S1-S2 T: A SELEÇÃO COMEÇA EM F
				if(selecEnd>=formatIndex&&formatIndex+formatLength>=selecEnd)return MindMarkAtributo.SPECIAL;					//S1-S2|F T: A SELEÇÃO TERMINA EM F
				if(selecStart<formatIndex&&formatIndex<selecEnd)return MindMarkAtributo.SPECIAL;								//S1-F-S2 T: F É SELECIONADO
				return MindMarkAtributo.INVISIBLE;
			default:	return getSpecialAtributo_TwoTags(doc,formatIndex,formatLength,formatIndex+formatLength+textLength,0);
		}
	}
	protected static MindMarkAtributo getSpecialAtributo_OneTagOnRight(MindMarkDocumento doc,int formatIndex,int formatLength,int textLength){
		final MindMarkEditor.Section section=(MindMarkEditor.Section)doc.getParagraphElement(formatIndex).getAttributes().getAttribute(MindMarkAtributo.SHOW_FORMAT);
		if(section==null)return MindMarkAtributo.INVISIBLE;
		final int selecStart=section.getIndex1();
		final int selecEnd=section.getIndex2();
		switch(section.getShowMode()){
			case MindMarkTexto.SHOW_FORMAT_ON_CHAR:
				if(selecStart>=formatIndex&&formatIndex+formatLength>=selecStart)return MindMarkAtributo.SPECIAL;			//T F|S1-S2: A SELEÇÃO COMEÇA EM F
				if(selecEnd>=formatIndex&&formatIndex+formatLength>=selecEnd)return MindMarkAtributo.SPECIAL;				//T S1-S2|F: A SELEÇÃO TERMINA EM F
				if(selecStart<formatIndex&&formatIndex<selecEnd)return MindMarkAtributo.SPECIAL;								//S1-F-S2 T: F É SELECIONADO
				return MindMarkAtributo.INVISIBLE;
			default:	return getSpecialAtributo_TwoTags(doc,formatIndex-textLength,0,formatIndex,formatLength);
		}
	}
	protected static boolean isStyled(MindMarkDocumento doc,int index){
		final Boolean isStyled=(Boolean)doc.getCharacterElement(index).getAttributes().getAttribute(SPECIAL_FORMAT);
		if(isStyled!=null&&isStyled==true)return true;
		return false;
	}
}