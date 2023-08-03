package component.window.font_chooser;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import architecture.micro_state.StateFlow;
import architecture.micro_state.StateId;
import architecture.micro_state.listener.StateListenerBundle.ActionOfState;
import architecture.rrf_vp.flow.FlowJoint;

public class FontChooserFlow implements FlowJoint<FontChooserFlow, FontChooser>{
//ROOT
	private FontChooser root;
		@Override public FontChooser getRoot() {return root;}
//STATE_FLOW
	public static enum State implements StateId{
	//VARS
		INACTIVE			(StateFlow.Level.LEVEL_1,StateFlow.Index.INDEX_1),
		BLOCKING			(StateFlow.Level.LEVEL_1,StateFlow.Index.INDEX_2);
	//LEVEL
		private StateFlow.Level level;
			public StateFlow.Level getLevel() {return level;}
	//INDEX
		private StateFlow.Index index;
			public StateFlow.Index getIndex() {return index;}
	//MAIN
		State(StateFlow.Level level,StateFlow.Index index){
			this.level=level;
			this.index=index;
		}
	}
	private StateFlow stateFlow=new StateFlow(State.INACTIVE);
		public long getStateId() throws Exception {return stateFlow.getStateId();}
		public void setState(State...ids) {
			try {
				stateFlow.setState(ids);
			} catch (Exception error) {
				error.printStackTrace();
			}
		}
//MAIN
	public FontChooserFlow(FontChooser root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {
		loadActions();
		loadStates();
	}
	private void loadActions() {
		getRoot().getView().
		getRoot().getView().getNomeTexto().getDocument().addDocumentListener(new DocumentListener() {
			@Override public void removeUpdate(DocumentEvent d) {textChanged(d);}
			@Override public void insertUpdate(DocumentEvent d) {textChanged(d);}
			@Override public void changedUpdate(DocumentEvent d) {}
			private void textChanged(DocumentEvent d) {
				try {
					final FontChooser_StateNode node=(FontChooser_StateNode)stateFlow.getStateNode();
					if(node.getNameTextChangedAction()!=null) {
						final String newText=d.getDocument().getText(0, d.getDocument().getLength());
						node.getNameTextChangedAction().run(newText);
					}
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
		});
		getRoot().getView().getEstiloTexto().getDocument().addDocumentListener(new DocumentListener() {
			@Override public void removeUpdate(DocumentEvent d) {textChanged(d);}
			@Override public void insertUpdate(DocumentEvent d) {textChanged(d);}
			@Override public void changedUpdate(DocumentEvent d) {}
			private void textChanged(DocumentEvent d) {
				try {
					final FontChooser_StateNode node=(FontChooser_StateNode)stateFlow.getStateNode();
					if(node.getStyleTextChangedAction()!=null) {
						final String newText=d.getDocument().getText(0, d.getDocument().getLength());
						node.getStyleTextChangedAction().run(newText);
					}
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
		});
		getRoot().getView().getTamanhoTexto().getDocument().addDocumentListener(new DocumentListener() {
			@Override public void removeUpdate(DocumentEvent d) {textChanged(d);}
			@Override public void insertUpdate(DocumentEvent d) {textChanged(d);}
			@Override public void changedUpdate(DocumentEvent d) {}
			private void textChanged(DocumentEvent d) {
				try {
					final FontChooser_StateNode node=(FontChooser_StateNode)stateFlow.getStateNode();
					if(node.getSizeTextChangedAction()!=null) {
						final String newText=d.getDocument().getText(0, d.getDocument().getLength());
						node.getSizeTextChangedAction().run(newText);
					}
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
		});
	}
	private void loadStates() {
		try {
			stateFlow.loadFlow(
					new FontChooser_StateNode(State.INACTIVE){{
						setStateLoadedAction(new ActionOfState() {
							public void run() {
								getRoot().getView().getOkBotao().setEnabled(true);
							}
						});
						setNameTextChangedAction(new ActionOfText() {
							public void run(String newText) {
								if(!getRoot().getRule().isFontNameValid(newText))setState(State.BLOCKING);
							}
						});
						setStyleTextChangedAction(new ActionOfText() {
							public void run(String newText) {
								if(!getRoot().getRule().isFontStyleValid(newText))setState(State.BLOCKING);
							}
						});
						setSizeTextChangedAction(new ActionOfText() {
							public void run(String newText) {
								try {
									final int size=Integer.parseInt(newText);
									if(!getRoot().getRule().isFontSizeValid(size))setState(State.BLOCKING);
								}catch (NumberFormatException error) {	//NOT A NUMBER
									setState(State.BLOCKING);
								}
							}
						});
					}},
					new FontChooser_StateNode(State.BLOCKING){{
						setStateLoadedAction(new ActionOfState() {
							public void run() {
								getRoot().getView().getOkBotao().setEnabled(false);
							}
						});
						setNameTextChangedAction(new ActionOfText() {
							public void run(String newText) {
								if(getRoot().getRule().isFontNameValid(newText))setState(State.INACTIVE);
							}
						});
						setStyleTextChangedAction(new ActionOfText() {
							public void run(String newText) {
								if(getRoot().getRule().isFontStyleValid(newText))setState(State.INACTIVE);
							}
						});
						setSizeTextChangedAction(new ActionOfText() {
							public void run(String newText) {
								try {
									final int size=Integer.parseInt(newText);
									if(getRoot().getRule().isFontSizeValid(size))setState(State.INACTIVE);
								}catch (NumberFormatException error) {	//NOT A NUMBER
									//NÃO MUDA O STATE
								}
							}
						});
					}}
			);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
}