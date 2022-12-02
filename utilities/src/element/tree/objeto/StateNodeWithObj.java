package element.tree.objeto;

import architecture.micro_state.StateId;
import architecture.micro_state.StateNode;

public class StateNodeWithObj extends StateNode{
//OBJETO
	private Objeto obj;
		public Objeto getObjeto() {return obj;}
		public void setObjeto(Objeto obj) {this.obj=obj;}
//MAIN
	public StateNodeWithObj(StateId...ids) {
		super(ids);
	}
}