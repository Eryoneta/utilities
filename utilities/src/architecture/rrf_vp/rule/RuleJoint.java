package architecture.rrf_vp.rule;

import architecture.rrf_vp.root.RootJoint;

public interface RuleJoint<S extends RuleJoint<S,R>,R extends RootJoint<R,S,?,?,?>>{
	public abstract R getRoot();
	public abstract void init();
}