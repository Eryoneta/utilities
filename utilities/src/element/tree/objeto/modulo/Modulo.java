package element.tree.objeto.modulo;

import element.tree.objeto.Objeto;

public class Modulo extends Objeto<ModuloRule,ModuloFlow,ModuloView,ModuloPlan> {
//RRF-VP
	@Override
	protected ModuloRule getRule() {return new ModuloRule(this);}
	@Override
	protected ModuloFlow getFlow() {return new ModuloFlow(this);}
	@Override
	protected ModuloView getView() {return new ModuloView(this);}
	@Override
	protected ModuloPlan getPlan() {return new ModuloPlan(this);}
//MAIN
	public Modulo(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}