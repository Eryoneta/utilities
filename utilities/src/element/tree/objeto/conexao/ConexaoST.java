package element.tree.objeto.conexao;
import element.tree.objeto.nodulo.Nodulo;
import element.tree.objeto.nodulo.NoduloST;
import element.tree.objeto.conexao.segmento.Segmento;
import element.tree.objeto.conexao.segmento.SegmentoST;
import element.tree.objeto.modulo.ModuloST;
public class ConexaoST{
//STATES
	public enum State{
		UNSELECTED,
		SELECTED,
		HIGHLIGHTED,
		TO_BE_CREATOR,
		TO_BE_DELETED;
		public boolean is(ConexaoST.State... states){
			for(ConexaoST.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
//STATE
	private ConexaoST.State state=ConexaoST.State.UNSELECTED;
		public ConexaoST.State getState(){return state;}
		public void setState(ConexaoST.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(ConexaoST.State.SELECTED)){
						this.state=state;							//UNSELECTED -> SELECTED
						for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.HIGHLIGHTED);
						cox.getPai().setState(ModuloST.State.HIGHLIGHTED);
						cox.getSon().setState(ModuloST.State.HIGHLIGHTED);
					}else if(state.is(ConexaoST.State.HIGHLIGHTED)){
						this.state=state;							//UNSELECTED -> HIGHLIGHTED
						for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.HIGHLIGHTED);
						cox.getPai().setState(ModuloST.State.HIGHLIGHTED);
						cox.getSon().setState(ModuloST.State.HIGHLIGHTED);
					}else if(state.is(ConexaoST.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
						for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.TO_BE_CREATOR);
					}else if(state.is(ConexaoST.State.TO_BE_DELETED)){
						this.state=state;							//UNSELECTED -> TO_BE_DELETED
						for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.TO_BE_DELETED);
					}
				break;
				case SELECTED:
					if(state.is(ConexaoST.State.UNSELECTED)){
						if(cox.getPai().getState().is(ModuloST.State.SELECTED)||cox.getSon().getState().is(ModuloST.State.SELECTED)){
							this.state=ConexaoST.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
							for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.HIGHLIGHTED);
							cox.getPai().setState(ModuloST.State.HIGHLIGHTED);
							cox.getSon().setState(ModuloST.State.HIGHLIGHTED);
						}else{
							this.state=state;						//SELECTED -> UNSELECTED
							for(Nodulo nod:cox.getNodulos())if(nod.getState().is(NoduloST.State.HIGHLIGHTED))nod.setState(NoduloST.State.UNSELECTED);
							if(cox.getPai().getState().is(ModuloST.State.HIGHLIGHTED))cox.getPai().setState(ModuloST.State.UNSELECTED);
							if(cox.getSon().getState().is(ModuloST.State.HIGHLIGHTED))cox.getSon().setState(ModuloST.State.UNSELECTED);
						}
					}
				break;
				case HIGHLIGHTED:
					if(state.is(ConexaoST.State.SELECTED)){
						this.state=state;							//HIGHLIGHTED -> SELECTED
						for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.HIGHLIGHTED);
						cox.getPai().setState(ModuloST.State.HIGHLIGHTED);
						cox.getSon().setState(ModuloST.State.HIGHLIGHTED);
					}else if(state.is(ConexaoST.State.UNSELECTED)){
						this.state=state;							//HIGHLIGHTED -> UNSELECTED
						if(cox.getPai().getState().is(ModuloST.State.SELECTED)||cox.getSon().getState().is(ModuloST.State.SELECTED)){
							this.state=ConexaoST.State.HIGHLIGHTED;	//UNSELECTED -> HIGHLIGHTED
						}
						if(getState().is(ConexaoST.State.UNSELECTED))for(Nodulo nod:cox.getNodulos())if(nod.getState().is(NoduloST.State.SELECTED)){
							this.state=ConexaoST.State.HIGHLIGHTED;	//UNSELECTED -> HIGHLIGHTED
							break;
						}
						if(getState().is(ConexaoST.State.UNSELECTED)){
							for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.UNSELECTED);
							cox.getPai().setState(ModuloST.State.UNSELECTED);
							cox.getSon().setState(ModuloST.State.UNSELECTED);
						}else{
							for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.HIGHLIGHTED);
							cox.getPai().setState(ModuloST.State.HIGHLIGHTED);
							cox.getSon().setState(ModuloST.State.HIGHLIGHTED);
						}
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(ConexaoST.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
						for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.UNSELECTED);
						for(Segmento seg:cox.getSegmentos())seg.setState(SegmentoST.State.UNSELECTED);
					}
				break;
				case TO_BE_DELETED:
					if(state.is(ConexaoST.State.UNSELECTED)){
						if(cox.getPai().getState().is(ModuloST.State.TO_BE_DELETED)||cox.getSon().getState().is(ModuloST.State.TO_BE_DELETED))return;
						this.state=state;							//TO_BE_DELETED -> UNSELECTED
						for(Nodulo nod:cox.getNodulos())nod.setState(NoduloST.State.UNSELECTED);
					}
				break;
			}
		}
//CONEXÃO
	private Conexao cox;
//MAIN
	public ConexaoST(Conexao cox){this.cox=cox;}
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+(state==null?0:state.hashCode());
		return result;
	}
}