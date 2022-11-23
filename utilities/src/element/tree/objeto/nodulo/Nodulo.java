package element.tree.objeto.nodulo;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import element.tree.propriedades.Grossura;
import element.tree.main.Tree;
import element.tree.objeto.Objeto;
import element.tree.objeto.ObjetoBoundsListener;
import element.tree.objeto.conexao.Conexao;
public class Nodulo extends Objeto{
//ST
	private final NoduloST ST=new NoduloST(this);
		public NoduloST getST(){return ST;}
		public NoduloST.State getState(){return getST().getState();}
		public void setState(NoduloST.State state){getST().setState(state);}
//UI
	private final NoduloUI UI=new NoduloUI(this);
		public NoduloUI getUI(){return UI;}
		public void draw(Graphics2D imagemEdit,int unit){
			getUI().draw(imagemEdit,unit);
		}
//VAR GLOBAIS
	public float getRoundValue(int unit){
		switch(getConexao().getGrossura().getIndex()){
			case Grossura.THIN:				return unit/4;
			case Grossura.MEDIUM:default:	return unit/2;
			case Grossura.WIDE:				return unit/1;
			case Grossura.ULTRA_WIDE:		return unit/0.5f;
		}
	}
//BOUNDS
	private int xIndex;
		public int getXIndex(){return xIndex;}
		public int getX(int unit){return ((Tree.getLocalX()+xIndex)*unit);}
	private int yIndex;
		public int getYIndex(){return yIndex;}
		public int getY(int unit){return ((Tree.getLocalY()+yIndex)*unit);}
	public Point getLocationIndex(){return new Point(getXIndex(),getYIndex());}
	public Point getLocation(int unit){return new Point(getX(unit),getY(unit));}
	public void justSetLocationIndex(int x,int y){
		this.xIndex=x;
		this.yIndex=y;
	}
	public void setLocationIndex(int x,int y){
		final Rectangle nodOldBounds=getFormIndex();
		final Rectangle coxOldBounds=getConexao().getFormIndex().getBounds();
		justSetLocationIndex(x,y);
		triggerBoundsListener(nodOldBounds,coxOldBounds);
	}
//FORMA
	public float getSize(int unit){
		switch(getConexao().getGrossura().getIndex()){
			case Grossura.THIN:				return unit*0.5f;
			case Grossura.MEDIUM:default:	return unit*1.0f;
			case Grossura.WIDE:				return unit*2.0f;
			case Grossura.ULTRA_WIDE:		return unit*3.0f;
		}
	}
	public Rectangle getFormIndex(){return new Rectangle(getXIndex(),getYIndex(),1,1);}
	public Rectangle getForm(int unit){
		final float size=getSize(unit);
		return new Rectangle((int)(getX(unit)-(size/2)),(int)(getY(unit)-(size/2)),(int)(size),(int)(size));
	}
@Override
	public boolean contains(Point mouse){return getForm(Tree.UNIT).contains(mouse);}
//LISTENER: DISPARA COM O MUDAR DE BOUNDS DE UM OBJETO
	private static List<ObjetoBoundsListener>boundsListeners=new ArrayList<ObjetoBoundsListener>();
		public static void addBoundsListener(ObjetoBoundsListener boundsListener){boundsListeners.add(boundsListener);}
		private void triggerBoundsListener(Rectangle nodOldBounds,Rectangle coxOldBounds){
			for(ObjetoBoundsListener boundsListener:boundsListeners){
				boundsListener.boundsChanged(this,nodOldBounds,getFormIndex());
			}
			for(ObjetoBoundsListener boundsListener:Conexao.boundsListeners){
				boundsListener.boundsChanged(getConexao(),coxOldBounds,getConexao().getFormIndex().getBounds());
			}
		}
//CONEXÃO
	private Conexao conexao;
		public Conexao getConexao(){return conexao;}
		public void setConexao(Conexao cox){conexao=cox;}
//INDEX ON COX
	private int indexOnCox=-1;
		public int getIndexOnCox(){return indexOnCox;}
		public void setIndexOnCox(int index){this.indexOnCox=index;}
//MAIN
	public Nodulo(Conexao cox,int x,int y){
		super(Objeto.Tipo.NODULO);
		setConexao(cox);
		justSetLocationIndex(x,y);
	}
//FUNCS
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+((ST==null)?0:ST.hashCode());
		result=prime*result+((conexao==null)?0:conexao.hashCode());
		result=prime*result+indexOnCox;
		result=prime*result+xIndex;
		result=prime*result+yIndex;
		return result;
	}
@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj==null)return false;
		if(!super.equals(obj))return false;
		if(getClass()!=obj.getClass())return false;
		final Nodulo nod=(Nodulo)obj;
		if(ST!=nod.ST)return false;
		if(conexao!=nod.conexao)return false;
		if(indexOnCox!=nod.indexOnCox)return false;
		if(xIndex!=nod.xIndex)return false;
		if(yIndex!=nod.yIndex)return false;
		return true;
	}
//TAG -> NOD
	public static Nodulo getNodulo(Element nodTag,Conexao cox){
		return new Nodulo(
				cox,											//COX
				Integer.parseInt(nodTag.getAttribute("x")),		//X
				Integer.parseInt(nodTag.getAttribute("y"))		//Y
		);
	}
//NOD -> TAG
	public void addToXML(Document xml,Element coxTag){
		final Element nodTag=xml.createElement("nod");			//NOD
		nodTag.setAttribute("x",String.valueOf(getXIndex()));	//X
		nodTag.setAttribute("y",String.valueOf(getYIndex()));	//Y
		coxTag.appendChild(nodTag);
	}
}