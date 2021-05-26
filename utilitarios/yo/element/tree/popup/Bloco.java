package element.tree.popup;
import java.awt.Rectangle;
@SuppressWarnings("serial")
public class Bloco extends Rectangle{
//FOCO
	private boolean focus;
		public void setFocus(boolean foco){focus=foco;}
		public boolean isFocused(){return focus;}

}