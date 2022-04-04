package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.Element;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.mindmarkdown.MindMarkTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMImageAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMTextStylerAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.view.SimpleParagraphView;
public class MMParagraphView extends SimpleParagraphView{
//TEXT
	private MindMarkTexto texto;
//VIEWS
	private MMParagraphCitationView citationView;
	private MMParagraphLineView lineView;
	private MMParagraphListView listView;
	private MMParagraphImageView imageView;
//MAIN
	public MMParagraphView(Element elem,MindMarkEditor editor){
		super(elem,editor);
		texto=editor.getTextEditor();
		citationView=new MMParagraphCitationView(this);
		lineView=new MMParagraphLineView(this);
		listView=new MMParagraphListView(this);
		imageView=new MMParagraphImageView(this,editor);
	}
//FUNCS
	public void paintContent(Graphics g,Shape s){super.paint(g,s);}
@Override
	public void paint(Graphics g,Shape s){
		final MMTextStylerAtributo.ColorSection color=(MMTextStylerAtributo.ColorSection)getElement().getAttributes().getAttribute(MMTextStylerAtributo.LINE_COLOR);
		if(color!=null)drawColor(g,s,color);
		final MMCitationAtributo.Citation citation=(MMCitationAtributo.Citation)getElement().getAttributes().getAttribute(MMCitationAtributo.CITATION);
		final MMLineAtributo.Line line=(MMLineAtributo.Line)getElement().getAttributes().getAttribute(MMLineAtributo.LINE);
		final MMListAtributo.List list=(MMListAtributo.List)getElement().getAttributes().getAttribute(MMListAtributo.LIST);
		final MMImageAtributo.Image image=(MMImageAtributo.Image)getElement().getAttributes().getAttribute(MMImageAtributo.IMAGE);
		if(citation!=null)citationView.draw(g,s,citation);
			else if(line!=null)lineView.draw(g,s,line);
				else if(list!=null)listView.draw(g,s,list);
					else if(image!=null)imageView.draw(g,s,image);
						else paintContent(g,s);
	}
		private void drawColor(Graphics g,Shape s,MMTextStylerAtributo.ColorSection color){
			final Rectangle area=s.getBounds();
			final Rectangle areaToColor=new Rectangle(-1,area.y,-1,area.height);
		//FUNDO
			if(color.getStartIndex()<getElement().getStartOffset()){	//COLOR COMEÇA ANTES DA LINHA
				areaToColor.x=area.x;
			}else try{
				areaToColor.x=texto.modelToView(color.getStartIndex()).x;
			}catch(Exception error){}	//TODO!!!
			if(color.getEndIndex()>=getElement().getEndOffset()){	//COLOR ACABA DEPOIS DA LINHA
				areaToColor.width=area.width-(areaToColor.x-area.x);
			}else try{
				areaToColor.width=texto.modelToView(color.getEndIndex()).x-areaToColor.x;
			}catch(Exception error){}	//TODO!!!
			g.setColor(color.getCor());
			g.fillRect(areaToColor.x,areaToColor.y,areaToColor.width,areaToColor.height);
		}
}