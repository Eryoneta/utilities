package element.tree.popup.borda;
import element.tree.Borda;
import element.tree.objeto.Objeto;
public interface BordaPickListener{
	public void bordaModified(Objeto[]objs,Borda[]oldBordas,Borda newBorda);
}