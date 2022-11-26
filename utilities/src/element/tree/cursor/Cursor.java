package element.tree.cursor;

import architecture.rrf_vp.Root;

public class Cursor extends Root<CursorRule,CursorFlow,CursorView,CursorPlan>{
	//RRF-VP
	@Override
	protected CursorRule getRule() {return new CursorRule(this);}
	@Override
	protected CursorFlow getFlow() {return new CursorFlow(this);}
	@Override
	protected CursorView getView() {return new CursorView(this);}
	@Override
	protected CursorPlan getPlan() {return new CursorPlan(this);}
//FUNCS
	@Override
	protected void init() {}
}