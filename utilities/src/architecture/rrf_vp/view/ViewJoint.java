package architecture.rrf_vp.view;

import architecture.rrf_vp.root.RootJoint;

public interface ViewJoint<R extends RootJoint<R, ?, ?, ?, ?>, J extends ViewJoint<R, J>> {
	public abstract R getRoot();
	public abstract void init();
}