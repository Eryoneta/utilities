package architecture.modular_tree.root;

import architecture.modular_tree.flow.ModularFlow;
import architecture.modular_tree.plan.ModularPlan;
import architecture.modular_tree.rule.ModularRule;
import architecture.modular_tree.view.ModularView;
import architecture.rrf_vp.root.RootJoint;

public interface ModularRoot<
	J extends ModularRoot<J, R, F, V, P>,
	R extends ModularRule<J, R, ?>,
	F extends ModularFlow<J, F, ?, ?>,
	V extends ModularView<J, V, ?>,
	P extends ModularPlan<J, P, ?>
> extends RootJoint<J, R, F, V, P> {}