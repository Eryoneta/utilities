package architecture.rrf_vp.root;

import architecture.rrf_vp.flow.FlowJoint;
import architecture.rrf_vp.plan.PlanJoint;
import architecture.rrf_vp.rule.RuleJoint;
import architecture.rrf_vp.view.ViewJoint;

public interface RootJoint<S extends RootJoint<S, R, F, V, P>, R extends RuleJoint<R, S>, F extends FlowJoint<F, S>, V extends ViewJoint<V, S>, P extends PlanJoint<P, S>> {
	public abstract R getRule();
	public abstract F getFlow();
	public abstract V getView();
	public abstract P getPlan();
}