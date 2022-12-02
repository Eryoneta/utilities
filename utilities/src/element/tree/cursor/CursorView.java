package element.tree.cursor;

import architecture.rrf_vp.view.ViewJoint;

public class CursorView implements ViewJoint<CursorView,Cursor> {
//ROOT
	private Cursor root;
	@Override
	public Cursor getRoot() {return root;}
//MAIN
	public CursorView(Cursor root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}