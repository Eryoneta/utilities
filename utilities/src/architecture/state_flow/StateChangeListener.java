package architecture.state_flow;

import architecture.state_flow.state.StateNode;

public interface StateChangeListener<SN extends StateNode> {
	public void stateChanged(SN oldState, SN newState);
}