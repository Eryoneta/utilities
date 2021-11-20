package utilitarios.visual.text.java.mindmarkdown.view.table;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.View;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
public class MMCellView extends BoxView{
//MAIN
	public MMCellView(Element elem){
		super(elem, View.Y_AXIS);
		setInsets((short)2,(short)2,(short)2,(short)2);
	}
//FUNCS
@Override
	public float getPreferredSpan(int axis){
		if(axis==View.X_AXIS)return getCellWidth();
		return super.getPreferredSpan(axis);
	}
@Override
	public float getMinimumSpan(int axis){return getPreferredSpan(axis);}
@Override
	public float getMaximumSpan(int axis){return getPreferredSpan(axis);}
@Override
	public float getAlignment(int axis){return 0;}
	public int getCellWidth(){
		int width=100;
		final Integer i=(Integer)getAttributes().getAttribute(MindMarkDocumento.CellParamWidth);
		if(i!=null)width=i.intValue();
		return width;
	}
}