package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.awt.Color;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMHiddenAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String HIDDEN="@";
	public final static String TAG=Pattern.quote(HIDDEN);
//VAR GLOBAIS
	public final static Color COLOR=new Color(0,0,0);
//MAIN
	public MMHiddenAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.Background,COLOR);
		setAtributo(StyleConstants.Foreground,COLOR);
	}
}