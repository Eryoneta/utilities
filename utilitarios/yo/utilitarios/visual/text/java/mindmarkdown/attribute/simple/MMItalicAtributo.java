package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMItalicAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String TAG="´";
//MAIN
	public MMItalicAtributo(){
		buildDefinition(Pattern.quote(TAG));
		setAtributo(StyleConstants.Italic,true);
	}
}