package architecture.rrf_vp.flow;

import architecture.rrf_vp.root.RootJoint;

public interface FlowJoint<S extends FlowJoint<S, R>, R extends RootJoint<R, ?, S, ?, ?>> {
	public abstract R getRoot();
	public abstract void init();
}