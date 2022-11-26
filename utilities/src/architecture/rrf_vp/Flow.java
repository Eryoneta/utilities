package architecture.rrf_vp;

public abstract class Flow<R> {
//ROOT
	protected R root;
//MAIN
	public Flow(R root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}