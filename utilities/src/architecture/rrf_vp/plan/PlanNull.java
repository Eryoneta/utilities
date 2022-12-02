package architecture.rrf_vp.plan;

import architecture.rrf_vp.root.RootJoint;

public class PlanNull<R extends RootJoint<R,?,?,?,PlanNull<R>>> implements PlanJoint<PlanNull<R>, R>{
	@Override public R getRoot() {return null;}
	@Override public void init() {}
}