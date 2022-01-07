package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.StyleConstants;

import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMLineAtributo extends MindMarkAtributo{
//TAGS
	private final static String TAG=Pattern.quote("===");
//DEFINITIONS
	private final static String DEFINITION=(
			pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
			notPrecededBy(ESCAPE)+
			group(TAG)+
			pseudoGroup(oneOrOther(lineBreak(),endOfText()))
	);
//VAR GLOBAIS
	public final static String LINE="line";
	public final static Color LINE_COLOR=new Color(190,190,190);
	public final static float LINE_WIDTH=4;
//MAIN
	public MMLineAtributo(){
		this.addAttribute(LINE,true);
		StyleConstants.setAlignment(this,StyleConstants.ALIGN_CENTER);
	}
//FUNCS
	public static void applyStyle(MindMarkDocumento doc){
		final MMLineAtributo atributo=new MMLineAtributo();
		final String texto=doc.getText();
		final Matcher textMatch=Pattern.compile(DEFINITION).matcher(texto);
		while(textMatch.find()){
		//TAG INI
			final int indexTag=textMatch.start(1);
			final int lengthTag=textMatch.group(1).length();
			doc.setCharacterAttributes(indexTag,lengthTag,MindMarkAtributo.SPECIAL,true);
		//LINE
			doc.setParagraphAttributes(indexTag,0,atributo,true);
		}
	}
}