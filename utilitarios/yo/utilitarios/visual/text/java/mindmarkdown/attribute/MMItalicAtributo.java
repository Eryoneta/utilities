package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMItalicAtributo extends MindMarkAtributo{
//TAGS
	private final static String TEXT_TAG=Pattern.quote("´");
	private final static String MULTILINE_TAG=Pattern.quote("´´´");
//DEFINITIONS
	private final static String TEXT_DEFINITION=(
			notPrecededBy(ESCAPE)+
			group(TEXT_TAG)+
			group(
					pseudoGroup(
							notFollowedBy(TEXT_TAG)+
							allExceptLineBreak()
					)+oneOrMore()
			)+
			notPrecededBy(ESCAPE)+
			group(TEXT_TAG)
	);
	private final static String MULTILINE_DEFINITION=(
			notPrecededBy(ESCAPE)+
			group(MULTILINE_TAG)+
			group(
					pseudoGroup(
							notFollowedBy(MULTILINE_TAG)+
							oneOrOther(allExceptLineBreak(),lineBreak())
					)+oneOrMore()
			)+
			notPrecededBy(ESCAPE)+
			group(MULTILINE_TAG)
	);
//MAIN
	public MMItalicAtributo(){
		StyleConstants.setItalic(this,true);
	}
//FUNCS
	public static void applyStyle(MindMarkDocumento doc){
		final MMItalicAtributo atributo=new MMItalicAtributo();
		final String texto=doc.getText();
		final Matcher textMatch=Pattern.compile(TEXT_DEFINITION).matcher(texto);
		while(textMatch.find()){
		//TAG INI
			final int indexTagIni=textMatch.start(1);
			final int lengthTagIni=textMatch.group(1).length();
			doc.setCharacterAttributes(indexTagIni,lengthTagIni,MindMarkAtributo.SPECIAL,true);
		//TEXTO
			final int indexTexto=textMatch.start(2);
			final int lengthTexto=textMatch.group(2).length();
			doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
		//TAG FIM
			final int indexTagFim=textMatch.start(3);
			final int lengthTagFim=textMatch.group(3).length();
			doc.setCharacterAttributes(indexTagFim,lengthTagFim,MindMarkAtributo.SPECIAL,true);
		}
		final Matcher multilineMatch=Pattern.compile(MULTILINE_DEFINITION).matcher(texto);
		while(multilineMatch.find()){
		//TAG INI
			final int indexTagIni=multilineMatch.start(1);
			final int lengthTagIni=multilineMatch.group(1).length();
			doc.setCharacterAttributes(indexTagIni,lengthTagIni,MindMarkAtributo.SPECIAL,true);
		//TEXTO
			final int indexTexto=multilineMatch.start(2);
			final int lengthTexto=multilineMatch.group(2).length();
			doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
		//TAG FIM
			final int indexTagFim=multilineMatch.start(3);
			final int lengthTagFim=multilineMatch.group(3).length();
			doc.setCharacterAttributes(indexTagFim,lengthTagFim,MindMarkAtributo.SPECIAL,true);
		}
	}
}