package element.tree.chunk;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Root;
import architecture.rrf_vp.Rule;

public class Chunk extends Root<Rule<?>,Flow<?>,ChunkView,ChunkPlan>{
//RRF-VP
	@Override
	protected Rule<?> getRule() {return null;}
	@Override
	protected Flow<?> getFlow() {return null;}
	@Override
	protected ChunkView getView() {return new ChunkView(this);}
	@Override
	protected ChunkPlan getPlan() {return new ChunkPlan(this);}
//FUNCS
	@Override
	protected void init() {}
}