package element.tree.objeto.nodulo;

import architecture.rrf_vp.view.ViewJoint;

public class NoduloView implements ViewJoint<NoduloView,Nodulo> {
//ROOT
	private Nodulo root;
	@Override
	public Nodulo getRoot() {return root;}
//MAIN
	public NoduloView(Nodulo root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}