package element.tree.selection_area;

import architecture.micro_state.StateFlow;
import architecture.micro_state.StateId;
import architecture.micro_state.StateNode;
import architecture.rrf_vp.flow.FlowJoint;

public class SelectionAreaFlow implements FlowJoint<SelectionAreaFlow,SelectionArea>{
//ROOT
	private SelectionArea root;
		@Override public SelectionArea getRoot() {return root;}
//STATE_FLOW
	public static enum State implements StateId{
		//VARS
			MOD1(StateFlow.createStateIdValue(1,1)),
			MOD2(StateFlow.createStateIdValue(1,2)),
			MOD3(StateFlow.createStateIdValue(2,1));
		//VALUE
			private long value;
		//MAIN
			State(long value){this.value=value;}
		//FUNCS
			@Override
			public long getId() {return value;}
	}
	private StateFlow stateFlow=new StateFlow();
//MAIN
	public SelectionAreaFlow(SelectionArea root) {
		this.root=root;
		stateFlow.loadFlow(
				new StateNode(State.MOD1,State.MOD3){{
					
				}}
		);
	}
//FUNCS
	@Override public void init() {}
}