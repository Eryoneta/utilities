package element.tree.objeto.conexao;

import architecture.rrf_vp.rule.RuleJoint;

public class ConexaoRule implements RuleJoint<ConexaoRule,Conexao> {
//ROOT
	private Conexao root;
	@Override
	public Conexao getRoot() {return root;}
//MAIN
	public ConexaoRule(Conexao root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}