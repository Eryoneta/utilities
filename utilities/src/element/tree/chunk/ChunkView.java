package element.tree.chunk;

import architecture.rrf_vp.view.ViewJoint;

public class ChunkView implements ViewJoint<ChunkView,Chunk> {
//ROOT
	private Chunk root;
	@Override
	public Chunk getRoot() {return root;}
//MAIN
	public ChunkView(Chunk root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}