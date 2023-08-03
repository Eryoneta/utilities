package element.tree.objeto.nodulo;

import architecture.rrf_vp.flow.FlowJoint;

public class NoduloFlow implements FlowJoint<NoduloFlow,Nodulo> {
//ROOT
	private Nodulo root;
	@Override
	public Nodulo getRoot() {return root;}
//MAIN
	public NoduloFlow(Nodulo root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}