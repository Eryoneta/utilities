package architecture.rrf_vp;

public abstract class Plan<R> {
//ROOT
	protected R root;
//MAIN
	public Plan(R root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}