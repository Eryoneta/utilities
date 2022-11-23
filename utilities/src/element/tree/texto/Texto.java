package element.tree.texto;
import element.tree.propriedades.Cor;
import component.text.java_based.mindmarkdown.MindMarkdownTexto;
import element.tree.main.TreeUI;
import element.tree.objeto.Objeto;
@SuppressWarnings("serial")
public class Texto extends MindMarkdownTexto implements WithObj{
//OBJETO
	private final TextoWithObj textoWithObj=new TextoWithObj(this);
		public Objeto getObjeto(){return textoWithObj.getObjeto();}
		public void setObjeto(Objeto obj){textoWithObj.setObjeto(obj);}
		public void clearObjeto(){textoWithObj.clearObjeto();}
		public void addObjetoSetListener(ObjetoSetListener objetoSetListener){textoWithObj.addObjetoSetListener(objetoSetListener);}
		public void triggerObjetoSetListener(Objeto oldObj,Objeto newObj){textoWithObj.triggerObjetoSetListener(oldObj,newObj);}
//MAIN
	public Texto(){
		final Cor cor=Cor.getChanged(TreeUI.FUNDO,0.7f);
		setSelectionColor(cor);
		setSelectedTextColor(cor.isDark()?TreeUI.Fonte.LIGHT:TreeUI.Fonte.DARK);
	}
}