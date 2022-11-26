package element.tree.selection_area;

import architecture.rrf_vp.Root;

public class SelectionArea extends Root<SelectionAreaRule,SelectionAreaFlow,SelectionAreaView,SelectionAreaPlan>{
//RRF-VP
	@Override
	protected SelectionAreaRule getRule() {return new SelectionAreaRule(this);}
	@Override
	protected SelectionAreaFlow getFlow() {return new SelectionAreaFlow(this);}
	@Override
	protected SelectionAreaView getView() {return new SelectionAreaView(this);}
	@Override
	protected SelectionAreaPlan getPlan() {return new SelectionAreaPlan(this);}
//FUNCS
	@Override
	protected void init() {}
}