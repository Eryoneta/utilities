package element.tree.objeto.conexao.segmento;

import architecture.rrf_vp.plan.PlanJoint;

public class SegmentoPlan implements PlanJoint<SegmentoPlan,Segmento> {
//ROOT
	private Segmento root;
	@Override
	public Segmento getRoot() {return root;}
//MAIN
	public SegmentoPlan(Segmento root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}