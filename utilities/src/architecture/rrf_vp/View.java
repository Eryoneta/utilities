package architecture.rrf_vp;

public abstract class View {
//ROOT
	protected Root root;
//MAIN
	public View(Root root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}