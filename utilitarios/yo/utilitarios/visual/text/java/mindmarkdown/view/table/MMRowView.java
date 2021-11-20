package utilitarios.visual.text.java.mindmarkdown.view.table;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.View;
public class MMRowView extends BoxView{
//MAIN
	public MMRowView(Element elem){
		super(elem, View.X_AXIS);
	}
//FUNCS
@Override
	public float getPreferredSpan(int axis){return super.getPreferredSpan(axis);}
@Override
	protected void layout(int width,int height){super.layout(width, height);}
@Override
	public float getMinimumSpan(int axis){return getPreferredSpan(axis);}
@Override
	public float getMaximumSpan(int axis){return getPreferredSpan(axis);}
@Override
	public float getAlignment(int axis){return 0;}
//DRAW
@Override
	protected void paintChild(Graphics g,Rectangle area,int index){
		super.paintChild(g,area,index);
		g.setColor(Color.BLACK);
		final int height=(int)getPreferredSpan(View.Y_AXIS) - 1;
		g.drawLine(area.x,area.y,area.x,area.y+height);
		g.drawLine(area.x+area.width,area.y,area.x+area.width,area.y+height);
	}
}