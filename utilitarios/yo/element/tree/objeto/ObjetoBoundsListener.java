package element.tree.objeto;
import java.awt.Rectangle;
public interface ObjetoBoundsListener{
	public void boundsChanged(Objeto obj,Rectangle oldBoundsIndex,Rectangle newBoundsIndex);
}