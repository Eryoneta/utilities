package element.tree.objeto.conexao;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import element.tree.propriedades.Borda;
import element.tree.propriedades.Grossura;
import element.tree.Tree;
import element.tree.objeto.Objeto;
import element.tree.objeto.ObjetoBoundsListener;
import element.tree.objeto.nodulo.Nodulo;
import element.tree.objeto.conexao.segmento.Segmento;
import element.tree.objeto.modulo.Modulo;
public class Conexao extends Objeto{
//ST
	private final ConexaoST ST=new ConexaoST(this);
		public ConexaoST getST(){return ST;}
		public ConexaoST.State getState(){return getST().getState();}
		public void setState(ConexaoST.State state){getST().setState(state);}
//UI
	private final ConexaoUI UI=new ConexaoUI(this);
		public ConexaoUI getUI(){return UI;}
		public void draw(Graphics2D imagemEdit,int unit){
			getUI().draw(imagemEdit,unit);
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
		//TODO: ADICIONAR INDEX
//		result=prime*result+getIndex();
//		result=prime*result+((ST==null)?0:ST.hashCode());
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
//		if(ST!=cox.ST)return false;
		if(pai!=cox.pai)return false;
		if(son!=cox.son)return false;
//		if(nodulos!=cox.nodulos)return false;
//		if(segmentos!=cox.segmentos)return false;
//		if(borda!=cox.borda)return false;
//		if(texto!=cox.texto)return false;
//		if(caret!=cox.caret)return false;
		return true;
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