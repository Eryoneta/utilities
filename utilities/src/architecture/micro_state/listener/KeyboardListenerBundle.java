package architecture.micro_state.listener;

import java.awt.event.KeyEvent;

public class KeyboardListenerBundle extends StateListenerBundle{
	private ActionOfKeyboard keyPressed;
		public ActionOfKeyboard getKeyPressedAction(){return keyPressed;}
		public void setKeyPressedAction(ActionOfKeyboard action){keyPressed=action;}
	private ActionOfKeyboard keyReleased;
		public ActionOfKeyboard getKeyReleasedAction(){return keyReleased;}
		public void setKeyReleasedAction(ActionOfKeyboard action){keyReleased=action;}
	private ActionOfKeyboard keyClicked;
		public ActionOfKeyboard getKeyClickedAction(){return keyClicked;}
		public void setKeyClickedAction(ActionOfKeyboard action){keyClicked=action;}
	public static abstract class ActionOfKeyboard extends Action{
		public abstract void run(KeyEvent k);
	}
}