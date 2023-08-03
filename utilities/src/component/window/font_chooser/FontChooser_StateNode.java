package component.window.font_chooser;

import architecture.micro_state.StateId;
import architecture.micro_state.StateNode;

public class FontChooser_StateNode extends StateNode{
//ACTION
	public static class ActionOfText{
		public void run(String newText) {}
	}
//NAME_TEXT_CHANGED
	private ActionOfText nameTextChanged;
		public void setNameTextChangedAction(ActionOfText action){nameTextChanged=action;}
		public ActionOfText getNameTextChangedAction() {return nameTextChanged;}
//STYLE_TEXT_CHANGED
	private ActionOfText styleTextChanged;
		public void setStyleTextChangedAction(ActionOfText action){styleTextChanged=action;}
		public ActionOfText getStyleTextChangedAction() {return styleTextChanged;}
//SIZE_TEXT_CHANGED
	private ActionOfText sizeTextChanged;
		public void setSizeTextChangedAction(ActionOfText action){sizeTextChanged=action;}
		public ActionOfText getSizeTextChangedAction() {return sizeTextChanged;}
//MAIN
	public FontChooser_StateNode(StateId...ids) throws Exception{
		super(ids);
	}
}