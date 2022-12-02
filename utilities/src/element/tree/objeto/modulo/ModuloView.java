package element.tree.objeto.modulo;

import architecture.rrf_vp.view.ViewJoint;

public class ModuloView implements ViewJoint<ModuloView,Modulo> {
//ROOT
	private Modulo root;
	@Override
	public Modulo getRoot() {return root;}
//MAIN
	public ModuloView(Modulo root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}