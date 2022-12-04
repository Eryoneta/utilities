package element.tree;

import java.awt.event.MouseEvent;

import architecture.micro_state.StateFlow;
import architecture.micro_state.StateId;
import architecture.micro_state.listener.MouseListenerBundle.ActionOfMouse;
import architecture.micro_state.listener.StateListenerBundle.ActionOfState;
import architecture.rrf_vp.flow.FlowJoint;
import element.tree.objeto.Objeto;

public class TreeFlow implements FlowJoint<TreeFlow,Tree> {
//ROOT
	private Tree root;
		@Override public Tree getRoot() {return root;}
//STATE_FLOW
	public static enum State implements StateId{
	//VARS
		INACTIVE			(StateFlow.Level.LEVEL_1,StateFlow.Index.INDEX_1),
		CREATING			(StateFlow.Level.LEVEL_1,StateFlow.Index.INDEX_2),
		CONNECTING			(StateFlow.Level.LEVEL_1,StateFlow.Index.INDEX_3),
		DELETING			(StateFlow.Level.LEVEL_1,StateFlow.Index.INDEX_4),
		EDITING_TITLE		(StateFlow.Level.LEVEL_1,StateFlow.Index.INDEX_5),
		
		DRAGGING			(StateFlow.Level.LEVEL_2,StateFlow.Index.INDEX_1),
		MOVING				(StateFlow.Level.LEVEL_2,StateFlow.Index.INDEX_2),
		SELECTING_AREA		(StateFlow.Level.LEVEL_2,StateFlow.Index.INDEX_3),
		
		WAITING_1			(StateFlow.Level.LEVEL_3,StateFlow.Index.INDEX_1),
		WAITING_2			(StateFlow.Level.LEVEL_3,StateFlow.Index.INDEX_2),
		WAITING_3			(StateFlow.Level.LEVEL_3,StateFlow.Index.INDEX_3),
		
		LEFT_BUTTON			(StateFlow.Level.LEVEL_4,StateFlow.Index.INDEX_1),
		MIDDLE_BUTTON		(StateFlow.Level.LEVEL_4,StateFlow.Index.INDEX_2),
		RIGHT_BUTTON		(StateFlow.Level.LEVEL_4,StateFlow.Index.INDEX_3),
		
		DRAGGING_VIEW		(StateFlow.Level.LEVEL_5,StateFlow.Index.INDEX_1),
		AUTO_DRAGGING_VIEW	(StateFlow.Level.LEVEL_5,StateFlow.Index.INDEX_2);
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
			stateFlow.setState(ids);
		}
		public void setStateObjs(Objeto... objs) throws Exception {
			((TreeFlow_StateNode)stateFlow.getStateNode()).setObjetos(objs);
		}
//MAIN
	public TreeFlow(Tree root) {
		this.root=root;
		try {
			stateFlow.loadFlow(
					new TreeFlow_StateNode(State.INACTIVE){{
						setMousePressedAction(new ActionOfMouse() {
							public void run(MouseEvent m) {
								setState(State.LEFT_BUTTON, State.WAITING_1, State.INACTIVE);
//								setStateObjs();
							}
						});
						setStateLoadedAction(new ActionOfState() {
							public void run() {
								
							}
						});
					}},
					new TreeFlow_StateNode(State.LEFT_BUTTON, State.WAITING_1, State.INACTIVE){{
						
					}}
			);
		} catch (Exception error) {
			//TODO: ERROR
			//Erro ao gerar fluxo de states
		}
	}
//FUNCS
	@Override public void init() {}
}