package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMStrikeThroughAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String TAG="~";
//MAIN
	public MMStrikeThroughAtributo(){
		buildDefinition(Pattern.quote(TAG));
		setAtributo(StyleConstants.StrikeThrough,true);
	}
}