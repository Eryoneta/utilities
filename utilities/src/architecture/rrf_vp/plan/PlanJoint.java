package architecture.rrf_vp.plan;

import architecture.rrf_vp.root.RootJoint;

public interface PlanJoint<R extends RootJoint<R, ?, ?, ?, J>, J extends PlanJoint<R, J>> {
	public abstract R getRoot();
	public abstract void init();
}