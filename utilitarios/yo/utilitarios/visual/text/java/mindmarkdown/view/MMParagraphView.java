package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMImageAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.view.SimpleParagraphView;
public class MMParagraphView extends SimpleParagraphView{
//
//	private boolean showSpecial=false;
//		public boolean isShowingSpecial(){return showSpecial;}
//		public void setShowSpecial(boolean showSpecial){this.showSpecial=showSpecial;}
//MAIN
	public MMParagraphView(Element elem,MindMarkEditor editor){
		super(elem,editor);
	}
//FUNCS
@Override
	public void paint(Graphics g,Shape s){
		final Rectangle area=s.getBounds();
		final MMCitationAtributo.Citation citation=(MMCitationAtributo.Citation)getElement().getAttributes().getAttribute(MMCitationAtributo.CITATION);
		final MMLineAtributo.Line line=(MMLineAtributo.Line)getElement().getAttributes().getAttribute(MMLineAtributo.LINE);
		final MMListAtributo.List list=(MMListAtributo.List)getElement().getAttributes().getAttribute(MMListAtributo.LIST);
		final MMImageAtributo.Image image=(MMImageAtributo.Image)getElement().getAttributes().getAttribute(MMImageAtributo.IMAGE);
		if(citation!=null)drawCitation(g,s,area,citation);
			else if(line!=null)drawLine(g,s,area,line);
				else if(list!=null)drawList(g,s,area,list);
					else if(image!=null)drawImage(g,s,area,image);
						else super.paint(g,s);
	}
	//CITATION
		private void drawCitation(Graphics g,Shape s,Rectangle area,MMCitationAtributo.Citation citation){
			final float spaceAbove=(float)getAttributes().getAttribute(StyleConstants.SpaceAbove);
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
	//LINE
		private void drawLine(Graphics g,Shape s,Rectangle area,MMLineAtributo.Line line){
		//LINHA
			g.setColor(MMLineAtributo.LINE_COLOR);
			final Graphics2D g2D=(Graphics2D)g;
			final Stroke oldStroke=g2D.getStroke();
			g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			final int y=area.y+(area.height/2);
			final int mediumSpace=area.width/8;
			final int bigSpace=area.width/4;
			switch(line.getType()){
				case MMLineAtributo.TYPE_0:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
					g.drawLine(area.x,y,area.x+area.width,y);
				break;
				case MMLineAtributo.TYPE_1:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
					g.drawLine(area.x+mediumSpace,y,area.x+area.width-mediumSpace,y);
				break;
				case MMLineAtributo.TYPE_2:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
					g.drawLine(area.x+bigSpace,y,area.x+area.width-bigSpace,y);
				break;
				case MMLineAtributo.TYPE_3:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
					g.drawLine(area.x,y,area.x+(area.width/2)-area.height,y);
					g.fillOval(area.x+(area.width/2)-(area.height/2),area.y,area.height,area.height);
					g.drawLine(area.x+(area.width/2)+area.height,y,area.x+area.width,y);
				break;
				case MMLineAtributo.TYPE_4:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
					g.drawLine(area.x+mediumSpace,y,area.x+(area.width/2)-area.height,y);
					g.fillOval(area.x+(area.width/2)-(area.height/2),area.y,area.height,area.height);
					g.drawLine(area.x+(area.width/2)+area.height,y,area.x+area.width-mediumSpace,y);
				break;
				case MMLineAtributo.TYPE_5:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
					g.drawLine(area.x+bigSpace,y,area.x+(area.width/2)-area.height,y);
					g.fillOval(area.x+(area.width/2)-(area.height/2),area.y,area.height,area.height);
					g.drawLine(area.x+(area.width/2)+area.height,y,area.x+area.width-bigSpace,y);
				break;
				case MMLineAtributo.TYPE_6:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
					g.fillPolygon(new int[]{area.x,area.x+(area.width/2),area.x+area.width,area.x+(area.width/2)},
							new int[]{y,area.y,y,area.y+area.height},4);
				break;
				case MMLineAtributo.TYPE_7:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
					g.fillPolygon(new int[]{area.x+mediumSpace,area.x+(area.width/2),area.x+area.width-mediumSpace,area.x+(area.width/2)},
							new int[]{y,area.y,y,area.y+area.height},4);
				break;
				case MMLineAtributo.TYPE_8:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
					g.fillPolygon(new int[]{area.x+bigSpace,area.x+(area.width/2),area.x+area.width-bigSpace,area.x+(area.width/2)},
							new int[]{y,area.y,y,area.y+area.height},4);
				break;
				case MMLineAtributo.TYPE_9:
					g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
					g2D.setPaint(new GradientPaint(
							area.x,y,Color.WHITE,area.x+(area.width/2),y,
							MMLineAtributo.LINE_COLOR,true));
					g.drawLine(area.x,y,area.x+area.width,y);
				break;
			}
			g2D.setStroke(oldStroke);
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
		//TEXTO
			super.paint(g,s);
		}
	//LIST
		private void drawList(Graphics g,Shape s,Rectangle area,MMListAtributo.List list){
			final float spaceAbove=(float)getAttributes().getAttribute(StyleConstants.SpaceAbove);
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
	//IMAGE
		private void drawImage(Graphics g,Shape s,Rectangle area,MMImageAtributo.Image image){
			final float spaceAbove=(float)getAttributes().getAttribute(StyleConstants.SpaceAbove);
			g.setColor(Color.GREEN);
			g.drawRect(area.x,area.y,area.width,(int)spaceAbove);
			//TEXTO
				super.paint(g,s);
		}
}