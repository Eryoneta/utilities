package architecture.micro_state.listener;

public class StateListenerBundle{
	private Action stateLoaded;
		public Action getStateLoadedAction(){return stateLoaded;}
		public void setStateLoadedAction(Action action){this.stateLoaded=action;}
	private Action stateUnloaded;
		public Action getStateUnloadedAction(){return stateUnloaded;}
		public void setStateUnloadedAction(Action action){this.stateUnloaded=action;}
	public static abstract class Action{
		public abstract void run();
	}
}