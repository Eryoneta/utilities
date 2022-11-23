package component.text.java_based.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMItalicAtributo extends MindMarkdownSimpleAtributo{
//TAG
	public final static String ITALIC="´";
	public final static String TAG=Pattern.quote(ITALIC);
//MAIN
	public MMItalicAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.Italic,true);
	}
}