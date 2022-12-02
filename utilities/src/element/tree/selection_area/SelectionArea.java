package element.tree.selection_area;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;

public class SelectionArea implements RootJoint<SelectionArea,SelectionAreaRule,SelectionAreaFlow,SelectionAreaView,SelectionAreaPlan>{
//RULE
	private SelectionAreaRule rule=new SelectionAreaRule(this);
		@Override public SelectionAreaRule getRule() {return rule;}
//FLOW
	private SelectionAreaFlow flow=new SelectionAreaFlow(this);
		@Override public SelectionAreaFlow getFlow() {return flow;}
//VIEW
	private SelectionAreaView view=new SelectionAreaView(this);
		@Override public SelectionAreaView getView() {return view;}
//PLAN
	private SelectionAreaPlan plan=new SelectionAreaPlan(this);
		@Override public SelectionAreaPlan getPlan() {return plan;}
//MAIN
	public SelectionArea() {
		Root.init(this);
	}
//FUNCS
}