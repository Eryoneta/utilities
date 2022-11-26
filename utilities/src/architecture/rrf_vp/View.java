package architecture.rrf_vp;

public abstract class View<R> {
//ROOT
	protected R root;
//MAIN
	public View(R root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}