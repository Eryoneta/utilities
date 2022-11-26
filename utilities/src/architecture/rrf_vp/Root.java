package architecture.rrf_vp;

public abstract class Root<R,F,V,P> {
//RRF-VP
	public R rule;
		protected abstract R getRule();
	public F flow;
		protected abstract F getFlow();
	public V view;
		protected abstract V getView();
	public P plan;
		protected abstract P getPlan();
//MAIN
	public Root() {
		rule=getRule();
		flow=getFlow();
		view=getView();
		plan=getPlan();
		initAll();
	}
//FUNCS
	private void initAll() {
		this.init();
		if(rule != null)((Rule<?>) rule).init();
		if(flow != null)((Flow<?>) flow).init();
		if(view != null)((View<?>) view).init();
		if(plan != null)((Plan<?>) plan).init();
	}
	protected abstract void init();
}