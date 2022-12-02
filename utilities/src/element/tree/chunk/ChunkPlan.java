package element.tree.chunk;

import architecture.rrf_vp.plan.PlanJoint;

public class ChunkPlan implements PlanJoint<ChunkPlan,Chunk> {
//ROOT
	private Chunk root;
	@Override
	public Chunk getRoot() {return root;}
//MAIN
	public ChunkPlan(Chunk root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
}