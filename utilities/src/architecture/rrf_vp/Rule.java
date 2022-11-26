package architecture.rrf_vp;

public abstract class Rule<R> {
//ROOT
	protected R root;
//MAIN
	public Rule(R root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}