package architecture.rrf_vp.view;

import architecture.rrf_vp.root.RootJoint;

public class ViewNull<R extends RootJoint<R,?,?,ViewNull<R>,?>> implements ViewJoint<ViewNull<R>, R>{
	@Override public R getRoot() {return null;}
	@Override public void init() {}
}