package element.tree.objeto.conexao.segmento;

import architecture.rrf_vp.rule.RuleJoint;

public class SegmentoRule implements RuleJoint<SegmentoRule,Segmento> {
//ROOT
	private Segmento root;
	@Override
	public Segmento getRoot() {return root;}
//MAIN
	public SegmentoRule(Segmento root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}