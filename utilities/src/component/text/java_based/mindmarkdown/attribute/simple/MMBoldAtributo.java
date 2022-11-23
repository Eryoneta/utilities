package component.text.java_based.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMBoldAtributo extends MindMarkdownSimpleAtributo{
//TAG
	public final static String BOLD="*";
	public final static String TAG=Pattern.quote(BOLD);
//MAIN
	public MMBoldAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.Bold,true);
	}
}