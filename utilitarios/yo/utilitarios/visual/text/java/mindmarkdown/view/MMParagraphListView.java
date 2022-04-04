package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
public class MMParagraphListView{
//VIEW_PAI
	private MMParagraphView paragraphView;
//MAIN
	public MMParagraphListView(MMParagraphView paragraphView){
		this.paragraphView=paragraphView;
	}
//DRAW
	public void draw(Graphics g,Shape s,MMListAtributo.List list){
		final Rectangle area=s.getBounds();
		final float spaceAbove=(float)paragraphView.getAttributes().getAttribute(StyleConstants.SpaceAbove);
		area.y+=spaceAbove;
		area.height-=spaceAbove;
	//BULLET
		g.setColor(MMListAtributo.COLOR);
		final Graphics2D g2D=(Graphics2D)g;
		final Stroke oldStroke=g2D.getStroke();
		final float leftIndent=(float)paragraphView.getAttributes().getAttribute(StyleConstants.LeftIndent);
		final int size=MMListAtributo.BULLET_SIZE;
		final Rectangle bulletArea=new Rectangle(
				(int)(area.x+leftIndent-(size+MMListAtributo.LINE_AREA)),
				area.y+((area.height-size)/2),
				size,
				size
		);
		g2D.setStroke(new BasicStroke(1));
		for(int i=0;i<list.getIndentationLevel()-1;i++){
			final int x=(int)(area.x+(MMListAtributo.LEFT_INDENT*i));
			g.drawLine(x,(int)(area.y-spaceAbove),
					x,(int)(area.y+area.height+spaceAbove));
		}
		if(list.isImmediateSon()){
			final int x=(int)(area.x+(MMListAtributo.LEFT_INDENT*(list.getIndentationLevel()-1-1)));
			g.drawLine(x+((size+MMListAtributo.LINE_AREA)/2),(int)(area.y-spaceAbove),
					x,(int)(area.y-spaceAbove));
		}
		final float sizeSmall=size/6;
		if(!list.isExtension()){
			switch(list.getBulletType()){
				case CHEFE:
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
				case PAI:
					g2D.setStroke(new BasicStroke(2));
					g.drawLine(bulletArea.x+(bulletArea.width/2),(int)(bulletArea.y+sizeSmall),
							bulletArea.x+(bulletArea.width/2),(int)(bulletArea.y+bulletArea.height-sizeSmall));
					g.drawLine((int)(bulletArea.x+sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+bulletArea.width-sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
				case SON_0:
					g2D.setStroke(new BasicStroke(2));
					g.drawLine((int)(bulletArea.x+sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+bulletArea.width-sizeSmall),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
				case SON_1:
					g2D.setStroke(new BasicStroke(3));
					g.drawLine((int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
				case SON_2:
					g2D.setStroke(new BasicStroke(2));
					g.drawLine((int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)),
							(int)(bulletArea.x+(bulletArea.width/2)),(int)(bulletArea.y+(bulletArea.height/2)));
				break;
			}
		}else{
			g2D.setStroke(new BasicStroke(1));
			g.drawLine((int)(bulletArea.x+(bulletArea.width/2)),bulletArea.y+bulletArea.height,
					bulletArea.x+bulletArea.width,bulletArea.y+bulletArea.height);
			g.drawLine((int)(bulletArea.x+(bulletArea.width/2)),area.y,
					(int)(bulletArea.x+(bulletArea.width/2)),bulletArea.y+bulletArea.height);
		}
		g2D.setStroke(new BasicStroke(MMListAtributo.LINE_WIDTH));
		final int x=(int)(area.x+leftIndent-(MMListAtributo.LINE_AREA/2));
	//LINHA
		if(!list.isExtension()){
			g.drawLine(x,(int)(area.y+size),
					x,(int)(area.y+area.height-size));
		}else{
			g.drawLine(x,(int)(area.y-size),
					x,(int)(area.y+area.height));
		}
		g2D.setStroke(oldStroke);
	//TEXTO
		paragraphView.paintContent(g,s);
	}
}