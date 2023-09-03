package architecture.rrf_vp.rule;

import architecture.rrf_vp.root.RootJoint;

public interface RuleJoint<R extends RootJoint<R, J, ?, ?, ?>, J extends RuleJoint<R, J>> {
	public abstract R getRoot();
	public abstract void init();
}