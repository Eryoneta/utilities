package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMSuperscriptAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String SUPERSCRIPT="$";
	public final static String TAG=Pattern.quote(SUPERSCRIPT);
//MAIN
	public MMSuperscriptAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.Superscript,true);
	}
}