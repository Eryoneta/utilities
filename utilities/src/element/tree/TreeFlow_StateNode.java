package element.tree;

import architecture.micro_state.StateId;
import architecture.micro_state.StateNode;
import architecture.micro_state.listener.InputListenerBundle;
import architecture.micro_state.listener.KeyboardListenerBundle.ActionOfKeyboard;
import architecture.micro_state.listener.MouseListenerBundle.ActionOfMouse;
import element.tree.objeto.Objeto;

public class TreeFlow_StateNode extends StateNode{
//LISTENERS
	private InputListenerBundle listenerBundle;
		public ActionOfMouse getMousePressedAction(){return listenerBundle.getMousePressedAction();}
		public void setMousePressedAction(ActionOfMouse action){listenerBundle.setMousePressedAction(action);}
		public ActionOfMouse getMouseReleasedAction(){return listenerBundle.getMouseReleasedAction();}
		public void setMouseReleasedAction(ActionOfMouse action){listenerBundle.setMouseReleasedAction(action);}
		public ActionOfMouse getMouseCliquedAction(){return listenerBundle.getMouseCliquedAction();}
		public void setMouseCliquedAction(ActionOfMouse action){listenerBundle.setMouseCliquedAction(action);}
		public ActionOfMouse getMouseDraggedAction(){return listenerBundle.getMouseDraggedAction();}
		public void setMouseDraggedAction(ActionOfMouse action){listenerBundle.setMouseDraggedAction(action);}
		public ActionOfMouse getMouseMovedAction(){return listenerBundle.getMouseDraggedAction();}
		public void setMouseMovedAction(ActionOfMouse action){listenerBundle.setMouseMovedAction(action);}
		public ActionOfKeyboard getKeyPressedAction(){return listenerBundle.getKeyPressedAction();}
		public void setKeyPressedAction(ActionOfKeyboard action){listenerBundle.setKeyPressedAction(action);}
		public ActionOfKeyboard getKeyReleasedAction(){return listenerBundle.getKeyReleasedAction();}
		public void setKeyReleasedAction(ActionOfKeyboard action){listenerBundle.setKeyReleasedAction(action);}
		public ActionOfKeyboard getKeyClickedAction(){return listenerBundle.getKeyClickedAction();}
		public void setKeyClickedAction(ActionOfKeyboard action){listenerBundle.setKeyClickedAction(action);}
//OBJETO
	private Objeto[]objs;
		public Objeto[]getObjetos() {return objs;}
		public void setObjetos(Objeto... objs) {this.objs=objs;}
//MAIN
	public TreeFlow_StateNode(StateId...ids) throws Exception{
		super(ids);
	}
}