package architecture.rrf_vp.plan;

import architecture.rrf_vp.root.RootJoint;

public interface PlanJoint<S extends PlanJoint<S, R>, R extends RootJoint<R, ?, ?, ?, S>> {
	public abstract R getRoot();
	public abstract void init();
}