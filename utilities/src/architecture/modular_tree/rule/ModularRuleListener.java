package architecture.modular_tree.rule;

public interface ModularRuleListener<R extends ModularRule<?, R, RL>, RL extends ModularRuleListener<R, RL>> {}