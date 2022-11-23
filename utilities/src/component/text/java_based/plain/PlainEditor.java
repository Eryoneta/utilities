package component.text.java_based.plain;
import java.awt.Font;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;
import component.text.java_based.plain.view.PlainViewFactory;
import element.tree.propriedades.Cor;
@SuppressWarnings("serial")
public class PlainEditor extends StyledEditorKit{
//VAR GLOBAIS
	public static Cor SPECIAL_CHARACTERS_COLOR=new Cor(93,157,125);
	public static Font DEFAULT_FONT=new Font("Courier New",Font.PLAIN,15);
	public static String SPACE_SYMBOL="∙";
	public static String ENTER_SYMBOL="¶";
//QUEBRA DE LINHA
	private boolean lineWrappable=false;	//NECESSÁRIO PARA DEFINIR SE A LARGURA DEVE OU NÃO EXPANDIR COM O NO_LINE_WRAP
		public boolean isLineWrappable(){return lineWrappable;}
		public void setLineWrappable(boolean wrappable){lineWrappable=wrappable;}
	private boolean lineWrap=false;
		public boolean isLineWrapped(){return lineWrap;}
		public void setLineWrap(boolean wrap){lineWrap=wrap;}
//CARACTERES INVISÍVEIS
	private boolean viewAllChars=false;
		public boolean isAllCharsVisible(){return viewAllChars;}
		public void setViewAllChars(boolean view){viewAllChars=view;}
//TAMANHO DO TAB
	private int tabSize=40;
		public int getTabSize(){return tabSize;}
		public void setTabSize(int size){tabSize=size;}
//INTERFACE
	private final PlainViewFactory viewFactory=new PlainViewFactory(this);
	@Override
		public ViewFactory getViewFactory(){return viewFactory;}
//MAIN
	public PlainEditor(){}
}