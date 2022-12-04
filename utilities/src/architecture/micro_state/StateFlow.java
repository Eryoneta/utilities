package architecture.micro_state;

import java.util.HashMap;

public class StateFlow {
//LEVEL
	public static enum Level{
		LEVEL_1(1),
		LEVEL_2(2),
		LEVEL_3(3),
		LEVEL_4(4),
		LEVEL_5(5),
		LEVEL_6(6),
		LEVEL_7(7),
		LEVEL_8(8),
		LEVEL_9(9),
		LEVEL_10(10),
		LEVEL_11(11),
		LEVEL_12(12),
		LEVEL_13(13),
		LEVEL_14(14),
		LEVEL_15(15),
		LEVEL_16(16),
		LEVEL_17(17),
		LEVEL_18(18);
	//VALUE
		private int value;
			public int getValue() {return value;}
	//MAIN
		Level(int value) {this.value=value;}
	}
//INDEX
	public static enum Index{
		INDEX_1(1),
		INDEX_2(2),
		INDEX_3(3),
		INDEX_4(4),
		INDEX_5(5),
		INDEX_6(6),
		INDEX_7(7),
		INDEX_8(8),
		INDEX_9(9);
	//VALUE
		private int value;
			public int getValue() {return value;}
	//MAIN
		Index(int value) {this.value=value;}
	}
//STATES
	private HashMap<Long,StateNode>allStates=new HashMap<>();
		public StateNode getStateNode(StateId... ids) throws Exception {
			return allStates.get(StateFlow.getStateValue(ids));
		}
		public StateNode getStateNode() throws Exception {
			return getStateNode(stateIds);
		}
//STATE
	private StateId[]stateIds;
		public long getStateId() throws Exception {
			return StateFlow.getStateValue(stateIds);
		}
		public void setState(StateId...ids) {this.stateIds=ids;}
//MAIN
	public StateFlow(StateId...ids) {
		this.stateIds=ids;
	}
//FUNCS
	public static long getStateValue(StateId... ids) throws Exception{
		if(!isAllLevelsAndIndexesDifferent(ids))throw new Exception("ERROR");		//TODO: ERROR!
		long total=0;
		for(StateId id:ids) {
			final long value=(long)(Math.pow(10,id.getLevel().getValue())*id.getIndex().getValue());
			total+=value;
		}
		return total;
	}
		private static boolean isAllLevelsAndIndexesDifferent(StateId... ids) {
			final HashMap<StateFlow.Level,HashMap<StateFlow.Index,Boolean>>checker=new HashMap<>();
			for(StateId id:ids) {
				if(checker.containsKey(id.getLevel())) return false;
				checker.put(id.getLevel(),new HashMap<>());
				if(checker.get(id.getLevel()).containsKey(id.getIndex())) return false;
				checker.get(id.getLevel()).put(id.getIndex(), true);
			}
			return true;
		}
	public void loadFlow(StateNode... states) {
		for(StateNode state:states)allStates.put(state.getId(),state);
	}
}