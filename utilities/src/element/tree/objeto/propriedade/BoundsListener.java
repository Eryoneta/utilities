package element.tree.objeto.propriedade;

import java.awt.Rectangle;

import element.tree.objeto.Objeto;

public interface BoundsListener{
	public void boundsChanged(Objeto obj,Rectangle oldBoundsIndex,Rectangle newBoundsIndex);
}