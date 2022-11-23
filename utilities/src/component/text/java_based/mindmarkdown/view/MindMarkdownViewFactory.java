package component.text.java_based.mindmarkdown.view;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import component.text.java_based.mindmarkdown.MindMarkdownEditor;
import component.text.java_based.plain.view.PlainLabelView;
import component.text.java_based.plain.view.PlainViewFactory;
public class MindMarkdownViewFactory extends PlainViewFactory{
//MAIN
	public MindMarkdownViewFactory(MindMarkdownEditor editor){
		super(editor);
	}
//FUNCS
@Override
	public View create(Element elem){
		final String kind=elem.getName();
		if(kind!=null){
			switch(kind){
				case AbstractDocument.ContentElementName:	return new PlainLabelView(elem,editorKit);
				case AbstractDocument.ParagraphElementName:	return new MMParagraphView(elem,(MindMarkdownEditor)editorKit);
				case AbstractDocument.SectionElementName:	return new BoxView(elem,View.Y_AXIS);
				case StyleConstants.ComponentElementName:	return new ComponentView(elem);
				case StyleConstants.IconElementName:		return new IconView(elem);
			}
		}
		return new PlainLabelView(elem,editorKit);
	}
}