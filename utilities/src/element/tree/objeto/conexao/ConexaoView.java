package element.tree.objeto.conexao;

import java.awt.Point;

import element.tree.objeto.ObjetoView;

public class ConexaoView extends ObjetoView<Conexao>{
//MAIN
	public ConexaoView(Conexao root) {
		super(root);
	}
//FUNCS
	@Override
	protected void init() {}
	@Override
	public boolean contains(Point mouse) {
		return false;
	}
}