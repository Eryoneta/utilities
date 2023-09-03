package architecture.modular_tree.rule;

import java.util.ArrayList;
import java.util.List;

import architecture.modular_tree.root.ModularRoot;
import architecture.rrf_vp.rule.RuleJoint;

public abstract class ModularRule<
	R extends ModularRoot<R, J, ?, ?, ?>,
	J extends ModularRule<R, J, RL>,
	RL extends ModularRuleListener<J, RL>
> implements RuleJoint<R, J> {
	// LISTENERS
	private List<RL> ruleListeners = new ArrayList<>();
	public List<RL> getRuleListeners() {
		return ruleListeners;
	}
	public void addRuleListener(RL ruleListener) {
		ruleListeners.add(ruleListener);
	}
}