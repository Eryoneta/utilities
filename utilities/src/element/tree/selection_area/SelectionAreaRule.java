package element.tree.selection_area;

import architecture.rrf_vp.rule.RuleJoint;

public class SelectionAreaRule implements RuleJoint<SelectionAreaRule,SelectionArea>{
//ROOT
	private SelectionArea root;
		@Override public SelectionArea getRoot() {return root;}
//MAIN
	public SelectionAreaRule(SelectionArea root) {
		this.root=root;
	}
//FUNCS
	@Override public void init() {}
}