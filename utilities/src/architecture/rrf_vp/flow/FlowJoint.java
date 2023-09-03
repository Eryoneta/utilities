package architecture.rrf_vp.flow;

import architecture.rrf_vp.root.RootJoint;

public interface FlowJoint<R extends RootJoint<R, ?, J, ?, ?>, J extends FlowJoint<R, J>> {
	public abstract R getRoot();
	public abstract void init();
}