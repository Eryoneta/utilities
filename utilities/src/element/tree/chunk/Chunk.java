package element.tree.chunk;

import architecture.rrf_vp.flow.FlowNull;
import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import architecture.rrf_vp.rule.RuleNull;

public class Chunk implements RootJoint<Chunk,RuleNull<Chunk>,FlowNull<Chunk>,ChunkView,ChunkPlan>{
//RULE
	@Override public RuleNull<Chunk> getRule() {return null;}
//FLOW
	@Override public FlowNull<Chunk> getFlow() {return null;}
//VIEW
	private ChunkView view=new ChunkView(this);
		@Override public ChunkView getView() {return view;}
//PLAN
	private ChunkPlan plan=new ChunkPlan(this);
		@Override public ChunkPlan getPlan() {return plan;}
//MAIN
	public Chunk() {
		Root.init(this);
	}
//FUNCS
}