package architecture.state_flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import architecture.state_flow.state.StateLevelIndex;
import architecture.state_flow.state.StateId;
import architecture.state_flow.state.StateNode;

@SuppressWarnings("unchecked")
public abstract class StateFlow<S extends StateId, SN extends StateNode> {
	// LISTENERS
	private List<StateChangeListener<SN>> stateChangeListeners = new ArrayList<>();
	public void addStateChangeListener(StateChangeListener<SN> stateChangeListener) {
		stateChangeListeners.add(stateChangeListener);
	}
	private void triggerStateChange(SN oldState, SN newState) {
		for (StateChangeListener<SN> stateChangeListener : stateChangeListeners) {
			stateChangeListener.stateChanged(oldState, newState);
		}
	}
	// STATES
	private HashMap<Long, SN> allStates = new HashMap<>();
	public SN getStateNode(S... ids) throws Exception {
		return allStates.get(StateFlow.getStateValue(ids));
	}
	public SN getStateNode() {
		try {
			return getStateNode(currentState);
		} catch (Exception e) {}
		return null;
	}
	//STATE
	private S[] currentState;
	public long getStateId() throws Exception {
		return StateFlow.getStateValue(currentState);
	}
	public void setState(S... ids) throws Exception {
		if (!isAllLevelsDifferent(ids)) {
			throw new Exception("Error: States of same Group cannot be together!");
		}
		final SN oldState = getStateNode();
		if(oldState != null) oldState.stateUnloaded();
		this.currentState = ids;
		final SN newState = getStateNode();
		if(newState != null) newState.stateLoaded();
		triggerStateChange(oldState, newState);
	}
	// MAIN
	public StateFlow() {}
	// FUNCS
	public static long getStateValue(StateId... ids) throws Exception {
		if (!isAllLevelsDifferent(ids)) {
			throw new Exception("Error: States of same Group cannot be together!");
		}
		long stateValue = 0;
		for (StateId id : ids) {
			stateValue += calculateIdValue(id);
		}
		return stateValue;
	}
	private static long calculateIdValue(StateId id) {
		int levelIndex = id.getLevelIndex().getValue();	// SEGUE DE 1 ATÉ 13, COM VALORES DE 0 ATÉ 12
		int unitIndex = id.getUnitIndex().getValue();	// SEGUE DE 1 ATÉ 24, COM VALORES DE 1 ATÉ 24
		// IDÉIA-BASE:
		//   CADA LEVEL SERIA UMA CASA DECIMAL: 1, 10, 100, 1.000, ATÉ O LIMITE DE LONG
		//   CADA UNIT SERIA UMA MÚLTIPLO DO ANTERIOR: *1, *2, *3, ..., *9
		//   DESSA FORMA, DIFERENTES LEVELS AFETAM DIFERENTES CASAS DECIMAIS: L2U3 + L1U2 = 30 + 2 = 32
		//   PROBLEMA: UNIT PODE TER APENAS 9 VALORES(MUITO POUCO!), ENQUANTO LEVELS PODEM TER ATÉ 18...
		// IDÉIA ATUAL:
		//   CADA LEVEL É UMA CASA BASE-25: 1, 25, 625, 15.625, ATÉ O LIMITE DE LONG
		//   CADA UNIT É UM MÚLTIPLO DO ANTERIOR: *1, *2, *3, ..., *24
		//   DESSA FORMA, FUNCIONA, COM UNITS COM ATÉ 24 VALORES, E LEVELS COM ATÉ 13: L2U3 + L1U2 = 75 + 2 = 77
		final long value = (long) (Math.pow(25, levelIndex) * unitIndex);
		return value;
	}
	private static boolean isAllLevelsDifferent(StateId... ids) {
		final HashMap<StateLevelIndex, Boolean> checker = new HashMap<>();
		for (StateId id : ids) {
			if(checker.containsKey(id.getLevelIndex())) {
				return false;	//NÃO PODEM SER DE MESMO LEVEL
			}
			checker.put(id.getLevelIndex(), true);
		}
		return true;
	}
	public void loadFlow(SN... states) throws Exception {
		for(SN state: states) {
			if(allStates.containsKey(state.getId())) {
				throw new Exception("Error: All States should have different ids!");
			}
			allStates.put(state.getId(), state);
		}
	}
}