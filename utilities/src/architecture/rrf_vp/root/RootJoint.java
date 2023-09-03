package architecture.rrf_vp.root;

import architecture.rrf_vp.flow.FlowJoint;
import architecture.rrf_vp.plan.PlanJoint;
import architecture.rrf_vp.rule.RuleJoint;
import architecture.rrf_vp.view.ViewJoint;

public interface RootJoint<
	J extends RootJoint<J, R, F, V, P>,
	R extends RuleJoint<J, R>,
	F extends FlowJoint<J, F>,
	V extends ViewJoint<J, V>,
	P extends PlanJoint<J, P>
> {
	public abstract R getRule();
	public abstract F getFlow();
	public abstract V getView();
	public abstract P getPlan();
}