package element.tree.objeto.modulo;

import architecture.rrf_vp.rule.RuleJoint;

public class ModuloRule implements RuleJoint<ModuloRule,Modulo> {
//ROOT
	private Modulo root;
		@Override public Modulo getRoot() {return root;}
//MAIN
	public ModuloRule(Modulo root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}