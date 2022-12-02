package element.tree;

import java.awt.Graphics2D;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.Elemento;
import element.Painel;

public class Tree extends Elemento implements RootJoint<Tree,TreeRule,TreeFlow,TreeView,TreePlan>{
//RULE
	private TreeRule rule=new TreeRule(this);
		@Override public TreeRule getRule() {return rule;}
//FLOW
	private TreeFlow flow=new TreeFlow(this);
		@Override public TreeFlow getFlow() {return flow;}
//VIEW
	private TreeView view=new TreeView(this);
		@Override public TreeView getView() {return view;}
//PLAN
	private TreePlan plan=new TreePlan(this);
		@Override public TreePlan getPlan() {return plan;}
//MAIN
	public Tree(Painel painel) {
		super(painel);
		Root.init(this);
	}
//FUNCS
	@Override
	protected void draw(Graphics2D imagemEdit) {
		
	}
}