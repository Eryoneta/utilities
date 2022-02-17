package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMUnderlineAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String TAG="_";
//MAIN
	public MMUnderlineAtributo(){
		buildDefinition(Pattern.quote(TAG));
		setAtributo(StyleConstants.Underline,true);
	}
}