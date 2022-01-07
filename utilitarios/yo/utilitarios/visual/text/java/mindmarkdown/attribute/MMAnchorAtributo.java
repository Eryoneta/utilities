package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMAnchorAtributo extends MindMarkAtributo{
//TAGS
	private final static String LEFT_TAG=Pattern.quote(":-");
	private final static String RIGHT_TAG=Pattern.quote("-:");
//DEFINITIONS
	private final static String DEFINITION=(
			notPrecededBy(ESCAPE)+
			group(LEFT_TAG)+zeroOrOne()+
			group(
					notFollowedBy(
							RIGHT_TAG+
							pseudoGroup(oneOrOther(endOfText(),lineBreak()))
					)+
					allExceptLineBreak()
			)+zeroOrMore()+
			notPrecededBy(ESCAPE)+
			group(RIGHT_TAG)+zeroOrOne()
	);
//MAIN
	public MMAnchorAtributo(){}
//FUNCS
	public static void applyStyle(MindMarkDocumento doc){
		final MMAnchorAtributo atributo=new MMAnchorAtributo();
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(DEFINITION).matcher(texto);
		while(match.find()){
			int index=-1;
			int anchor=-1;
		//TAG INI
			if(match.group(1)!=null){
				final int indexTagLeft=match.start(1);
				final int lengthTagLeft=match.group(1).length();
				doc.setCharacterAttributes(indexTagLeft,lengthTagLeft,MindMarkAtributo.SPECIAL,true);
				if(index==-1)index=indexTagLeft;
				anchor=StyleConstants.ALIGN_LEFT;
			}
		//TAG FIM
			if(match.group(3)!=null){
				final int indexTagRight=match.start(3);
				final int lengthTagRight=match.group(3).length();
				doc.setCharacterAttributes(indexTagRight,lengthTagRight,MindMarkAtributo.SPECIAL,true);
				if(index==-1)index=indexTagRight;
				if(anchor==StyleConstants.ALIGN_LEFT){
					anchor=StyleConstants.ALIGN_CENTER;
				}else anchor=StyleConstants.ALIGN_RIGHT;
			}
		//ANCHOR
			if(match.group(2)!=null){
				final int indexTagTexto=match.start(2);
				if(index==-1)index=indexTagTexto;
			}
			if(anchor==-1)continue;
			StyleConstants.setAlignment(atributo,anchor);
			doc.setParagraphAttributes(index,0,atributo,true);
		}
	}
}