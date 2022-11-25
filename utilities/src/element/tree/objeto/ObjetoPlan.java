package element.tree.objeto;

import architecture.rrf_vp.Plan;
import architecture.rrf_vp.Root;

public abstract class ObjetoPlan extends Plan {
//MAIN
	public ObjetoPlan(Root root) {
		super(root);
	}
//FUNCS
	@Override
	protected abstract void init();
}