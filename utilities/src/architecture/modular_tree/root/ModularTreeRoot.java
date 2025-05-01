package architecture.modular_tree.root;

import architecture.rrf_vp.root.Root;

public class ModularTreeRoot extends Root {
	public static void init(ModularRoot<?, ?, ?, ?, ?> root) {
		Root.init(root);
		if (root.getFlow() != null) root.getFlow().initTriggers();
		if (root.getFlow() != null) root.getFlow().initFlow();
	}
}