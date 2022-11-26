package element.tree.objeto.nodulo;

import element.tree.objeto.Objeto;

public class Nodulo extends Objeto<NoduloRule,NoduloFlow,NoduloView,NoduloPlan>{
//RRF-VP
	@Override
	protected NoduloRule getRule() {return new NoduloRule(this);}
	@Override
	protected NoduloFlow getFlow() {return new NoduloFlow(this);}
	@Override
	protected NoduloView getView() {return new NoduloView(this);}
	@Override
	protected NoduloPlan getPlan() {return new NoduloPlan(this);}
//MAIN
	public Nodulo(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}