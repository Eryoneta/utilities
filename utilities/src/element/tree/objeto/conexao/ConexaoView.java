package element.tree.objeto.conexao;

import architecture.rrf_vp.view.ViewJoint;

public class ConexaoView implements ViewJoint<ConexaoView,Conexao> {
//ROOT
	private Conexao root;
	@Override
	public Conexao getRoot() {return root;}
//MAIN
	public ConexaoView(Conexao root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}