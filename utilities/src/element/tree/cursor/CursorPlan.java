package element.tree.cursor;

import architecture.rrf_vp.plan.PlanJoint;

public class CursorPlan implements PlanJoint<CursorPlan,Cursor> {
//ROOT
	private Cursor root;
	@Override
	public Cursor getRoot() {return root;}
//MAIN
	public CursorPlan(Cursor root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}