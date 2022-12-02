package architecture.micro_state;

import architecture.micro_state.listener.StateListenerBundle;
import architecture.micro_state.listener.StateListenerBundle.Action;

public class StateNode{
//ID
	private long id;
		public long getId() {return id;}
//LISTENERS
	private StateListenerBundle listenerBundle;
		public Action getStateLoadedAction(){return listenerBundle.getStateLoadedAction();}
		public void setStateLoadedAction(Action action){listenerBundle.setStateLoadedAction(action);}
		public Action getStateUnloadedAction(){return listenerBundle.getStateUnloadedAction();}
		public void setStateUnloadedAction(Action action){listenerBundle.setStateUnloadedAction(action);}
//MAIN
	public StateNode(StateId...ids) {
		this.id=getValue(ids);
	}
//FUNCS
	private int getValue(StateId... states) {
		int total=0;
		for(StateId state:states)total+=state.getId();
		return total;
	}
}