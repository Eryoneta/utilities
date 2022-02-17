package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMSubscriptAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String TAG="%";
//MAIN
	public MMSubscriptAtributo(){
		buildDefinition(Pattern.quote(TAG));
		setAtributo(StyleConstants.Subscript,true);
	}
}