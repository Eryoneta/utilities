package element.tree.objeto.conexao.segmento;
import element.tree.objeto.conexao.ConexaoST;
public class SegmentoST{
//STATES
	public enum State{
		UNSELECTED,
		TO_BE_CREATOR;
		public boolean is(SegmentoST.State... states){
			for(SegmentoST.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
//STATE
	private SegmentoST.State state=SegmentoST.State.UNSELECTED;
		public SegmentoST.State getState(){return state;}
		public void setState(SegmentoST.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(SegmentoST.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
						seg.getConexao().setState(ConexaoST.State.TO_BE_CREATOR);
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(SegmentoST.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
					}
				break;
			}
		}
//SEGMENTO
	private Segmento seg;
//MAIN
	public SegmentoST(Segmento seg){this.seg=seg;}
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+(state==null?0:state.hashCode());
		return result;
	}
}