package element.tree;

import java.awt.Graphics2D;
import java.awt.Point;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.Elemento;
import element.Painel;

public class Tree extends Elemento implements RootJoint<Tree,TreeRule,TreeFlow,TreeView,TreePlan>{
//RRF-VP
	private TreeRule rule=new TreeRule(this);
		@Override public TreeRule getRule() {return rule;}
	private TreeFlow flow=new TreeFlow(this);
		@Override public TreeFlow getFlow() {return flow;}
	private TreeView view=new TreeView(this);
		@Override public TreeView getView() {return view;}
	private TreePlan plan=new TreePlan(this);
		@Override public TreePlan getPlan() {return plan;}
//MAIN
	public Tree(Painel painel) {
		super(painel);
		Root.init(this);
	}
//LOCAL
	public int getViewX(){return getView().getViewX();}
	public int getViewY(){return getView().getViewY();}
	public Point getViewLocation(){return getView().getViewLocation();}
	public void setViewLocation(int x,int y){getView().setViewLocation(x, y);;}
//UNIT
	public byte getViewUnit() {return getView().getViewUnit();}
//FUNCS
	@Override protected void draw(Graphics2D imagemEdit) {}
}