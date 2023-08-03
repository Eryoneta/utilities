package element.tree.selection_area;

import architecture.rrf_vp.plan.PlanJoint;

public class SelectionAreaPlan implements PlanJoint<SelectionAreaPlan,SelectionArea>{
//ROOT
	private SelectionArea root;
		@Override public SelectionArea getRoot() {return root;}
//MAIN
	public SelectionAreaPlan(SelectionArea root) {
		this.root=root;
	}
//FUNCS
	@Override public void init() {}
}