package architecture.micro_state.listener;

import architecture.micro_state.listener.KeyboardListenerBundle.ActionOfKeyboard;
import architecture.micro_state.listener.MouseListenerBundle.ActionOfMouse;

public class InputListenerBundle extends StateListenerBundle{
	private MouseListenerBundle mouseListenerBundle=new MouseListenerBundle();
		public ActionOfMouse getMousePressedAction(){return mouseListenerBundle.getMousePressedAction();}
		public void setMousePressedAction(ActionOfMouse action){mouseListenerBundle.setMousePressedAction(action);}
		public ActionOfMouse getMouseReleasedAction(){return mouseListenerBundle.getMouseReleasedAction();}
		public void setMouseReleasedAction(ActionOfMouse action){mouseListenerBundle.setMouseReleasedAction(action);}
		public ActionOfMouse getMouseCliquedAction(){return mouseListenerBundle.getMouseCliquedAction();}
		public void setMouseCliquedAction(ActionOfMouse action){mouseListenerBundle.setMouseCliquedAction(action);}
		public ActionOfMouse getMouseDraggedAction(){return mouseListenerBundle.getMouseDraggedAction();}
		public void setMouseDraggedAction(ActionOfMouse action){mouseListenerBundle.setMouseDraggedAction(action);}
		public ActionOfMouse getMouseMovedAction(){return mouseListenerBundle.getMouseDraggedAction();}
		public void setMouseMovedAction(ActionOfMouse action){mouseListenerBundle.setMouseMovedAction(action);}
	private KeyboardListenerBundle keyboardListenerBundle=new KeyboardListenerBundle();
		public ActionOfKeyboard getKeyPressedAction(){return keyboardListenerBundle.getKeyPressedAction();}
		public void setKeyPressedAction(ActionOfKeyboard action){keyboardListenerBundle.setKeyPressedAction(action);}
		public ActionOfKeyboard getKeyReleasedAction(){return keyboardListenerBundle.getKeyReleasedAction();}
		public void setKeyReleasedAction(ActionOfKeyboard action){keyboardListenerBundle.setKeyReleasedAction(action);}
		public ActionOfKeyboard getKeyClickedAction(){return keyboardListenerBundle.getKeyClickedAction();}
		public void setKeyClickedAction(ActionOfKeyboard action){keyboardListenerBundle.setKeyClickedAction(action);}
}