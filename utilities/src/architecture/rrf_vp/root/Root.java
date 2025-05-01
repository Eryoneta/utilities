package architecture.rrf_vp.root;

public class Root {
	public static void init(RootJoint<?, ?, ?, ?, ?> root) {
		if (root.getRule() != null) root.getRule().init();
		if (root.getFlow() != null) root.getFlow().init();
		if (root.getView() != null) root.getView().init();
		if (root.getPlan() != null) root.getPlan().init();
	}
}