package element.tree.cursor;

import architecture.rrf_vp.flow.FlowJoint;

public class CursorFlow implements FlowJoint<CursorFlow,Cursor> {
//ROOT
	private Cursor root;
	@Override
	public Cursor getRoot() {return root;}
//MAIN
	public CursorFlow(Cursor root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}