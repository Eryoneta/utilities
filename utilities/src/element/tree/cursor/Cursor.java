package element.tree.cursor;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Plan;
import architecture.rrf_vp.Root;
import architecture.rrf_vp.Rule;
import architecture.rrf_vp.View;

public class Cursor extends Root{
//RRF-VP
	@Override
	protected Rule getRule() {return new CursorRule(this);}
	@Override
	protected Flow getFlow() {return new CursorFlow(this);}
	@Override
	protected View getView() {return new CursorView(this);}
	@Override
	protected Plan getPlan() {return new CursorPlan(this);}
//FUNCS
	@Override
	protected void init() {}
}