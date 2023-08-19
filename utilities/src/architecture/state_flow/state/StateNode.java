package architecture.state_flow.state;

import architecture.state_flow.StateFlow;

public abstract class StateNode {
	//ID
	private long id;
	public long getId() {
		return id;
	}
	// MAIN
	public StateNode(StateId... ids) throws Exception {
		if (ids.length == 0) {
			throw new Exception("Error: A StateNode needs a StateId!");
		}
		this.id = StateFlow.getStateValue(ids);
	}
	// ACTIONS
	public void stateLoaded() {}
	public void stateUnloaded() {}
}