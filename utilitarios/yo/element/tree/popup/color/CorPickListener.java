package element.tree.popup.color;
import element.tree.propriedades.Cor;
import element.tree.objeto.modulo.Modulo;
public interface CorPickListener{
	public void colorModified(Modulo[]mods,Cor[]oldCores,Cor newCor);
}