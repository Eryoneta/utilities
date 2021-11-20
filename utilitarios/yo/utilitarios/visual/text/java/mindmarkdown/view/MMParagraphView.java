package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.text.Element;
import javax.swing.text.View;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.view.SimpleParagraphView;
public class MMParagraphView extends SimpleParagraphView{
//VAR GLOBAIS
	public static Color BACKGROUND=new Color(230,230,230);
	public static Color LINE=new Color(200,200,200);
//MAIN
	public MMParagraphView(Element elem,MindMarkEditor editor){
		super(elem,editor);
	}
//FUNCS
//@Override
//	protected void paintChild(Graphics g,Rectangle area,int index){
//		if(getAttributes().containsAttribute(MindMarkDocumento.CITATION,true)){
//		//FUNDO
//			g.setColor(BACKGROUND);
//			g.fillRect(0,area.y,(int)getMaximumSpan(View.X_AXIS),area.height);
//		//TEXTO
//			super.paintChild(g,area,index);
//		//LINHA
//			final Graphics2D g2D=(Graphics2D)g;
//			final Stroke stroke=g2D.getStroke();
//			g2D.setStroke(new BasicStroke(10));
//			g.setColor(LINE);	//TODO: ???
//			g2D.setStroke(stroke);
//			g.drawLine(4,area.y,4,area.y+area.height);
//		}else super.paintChild(g,area,index);
//	}
@Override
	public void paint(Graphics g,Shape s){
		final Rectangle area=s.getBounds();
		if(getAttributes().containsAttribute(MindMarkDocumento.CITATION,true)){
		//FUNDO
			g.setColor(BACKGROUND);
			g.fillRect(0,area.y,(int)getMaximumSpan(View.X_AXIS),area.height);
		//TEXTO
			super.paint(g,s);
		//LINHA
			final Graphics2D g2D=(Graphics2D)g;
			final Stroke stroke=g2D.getStroke();
			g2D.setStroke(new BasicStroke(10));
			g.setColor(LINE);	//TODO: ???
			g2D.setStroke(stroke);
			g.drawLine(4,area.y,4,area.y+area.height);
		}else super.paint(g,s);
	}
}