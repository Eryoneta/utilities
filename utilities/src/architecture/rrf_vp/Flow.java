package architecture.rrf_vp;

public abstract class Flow {
//ROOT
	protected Root root;
//MAIN
	public Flow(Root root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}