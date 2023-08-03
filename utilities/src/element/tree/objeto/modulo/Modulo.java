package element.tree.objeto.modulo;

import java.awt.Point;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.tree.Tree;
import element.tree.objeto.Objeto;

public class Modulo extends Objeto implements RootJoint<Modulo,ModuloRule,ModuloFlow,ModuloView,ModuloPlan> {
//RRF-VP
	private ModuloRule rule=new ModuloRule(this);
		@Override public ModuloRule getRule() {return rule;}
	private ModuloFlow flow=new ModuloFlow(this);
		@Override public ModuloFlow getFlow() {return flow;}
	private ModuloView view=new ModuloView(this);
		@Override public ModuloView getView() {return view;}
	private ModuloPlan plan=new ModuloPlan(this);
		@Override public ModuloPlan getPlan() {return plan;}
//TREE
	private Tree tree;
		public Tree getTree() {return tree;}
//MAIN
	public Modulo() {
		super(Objeto.Tipo.MODULO);
		Root.init(this);
	}
	public Modulo(Tree tree) {
		super(Objeto.Tipo.MODULO);
		Root.init(this);
		this.tree=tree;
	}
//FUNCS
	@Override public boolean contains(Point mouse) {
		return false;
	}
}