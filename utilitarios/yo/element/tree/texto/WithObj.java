package element.tree.texto;
import element.tree.objeto.Objeto;
public interface WithObj{
//OBJETO
	public Objeto getObjeto();
	public void setObjeto(Objeto obj);
	public void clearObjeto();
//LISTENER
	public void addObjetoSetListener(ObjetoSetListener objetoSetListener);
	public void triggerObjetoSetListener(Objeto oldObj,Objeto newObj);
}