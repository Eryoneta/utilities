package element.tree.objeto.conexao.segmento;

import element.tree.objeto.Objeto;

public class Segmento extends Objeto<SegmentoRule,SegmentoFlow,SegmentoView,SegmentoPlan> {
//RRF-VP
	@Override
	protected SegmentoRule getRule() {return new SegmentoRule(this);}
	@Override
	protected SegmentoFlow getFlow() {return new SegmentoFlow(this);}
	@Override
	protected SegmentoView getView() {return new SegmentoView(this);}
	@Override
	protected SegmentoPlan getPlan() {return new SegmentoPlan(this);}
//MAIN
	public Segmento(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}