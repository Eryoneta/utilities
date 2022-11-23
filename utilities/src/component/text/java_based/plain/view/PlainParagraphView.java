package component.text.java_based.plain.view;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import component.text.java_based.plain.PlainEditor;
public class PlainParagraphView extends ParagraphView{
//EDITOR_KIT
	protected PlainEditor editorKit;
//MAIN
	public PlainParagraphView(Element elem,PlainEditor editor){
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