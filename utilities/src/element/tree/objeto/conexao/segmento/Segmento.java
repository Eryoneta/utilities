package element.tree.objeto.conexao.segmento;

import java.awt.Point;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.tree.objeto.Objeto;

public class Segmento extends Objeto implements RootJoint<Segmento,SegmentoRule,SegmentoFlow,SegmentoView,SegmentoPlan>{
//RULE
	private SegmentoRule rule=new SegmentoRule(this);
		@Override public SegmentoRule getRule() {return rule;}
//FLOW
	private SegmentoFlow flow=new SegmentoFlow(this);
		@Override public SegmentoFlow getFlow() {return flow;}
//VIEW
	private SegmentoView view=new SegmentoView(this);
		@Override public SegmentoView getView() {return view;}
//PLAN
	private SegmentoPlan plan=new SegmentoPlan(this);
		@Override public SegmentoPlan getPlan() {return plan;}
//MAIN
	public Segmento(Tipo tipo) {
		super(tipo);
		Root.init(this);
	}
//FUNCS
	@Override public boolean contains(Point mouse) {
		return false;
	}
}