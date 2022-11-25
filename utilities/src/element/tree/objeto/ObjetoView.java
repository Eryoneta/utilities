package element.tree.objeto;

import architecture.rrf_vp.Root;
import architecture.rrf_vp.View;

public abstract class ObjetoView extends View {
//MAIN
	public ObjetoView(Root root) {
		super(root);
	}
//FUNCS
	@Override
	protected abstract void init();
}