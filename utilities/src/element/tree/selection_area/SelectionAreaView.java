package element.tree.selection_area;

import architecture.rrf_vp.view.ViewJoint;

public class SelectionAreaView implements ViewJoint<SelectionAreaView,SelectionArea>{
//ROOT
	private SelectionArea root;
		@Override public SelectionArea getRoot() {return root;}
//MAIN
	public SelectionAreaView(SelectionArea root) {
		this.root=root;
	}
//FUNCS
	@Override public void init() {}
}