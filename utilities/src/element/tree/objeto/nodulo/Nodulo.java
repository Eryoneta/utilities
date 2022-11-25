package element.tree.objeto.nodulo;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Plan;
import architecture.rrf_vp.Rule;
import architecture.rrf_vp.View;
import element.tree.objeto.Objeto;

public class Nodulo extends Objeto{
//RRF-VP
	@Override
	protected Rule getRule() {return new NoduloRule(this);}
	@Override
	protected Flow getFlow() {return new NoduloFlow(this);}
	@Override
	protected View getView() {return new NoduloView(this);}
	@Override
	protected Plan getPlan() {return new NoduloPlan(this);}
//MAIN
	public Nodulo(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}