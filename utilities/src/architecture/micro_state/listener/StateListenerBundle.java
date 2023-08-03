package architecture.micro_state.listener;

public class StateListenerBundle{
//LOADED
	private ActionOfState stateLoaded;
		public ActionOfState getStateLoadedAction(){return stateLoaded;}
		public void setStateLoadedAction(ActionOfState action){this.stateLoaded=action;}
//UNLOADED
	private ActionOfState stateUnloaded;
		public ActionOfState getStateUnloadedAction(){return stateUnloaded;}
		public void setStateUnloadedAction(ActionOfState action){this.stateUnloaded=action;}
//ACTION
	public static class ActionOfState{
		public void run(){}
	}
}