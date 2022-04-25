package element.tree.objeto.conexao.segmento;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import element.tree.main.Tree;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.nodulo.Nodulo;
public class Segmento extends Objeto{
//ST
	private final SegmentoST ST=new SegmentoST(this);
		public SegmentoST getST(){return ST;}
		public SegmentoST.State getState(){return getST().getState();}
		public void setState(SegmentoST.State state){getST().setState(state);}
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
		result=prime*result+((ST==null)?0:ST.hashCode());
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
		if(ST!=seg.ST)return false;
		if(conexao!=seg.conexao)return false;
		if(ponta1!=seg.ponta1)return false;
		if(ponta2!=seg.ponta2)return false;
		return true;
	}
}