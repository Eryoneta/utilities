package architecture.modular_tree.flow;

import architecture.modular_tree.plan.ModularPlan;
import architecture.modular_tree.plan.ModularPlanListener;
import architecture.modular_tree.root.ModularRoot;
import architecture.modular_tree.rule.ModularRule;
import architecture.modular_tree.rule.ModularRuleListener;
import architecture.modular_tree.view.ModularView;
import architecture.modular_tree.view.ModularViewListener;
import architecture.rrf_vp.flow.FlowJoint;
import architecture.state_flow.StateFlow;
import architecture.state_flow.state.StateId;
import architecture.state_flow.state.StateNode;

public abstract class ModularFlow<
	R extends ModularRoot<R, ?, J, ?, ?>,
	J extends ModularFlow<R, J, SI,SN>,
	SI extends StateId,
	SN extends StateNode 
		& ModularRuleListener<? extends ModularRule<R, ?, ?>, ?> 
		& ModularViewListener<? extends ModularView<R, ?, ?>, ?> 
		& ModularPlanListener<? extends ModularPlan<R, ?, ?>, ?>
> extends StateFlow<SI, SN> implements FlowJoint<R, J> {
	public abstract void initTriggers();
	public abstract void initFlow();
}