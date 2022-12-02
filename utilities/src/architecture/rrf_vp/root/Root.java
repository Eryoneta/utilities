package architecture.rrf_vp.root;

public class Root {
	public static void init(RootJoint<?,?,?,?,?>root) {
		root.getRule().init();
		root.getFlow().init();
		root.getView().init();
		root.getPlan().init();
	}
}