package architecture.modular_tree.plan;

import java.util.ArrayList;
import java.util.List;

import architecture.modular_tree.root.ModularRoot;
import architecture.rrf_vp.plan.PlanJoint;

public abstract class ModularPlan<
	R extends ModularRoot<R, ?, ?, ?, J>,
	J extends ModularPlan<R, J, PL>,
	PL extends ModularPlanListener<J, PL>
> implements PlanJoint<R, J> {
	// LISTENERS
	private List<PL> planListeners = new ArrayList<>();
	public List<PL> getPlanListeners() {
		return planListeners;
	}
	public void addPlanListener(PL planListener) {
		planListeners.add(planListener);
	}
}