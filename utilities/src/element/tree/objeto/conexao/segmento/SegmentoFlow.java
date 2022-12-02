package element.tree.objeto.conexao.segmento;

import architecture.rrf_vp.flow.FlowJoint;

public class SegmentoFlow implements FlowJoint<SegmentoFlow,Segmento> {
//ROOT
	private Segmento root;
	@Override
	public Segmento getRoot() {return root;}
//MAIN
	public SegmentoFlow(Segmento root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}