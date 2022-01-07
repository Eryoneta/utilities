package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMImageAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.view.SimpleParagraphView;
public class MMParagraphView extends SimpleParagraphView{
//MAIN
	public MMParagraphView(Element elem,MindMarkEditor editor){
		super(elem,editor);
	}
//FUNCS
@Override
	public void paint(Graphics g,Shape s){
		final Rectangle area=s.getBounds();
		final MMCitationAtributo.Citation citation=(MMCitationAtributo.Citation)getElement().getAttributes().getAttribute(MMCitationAtributo.CITATION);
		final Boolean line=(Boolean)getElement().getAttributes().getAttribute(MMLineAtributo.LINE);
		final MMListAtributo.List list=(MMListAtributo.List)getElement().getAttributes().getAttribute(MMListAtributo.LIST);
		final MMImageAtributo.Image image=(MMImageAtributo.Image)getElement().getAttributes().getAttribute(MMImageAtributo.IMAGE);
		if(citation!=null)drawCitation(g,s,area,citation);
			else if(line!=null)drawLine(g,s,area);
				else if(list!=null)drawList(g,s,area,list);
					else if(image!=null)drawImage(g,s,area,image);
						else super.paint(g,s);
	}
		private void drawCitation(Graphics g,Shape s,Rectangle area,MMCitationAtributo.Citation citation){
			final Float spaceAbove=(Float)getAttributes().getAttribute(StyleConstants.SpaceAbove);
			final int level=citation.getLevel();
		//FUNDO
			for(int l=0;l<level;l++){
				final float leftIndent=(MMCitationAtributo.LEFT_INDENT*l);
				final float spacedAbove=(l==level-1?spaceAbove:0f);
				switch(l){
					case 0:	g.setColor(MMCitationAtributo.BACKGROUND_COLOR_1);	break;
					case 1:	g.setColor(MMCitationAtributo.BACKGROUND_COLOR_2);	break;
					case 2:	g.setColor(MMCitationAtributo.BACKGROUND_COLOR_3);	break;
				}
				g.fillRect(
						(int)(area.x+leftIndent),
						(int)(area.y+spacedAbove),
						(int)(area.width-leftIndent),
						(int)(area.height-spacedAbove));
			}
		//LINHA
			final Graphics2D g2D=(Graphics2D)g;
			final Stroke oldStroke=g2D.getStroke();
			g2D.setStroke(new BasicStroke(MMCitationAtributo.LINE_WIDTH,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
			g.setColor(MMCitationAtributo.LINE_COLOR);
			for(int l=0;l<level;l++){
				final int x=area.x+(MMCitationAtributo.LEFT_INDENT*l);
				final float spacedAbove=(l==level-1?spaceAbove:0f);
				g.drawLine(
						x,(int)(area.y+spacedAbove),
						x,(int)(area.y+area.height));
			}
			g2D.setStroke(oldStroke);
		//TEXTO
			super.paint(g,s);
		}
		private void drawLine(Graphics g,Shape s,Rectangle area){
		//LINHA
			g.setColor(MMLineAtributo.LINE_COLOR);
			final Graphics2D g2D=(Graphics2D)g;
			final Stroke oldStroke=g2D.getStroke();
			g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
			final int y=area.y+(area.height/2);
			g.drawLine(area.x,y,area.width,y);
			g2D.setStroke(oldStroke);
		//TEXTO
			super.paint(g,s);
		}
		private void drawList(Graphics g,Shape s,Rectangle area,MMListAtributo.List list){
			final Float spaceAbove=(Float)getAttributes().getAttribute(StyleConstants.SpaceAbove);
			area.y+=spaceAbove;
			area.height-=spaceAbove;
		//BULLET
			g.setColor(MMListAtributo.BULLET_COLOR);
			final Graphics2D g2D=(Graphics2D)g;
			final Stroke oldStroke=g2D.getStroke();
			final float leftIndent=(float)getAttributes().getAttribute(StyleConstants.LeftIndent);
			final int size=MMListAtributo.BULLET_SIZE;
			final Rectangle bulletArea=new Rectangle(
					(int)(area.x+leftIndent-(size+MMListAtributo.LINE_AREA)),
					area.y+((area.height-size)/2),
					size,
					size
			);
			final float sizeSmall=size/6;
			switch(list.getBulletType()){
				case MMListAtributo.BULLET_0:
					g2D.setStroke(new BasicStroke(1));
					g.drawLine(bulletArea.x+(bulletArea.width/2),(int)(bulletArea.y+sizeSmall),
							bulletArea.x+(bulletArea.width/2),(int)(bulletArea.y+bulletArea.height-sizeSmall));
					g.drawLine((int)(bulletArea.x+sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+bulletArea.width-sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)));
					g.drawLine((int)(bulletArea.x+sizeSmall),(int)(bulletArea.y+sizeSmall),
							(int)(bulletArea.x+bulletArea.width-sizeSmall),(int)(bulletArea.y+bulletArea.height-sizeSmall));
					g.drawLine((int)(bulletArea.x+bulletArea.width-sizeSmall),(int)(bulletArea.y+sizeSmall),
							(int)(bulletArea.x+sizeSmall),(int)(bulletArea.y+bulletArea.height-sizeSmall));
				break;
				case MMListAtributo.BULLET_1:
					g2D.setStroke(new BasicStroke(2));
					g.drawLine(bulletArea.x+(bulletArea.width/2),(int)(bulletArea.y+sizeSmall),
							bulletArea.x+(bulletArea.width/2),(int)(bulletArea.y+bulletArea.height-sizeSmall));
					g.drawLine((int)(bulletArea.x+sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+bulletArea.width-sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
				case MMListAtributo.BULLET_2:
					g2D.setStroke(new BasicStroke(2));
					g.drawLine((int)(bulletArea.x+sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+bulletArea.width-sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
				case MMListAtributo.BULLET_3:
					g2D.setStroke(new BasicStroke(3));
					g.drawLine((int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
				case MMListAtributo.BULLET_4:
					g2D.setStroke(new BasicStroke(2));
					g.drawLine((int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
			}
			g2D.setStroke(new BasicStroke(MMListAtributo.LINE_WIDTH));
			final int x=(int)(area.x+leftIndent-(MMListAtributo.LINE_AREA/2));
		//LINHA
			g.drawLine(x,(int)(area.y+size),
					x,(int)(area.y+area.height-size));
			g2D.setStroke(oldStroke);
		//TEXTO
			super.paint(g,s);
		}
		private void drawImage(Graphics g,Shape s,Rectangle area,MMImageAtributo.Image image){
			
		}
}