package element.tree.chunk;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Plan;
import architecture.rrf_vp.Root;
import architecture.rrf_vp.Rule;
import architecture.rrf_vp.View;

public class Chunk extends Root{
//RRF-VP
	@Override
	protected Rule getRule() {return null;}
	@Override
	protected Flow getFlow() {return null;}
	@Override
	protected View getView() {return new ChunkView(this);}
	@Override
	protected Plan getPlan() {return new ChunkPlan(this);}
//FUNCS
	@Override
	protected void init() {}
}