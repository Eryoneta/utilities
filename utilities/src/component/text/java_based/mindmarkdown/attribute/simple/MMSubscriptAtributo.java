package component.text.java_based.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMSubscriptAtributo extends MindMarkdownSimpleAtributo{
//TAG
	public final static String SUBSCRIPT="%";
	public final static String TAG=Pattern.quote(SUBSCRIPT);
//MAIN
	public MMSubscriptAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.Subscript,true);
	}
}