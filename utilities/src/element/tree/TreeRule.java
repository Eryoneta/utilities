package element.tree;

import architecture.rrf_vp.rule.RuleJoint;

public class TreeRule implements RuleJoint<TreeRule,Tree> {
//ROOT
	private Tree root;
	@Override
	public Tree getRoot() {return root;}
//MAIN
	public TreeRule(Tree root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}