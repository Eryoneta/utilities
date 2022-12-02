package architecture.micro_state.listener;

import java.awt.event.MouseEvent;

public class MouseListenerBundle extends StateListenerBundle{
	private ActionOfMouse mousePressed;
		public ActionOfMouse getMousePressedAction(){return mousePressed;}
		public void setMousePressedAction(ActionOfMouse action){mousePressed=action;}
	private ActionOfMouse mouseReleased;
		public ActionOfMouse getMouseReleasedAction(){return mouseReleased;}
		public void setMouseReleasedAction(ActionOfMouse action){mouseReleased=action;}
	private ActionOfMouse mouseCliqued;
		public ActionOfMouse getMouseCliquedAction(){return mouseCliqued;}
		public void setMouseCliquedAction(ActionOfMouse action){mouseCliqued=action;}
	private ActionOfMouse mouseDragged;
		public ActionOfMouse getMouseDraggedAction(){return mouseDragged;}
		public void setMouseDraggedAction(ActionOfMouse action){mouseDragged=action;}
	private ActionOfMouse mouseMoved;
		public ActionOfMouse getMouseMovedAction(){return mouseMoved;}
		public void setMouseMovedAction(ActionOfMouse action){mouseMoved=action;}
	public static abstract class ActionOfMouse extends Action{
		public abstract void run(MouseEvent m);
	}
}