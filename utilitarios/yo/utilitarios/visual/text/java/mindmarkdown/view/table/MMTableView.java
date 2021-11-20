package utilitarios.visual.text.java.mindmarkdown.view.table;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.View;
public class MMTableView extends BoxView{
//MAIN
	public MMTableView(Element elem){
		super(elem,View.Y_AXIS);
	}
//FUNCS
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
		g.drawLine(area.x,area.y,area.x+area.width,area.y);
		int lastY=area.y+area.height;
		if(index==getViewCount()-1)lastY--;
		g.drawLine(area.x,lastY,area.x+area.width,lastY);
	}
}