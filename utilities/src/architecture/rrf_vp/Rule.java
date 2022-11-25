package architecture.rrf_vp;

public abstract class Rule {
//ROOT
	protected Root root;
//MAIN
	public Rule(Root root) {
		this.root=root;
	}
//FUNCS
	protected abstract void init();
}