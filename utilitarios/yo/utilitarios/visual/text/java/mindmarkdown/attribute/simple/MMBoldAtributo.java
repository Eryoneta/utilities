package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMBoldAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String TAG="*";
//MAIN
	public MMBoldAtributo(){
		buildDefinition(Pattern.quote(TAG));
		setAtributo(StyleConstants.Bold,true);
	}
}