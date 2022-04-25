package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.awt.Color;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MMHighlightAtributo extends MindMarkSimpleAtributo{
//TAG
	public final static String HIGHLIGHT="`";
	public final static String TAG=Pattern.quote(HIGHLIGHT);
//VAR GLOBAIS
	public final static String FONT_NAME="Courier New";
	public final static Color BACKGROUND_COLOR=new Color(220,220,220);
//MAIN
	public MMHighlightAtributo(){
		buildDefinition(TAG,false,TAG);
		setAtributo(StyleConstants.FontFamily,FONT_NAME);
		setAtributo(StyleConstants.Background,BACKGROUND_COLOR);
	}
}