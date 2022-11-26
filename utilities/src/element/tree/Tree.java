package element.tree;

import element.Elemento;
import element.Painel;

public class Tree extends Elemento<TreeRule,TreeFlow,TreeView,TreePlan>{
//RRF-VP
	@Override
	protected TreeRule getRule() {return new TreeRule(this);}
	@Override
	protected TreeFlow getFlow() {return new TreeFlow(this);}
	@Override
	protected TreeView getView() {return new TreeView(this);}
	@Override
	protected TreePlan getPlan() {return new TreePlan(this);}
//MAIN
	public Tree(Painel painel) {
		super(painel);
	}
//FUNCS
	@Override
	protected void init() {}
}