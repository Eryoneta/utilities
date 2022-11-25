package element.tree.objeto;

import architecture.rrf_vp.Root;
import architecture.rrf_vp.Rule;

public abstract class ObjetoRule extends Rule{
//MAIN
	public ObjetoRule(Root root) {
		super(root);
	}
//FUNCS
	@Override
	protected abstract void init();
}