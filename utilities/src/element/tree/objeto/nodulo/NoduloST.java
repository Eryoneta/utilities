package element.tree.objeto.nodulo;
import element.tree.objeto.conexao.ConexaoST;
public class NoduloST{
//STATES
	public enum State{
		UNSELECTED,
		SELECTED,
		HIGHLIGHTED,
		TO_BE_CREATOR,
		TO_BE_DELETED;
		public boolean is(NoduloST.State... states){
			for(NoduloST.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
//STATE
	private NoduloST.State state=NoduloST.State.UNSELECTED;
		public NoduloST.State getState(){return state;}
		public void setState(NoduloST.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(NoduloST.State.SELECTED)){
						this.state=state;							//UNSELECTED -> SELECTED
						nod.getConexao().setState(ConexaoST.State.HIGHLIGHTED);
					}else if(state.is(NoduloST.State.HIGHLIGHTED)){
						this.state=state;							//UNSELECTED -> HIGHLIGHTED
					}else if(state.is(NoduloST.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
					}else if(state.is(NoduloST.State.TO_BE_DELETED)){
						this.state=state;							//UNSELECTED -> TO_BE_DELETED
					}
				break;
				case SELECTED:
					if(state.is(NoduloST.State.UNSELECTED)){
						if(nod.getConexao().getState().is(ConexaoST.State.SELECTED)){
							this.state=NoduloST.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
						}else{
							this.state=state;						//SELECTED -> UNSELECTED
							if(nod.getConexao().getState().is(ConexaoST.State.HIGHLIGHTED))nod.getConexao().setState(ConexaoST.State.UNSELECTED);
							if(nod.getConexao().getState().is(ConexaoST.State.HIGHLIGHTED)){
								this.state=NoduloST.State.HIGHLIGHTED;//SELECTED -> HIGHLIGHTED
							}
							
						}
					}
				break;
				case HIGHLIGHTED:
					if(state.is(NoduloST.State.SELECTED)){
						this.state=state;							//HIGHLIGHTED -> SELECTED
						if(nod.getConexao().getState().is(ConexaoST.State.HIGHLIGHTED))nod.getConexao().setState(ConexaoST.State.UNSELECTED);
					}else if(state.is(NoduloST.State.UNSELECTED)){
						if(!nod.getConexao().getState().is(ConexaoST.State.SELECTED,ConexaoST.State.HIGHLIGHTED)){
							this.state=state;							//HIGHLIGHTED -> UNSELECTED
						}
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(NoduloST.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
					}
				break;
				case TO_BE_DELETED:
					if(state.is(NoduloST.State.UNSELECTED)){
						if(nod.getConexao().getState().is(ConexaoST.State.TO_BE_DELETED))return;
						this.state=state;							//TO_BE_DELETED -> UNSELECTED
					}
				break;
			}
		}
//NODULO
	private Nodulo nod;
//MAIN
	public NoduloST(Nodulo nod){this.nod=nod;}
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+(state==null?0:state.hashCode());
		return result;
	}
}