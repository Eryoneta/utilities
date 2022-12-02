package architecture.rrf_vp.rule;

import architecture.rrf_vp.root.RootJoint;

public class RuleNull<R extends RootJoint<R,RuleNull<R>,?,?,?>> implements RuleJoint<RuleNull<R>, R>{
	@Override public R getRoot() {return null;}
	@Override public void init() {}
}