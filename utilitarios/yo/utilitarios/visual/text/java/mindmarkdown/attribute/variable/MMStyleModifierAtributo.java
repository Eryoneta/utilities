package utilitarios.visual.text.java.mindmarkdown.attribute.variable;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMBoldAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMHighlightAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMItalicAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMStrikeThroughAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSubscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSuperscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMUnderlineAtributo;
@SuppressWarnings("serial")
public class MMStyleModifierAtributo extends MindMarkVariableAtributo{
//SYMBOLS
	private final static String FONT_VAR=":";
	private final static String FOREGROUND_VAR="#";
	private final static String BACKGROUND_VAR="##";
//TAGS
	private final static String TAG=Pattern.quote(":");
	private final static String FONT_VAR_TAG=Pattern.quote(FONT_VAR);
	private final static String FOREGROUND_VAR_TAG=Pattern.quote(FOREGROUND_VAR);
	private final static String BACKGROUND_VAR_TAG=Pattern.quote(BACKGROUND_VAR);
//DEFINITION
	private final String variablesDefinition=(
		//(?:(?:(?<fontTag>:)(?<fontValue>[a-zA-Z0-9-_ ]+))|(?:(?<colorTag>##|#)(?<colorValue>[0-9A-Fa-f]+))|(?:(?<tag>[*´~_`$%])))?
			pseudoGroup(oneOrOther(
					pseudoGroup(
							namedGroup("fontTag",FONT_VAR_TAG)+
							namedGroup("fontValue",
									characters(range("a","z"),range("A","Z"),range(0,9),"-","_"," ")+oneOrMore()
							)
					),
					pseudoGroup(
							namedGroup("colorTag",
									oneOrOther(BACKGROUND_VAR_TAG,FOREGROUND_VAR_TAG)
							)+
							namedGroup("colorValue",
									characters(range(0,9),range("A","F"),range("a","f"))+oneOrMore()
							)
					),
					pseudoGroup(
							namedGroup("tag",
									characters(MMBoldAtributo.TAG,MMHighlightAtributo.TAG,MMItalicAtributo.TAG,MMStrikeThroughAtributo.TAG,MMUnderlineAtributo.TAG,
											MMSuperscriptAtributo.TAG,MMSubscriptAtributo.TAG)
							)
					)
			))+zeroOrOne()
	);
//MAIN
	public MMStyleModifierAtributo(){
		buildDefinitionWithVars(TAG,true);
	}
//FUNCS
@Override
	protected void setVarsAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String vars){
		final Matcher match=Pattern.compile(variablesDefinition).matcher(vars);
		while(match.find()){
			if(match.group("fontTag")!=null){
				setAtributo(atributo,match.group("fontTag"),match.group("fontValue"));
			}else if(match.group("colorTag")!=null){
				setAtributo(atributo,match.group("colorTag"),match.group("colorValue"));
			}else if(match.group("tag")!=null){
				setAtributo(atributo,match.group("tag"),"");
			}
		}
	}
		private void setAtributo(MindMarkAtributo atributo,String var,String valor){
			if(var==null||valor==null)return;
			switch(var){
				case FONT_VAR:
					StyleConstants.setFontFamily(atributo,valor);
					return;
				case FOREGROUND_VAR:
					try{
						final Color foreground=Color.decode("#"+valor);
						StyleConstants.setForeground(atributo,foreground);
					}catch(NumberFormatException error){}
					return;
				case BACKGROUND_VAR:
					try{
						final Color background=Color.decode("#"+valor);
						StyleConstants.setBackground(atributo,background);
					}catch(NumberFormatException error){}
					return;
				case MMBoldAtributo.TAG:
					StyleConstants.setBold(atributo,true);
					return;
				case MMHighlightAtributo.TAG:
					StyleConstants.setFontFamily(atributo,MMHighlightAtributo.FONT_NAME);
					StyleConstants.setBackground(atributo,MMHighlightAtributo.BACKGROUND_COLOR);
					return;
				case MMItalicAtributo.TAG:
					StyleConstants.setItalic(atributo,true);
					return;
				case MMStrikeThroughAtributo.TAG:
					StyleConstants.setStrikeThrough(atributo,true);
					return;
				case MMUnderlineAtributo.TAG:
					StyleConstants.setUnderline(atributo,true);
					return;
				case MMSuperscriptAtributo.TAG:
					StyleConstants.setSuperscript(atributo,true);
					return;
				case MMSubscriptAtributo.TAG:
					StyleConstants.setSubscript(atributo,true);
					return;
			}
		}
}