package element.tree.objeto.conexao.segmento;

import java.awt.Point;

import element.tree.objeto.ObjetoView;

public class SegmentoView extends ObjetoView<Segmento>{
//MAIN
	public SegmentoView(Segmento root) {
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