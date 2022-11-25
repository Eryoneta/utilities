package element.tree.objeto;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Root;

public abstract class ObjetoFlow extends Flow {
//MAIN
	public ObjetoFlow(Root root) {
		super(root);
	}
//FUNCS
	@Override
	protected abstract void init();
}