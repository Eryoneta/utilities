package element.tree.objeto.conexao.segmento;

import architecture.rrf_vp.view.ViewJoint;

public class SegmentoView implements ViewJoint<SegmentoView,Segmento> {
//ROOT
	private Segmento root;
	@Override
	public Segmento getRoot() {return root;}
//MAIN
	public SegmentoView(Segmento root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}