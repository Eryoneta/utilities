package element.tree.objeto.conexao;

import architecture.rrf_vp.plan.PlanJoint;

public class ConexaoPlan implements PlanJoint<ConexaoPlan,Conexao> {
//ROOT
	private Conexao root;
	@Override
	public Conexao getRoot() {return root;}
//MAIN
	public ConexaoPlan(Conexao root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}