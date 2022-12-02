package element.tree.objeto.modulo;

import architecture.rrf_vp.plan.PlanJoint;

public class ModuloPlan implements PlanJoint<ModuloPlan,Modulo> {
//ROOT
	private Modulo root;
	@Override
	public Modulo getRoot() {return root;}
//MAIN
	public ModuloPlan(Modulo root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}