package element.tree;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import element.tree.objeto.modulo.ModuloUI;
import element.tree.propriedades.Cor;
public class SelecaoUI{
//SELEÇÃO
	private Selecao selecao;
//MAIN
	public SelecaoUI(Selecao selecao){
		this.selecao=selecao;
	}
//DRAW
	public void draw(Graphics2D imagemEdit){
		imagemEdit.setStroke(new BasicStroke(1));
		switch(selecao.getState()){
			case TO_SELECT:default:	imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.FUNDO,0.6f));	break;
			case TO_CREATE:			imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.CREATE,0.6f));	break;
			case TO_CREATE_SON:		imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.SON,0.6f));		break;
			case TO_CREATE_PAI:		imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.PAI,0.6f));		break;
			case TO_DELETE:			imagemEdit.setColor(Cor.getTransparent(ModuloUI.Cores.DELETE,0.6f));	break;
		}
		imagemEdit.fillRect(selecao.getAncoraX(),selecao.getAncoraY(),selecao.getAreaX(),selecao.getAreaY());
		switch(selecao.getState()){
			case TO_SELECT:default:	imagemEdit.setColor(ModuloUI.Cores.FUNDO);	break;
			case TO_CREATE:			imagemEdit.setColor(ModuloUI.Cores.CREATE);	break;
			case TO_CREATE_SON:		imagemEdit.setColor(ModuloUI.Cores.SON);		break;
			case TO_CREATE_PAI:		imagemEdit.setColor(ModuloUI.Cores.PAI);		break;
			case TO_DELETE:			imagemEdit.setColor(ModuloUI.Cores.DELETE);	break;
		}
		imagemEdit.drawRect(selecao.getAncoraX(),selecao.getAncoraY(),selecao.getAreaX(),selecao.getAreaY());
	}
}