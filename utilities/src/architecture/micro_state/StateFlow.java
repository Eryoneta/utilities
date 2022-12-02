package architecture.micro_state;

import java.util.HashMap;

public class StateFlow {
//STATES
	private HashMap<Long,StateNode>allStates=new HashMap<>();
		public StateNode getState(long id) {
			return allStates.get(id);
		}
//MAIN
	public StateFlow() {}
//FUNCS
	public static long createStateIdValue(int level,int index){
		return (long)(Math.pow(10,level)+index);
	}
	public void loadFlow(StateNode... states) {
		for(StateNode state:states)allStates.put(state.getId(),state);
	}
}