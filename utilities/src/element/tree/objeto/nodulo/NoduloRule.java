package element.tree.objeto.nodulo;

import architecture.rrf_vp.rule.RuleJoint;

public class NoduloRule implements RuleJoint<NoduloRule,Nodulo> {
//ROOT
	private Nodulo root;
	@Override
	public Nodulo getRoot() {return root;}
//MAIN
	public NoduloRule(Nodulo root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}