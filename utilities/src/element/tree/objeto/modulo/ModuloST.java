package element.tree.objeto.modulo;
import element.tree.main.Tree;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.ConexaoST;
public class ModuloST{
//STATES
	public enum State{
		UNSELECTED,
		SELECTED,
		HIGHLIGHTED,
		TO_BE_CREATOR,
		TO_BE_SON,
		TO_BE_PAI,
		TO_BE_DELETED;
		public boolean is(ModuloST.State... states){
			for(ModuloST.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
//STATE
	private State state=ModuloST.State.UNSELECTED;
		public ModuloST.State getState(){return state;}
		public void setState(ModuloST.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(ModuloST.State.SELECTED)){
						this.state=state;							//UNSELECTED -> SELECTED
						for(Conexao cox:mod.getConexoes())cox.setState(ConexaoST.State.HIGHLIGHTED);
					}else if(state.is(ModuloST.State.HIGHLIGHTED)){
						this.state=state;							//UNSELECTED -> HIGHLIGHTED
					}else if(state.is(ModuloST.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
					}else if(state.is(ModuloST.State.TO_BE_SON)){
						if(mod==Tree.getMestre())return;	//MESTRE NÃO PODE SER SON
						this.state=state;							//UNSELECTED -> TO_BE_SON
					}else if(state.is(ModuloST.State.TO_BE_PAI)){
						this.state=state;							//UNSELECTED -> TO_BE_PAI
					}else if(state.is(ModuloST.State.TO_BE_DELETED)){
						if(mod==Tree.getMestre())return;	//MESTRE NÃO PODE SER DEL
						this.state=state;							//UNSELECTED -> TO_BE_DELETED
						for(Conexao cox:mod.getConexoes())cox.setState(ConexaoST.State.TO_BE_DELETED);
					}
				break;
				case SELECTED:
					if(state.is(ModuloST.State.UNSELECTED)){
						for(Conexao cox:mod.getConexoes())if(cox.getState().is(ConexaoST.State.SELECTED)){
							this.state=ModuloST.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
							break;
						}
						if(getState().is(ModuloST.State.SELECTED)){
							this.state=state;						//SELECTED -> UNSELECTED
						}
						for(Conexao cox:mod.getConexoes())if(cox.getState().is(ConexaoST.State.HIGHLIGHTED))cox.setState(ConexaoST.State.UNSELECTED);
						if(!getState().is(ModuloST.State.UNSELECTED))for(Conexao cox:mod.getConexoes())if(cox.getState().is(ConexaoST.State.HIGHLIGHTED)){
							this.state=ModuloST.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
							break;
						}
					}
				break;
				case HIGHLIGHTED:
					if(state.is(ModuloST.State.SELECTED)){
						this.state=state;							//HIGHLIGHTED -> SELECTED
						for(Conexao cox:mod.getConexoes())cox.setState(ConexaoST.State.HIGHLIGHTED);
					}else if(state.is(ModuloST.State.UNSELECTED)){
						this.state=state;							//HIGHLIGHTED -> UNSELECTED
						for(Conexao cox:mod.getConexoes())if(cox.getState().is(ConexaoST.State.SELECTED,ConexaoST.State.HIGHLIGHTED)){
							this.state=ModuloST.State.HIGHLIGHTED;	//UNSELECTED -> HIGHLIGHTED
							break;
						}
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(ModuloST.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
					}
				break;
				case TO_BE_SON:
					if(state.is(ModuloST.State.UNSELECTED)){
						this.state=state;							//TO_BE_SON -> UNSELECTED
					}
				break;
				case TO_BE_PAI:
					if(state.is(ModuloST.State.UNSELECTED)){
						this.state=state;							//TO_BE_PAI -> UNSELECTED
					}
				break;
				case TO_BE_DELETED:
					if(state.is(ModuloST.State.UNSELECTED)){
						this.state=state;							//TO_BE_DELETED -> UNSELECTED
						for(Conexao cox:mod.getConexoes())cox.setState(ConexaoST.State.UNSELECTED);
					}
				break;
			}
		}
//MÓDULO
	private Modulo mod;
//MAIN
	public ModuloST(Modulo mod){this.mod=mod;}
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+(state==null?0:state.hashCode());
		return result;
	}
}