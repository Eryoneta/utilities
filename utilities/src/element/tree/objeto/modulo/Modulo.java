package element.tree.objeto.modulo;

import java.awt.Point;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.tree.objeto.Objeto;

public class Modulo extends Objeto implements RootJoint<Modulo,ModuloRule,ModuloFlow,ModuloView,ModuloPlan> {
//RULE
	private ModuloRule rule=new ModuloRule(this);
		@Override public ModuloRule getRule() {return rule;}
//FLOW
	private ModuloFlow flow=new ModuloFlow(this);
		@Override public ModuloFlow getFlow() {return flow;}
//VIEW
	private ModuloView view=new ModuloView(this);
		@Override public ModuloView getView() {return view;}
//PLAN
	private ModuloPlan plan=new ModuloPlan(this);
		@Override public ModuloPlan getPlan() {return plan;}
//MAIN
	public Modulo(Tipo tipo) {
		super(tipo);
		Root.init(this);
	}
//FUNCS
	@Override public boolean contains(Point mouse) {
		return false;
	}
}