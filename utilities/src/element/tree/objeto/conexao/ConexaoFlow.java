package element.tree.objeto.conexao;

import architecture.rrf_vp.flow.FlowJoint;

public class ConexaoFlow implements FlowJoint<ConexaoFlow,Conexao> {
//ROOT
	private Conexao root;
	@Override
	public Conexao getRoot() {return root;}
//MAIN
	public ConexaoFlow(Conexao root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}