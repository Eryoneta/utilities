package utilitarios.visual.text.java.mindmarkdown.attribute.variable;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMBoldAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMHiddenAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMHighlightAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMItalicAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMStrikeThroughAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSubscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSuperscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMUnderlineAtributo;
@SuppressWarnings("serial")
public class MMTextStylerAtributo extends MindMarkVariableAtributo{
//SYMBOLS
	private final static String FONT_VAR=":";
	private final static String FOREGROUND_VAR="#";
	private final static String BACKGROUND_VAR="##";
	private final static String LINE_BACKGROUND_VAR="###";
//TAGS
	public final static String TAG=Pattern.quote(":");
	private final static String FONT_VAR_TAG=Pattern.quote(FONT_VAR);
	private final static String FOREGROUND_VAR_TAG=Pattern.quote(FOREGROUND_VAR);
	private final static String BACKGROUND_VAR_TAG=Pattern.quote(BACKGROUND_VAR);
	private final static String LINE_BACKGROUND_VAR_TAG=Pattern.quote(LINE_BACKGROUND_VAR);
//DEFINITION
	private final String variablesDefinition=(
		//(?:(?:(?<fontTag>:)(?<fontValue>`|[a-zA-Z0-9-_ ]+))|(?:(?<colorTag>###|##|#)(?<colorValue>[`@>=*+-. ]|[0-9A-Fa-f]{6}))|(?:(?<tag>[`*´~_$%@])))?
			pseudoGroup(oneOrOther(
					pseudoGroup(
							namedGroup("fontTag",FONT_VAR_TAG)+
							namedGroup("fontValue",
									oneOrOther(
											MMHighlightAtributo.TAG,
											characters(range("a","z"),range("A","Z"),range(0,9),"-","_"," ")+oneOrMore()
									)
							)
					),
					pseudoGroup(
							namedGroup("colorTag",
									oneOrOther(LINE_BACKGROUND_VAR_TAG,BACKGROUND_VAR_TAG,FOREGROUND_VAR_TAG)
							)+
							namedGroup("colorValue",
									oneOrOther(
											characters(MMHighlightAtributo.TAG,MMHiddenAtributo.TAG,MMLineAtributo.TAG,
													MMListAtributo.CHEFE_TAG,MMListAtributo.PAI_TAG,MMListAtributo.SON_0_TAG,MMListAtributo.SON_1_TAG,MMListAtributo.SON_2_TAG),
											characters(range(0,9),range("A","F"),range("a","f"))+occurs(6)
									)
							)
					),
					pseudoGroup(
							namedGroup("tag",
									characters(MMHighlightAtributo.TAG,MMBoldAtributo.TAG,MMItalicAtributo.TAG,MMStrikeThroughAtributo.TAG,
											MMUnderlineAtributo.TAG,MMSuperscriptAtributo.TAG,MMSubscriptAtributo.TAG,MMHiddenAtributo.TAG)
							)
					)
			))+zeroOrOne()
	);
//VAR GLOBAIS
	public final static String LINE_COLOR="line-color";
//COLOR_SECTION
	public class ColorSection{
	//START
		private int startIndex=-1;
			public int getStartIndex(){return startIndex;}
	//END
		private int endIndex=-1;
			public int getEndIndex(){return endIndex;}
	//COR
		private Color cor;
			public Color getCor(){return cor;}
	//MAIN
		public ColorSection(int startIndex,int endIndex,Color cor){
			this.startIndex=startIndex;
			this.endIndex=endIndex;
			this.cor=cor;
		}
	}
//MAIN
	public MMTextStylerAtributo(){
		buildDefinitionWithVars("",TAG,true,false,"");
	}
//FUNCS
	private MindMarkDocumento doc;
@Override
	public void applyStyle(MindMarkDocumento doc){
		this.doc=doc;
		super.applyStyle(doc);
	}
@Override
	protected void setVarsAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String vars){
		final Matcher match=Pattern.compile(variablesDefinition).matcher(vars);
		while(match.find()){
			if(match.group("fontTag")!=null){			//FONT
				setAtributo(atributo,match.group("fontTag"),match.group("fontValue"));
			}else if(match.group("colorTag")!=null){	//COLOR
				final String colorTag=match.group("colorTag");
				final String colorValue=match.group("colorValue");
				switch(colorTag){
					default:					//#,##
						setAtributo(atributo,colorTag,colorValue);
					break;
					case LINE_BACKGROUND_VAR:	//###
						final  MindMarkAtributo colorAtributo=new MindMarkAtributo();
						try{
							final Color cor=getCor(colorValue);
							if(cor!=null){
								colorAtributo.addAttribute(LINE_COLOR,new ColorSection(indexTexto,indexTexto+texto.length(),cor));
								doc.setParagraphAttributes(indexTexto,texto.length(),colorAtributo,false);
							}
						}catch(NumberFormatException error){}//TODO
					break;
				}
			}else if(match.group("tag")!=null){			//SHORTCUT
				setAtributo(atributo,match.group("tag"),"");
			}
		}
	}
		private void setAtributo(MindMarkAtributo atributo,String var,String valor){
			if(var==null||valor==null)return;
			switch(var){
				case FONT_VAR:
					if(valor.equals(MMHighlightAtributo.HIGHLIGHT)) {
						StyleConstants.setFontFamily(atributo,MMHighlightAtributo.FONT_NAME);
					}else StyleConstants.setFontFamily(atributo,valor);
					return;
				case FOREGROUND_VAR:
					final Color corForeground=getCor(valor);
					if(corForeground!=null)StyleConstants.setForeground(atributo,corForeground);
					return;
				case BACKGROUND_VAR:
					final Color corBackground=getCor(valor);
					if(corBackground!=null)StyleConstants.setBackground(atributo,corBackground);
					return;
				case MMBoldAtributo.BOLD:
					StyleConstants.setBold(atributo,true);
					return;
				case MMHighlightAtributo.HIGHLIGHT:
					StyleConstants.setFontFamily(atributo,MMHighlightAtributo.FONT_NAME);
					StyleConstants.setBackground(atributo,MMHighlightAtributo.BACKGROUND_COLOR);
					return;
				case MMHiddenAtributo.HIDDEN:
					StyleConstants.setForeground(atributo,MMHiddenAtributo.COLOR);
					StyleConstants.setBackground(atributo,MMHiddenAtributo.COLOR);
					return;
				case MMItalicAtributo.ITALIC:
					StyleConstants.setItalic(atributo,true);
					return;
				case MMStrikeThroughAtributo.STRIKE_THROUGH:
					StyleConstants.setStrikeThrough(atributo,true);
					return;
				case MMUnderlineAtributo.UNDERLINE:
					StyleConstants.setUnderline(atributo,true);
					return;
				case MMSuperscriptAtributo.SUPERSCRIPT:
					StyleConstants.setSuperscript(atributo,true);
					return;
				case MMSubscriptAtributo.SUBSCRIPT:
					StyleConstants.setSubscript(atributo,true);
					return;
			}
		}
		private Color getCor(String valor){
			switch(valor){
				case MMHighlightAtributo.HIGHLIGHT:		return MMHighlightAtributo.BACKGROUND_COLOR;
				case MMHiddenAtributo.HIDDEN:			return MMHiddenAtributo.COLOR;
				case MMLineAtributo.DEFAULT_OPTION:		return MMLineAtributo.COLOR;
				case MMListAtributo.CHEFE:case MMListAtributo.PAI:case MMListAtributo.SON_0:case MMListAtributo.SON_1:case MMListAtributo.SON_2:
					return MMListAtributo.COLOR;
				default:
					try{
						return Color.decode("#"+valor);
					}catch(NumberFormatException error){
						return null;
					}
			}
		}
}