package component.text.java_based.mindmarkdown.attribute.variable;
import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

import component.text.java_based.mindmarkdown.MindMarkdownDocumento;
import component.text.java_based.mindmarkdown.attribute.MMCitationAtributo;
import component.text.java_based.mindmarkdown.attribute.MMLineAtributo;
import component.text.java_based.mindmarkdown.attribute.MMListAtributo;
import component.text.java_based.mindmarkdown.attribute.MindMarkdownAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMBoldAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMHiddenAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMHighlightAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMItalicAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMStrikeThroughAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMSubscriptAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMSuperscriptAtributo;
import component.text.java_based.mindmarkdown.attribute.simple.MMUnderlineAtributo;
import tool.color_gallery.ColorGallery;
@SuppressWarnings({"serial","unchecked"})
public class MMTextStylerAtributo extends MindMarkdownVariableAtributo{
//VAR GLOBAIS
	public final static String LINE_COLOR="line-color";
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
		//(?:(?:(?<fontTag>:)(?<fontValue>`|[a-zA-Z0-9-_ ]+))|(?:(?<colorTag>###|##|#)(?<colorValue>[`@=*+-. ]|>+|[0-9A-Fa-f]{6}|\w+))|(?:(?<tag>[`*´~_$%@])))?
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
											MMCitationAtributo.START_TAG+oneOrMore(),
											characters(range(0,9),range("A","F"),range("a","f"))+occurs(6),
											word()+oneOrMore()
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
	private MindMarkdownDocumento doc;
@Override
	public void applyStyle(MindMarkdownDocumento doc){
		this.doc=doc;
		super.applyStyle(doc);
	}
@Override
	protected void setVarsAtributos(MindMarkdownAtributo atributo,int indexTexto,String texto,String vars){
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
						setParagraphAtributo(indexTexto,texto,colorValue);
					break;
				}
			}else if(match.group("tag")!=null){			//SHORTCUT
				setAtributo(atributo,match.group("tag"),"");
			}
		}
	}
		private void setParagraphAtributo(int indexTexto,String texto,String colorValue){
			try{
				final Color cor=getCor(colorValue);
				if(cor!=null){
					final ColorSection sectionCor=new ColorSection(indexTexto,indexTexto+texto.length(),cor);
					final Element rootElement=doc.getDefaultRootElement();
					final int startElemIndex=rootElement.getElementIndex(indexTexto);
					int endElemIndex=rootElement.getElementIndex(indexTexto+texto.length()-1);
					if(endElemIndex<startElemIndex)endElemIndex=startElemIndex;
					for(int i=startElemIndex;i<=endElemIndex;i++){
						final Element paragraph=rootElement.getElement(i);
						final MutableAttributeSet paragraphAtrrs=(MutableAttributeSet)paragraph.getAttributes();
						final AtributeList<MMTextStylerAtributo.ColorSection>cores=(AtributeList<MMTextStylerAtributo.ColorSection>)paragraphAtrrs.getAttribute(MMTextStylerAtributo.LINE_COLOR);
						if(cores==null){
							final AtributeList<MMTextStylerAtributo.ColorSection>newCores=new AtributeList<>();
							newCores.setKey(i);
							newCores.add(sectionCor);
							final MindMarkdownAtributo colorAtributo=new MindMarkdownAtributo();
							colorAtributo.addAttribute(LINE_COLOR,newCores);
							doc.setParagraphAttributes(paragraph.getStartOffset(),0,colorAtributo,false);
						}else cores.add(sectionCor);
					}
				}
			}catch(NumberFormatException error){}//TODO
		}
		private void setAtributo(MindMarkdownAtributo atributo,String var,String valor){
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
				//CITATION
					if(valor.matches(startOfText()+MMCitationAtributo.START+oneOrMore()+endOfText())){
						int level=(valor.length()/MMCitationAtributo.START.length())-1;
						if(level<0||level>=MMCitationAtributo.BACKGROUND_COLOR.length){
							return MMCitationAtributo.BACKGROUND_COLOR[MMCitationAtributo.BACKGROUND_COLOR.length-1];
						}
						return MMCitationAtributo.BACKGROUND_COLOR[level];
				//HEX_COLOR
					}else if(valor.matches(startOfText()+characters(range(0,9),range("A","F"),range("a","f"))+occurs(6)+endOfText())){	//^[0-9A-Fa-f]{6}$
						try{
							return Color.decode("#"+valor);
						}catch(NumberFormatException error){}
				//NAMED_COLOR
					}else{
						final List<ColorGallery.NamedColor>cores=ColorGallery.search(valor);
						if(!cores.isEmpty())return cores.get(0).getCor();
					}
					return null;
			}
		}
}