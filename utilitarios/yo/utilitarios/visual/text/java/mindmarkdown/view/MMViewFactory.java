package utilitarios.visual.text.java.mindmarkdown.view;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.view.SimpleLabelView;
import utilitarios.visual.text.java.view.SimpleViewFactory;
public class MMViewFactory extends SimpleViewFactory{
//MAIN
	public MMViewFactory(MindMarkEditor editor){
		super(editor);
	}
//FUNCS
@Override
	public View create(Element elem){
		final String kind=elem.getName();
		if(kind!=null){
			switch(kind){
				case AbstractDocument.ContentElementName:	return new SimpleLabelView(elem,editorKit);
				case AbstractDocument.ParagraphElementName:	return new MMParagraphView(elem,(MindMarkEditor)editorKit);
				case AbstractDocument.SectionElementName:	return new BoxView(elem,View.Y_AXIS);
				case StyleConstants.ComponentElementName:	return new ComponentView(elem);
				case StyleConstants.IconElementName:		return new IconView(elem);
			}
		}
		return new SimpleLabelView(elem,editorKit);
	}
}