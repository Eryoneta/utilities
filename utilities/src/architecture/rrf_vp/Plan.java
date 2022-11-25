package architecture.rrf_vp;

public abstract class Plan {
//ROOT
	protected Root root;
//MAIN
	public Plan(Root root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}