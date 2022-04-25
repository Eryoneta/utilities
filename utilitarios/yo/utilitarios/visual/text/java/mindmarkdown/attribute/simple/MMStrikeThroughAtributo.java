package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMStrikeThroughAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String STRIKE_THROUGH="~";
	public final static String TAG=Pattern.quote(STRIKE_THROUGH);
//MAIN
	public MMStrikeThroughAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.StrikeThrough,true);
	}
}