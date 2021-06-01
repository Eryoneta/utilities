package element.tree.objeto.conexao;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import element.tree.propriedades.Cor;
import element.tree.propriedades.Grossura;
import element.tree.Tree;
import element.tree.objeto.Objeto;
import element.tree.objeto.ObjetoBoundsListener;
public class Nodulo extends Objeto{
//STATES
	public enum State{
		UNSELECTED,
		SELECTED,
		HIGHLIGHTED,
		TO_BE_CREATOR,
		TO_BE_DELETED;
		public boolean is(Nodulo.State... states){
			for(Nodulo.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
	private Nodulo.State state=Nodulo.State.UNSELECTED;
		public Nodulo.State getState(){return state;}
		public void setState(Nodulo.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(Nodulo.State.SELECTED)){
						this.state=state;							//UNSELECTED -> SELECTED
						getConexao().setState(Conexao.State.HIGHLIGHTED);
					}else if(state.is(Nodulo.State.HIGHLIGHTED)){
						this.state=state;							//UNSELECTED -> HIGHLIGHTED
					}else if(state.is(Nodulo.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
					}else if(state.is(Nodulo.State.TO_BE_DELETED)){
						this.state=state;							//UNSELECTED -> TO_BE_DELETED
					}
				break;
				case SELECTED:
					if(state.is(Nodulo.State.UNSELECTED)){
						if(getConexao().getState().is(Conexao.State.SELECTED)){
							this.state=Nodulo.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
						}else{
							this.state=state;						//SELECTED -> UNSELECTED
							if(getConexao().getState().is(Conexao.State.HIGHLIGHTED))getConexao().setState(Conexao.State.UNSELECTED);
							if(getConexao().getState().is(Conexao.State.HIGHLIGHTED)){
								this.state=Nodulo.State.HIGHLIGHTED;//SELECTED -> HIGHLIGHTED
							}
							
						}
					}
				break;
				case HIGHLIGHTED:
					if(state.is(Nodulo.State.SELECTED)){
						this.state=state;							//HIGHLIGHTED -> SELECTED
						if(getConexao().getState().is(Conexao.State.HIGHLIGHTED))getConexao().setState(Conexao.State.UNSELECTED);
					}else if(state.is(Nodulo.State.UNSELECTED)){
						if(!getConexao().getState().is(Conexao.State.SELECTED,Conexao.State.HIGHLIGHTED)){
							this.state=state;							//HIGHLIGHTED -> UNSELECTED
						}
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(Nodulo.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
					}
				break;
				case TO_BE_DELETED:
					if(state.is(Nodulo.State.UNSELECTED)){
						if(getConexao().getState().is(Conexao.State.TO_BE_DELETED))return;
						this.state=state;							//TO_BE_DELETED -> UNSELECTED
					}
				break;
			}
		}
//VAR GLOBAIS
	public float getRoundValue(){
		switch(getConexao().getGrossura().getIndex()){
			case Grossura.THIN:				return Tree.UNIT/4;
			case Grossura.MEDIUM:default:	return Tree.UNIT/2;
			case Grossura.WIDE:				return Tree.UNIT/1;
			case Grossura.ULTRA_WIDE:		return Tree.UNIT/0.5f;
		}
	}
//BOUNDS
	private int xIndex;
		public int getXIndex(){return xIndex;}
		public int getX(){return ((Tree.getLocalX()+xIndex)*Tree.UNIT);}
	private int yIndex;
		public int getYIndex(){return yIndex;}
		public int getY(){return ((Tree.getLocalY()+yIndex)*Tree.UNIT);}
	public Point getLocationIndex(){return new Point(getXIndex(),getYIndex());}
	public Point getLocation(){return new Point(getX(),getY());}
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
	public float getWidth(){
		switch(getConexao().getGrossura().getIndex()){
			case Grossura.THIN:				return Tree.UNIT*0.5f;
			case Grossura.MEDIUM:default:	return Tree.UNIT*1.0f;
			case Grossura.WIDE:				return Tree.UNIT*2.0f;
			case Grossura.ULTRA_WIDE:		return Tree.UNIT*3.0f;
		}
	}
	public Rectangle getFormIndex(){return new Rectangle(getXIndex(),getYIndex(),1,1);}
	public Rectangle getForm(){
		final float width=getWidth();
		return new Rectangle((int)(getX()-(width/2)),(int)(getY()-(width/2)),(int)(width),(int)(width));
	}
@Override
	public boolean contains(Point mouse){return getForm().contains(mouse);}
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
		result=prime*result+((state==null)?0:state.hashCode());
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
		if(state!=nod.state)return false;
		if(conexao!=nod.conexao)return false;
		if(indexOnCox!=nod.indexOnCox)return false;
		if(xIndex!=nod.xIndex)return false;
		if(yIndex!=nod.yIndex)return false;
		return true;
	}
//DRAW
	public void draw(Graphics2D imagemEdit,int unit){
		final float width=getWidth();
		final float round=getRoundValue();
		float borda=getWidth()/2;
		switch(getConexao().getGrossura().getIndex()){
			case Grossura.THIN:				borda=getWidth()/1;break;
			case Grossura.MEDIUM:default:	borda=getWidth()/2;break;
			case Grossura.WIDE:				borda=getWidth()/3;break;
			case Grossura.ULTRA_WIDE:		borda=getWidth()/4;break;
		}
		drawBrilho(imagemEdit,unit,width,round,borda);
		drawPonto(imagemEdit,unit,width,round);
	}
		private void drawBrilho(Graphics2D imagemEdit,int unit,float width,float round,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(getState().is(Nodulo.State.UNSELECTED,Nodulo.State.HIGHLIGHTED,Nodulo.State.TO_BE_CREATOR))return;
			imagemEdit.setColor(getBrilhoCor(getState()));
			final float x=getX()-(width/2)-borda;
			final float y=getY()-(width/2)-borda;
			width+=borda*2;
			imagemEdit.fill(new RoundRectangle2D.Float(x,y,width,width,round,round));
		}
		private void drawPonto(Graphics2D imagemEdit,int unit,float width,float round){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			imagemEdit.setColor(getPontoCor(getState()));
			final float x=getX()-(width/2);
			final float y=getY()-(width/2);
			imagemEdit.fill(new RoundRectangle2D.Float(x,y,width,width,round,round));
		}
	public Cor getBrilhoCor(Nodulo.State state){
		switch(getState()){
			case SELECTED:default:	return getConexao().getBrilhoCor(Conexao.State.SELECTED);
			case TO_BE_DELETED:		return getConexao().getBrilhoCor(Conexao.State.TO_BE_DELETED);	
		}
	}
	public Cor getPontoCor(Nodulo.State state){
		switch(getState()){
			case UNSELECTED:default:	return getConexao().getLinhaCor(Conexao.State.UNSELECTED);
			case SELECTED:				return getConexao().getLinhaCor(Conexao.State.SELECTED);
			case HIGHLIGHTED:			return getConexao().getLinhaCor(Conexao.State.HIGHLIGHTED);
			case TO_BE_CREATOR:			return getConexao().getLinhaCor(Conexao.State.TO_BE_CREATOR);
			case TO_BE_DELETED:			return getConexao().getLinhaCor(Conexao.State.TO_BE_DELETED);
		}
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