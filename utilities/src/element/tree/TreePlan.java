package element.tree;

import architecture.rrf_vp.plan.PlanJoint;
import element.tree.objeto.modulo.Modulo;

public class TreePlan implements PlanJoint<TreePlan,Tree> {
//ROOT
	private Tree root;
		@Override public Tree getRoot() {return root;}
//MÓDULOS ESPECIAIS
	private Modulo mestre;
		public Modulo getMestre(){return mestre;}
	private Modulo ghost;
		public Modulo getGhost(){return ghost;}
//CONFIGURAÇÕES
	private boolean enabled=true;
		public void setEnabled(boolean enabled){
			this.enabled=enabled;
			getRoot().getView().draw();
		}
		public boolean isEnabled(){return enabled;}
//MAIN
	public TreePlan(Tree root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}