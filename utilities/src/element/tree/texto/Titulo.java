package element.tree.texto;
import element.tree.propriedades.Cor;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import component.text.java_based.plain.PlainTexto;
import element.tree.main.Tree;
import element.tree.main.TreeUI;
import element.tree.objeto.Objeto;
import element.tree.objeto.modulo.Modulo;
import element.tree.objeto.modulo.ModuloUI;
@SuppressWarnings("serial")
public class Titulo extends PlainTexto implements WithObj{
//TREE
	private Tree tree;
//OBJETO
	private final TextoWithObj textoWithObj=new TextoWithObj(this);
		public Objeto getObjeto(){return textoWithObj.getObjeto();}
		public void setObjeto(Objeto obj){textoWithObj.setObjeto(obj);}
		public void clearObjeto(){textoWithObj.clearObjeto();}
		public void addObjetoSetListener(ObjetoSetListener objetoSetListener){textoWithObj.addObjetoSetListener(objetoSetListener);}
		public void triggerObjetoSetListener(Objeto oldObj,Objeto newObj){textoWithObj.triggerObjetoSetListener(oldObj,newObj);}
//MAIN
	public Titulo(Tree tree){
		this.tree=tree;
		final Cor cor=Cor.getChanged(TreeUI.FUNDO,0.7f);
		setSelectionColor(cor);
		setSelectedTextColor(cor.isDark()?TreeUI.Fonte.LIGHT:TreeUI.Fonte.DARK);
	}
//FUNCS
@Override
	protected void paintComponent(Graphics imagemEdit){
		config(imagemEdit);
		imagemEdit.setColor(getBackground());
		final int round=ModuloUI.getRoundValue(Tree.UNIT);
		imagemEdit.fillRoundRect(0,0,getWidth(),getHeight(),round,round);
		super.paintComponent(imagemEdit);
	}
@Override
	protected void paintBorder(Graphics imagemEdit){
		config(imagemEdit);
		if(getObjeto()!=null){
			final Modulo mod=(Modulo)getObjeto();
			imagemEdit.setColor(Cor.getInverted(mod.getCor()));
			((Graphics2D)imagemEdit).setStroke(mod.getBorda().getVisual(TreeUI.getBordaValue(Tree.UNIT)));
		}else imagemEdit.setColor(getForeground());
		final int round=ModuloUI.getRoundValue(Tree.UNIT);
		imagemEdit.drawRoundRect(0,0,getWidth(),getHeight(),round,round);
	}
	private void config(Graphics imagemEdit){
		final boolean antialias=(tree.isAntialias()&&(tree.getObjetos().getAll().size()<tree.getObjetosLimite()));
		((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,(antialias?RenderingHints.VALUE_ANTIALIAS_ON:RenderingHints.VALUE_ANTIALIAS_OFF));
		((Graphics2D)imagemEdit).setStroke(new BasicStroke(TreeUI.getBordaValue(Tree.UNIT)));
	}

}