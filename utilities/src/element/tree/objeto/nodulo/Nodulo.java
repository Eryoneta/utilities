package element.tree.objeto.nodulo;

import java.awt.Point;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.tree.objeto.Objeto;

public class Nodulo extends Objeto implements RootJoint<Nodulo,NoduloRule,NoduloFlow,NoduloView,NoduloPlan>{
//RULE
	private NoduloRule rule=new NoduloRule(this);
		@Override public NoduloRule getRule() {return rule;}
//FLOW
	private NoduloFlow flow=new NoduloFlow(this);
		@Override public NoduloFlow getFlow() {return flow;}
//VIEW
	private NoduloView view=new NoduloView(this);
		@Override public NoduloView getView() {return view;}
//PLAN
	private NoduloPlan plan=new NoduloPlan(this);
		@Override public NoduloPlan getPlan() {return plan;}
//MAIN
	public Nodulo(Tipo tipo) {
		super(tipo);
		Root.init(this);
	}
//FUNCS
	@Override public boolean contains(Point mouse) {
		return false;
	}
}