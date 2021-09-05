package element.tree.objeto.conexao;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import element.tree.propriedades.Borda;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Grossura;
import element.tree.Tree;
import element.tree.objeto.Objeto;
import element.tree.objeto.ObjetoBoundsListener;
import element.tree.objeto.modulo.Modulo;
public class Conexao extends Objeto{
//STATES
	public enum State{
		UNSELECTED,
		SELECTED,
		HIGHLIGHTED,
		TO_BE_CREATOR,
		TO_BE_DELETED;
		public boolean is(Conexao.State... states){
			for(Conexao.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
	private Conexao.State state=Conexao.State.UNSELECTED;
		public Conexao.State getState(){return state;}
		public void setState(Conexao.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(Conexao.State.SELECTED)){
						this.state=state;							//UNSELECTED -> SELECTED
						for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.HIGHLIGHTED);
						getPai().setState(Modulo.State.HIGHLIGHTED);
						getSon().setState(Modulo.State.HIGHLIGHTED);
					}else if(state.is(Conexao.State.HIGHLIGHTED)){
						this.state=state;							//UNSELECTED -> HIGHLIGHTED
						for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.HIGHLIGHTED);
						getPai().setState(Modulo.State.HIGHLIGHTED);
						getSon().setState(Modulo.State.HIGHLIGHTED);
					}else if(state.is(Conexao.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
						for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.TO_BE_CREATOR);
					}else if(state.is(Conexao.State.TO_BE_DELETED)){
						this.state=state;							//UNSELECTED -> TO_BE_DELETED
						for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.TO_BE_DELETED);
					}
				break;
				case SELECTED:
					if(state.is(Conexao.State.UNSELECTED)){
						if(getPai().getState().is(Modulo.State.SELECTED)||getSon().getState().is(Modulo.State.SELECTED)){
							this.state=Conexao.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
							for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.HIGHLIGHTED);
							getPai().setState(Modulo.State.HIGHLIGHTED);
							getSon().setState(Modulo.State.HIGHLIGHTED);
						}else{
							this.state=state;						//SELECTED -> UNSELECTED
							for(Nodulo nod:getNodulos())if(nod.getState().is(Nodulo.State.HIGHLIGHTED))nod.setState(Nodulo.State.UNSELECTED);
							if(getPai().getState().is(Modulo.State.HIGHLIGHTED))getPai().setState(Modulo.State.UNSELECTED);
							if(getSon().getState().is(Modulo.State.HIGHLIGHTED))getSon().setState(Modulo.State.UNSELECTED);
						}
					}
				break;
				case HIGHLIGHTED:
					if(state.is(Conexao.State.SELECTED)){
						this.state=state;							//HIGHLIGHTED -> SELECTED
						for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.HIGHLIGHTED);
						getPai().setState(Modulo.State.HIGHLIGHTED);
						getSon().setState(Modulo.State.HIGHLIGHTED);
					}else if(state.is(Conexao.State.UNSELECTED)){
						this.state=state;							//HIGHLIGHTED -> UNSELECTED
						if(getPai().getState().is(Modulo.State.SELECTED)||getSon().getState().is(Modulo.State.SELECTED)){
							this.state=Conexao.State.HIGHLIGHTED;	//UNSELECTED -> HIGHLIGHTED
						}
						if(getState().is(Conexao.State.UNSELECTED))for(Nodulo nod:getNodulos())if(nod.getState().is(Nodulo.State.SELECTED)){
							this.state=Conexao.State.HIGHLIGHTED;	//UNSELECTED -> HIGHLIGHTED
							break;
						}
						if(getState().is(Conexao.State.UNSELECTED)){
							for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.UNSELECTED);
							getPai().setState(Modulo.State.UNSELECTED);
							getSon().setState(Modulo.State.UNSELECTED);
						}else{
							for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.HIGHLIGHTED);
							getPai().setState(Modulo.State.HIGHLIGHTED);
							getSon().setState(Modulo.State.HIGHLIGHTED);
						}
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(Conexao.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
						for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.UNSELECTED);
						for(Segmento seg:getSegmentos())seg.setState(Segmento.State.UNSELECTED);
					}
				break;
				case TO_BE_DELETED:
					if(state.is(Conexao.State.UNSELECTED)){
						if(getPai().getState().is(Modulo.State.TO_BE_DELETED)||getSon().getState().is(Modulo.State.TO_BE_DELETED))return;
						this.state=state;							//TO_BE_DELETED -> UNSELECTED
						for(Nodulo nod:getNodulos())nod.setState(Nodulo.State.UNSELECTED);
					}
				break;
			}
		}
//LOCAL
	public int getX1Index(){return son.getMeioXIndex();}
	public int getX1(int unit){return son.getMeioX(unit);}
	public int getY1Index(){return son.getMeioYIndex();}
	public int getY1(int unit){return son.getMeioY(unit);}
	public int getX2Index(){return pai.getMeioXIndex();}
	public int getX2(int unit){return pai.getMeioX(unit);}
	public int getY2Index(){return pai.getMeioYIndex();}
	public int getY2(int unit){return pai.getMeioY(unit);}
//FORMA
	public Path2D.Float getFormIndex(){
		final List<Segmento>segs=getSegmentos();
		final Path2D.Float forma=new Path2D.Float();
		forma.moveTo(getX1Index(),getY1Index());				//SON
		for(Segmento seg:segs){
			forma.lineTo(seg.getX2Index(),seg.getY2Index());	//NODS E PAI
		}
		final Rectangle area=forma.getBounds();
		final int xMin=(area.width==0?1:0);		//IMPEDE ÁREA DE WIDTH 0
		final int yMin=(area.height==0?1:0);	//IMPEDE ÁREA DE HEIGHT 0
		for(int i=segs.size()-1;i>=0;i--){	//FECHAR A LINHA
			final Segmento seg=segs.get(i);
			forma.lineTo(seg.getX1Index()+xMin,seg.getY1Index()+yMin);	//NODS E SON
		}
		return forma;
	}
	public Path2D.Float getForm(int unit){
		final List<Segmento>segs=getSegmentos();
		final Path2D.Float forma=new Path2D.Float();
		forma.moveTo(getX1(unit),getY1(unit));			//SON
		for(Segmento seg:segs){
			forma.lineTo(seg.getX2(unit),seg.getY2(unit));		//NODS E PAI
		}
		for(int i=segs.size()-1;i>=0;i--){		//FECHAR A LINHA
			final Segmento seg=segs.get(i);
			forma.lineTo(seg.getX1(unit),seg.getY1(unit));		//NODS E SON
		}
		return forma;
	}
@Override
	public boolean contains(Point mouse){
		final Path2D forma=getForm(Tree.UNIT);
		float raio=Tree.UNIT;
		switch(getGrossura().getIndex()){
			case Grossura.THIN:				raio*=0.5f;	break;
			case Grossura.MEDIUM:default:	raio*=0.6f;	break;
			case Grossura.WIDE:				raio*=1.0f;	break;
			case Grossura.ULTRA_WIDE:		raio*=2.0f;	break;
		}
		final Rectangle area=new Rectangle((int)(mouse.x-raio),(int)(mouse.y-raio),(int)(raio*2),(int)(raio*2));
		return forma.intersects(area);
	}
	public Segmento segmentContains(Point mouse){
		float raio=Tree.UNIT;
		switch(getGrossura().getIndex()){
			case Grossura.THIN:				raio*=0.5f;	break;
			case Grossura.MEDIUM:default:	raio*=0.6f;	break;
			case Grossura.WIDE:				raio*=1.0f;	break;
			case Grossura.ULTRA_WIDE:		raio*=2.0f;	break;
		}
		final Rectangle area=new Rectangle((int)(mouse.x-raio),(int)(mouse.y-raio),(int)(raio*2),(int)(raio*2));
		for(Segmento seg:getSegmentos()){
			final Line2D forma=new Line2D.Float(seg.getX1(Tree.UNIT),seg.getY1(Tree.UNIT),seg.getX2(Tree.UNIT),seg.getY2(Tree.UNIT));
			if(forma.intersects(area))return seg;
		}
		return null;
	}
//LISTENER: DISPARA COM O MUDAR DE BOUNDS DE UM OBJETO
	public static List<ObjetoBoundsListener>boundsListeners=new ArrayList<ObjetoBoundsListener>();
		public static void addBoundsListener(ObjetoBoundsListener boundsListener){boundsListeners.add(boundsListener);}
//MÓDULOS
	private Modulo pai;
		public Modulo getPai(){return pai;}
		public void setPai(Modulo mod){
			pai=mod;
		}
	private Modulo son;
		public Modulo getSon(){return son;}
		public void setSon(Modulo mod){
			son=mod;
		}
//NÓDULOS
	private List<Nodulo>nodulos=new ArrayList<Nodulo>();
		public List<Nodulo>getNodulos(){return nodulos;}
		public boolean addNodulo(Nodulo nod){
			if(nod.getIndexOnCox()==-1){
				nod.setConexao(this);
				nod.setIndexOnCox(0);
				nodulos.add(nod);
				addSegmentos(nod);
			}else{
				nod.setConexao(this);
				nodulos.add(nod.getIndexOnCox(),nod);
				addSegmentos(nod);
			}
			for(int i=nod.getIndexOnCox(),size=getNodulos().size();i<size;i++){
				final Nodulo nodNext=getNodulos().get(i);
				nodNext.setIndexOnCox(i);	//ATUALIZA OS INDEXES DOS NODS APÓS ESTE
			}
			return true;
		}
		public boolean delNodulo(Nodulo nod){
			delSegmentos(nod);
			final boolean result=nodulos.remove(nod);
			for(int i=nod.getIndexOnCox(),size=getNodulos().size();i<size;i++){
				final Nodulo nodNext=getNodulos().get(i);
				nodNext.setIndexOnCox(i);	//ATUALIZA OS INDEXES DOS NODS APÓS ESTE
			}
			return result;
		}
//SEGMENTOS
	private List<Segmento>segmentos=new ArrayList<>();
		public List<Segmento>getSegmentos(){return segmentos;}
		private boolean addSegmentos(Nodulo nod){
			final int index=getNodulos().indexOf(nod);
			if(index==-1)return false;
			final Objeto obj1=(index==0?getSon():getNodulos().get(index-1));						//NOD ANTERIOR OU SON
			final Objeto obj2=(index==getNodulos().size()-1?getPai():getNodulos().get(index+1));	//NOD POSTERIOR OU PAI
			final Segmento seg=getSegmentos().get(index);
			if(seg.getPonta1()==obj1&&seg.getPonta2()==obj2){
				getSegmentos().remove(index);
				final Segmento seg1=new Segmento(this,obj1,nod);
				seg1.setIndex(index);
				final Segmento seg2=new Segmento(this,nod,obj2);
				seg2.setIndex(index+1);
				getSegmentos().add(index,seg2);		//AO COLOCAR COM INDEX, SE COLOCA DO 2 AO 1
				getSegmentos().add(index,seg1);
				return true;
			}
			return false;
		}
		private boolean delSegmentos(Nodulo nod){
			final int index=getNodulos().indexOf(nod);
			if(index==-1)return false;
			final Objeto obj1=(index==0?getSon():getNodulos().get(index-1));						//NOD ANTERIOR OU SON
			final Objeto obj2=(index+1>=getNodulos().size()?getPai():getNodulos().get(index+1));	//NOD POSTERIOR OU PAI
			final Segmento seg1=getSegmentos().get(index);
			final Segmento seg2=getSegmentos().get(index+1);
			if(seg1.getPonta1()==obj1&&seg1.getPonta2()==nod&&seg2.getPonta1()==nod&&seg2.getPonta2()==obj2){
				getSegmentos().remove(index);
				getSegmentos().remove(index);	//ELIMINA O QUE ESTAVA DO INDEX+1
				final Segmento seg=new Segmento(this,obj1,obj2);
				seg.setIndex(index);
				getSegmentos().add(index,seg);
				return true;
			}
			return false;
		}
//BORDA
	private Borda borda=new Borda(Borda.SOLID);
		public Borda getBorda(){return borda;}
		public boolean setBorda(Borda borda){
			if(borda!=this.borda){
				this.borda=borda;
				return true;
			}
			return false;
		}
//GROSSURA
	private Grossura grossura=new Grossura(Grossura.MEDIUM);
		public Grossura getGrossura(){return grossura;}
		public boolean setGrossura(Grossura grossura){
			if(grossura!=this.grossura){
				this.grossura=grossura;
				return true;
			}
			return false;
		}
//TEXTO
	private List<String>texto=new ArrayList<String>();
		public List<String>getTexto(){return texto;}
		public void setTexto(List<String>texto){this.texto=texto;}
		public void setTexto(String texto){setTexto(Arrays.asList(texto.split("\n",-1)));}
		public String getText(){return String.join("\n",texto);}
//POSIÇÃO DO CURSOR
	private int caret=0;
		public void setCaret(int caret){this.caret=caret;}
		public int getCaret(){return caret;}
//MAIN
	public Conexao(Modulo pai,Modulo son){
		super(Objeto.Tipo.CONEXAO);
		segmentos.add(new Segmento(this,son,pai));
		setPai(pai);
		setSon(son);
	}
//FUNCS
@Override
	public int hashCode(){	//ALGUMAS PARTES SÃO DESNECESSÁRIAS PARA DISTINÇÃO
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+getIndex();
//		result=prime*result+((state==null)?0:state.hashCode());
		result=prime*result+((pai==null)?0:pai.hashCode());
		result=prime*result+((son==null)?0:son.hashCode());
//		result=prime*result+((nodulos==null)?0:nodulos.hashCode());
//		result=prime*result+((segmentos==null)?0:segmentos.hashCode());
//		result=prime*result+((borda==null)?0:borda.hashCode());
////		result=prime*result+Arrays.hashCode(texto);
//		result=prime*result+caret;
		return result;
	}
@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj==null)return false;
		if(!super.equals(obj))return false;
		if(getClass()!=obj.getClass())return false;
		final Conexao cox=(Conexao)obj;
//		if(state!=cox.state)return false;
		if(pai!=cox.pai)return false;
		if(son!=cox.son)return false;
//		if(nodulos!=cox.nodulos)return false;
//		if(segmentos!=cox.segmentos)return false;
//		if(borda!=cox.borda)return false;
//		if(texto!=cox.texto)return false;
//		if(caret!=cox.caret)return false;
		return true;
	}
//DRAW
	public void draw(Graphics2D imagemEdit,int unit){
		final float borda=Tree.getBordaValue(unit);
		drawBrilho(imagemEdit,unit,borda);
		drawLinha(imagemEdit,unit,borda);
		drawPonta(imagemEdit,unit);
	}
		private void drawBrilho(Graphics2D imagemEdit,int unit,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(getState().is(Conexao.State.UNSELECTED,Conexao.State.HIGHLIGHTED))return;
			switch(getGrossura().getIndex()){
				case Grossura.THIN:
					imagemEdit.setStroke(new BasicStroke(getGrossura().getValor(borda)*4*4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
				case Grossura.MEDIUM:default:
					imagemEdit.setStroke(new BasicStroke(getGrossura().getValor(borda)*3*3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
				case Grossura.WIDE:
					imagemEdit.setStroke(new BasicStroke(getGrossura().getValor(borda)*2*2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
				case Grossura.ULTRA_WIDE:
					imagemEdit.setStroke(new BasicStroke(getGrossura().getValor(borda)*1.5f*1.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
			}
			imagemEdit.setColor(getBrilhoCor(getState()));
			if(getState().is(Conexao.State.TO_BE_CREATOR)){		//BRILHO DO SEG TO_BE_CREATOR
				for(Segmento seg:getSegmentos()){
					if(seg.getState().is(Segmento.State.UNSELECTED))continue;
					final Path2D.Float forma=new Path2D.Float();
					forma.moveTo(seg.getX1(unit),seg.getY1(unit));
					forma.lineTo(seg.getX2(unit),seg.getY2(unit));
					imagemEdit.draw(forma);
				}
				return;
			}
			final Path2D.Float forma=new Path2D.Float();
			forma.moveTo(getX1(unit),getY1(unit));
			for(Segmento seg:getSegmentos()){
				if(seg.getPonta2()==Tree.getGhost()){
					forma.lineTo(Tree.getGhost().getX(unit),Tree.getGhost().getY(unit));
				}else forma.lineTo(seg.getX2(unit),seg.getY2(unit));
			}
			imagemEdit.draw(forma);								//BRILHO DO SELECTED OU TO_BE_DELETED
		}
		private void drawLinha(Graphics2D imagemEdit,int unit,float borda){
			if(unit<=2&&getGrossura().getIndex()==Grossura.THIN)return;		//EM ZOOM<=2 COM GROSSURA FINA, É IRRELEVANTE
			if(unit<=1&&getGrossura().getIndex()==Grossura.MEDIUM)return;	//EM ZOOM<=1 COM GROSSURA MEDIUM, É IRRELEVANTE
			imagemEdit.setStroke(getBorda().getVisual(getGrossura().getValor(borda)));
			imagemEdit.setColor(getLinhaCor(getState()));
			final Path2D.Float forma=new Path2D.Float();
			forma.moveTo(getX1(unit),getY1(unit));
			for(Segmento seg:getSegmentos()){
				if(seg.getPonta2()==Tree.getGhost()){
					forma.lineTo(Tree.getGhost().getX(unit),Tree.getGhost().getY(unit));
				}else forma.lineTo(seg.getX2(unit),seg.getY2(unit));
			}
			imagemEdit.draw(forma);
		}
		private void drawPonta(Graphics2D imagemEdit,int unit){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			float x1=getX1(unit);
			float y1=getY1(unit);
			float x2=getX2(unit);
			float y2=getY2(unit);
			if(getPai()==Tree.getGhost()){
				x2=Tree.getGhost().getX(unit);
				y2=Tree.getGhost().getY(unit);
			}
			if(!getNodulos().isEmpty()){
				final Nodulo nod=getNodulos().get(getNodulos().size()-1);
				x1=nod.getX(unit);
				y1=nod.getY(unit);
			}
			final Point2D.Float ponta=getPonta(unit,x1,y1,x2,y2);
			float size=unit;
			switch(getGrossura().getIndex()){
				case Grossura.THIN:				size*=0.5f;	break;
				case Grossura.MEDIUM:default:	size*=1.0f;	break;
				case Grossura.WIDE:				size*=2.0f;	break;
				case Grossura.ULTRA_WIDE:		size*=3.0f;	break;
			}
			final float x=(float)(ponta.getX())-(size/2);
			final float y=(float)(ponta.getY())-(size/2);
			imagemEdit.fill(new Ellipse2D.Float(x,y,size,size));
		}
			private Point2D.Float getPonta(int unit,float x1,float y1,float x2,float y2){
				float x=0;
				float y=0;
				if(getPai()==Tree.getGhost()){
					x=x2;
					y=y2;
				}else{
					if(y1>y2){
						if(x1>x2){	//DIR-INF
							x=getPai().getX(unit)+getPai().getWidth(unit);
							y=y2+(((y1-y2)*(getPai().getX(unit)+getPai().getWidth(unit)-x2))/Math.max(1,x1-x2));
							if(y>getPai().getY(unit)+getPai().getHeight(unit)){	//INF-DIR
								x=x2+(((x1-x2)*(getPai().getY(unit)+getPai().getHeight(unit)-y2))/Math.max(1,y1-y2));
								y=getPai().getY(unit)+getPai().getHeight(unit);
							}
						}else{		//INF-ESQ
							x=x2-(((x2-x1)*(getPai().getY(unit)+getPai().getHeight(unit)-y2))/Math.max(1,y1-y2));
							y=getPai().getY(unit)+getPai().getHeight(unit);
							if(x<getPai().getX(unit)){	//ESQ-INF
								x=getPai().getX(unit);
								y=y2+(((y1-y2)*(x2-getPai().getX(unit)))/Math.max(1,x2-x1));
							}
						}
					}else{
						if(x1<x2){	//ESQ-SUP
							x=getPai().getX(unit);
							y=y2-(((y2-y1)*(x2-getPai().getX(unit)))/Math.max(1,x2-x1));
							if(y<getPai().getY(unit)){	//SUP-ESQ
								x=x2-(((x2-x1)*(y2-getPai().getY(unit)))/Math.max(1,y2-y1));
								y=getPai().getY(unit);
							}
						}else{
							if(y2==y1)x=getPai().getX(unit)+getPai().getWidth(unit)+1;else{	//SUP-DIR
								x=x2+(((x1-x2)*(y2-getPai().getY(unit)))/Math.max(1,y2-y1));
								y=getPai().getY(unit);
							}
							if(x>getPai().getX(unit)+getPai().getWidth(unit)){	//DIR-SUP
								x=getPai().getX(unit)+getPai().getWidth(unit);
								y=y2-(((y2-y1)*(getPai().getX(unit)+getPai().getWidth(unit)-x2))/Math.max(1,x1-x2));
							}
						}
					}
				}
				return new Point2D.Float(x,y);
			}
	public Cor getBrilhoCor(Conexao.State state){
		switch(state){
			case SELECTED:default:	return getSon().getBrilhoCor(Modulo.State.SELECTED);
			case TO_BE_CREATOR:		return getSon().getBrilhoCor(Modulo.State.TO_BE_CREATOR);
			case TO_BE_DELETED:		return getSon().getBrilhoCor(Modulo.State.TO_BE_DELETED);
		}
	}
	public Cor getLinhaCor(Conexao.State state){
		switch(state){
			case UNSELECTED:default:	return getSon().getBordaCor(Modulo.State.UNSELECTED);
			case SELECTED:				return getSon().getBordaCor(Modulo.State.SELECTED);
			case HIGHLIGHTED:
				if(getSon().getState().is(Modulo.State.SELECTED)){
					return getSon().getBordaCor(Modulo.State.SELECTED);
				}else return getSon().getBordaCor(Modulo.State.HIGHLIGHTED);
			case TO_BE_CREATOR:			return getSon().getBordaCor(Modulo.State.TO_BE_CREATOR);
			case TO_BE_DELETED:			return getSon().getBordaCor(Modulo.State.TO_BE_DELETED);
		}
	}
//TAG -> COX
	public static Conexao getConexao(Element coxTag,HashMap<Integer,Modulo>ids){
		final Modulo modPai=ids.get(Integer.parseInt(coxTag.getAttribute("pai")));			//PAI
		final Modulo modSon=ids.get(Integer.parseInt(coxTag.getAttribute("son")));			//SON
		return new Conexao(modPai,modSon){{			//COX
			final String borderIndex=coxTag.getAttribute("border");
			if(!borderIndex.equals(""))setBorda(new Borda(Integer.parseInt(borderIndex)));		//BORDER
			final String widthIndex=coxTag.getAttribute("width");
			if(!widthIndex.equals(""))setGrossura(new Grossura(Integer.parseInt(widthIndex)));	//WIDTH
//			final NodeList nods=coxTag.getElementsByTagName("nod");		//NODS(SÃO INSERIDAS APROPRIADAMENTE PELO TREE)
//			for(int n=0,size=nods.getLength();n<size;n++){
//				final Element nodTag=(Element)nods.item(n);
//				addNodulo(Nodulo.getNodulo(nodTag,this));
//			}
			final Element textTag=(Element)coxTag.getElementsByTagName("text").item(0);			//TEXT
			setTexto(textTag.getTextContent());
		}};
	}
//COX -> TAG
	public void addToXML(Document xml,Element mindTag,HashMap<Modulo,Integer>ids){
		final Integer pai=ids.get(getPai()),son=ids.get(getSon());
		if(pai==null||son==null)return;
		final Element coxTag=xml.createElement("cox");						//COX
		coxTag.setAttribute("pai",String.valueOf(pai));							//PAI
		coxTag.setAttribute("son",String.valueOf(son));							//SON
		coxTag.setAttribute("border",String.valueOf(getBorda().getIndex()));	//BORDER
		coxTag.setAttribute("width",String.valueOf(getGrossura().getIndex()));	//WIDTH
		final Element textTag=xml.createElement("text");						//TEXT
		textTag.setTextContent(getText());
		coxTag.appendChild(textTag);
		for(Nodulo nod:getNodulos())nod.addToXML(xml,coxTag);					//NODS
		mindTag.appendChild(coxTag);
	}
}