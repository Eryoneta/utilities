package element.tree;

import architecture.rrf_vp.flow.FlowJoint;

public class TreeFlow implements FlowJoint<TreeFlow,Tree> {
//ROOT
	private Tree root;
	@Override
	public Tree getRoot() {return root;}
//MAIN
	public TreeFlow(Tree root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}