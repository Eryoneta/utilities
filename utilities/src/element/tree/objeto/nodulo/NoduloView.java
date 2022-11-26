package element.tree.objeto.nodulo;

import java.awt.Point;

import element.tree.objeto.ObjetoView;

public class NoduloView extends ObjetoView<Nodulo>{
//MAIN
	public NoduloView(Nodulo root) {
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