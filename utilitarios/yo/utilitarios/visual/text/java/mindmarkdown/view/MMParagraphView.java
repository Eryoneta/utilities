package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.mindmarkdown.MindMarkTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMImageAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMTextStylerAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo.AtributeList;
import utilitarios.visual.text.java.view.SimpleParagraphView;
@SuppressWarnings("unchecked")
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
		final AtributeList<MMTextStylerAtributo.ColorSection>cores=
				(AtributeList<MMTextStylerAtributo.ColorSection>)getElement().getAttributes().getAttribute(MMTextStylerAtributo.LINE_COLOR);
		if(cores!=null){
			for(MMTextStylerAtributo.ColorSection cor:cores)drawColor(g,s,cor);
		}
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
			Rectangle startPosition=new Rectangle();
			Rectangle endPosition=new Rectangle();
			try{
				startPosition=texto.modelToView(Math.max(color.getStartIndex(),getElement().getStartOffset()));
				endPosition=texto.modelToView(Math.min(color.getEndIndex(),getElement().getEndOffset()-1));
			}catch(BadLocationException error){}
			g.setColor(color.getCor());
		//LEFT_INDENT
			Float leftIndent=(Float)getAttributes().getAttribute(StyleConstants.LeftIndent);
			if(leftIndent==null)leftIndent=0f;
			final int indentedX=(int)(area.x+leftIndent);
		//LINE_WRAPPED
			if(color.getEndIndex()<=getElement().getEndOffset()&&startPosition.y<endPosition.y){
				g.fillRect(
						startPosition.x,
						startPosition.y,
						area.width-startPosition.x,
						startPosition.height);
				g.fillRect(
						indentedX,
						startPosition.y+startPosition.height,
						area.width-indentedX,
						endPosition.y-startPosition.y-startPosition.height);
				g.fillRect(
						indentedX,
						endPosition.y,
						endPosition.x-indentedX,
						endPosition.height);
		//NORMAL
			}else{
				final Rectangle areaToColor=new Rectangle(
						startPosition.x,
						startPosition.y,
						endPosition.x-startPosition.x,
						endPosition.y-startPosition.y+startPosition.height);
				if(color.getEndIndex()>getElement().getEndOffset()-1){
					areaToColor.width=area.width-areaToColor.x;		//AREA SEGUE ATÉ O FIM DA LINHA
				}
				g.fillRect(areaToColor.x,areaToColor.y,areaToColor.width,areaToColor.height);
			}
		}
}