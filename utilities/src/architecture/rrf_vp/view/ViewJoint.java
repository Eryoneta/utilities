package architecture.rrf_vp.view;

import architecture.rrf_vp.root.RootJoint;

public interface ViewJoint<S extends ViewJoint<S,R>,R extends RootJoint<R,?,?,S,?>>{
	public abstract R getRoot();
	public abstract void init();
}