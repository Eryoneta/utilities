package utilitarios.visual.text.java.view;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import utilitarios.visual.text.java.SimpleEditor;
public class SimpleParagraphView extends ParagraphView{
//EDITOR_KIT
	protected SimpleEditor editorKit;
//MAIN
	public SimpleParagraphView(Element elem,SimpleEditor editor){
		super(elem);
		editorKit=editor;
	}
//FUNCS
@Override
	public float nextTabStop(float x,int tabOffset){	//MUDA O TAMANHO DO TAB
		if(getTabSet()==null){
			return (float)(getTabBase()+(((int)x/editorKit.getTabSize()+1)*editorKit.getTabSize()));
		}else return super.nextTabStop(x,tabOffset);
	}
@Override
	public void layout(int width,int height){	//DEFINE SE A LINHA É WRAPPED OU NÃO
		if(!editorKit.isLineWrappable()||editorKit.isLineWrapped()){
			super.layout(width,height);
		}else super.layout(Short.MAX_VALUE,height);
	}
@Override
	public float getMinimumSpan(int axis){		//CONTROLA A LARGURA DA LINHA
		if(!editorKit.isLineWrappable()||editorKit.isLineWrapped()){
			return super.getMinimumSpan(axis);
		}else return super.getPreferredSpan(axis);
	}
}