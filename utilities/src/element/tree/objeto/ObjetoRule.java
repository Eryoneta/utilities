package element.tree.objeto;

import architecture.rrf_vp.Rule;

public abstract class ObjetoRule<R> extends Rule<R>{
//MAIN
	public ObjetoRule(R root) {
		super(root);
	}
//FUNCS
	@Override
	protected abstract void init();
}