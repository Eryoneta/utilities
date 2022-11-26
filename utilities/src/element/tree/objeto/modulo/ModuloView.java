package element.tree.objeto.modulo;

import java.awt.Point;

import element.tree.objeto.ObjetoView;

public class ModuloView extends ObjetoView<Modulo> {
//MAIN
	public ModuloView(Modulo root) {
		super(root);
	}
//FUNCS
	@Override
	protected void init() {}
	@Override
	public boolean contains(Point mouse) {
		root.rule.teste();
		return false;
	}
}