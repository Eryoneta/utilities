package element.tree.cursor;

import java.awt.Point;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.tree.objeto.Objeto;

public class Cursor extends Objeto implements RootJoint<Cursor,CursorRule,CursorFlow,CursorView,CursorPlan>{
//RULE
	private CursorRule rule=new CursorRule(this);
		@Override public CursorRule getRule() {return rule;}
//FLOW
	private CursorFlow flow=new CursorFlow(this);
		@Override public CursorFlow getFlow() {return flow;}
//VIEW
	private CursorView view=new CursorView(this);
		@Override public CursorView getView() {return view;}
//PLAN
	private CursorPlan plan=new CursorPlan(this);
		@Override public CursorPlan getPlan() {return plan;}
//MAIN
	public Cursor(Tipo tipo) {
		super(tipo);
		Root.init(this);
	}
//FUNCS
	@Override public boolean contains(Point mouse) {
		return false;
	}
}