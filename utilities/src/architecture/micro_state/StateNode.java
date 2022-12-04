package architecture.micro_state;

import architecture.micro_state.listener.StateListenerBundle;
import architecture.micro_state.listener.StateListenerBundle.ActionOfState;

public class StateNode{
//ID
	private long id;
		public long getId() {return id;}
//LISTENERS
	private StateListenerBundle listenerBundle;
		public ActionOfState getStateLoadedAction(){return listenerBundle.getStateLoadedAction();}
		public void setStateLoadedAction(ActionOfState action){listenerBundle.setStateLoadedAction(action);}
		public ActionOfState getStateUnloadedAction(){return listenerBundle.getStateUnloadedAction();}
		public void setStateUnloadedAction(ActionOfState action){listenerBundle.setStateUnloadedAction(action);}
//MAIN
	public StateNode(StateId...ids) throws Exception{
		this.id=StateFlow.getStateValue(ids);
	}
}