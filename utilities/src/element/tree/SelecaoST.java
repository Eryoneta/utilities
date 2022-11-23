package element.tree;
public class SelecaoST{
//STATES
	public enum State{
		TO_UNSELECT,
		TO_SELECT,
		TO_CREATE,
		TO_CREATE_SON,
		TO_CREATE_PAI,
		TO_DELETE;
		public boolean is(SelecaoST.State... states){
			for(SelecaoST.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
//STATE
	private State state=SelecaoST.State.TO_SELECT;
		public SelecaoST.State getState(){return state;}
		public void setState(SelecaoST.State state){this.state=state;}
//SELEÇÃO
//	private Selecao selecao;
//MAIN
	public SelecaoST(Selecao selecao){
//		this.selecao=selecao;
	}
}