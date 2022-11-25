package element.tree.objeto.modulo;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Plan;
import architecture.rrf_vp.Rule;
import architecture.rrf_vp.View;
import element.tree.objeto.Objeto;

public class Modulo extends Objeto {
//RRF-VP
	@Override
	protected Rule getRule() {return new ModuloRule(this);}
	@Override
	protected Flow getFlow() {return new ModuloFlow(this);}
	@Override
	protected View getView() {return new ModuloView(this);}
	@Override
	protected Plan getPlan() {return new ModuloPlan(this);}
//MAIN
	public Modulo(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}