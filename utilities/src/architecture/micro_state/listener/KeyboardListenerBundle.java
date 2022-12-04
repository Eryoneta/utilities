package architecture.micro_state.listener;

import java.awt.event.KeyEvent;

public class KeyboardListenerBundle extends StateListenerBundle{
//PRESSED
	private ActionOfKeyboard keyPressed;
		public ActionOfKeyboard getKeyPressedAction(){return keyPressed;}
		public void setKeyPressedAction(ActionOfKeyboard action){keyPressed=action;}
//RELEASED
	private ActionOfKeyboard keyReleased;
		public ActionOfKeyboard getKeyReleasedAction(){return keyReleased;}
		public void setKeyReleasedAction(ActionOfKeyboard action){keyReleased=action;}
//CLICKED
	private ActionOfKeyboard keyClicked;
		public ActionOfKeyboard getKeyClickedAction(){return keyClicked;}
		public void setKeyClickedAction(ActionOfKeyboard action){keyClicked=action;}
//ACTION
	public static class ActionOfKeyboard{
		public void run(KeyEvent k) {}
	}
}