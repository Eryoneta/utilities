package architecture.rrf_vp;

public abstract class Root {
//RULE
	protected Rule rule;
		protected abstract Rule getRule();
//FLOW
	protected Flow flow;
		protected abstract Flow getFlow();
//VIEW
	protected View view;
		protected abstract View getView();
//PLAN
	protected Plan plan;
		protected abstract Plan getPlan();
//MAIN
	public Root() {
		initAll();
	}
//FUNCS
	protected abstract void init();
	protected void initAll() {
		this.init();
		if(rule != null)rule.init();
		if(flow != null)flow.init();
		if(view != null)view.init();
		if(plan != null)plan.init();
	}
}