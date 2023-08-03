package architecture.rrf_vp.flow;

import architecture.rrf_vp.root.RootJoint;

public class FlowNull<R extends RootJoint<R,?,FlowNull<R>,?,?>> implements FlowJoint<FlowNull<R>, R>{
	@Override public R getRoot() {return null;}
	@Override public void init() {}
}