package element.tree.objeto.modulo;

import architecture.rrf_vp.flow.FlowJoint;

public class ModuloFlow implements FlowJoint<ModuloFlow,Modulo> {
//ROOT
	private Modulo root;
		@Override public Modulo getRoot() {return root;}
//MAIN
	public ModuloFlow(Modulo root) {
		this.root=root;
	}
//FUNCS
	@Override public void init() {}
}