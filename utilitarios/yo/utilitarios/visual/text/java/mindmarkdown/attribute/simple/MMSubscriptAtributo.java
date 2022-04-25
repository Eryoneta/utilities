package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMSubscriptAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String SUBSCRIPT="%";
	public final static String TAG=Pattern.quote(SUBSCRIPT);
//MAIN
	public MMSubscriptAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.Subscript,true);
	}
}