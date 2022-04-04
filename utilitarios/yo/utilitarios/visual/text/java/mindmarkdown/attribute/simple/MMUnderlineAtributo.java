package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMUnderlineAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String UNDERLINE="_";
	public final static String TAG=Pattern.quote(UNDERLINE);
//MAIN
	public MMUnderlineAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.Underline,true);
	}
}