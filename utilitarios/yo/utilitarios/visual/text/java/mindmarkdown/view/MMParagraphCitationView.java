package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo.Citation;
public class MMParagraphCitationView {
//VIEW_PAI
	private MMParagraphView paragraphView;
//MAIN
	public MMParagraphCitationView(MMParagraphView paragraphView){
		this.paragraphView=paragraphView;
	}
//DRAW
	public void draw(Graphics g,Shape s,MMCitationAtributo.Citation citation){
		final Rectangle area=s.getBounds();
		final float spaceAbove=(float)paragraphView.getAttributes().getAttribute(StyleConstants.SpaceAbove);
		final int totalLevel=citation.getTotalLevel();
	//FUNDO
		for(int l=0;l<totalLevel;l++){
			final float leftIndent=(MMCitationAtributo.LEFT_INDENT*l);
			final float spacedAbove=(citation.getLevels()[l].getType()==Citation.Level.Type.HEAD?spaceAbove:0f);
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
		for(int l=0;l<totalLevel;l++){
			final int x=area.x+(MMCitationAtributo.LEFT_INDENT*l);
			final float spacedAbove=(citation.getLevels()[l].getType()==Citation.Level.Type.HEAD?spaceAbove:0f);
			g.drawLine(
					x,(int)(area.y+spacedAbove),
					x,(int)(area.y+area.height));
		}
		g2D.setStroke(oldStroke);
	//TEXTO
		paragraphView.paintContent(g,s);
	}
}
