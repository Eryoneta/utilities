package element.tree.objeto;

import architecture.rrf_vp.Flow;

public abstract class ObjetoFlow<R> extends Flow<R> {
//MAIN
	public ObjetoFlow(R root) {
		super(root);
	}
//FUNCS
	@Override
	protected abstract void init();
}