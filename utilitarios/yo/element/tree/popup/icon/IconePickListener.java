package element.tree.popup.icon;
import element.tree.Icone;
import element.tree.objeto.modulo.Modulo;
public interface IconePickListener{
	public void iconAdded(Modulo[]mods,Icone icone);
	public void iconRemoved(Modulo[]mods,Icone icone);
}