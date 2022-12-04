package architecture.micro_state.listener;

import java.awt.event.MouseEvent;

public class MouseListenerBundle extends StateListenerBundle{
//PRESSED
	private ActionOfMouse mousePressed;
		public ActionOfMouse getMousePressedAction(){return mousePressed;}
		public void setMousePressedAction(ActionOfMouse action){mousePressed=action;}
//RELEASED
	private ActionOfMouse mouseReleased;
		public ActionOfMouse getMouseReleasedAction(){return mouseReleased;}
		public void setMouseReleasedAction(ActionOfMouse action){mouseReleased=action;}
//CLICKED
	private ActionOfMouse mouseCliqued;
		public ActionOfMouse getMouseCliquedAction(){return mouseCliqued;}
		public void setMouseCliquedAction(ActionOfMouse action){mouseCliqued=action;}
//DRAGGED
	private ActionOfMouse mouseDragged;
		public ActionOfMouse getMouseDraggedAction(){return mouseDragged;}
		public void setMouseDraggedAction(ActionOfMouse action){mouseDragged=action;}
//MOVED
	private ActionOfMouse mouseMoved;
		public ActionOfMouse getMouseMovedAction(){return mouseMoved;}
		public void setMouseMovedAction(ActionOfMouse action){mouseMoved=action;}
//ACTION
	public static class ActionOfMouse{
		public void run(MouseEvent m) {}
	}
}