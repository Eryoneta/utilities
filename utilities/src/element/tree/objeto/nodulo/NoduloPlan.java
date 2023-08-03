package element.tree.objeto.nodulo;

import architecture.rrf_vp.plan.PlanJoint;

public class NoduloPlan implements PlanJoint<NoduloPlan,Nodulo> {
//ROOT
	private Nodulo root;
	@Override
	public Nodulo getRoot() {return root;}
//MAIN
	public NoduloPlan(Nodulo root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}