package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMStyleModifierAtributo extends MindMarkAtributo{
//SYMBOL
	private final static String FONT_VAR=":";
	private final static String FOREGROUND_VAR="#";
	private final static String BACKGROUND_VAR="##";
//TAGS
	private final static String START_TAG=Pattern.quote("[");
	private final static String END_TAG=Pattern.quote("]");
	private final static String START_VAR_TAG=Pattern.quote("{");
	private final static String FONT_VAR_TAG=Pattern.quote(FONT_VAR);
	private final static String FOREGROUND_VAR_TAG=Pattern.quote(FOREGROUND_VAR);
	private final static String BACKGROUND_VAR_TAG=Pattern.quote(BACKGROUND_VAR);
	private final static String END_VAR_TAG=Pattern.quote("}");
//DEFINITIONS
	private final static String DEFINITION=(
			notPrecededBy(ESCAPE)+
			group(START_TAG)+							//GROUP 1
			group(allExceptLineBreak()+oneOrMore())+	//GROUP 2
			group(										//GROUP 3
					notPrecededBy(ESCAPE)+
					END_TAG+
					START_VAR_TAG+
					pseudoGroup(
							group(oneOrOther(FONT_VAR_TAG,FOREGROUND_VAR_TAG,BACKGROUND_VAR_TAG))+	//GROUP 4
							group(characters("a-z","A-Z","0-9","-","_"," ")+zeroOrMore())			//GROUP 5
					)+zeroOrOne()+
					pseudoGroup(
							group(oneOrOther(FONT_VAR_TAG,FOREGROUND_VAR_TAG,BACKGROUND_VAR_TAG))+	//GROUP 6
							group(characters("a-z","A-Z","0-9","-","_"," ")+zeroOrMore())			//GROUP 7
					)+zeroOrOne()+
					pseudoGroup(
							group(oneOrOther(FONT_VAR_TAG,FOREGROUND_VAR_TAG,BACKGROUND_VAR_TAG))+	//GROUP 8
							group(characters("a-z","A-Z","0-9","-","_"," ")+zeroOrMore())			//GROUP 9
					)+zeroOrOne()+
					notPrecededBy(ESCAPE)+
					END_VAR_TAG
			)
	);
//MAIN
	public MMStyleModifierAtributo(){}
//FUNCS
	public static void applyStyle(MindMarkDocumento doc){
		final MMStyleModifierAtributo atributo=new MMStyleModifierAtributo();
		final String texto=doc.getText();
		final Matcher textMatch=Pattern.compile(DEFINITION).matcher(texto);
		while(textMatch.find()){
		//TAG INI
			final int indexTextoTagIni=textMatch.start(1);
			final int lengthTextoTagIni=textMatch.group(1).length();
			doc.setCharacterAttributes(indexTextoTagIni,lengthTextoTagIni,MindMarkAtributo.SPECIAL,true);
		//TEXTO
			final int indexTexto=textMatch.start(2);
			final int lengthTexto=textMatch.group(2).length();
		//TAG FIM
			final int indexTextoTagFim=textMatch.start(3);
			final int lengthTextoTagFim=textMatch.group(3).length();
			doc.setCharacterAttributes(indexTextoTagFim,lengthTextoTagFim,MindMarkAtributo.SPECIAL,true);
		//STYLE
			final String var_1=textMatch.group(4);
			final String var_1Valor=textMatch.group(5);
			setAtributo(atributo,var_1,var_1Valor);
			final String var_2=textMatch.group(6);
			final String var_2Valor=textMatch.group(7);
			setAtributo(atributo,var_2,var_2Valor);
			final String var_3=textMatch.group(8);
			final String var_3Valor=textMatch.group(9);
			setAtributo(atributo,var_3,var_3Valor);
			doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
		}
	}
		private static void setAtributo(MMStyleModifierAtributo atributo,String var,String valor){
			if(var==null||valor==null)return;
			switch(var){
				case FONT_VAR:
					StyleConstants.setFontFamily(atributo,valor);
				break;
				case FOREGROUND_VAR:
					final Color foreground=Color.decode("#"+valor);
					StyleConstants.setForeground(atributo,foreground);
				break;
				case BACKGROUND_VAR:
					final Color background=Color.decode("#"+valor);
					StyleConstants.setBackground(atributo,background);
				break;
			}
		}
}