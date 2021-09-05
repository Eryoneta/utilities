package element.tree.objeto.conexao;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import element.tree.Tree;
import element.tree.objeto.Objeto;
public class Segmento extends Objeto{
//STATES
	public enum State{
		UNSELECTED,
		TO_BE_CREATOR;
		public boolean is(Segmento.State... states){
			for(Segmento.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
	private Segmento.State state=Segmento.State.UNSELECTED;
		public Segmento.State getState(){return state;}
		public void setState(Segmento.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(Segmento.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
						getConexao().setState(Conexao.State.TO_BE_CREATOR);
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(Segmento.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
					}
				break;
			}
		}
//CONEXÃO
	public Conexao conexao;
		public Conexao getConexao(){return conexao;}
		public void setConexao(Conexao cox){conexao=cox;}
//PONTAS
	private Objeto ponta1;
		public Objeto getPonta1(){return ponta1;}
		public void setPonta1(Objeto obj){ponta1=obj;}
	private Objeto ponta2;
		public Objeto getPonta2(){return ponta2;}
		public void setPonta2(Objeto obj){ponta2=obj;}
//LOCAL
	public int getX1Index(){
		switch(getPonta1().getTipo()){
			case MODULO:default:	return getConexao().getX1Index();	//SON
			case NODULO:			return ((Nodulo)getPonta1()).getXIndex();
		}
	}
	public int getX1(int unit){
		switch(getPonta1().getTipo()){
			case MODULO:default:	return getConexao().getX1(unit);	//SON
			case NODULO:			return ((Nodulo)getPonta1()).getX(unit);
		}
	}
	public int getY1Index(){
		switch(getPonta1().getTipo()){
			case MODULO:default:	return getConexao().getY1Index();	//SON
			case NODULO:			return ((Nodulo)getPonta1()).getYIndex();
		}
	}
	public int getY1(int unit){
		switch(getPonta1().getTipo()){
			case MODULO:default:	return getConexao().getY1(unit);		//SON
			case NODULO:			return ((Nodulo)getPonta1()).getY(unit);
		}
	}
	public int getX2Index(){
		switch(getPonta2().getTipo()){
			case MODULO:default:	return getConexao().getX2Index();	//PAI
			case NODULO:			return ((Nodulo)getPonta2()).getXIndex();
		}
	}
	public int getX2(int unit){
		switch(getPonta2().getTipo()){
			case MODULO:default:	return getConexao().getX2(unit);		//PAI
			case NODULO:			return ((Nodulo)getPonta2()).getX(unit);
		}
	}
	public int getY2Index(){
		switch(getPonta2().getTipo()){
			case MODULO:default:	return getConexao().getY2Index();	//PAI
			case NODULO:			return ((Nodulo)getPonta2()).getYIndex();
		}
	}
	public int getY2(int unit){
		switch(getPonta2().getTipo()){
			case MODULO:default:	return getConexao().getY2(unit);		//PAI
			case NODULO:			return ((Nodulo)getPonta2()).getY(unit);
		}
	}
//FORMA
	public Line2D getForm(int unit){return new Line2D.Float(getX1(unit),getY1(unit),getX2(unit),getY2(unit));}
	public boolean contains(Point mouse){
		final Line2D forma=getForm(Tree.UNIT);
		final float raio=Tree.UNIT*0.6f;
		final Rectangle area=new Rectangle((int)(mouse.x-raio),(int)(mouse.y-raio),(int)(raio*2),(int)(raio*2));
		return forma.intersects(area);
	}
//MAIN
	public Segmento(Conexao cox,Objeto obj1,Objeto obj2){
		super(Objeto.Tipo.SEGMENTO);
		setConexao(cox);
		setPonta1(obj1);
		setPonta2(obj2);
	}
//FUNCS
@Override
	public int hashCode(){	//ALGUMAS PARTES SÃO DESNECESSÁRIAS PARA DISTINÇÃO
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+((state==null)?0:state.hashCode());
		result=prime*result+((conexao==null)?0:conexao.hashCode());
		result=prime*result+((ponta1==null)?0:ponta1.hashCode());
		result=prime*result+((ponta2==null)?0:ponta2.hashCode());
		return result;
	}
@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(!super.equals(obj))return false;
		if(getClass()!=obj.getClass())return false;
		final Segmento seg=(Segmento)obj;
		if(!super.equals(obj))return false;
		if(state!=seg.state)return false;
		if(conexao!=seg.conexao)return false;
		if(ponta1!=seg.ponta1)return false;
		if(ponta2!=seg.ponta2)return false;
		return true;
	}
}