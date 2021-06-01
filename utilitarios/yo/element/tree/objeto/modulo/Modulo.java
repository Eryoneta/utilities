package element.tree.objeto.modulo;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import element.tree.objeto.Objeto;
import element.tree.objeto.ObjetoBoundsListener;
import element.tree.objeto.conexao.Conexao;
import element.tree.popup.icon.IconePick;
import element.tree.propriedades.Borda;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Icone;
import element.tree.Tree;
public class Modulo extends Objeto{
//STATES
	public enum State{
		UNSELECTED,
		SELECTED,
		HIGHLIGHTED,
		TO_BE_CREATOR,
		TO_BE_SON,
		TO_BE_PAI,
		TO_BE_DELETED;
		public boolean is(Modulo.State... states){
			for(Modulo.State state:states)if(this.equals(state))return true;
			return false;
		}
	}
	private State state=Modulo.State.UNSELECTED;
		public Modulo.State getState(){return state;}
		public void setState(Modulo.State state){
			switch(getState()){
				case UNSELECTED:
					if(state.is(Modulo.State.SELECTED)){
						this.state=state;							//UNSELECTED -> SELECTED
						for(Conexao cox:getConexoes())cox.setState(Conexao.State.HIGHLIGHTED);
					}else if(state.is(Modulo.State.HIGHLIGHTED)){
						this.state=state;							//UNSELECTED -> HIGHLIGHTED
					}else if(state.is(Modulo.State.TO_BE_CREATOR)){
						this.state=state;							//UNSELECTED -> TO_BE_CREATOR
					}else if(state.is(Modulo.State.TO_BE_SON)){
						if(this==Tree.getMestre())return;	//MESTRE NÃO PODE SER SON
						this.state=state;							//UNSELECTED -> TO_BE_SON
					}else if(state.is(Modulo.State.TO_BE_PAI)){
						this.state=state;							//UNSELECTED -> TO_BE_PAI
					}else if(state.is(Modulo.State.TO_BE_DELETED)){
						if(this==Tree.getMestre())return;	//MESTRE NÃO PODE SER DEL
						this.state=state;							//UNSELECTED -> TO_BE_DELETED
						for(Conexao cox:getConexoes())cox.setState(Conexao.State.TO_BE_DELETED);
					}
				break;
				case SELECTED:
					if(state.is(Modulo.State.UNSELECTED)){
						for(Conexao cox:getConexoes())if(cox.getState().is(Conexao.State.SELECTED)){
							this.state=Modulo.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
							break;
						}
						if(getState().is(Modulo.State.SELECTED)){
							this.state=state;						//SELECTED -> UNSELECTED
						}
						for(Conexao cox:getConexoes())if(cox.getState().is(Conexao.State.HIGHLIGHTED))cox.setState(Conexao.State.UNSELECTED);
						if(!getState().is(Modulo.State.UNSELECTED))for(Conexao cox:getConexoes())if(cox.getState().is(Conexao.State.HIGHLIGHTED)){
							this.state=Modulo.State.HIGHLIGHTED;	//SELECTED -> HIGHLIGHTED
							break;
						}
					}
				break;
				case HIGHLIGHTED:
					if(state.is(Modulo.State.SELECTED)){
						this.state=state;							//HIGHLIGHTED -> SELECTED
						for(Conexao cox:getConexoes())cox.setState(Conexao.State.HIGHLIGHTED);
					}else if(state.is(Modulo.State.UNSELECTED)){
						this.state=state;							//HIGHLIGHTED -> UNSELECTED
						for(Conexao cox:getConexoes())if(cox.getState().is(Conexao.State.SELECTED,Conexao.State.HIGHLIGHTED)){
							this.state=Modulo.State.HIGHLIGHTED;	//UNSELECTED -> HIGHLIGHTED
							break;
						}
					}
				break;
				case TO_BE_CREATOR:
					if(state.is(Modulo.State.UNSELECTED)){
						this.state=state;							//TO_BE_CREATOR -> UNSELECTED
					}
				break;
				case TO_BE_SON:
					if(state.is(Modulo.State.UNSELECTED)){
						this.state=state;							//TO_BE_SON -> UNSELECTED
					}
				break;
				case TO_BE_PAI:
					if(state.is(Modulo.State.UNSELECTED)){
						this.state=state;							//TO_BE_PAI -> UNSELECTED
					}
				break;
				case TO_BE_DELETED:
					if(state.is(Modulo.State.UNSELECTED)){
						this.state=state;							//TO_BE_DELETED -> UNSELECTED
						for(Conexao cox:getConexoes())cox.setState(Conexao.State.UNSELECTED);
					}
				break;
			}
		}
//VAR GLOBAIS
	public static int getRoundValue(){return Tree.UNIT;}
	public static class Cores{
		public static Cor FUNDO=new Cor(215,215,215);
		public static Cor SELECT=new Cor(120,120,120);
		public static Cor CREATE=new Cor(65,231,82);
		public static Cor PAI=new Cor(117,132,236);
		public static Cor SON=new Cor(236,220,117);
		public static Cor DELETE=new Cor(170,45,45);
	}
//LOCAL
	private int xIndex;
		public int getXIndex(){return xIndex;}
		public int getX(){return (Tree.getLocalX()+xIndex)*Tree.UNIT;}
		public int getMeioXIndex(){return (getXIndex()+(getWidthIndex()/2))-(getWidthIndex()%2==0?0:1);}	//(X+(WIDTH/2))-(AJUSTE PARA WIDTHS ÍMPARES)
		public int getMeioX(){return (getX()+(getWidth()/2))-(getWidthIndex()%2==0?0:Tree.UNIT/2);}			//(X+(WIDTH/2))-(AJUSTE PARA WIDTHS ÍMPARES)
		public void setXIndex(int x){xIndex=x;}
	private int yIndex;
		public int getYIndex(){return yIndex;}
		public int getY(){return (Tree.getLocalY()+yIndex)*Tree.UNIT;}
		public int getMeioYIndex(){return (getYIndex()+(getHeightIndex()/2))-(getHeightIndex()%2==0?0:1);}	//(Y+(HEIGHT/2))-(AJUSTE PARA HEIGHTS ÍMPARES)
		public int getMeioY(){return (getY()+(getHeight()/2))-(getHeightIndex()%2==0?0:Tree.UNIT/2);}		//(Y+(HEIGHT/2))-(AJUSTE PARA HEIGHTS ÍMPARES)
		public void setYIndex(int y){yIndex=y;}
	public Point getLocationIndex(){return new Point(getXIndex(),getYIndex());}
	public Point getLocation(){return new Point(getX(),getY());}
	public void justSetLocationIndex(int x,int y){
		this.xIndex=x;
		this.yIndex=y;
	}
	public void setLocationIndex(int x,int y){
		final Rectangle modOldBounds=getFormIndex();
		final Rectangle[]coxsOldBounds=new Rectangle[getConexoes().size()];
		for(int i=0,size=getConexoes().size();i<size;i++){
			coxsOldBounds[i]=getConexoes().get(i).getFormIndex().getBounds();
		}
		justSetLocationIndex(x,y);
		triggerBoundsListener(modOldBounds,coxsOldBounds);
	}
//FORMA
	private int widthIndex;
		public int getWidthIndex(){return widthIndex;}
		public int getWidth(){return widthIndex*Tree.UNIT;}
	private int heightIndex;
		public int getHeightIndex(){return heightIndex;}
		public int getHeight(){return heightIndex*Tree.UNIT;}
	public Dimension getSizeIndex(){return new Dimension(getWidthIndex(),getHeightIndex());}
	public Rectangle getFormIndex(){return new Rectangle(getXIndex(),getYIndex(),getWidthIndex(),getHeightIndex());}
	public Rectangle getForm(){return new Rectangle(getX(),getY(),getWidth(),getHeight());}
	public void justSetSize(){
		final int unit=8;	//O TAMANHO É FIXO, INDEPENDENTE DO ZOOM ATUAL
		final Graphics imagemEdit=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).getGraphics();
	//WIDTH
		widthIndex=0;
		if(Tree.Fonte.FONTE!=null)imagemEdit.setFont(Modulo.getFont(unit));
		int width=0;
		for(String linha:titulo){
			width=Math.max(width,imagemEdit.getFontMetrics().stringWidth(linha));
		}
		widthIndex=(int)Math.ceil((float)width/unit);
	//HEIGHT
		final int tituloQtd=(titulo.length==1&&titulo[0].isEmpty()?0:titulo.length);	//SE TITULO VAZIO
		int height=(imagemEdit.getFontMetrics().getHeight()*tituloQtd);
		heightIndex=(int)Math.ceil((float)height/unit);
	//ADD ICONS
		if(isIconified()){
			final int newWidthIndex=(int)Math.ceil(((float)Icone.getRelativeSize(unit)*getIcones().size())/unit);
			widthIndex=Math.max(widthIndex,newWidthIndex);
			heightIndex+=2;	//TAMANHO EM UNIT DOS ICONES
		}
	//SET
		final int minSize=2;
		final int extraWidth=(tituloQtd==0?0:1);	//ADICIONA 1 PARA TER UM ESPAÇO A MAIS, SE TITULO PRESENTE
		widthIndex=Math.max(minSize,widthIndex+extraWidth);
		heightIndex=Math.max(minSize,heightIndex);
	}
	public void setSize(){
		final Rectangle modOldBounds=getFormIndex();
		final Rectangle[]coxsOldBounds=new Rectangle[getConexoes().size()];
		for(int i=0,size=getConexoes().size();i<size;i++){
			coxsOldBounds[i]=getConexoes().get(i).getFormIndex().getBounds();
		}
		justSetSize();
		triggerBoundsListener(modOldBounds,coxsOldBounds);
	}
@Override
	public boolean contains(Point mouse){return getForm().contains(mouse);}
//LISTENER: DISPARA COM O MUDAR DE BOUNDS DE UM OBJETO
	private static List<ObjetoBoundsListener>boundsListeners=new ArrayList<ObjetoBoundsListener>();
		public static void addBoundsListener(ObjetoBoundsListener boundsListener){boundsListeners.add(boundsListener);}
		private void triggerBoundsListener(Rectangle modOldBounds,Rectangle[]coxsOldBounds){
			for(ObjetoBoundsListener boundsListener:boundsListeners){
				boundsListener.boundsChanged(this,modOldBounds,getFormIndex());
			}
			for(ObjetoBoundsListener boundsListener:Conexao.boundsListeners){
				for(int i=0,size=getConexoes().size();i<size;i++){
					final Conexao cox=getConexoes().get(i);
					boundsListener.boundsChanged(cox,coxsOldBounds[i],cox.getFormIndex().getBounds());
				}
			}
		}
//COR
	private Cor cor=Modulo.Cores.FUNDO;
		public Cor getCor(){return cor;}
		public void setCor(Cor cor){this.cor=cor;}
//BORDA
	private Borda borda=new Borda(Borda.SOLID);
		public Borda getBorda(){return borda;}
		public boolean setBorda(Borda borda){
			if(this==Tree.getMestre())return false;
			if(borda!=this.borda){
				this.borda=borda;
				return true;
			}
			return false;
		}
//ICONES
	private List<Icone>icones=new ArrayList<Icone>();
		public List<Icone>getIcones(){return icones;}
		public boolean justAddIcone(Icone icone){	//EVITA BOUNDS_LISTENER
			boolean added=icones.add(icone);
			justSetSize();
			return added;
		}
		public boolean addIcone(Icone icone){
			boolean added=icones.add(icone);
			setSize();
			return added;
		}
		public Icone delIcone(Icone icone){
			for(int i=icones.size()-1;i>=0;i--){
				if(icone.getRelativeLink().equals(icones.get(i).getRelativeLink())){
					icones.remove(i);
					setSize();
					return icone;
				}
			}
			return icone;
		}
		public boolean isIconified(){return !icones.isEmpty();}
//TITULO
	private String[]titulo;
		public String getTitle(){return String.join("\n",titulo);}
		public void justSetTitle(String titulo){	//EVITA BOUNDS_LISTENER
			this.titulo=titulo.split("\n",-1);
			justSetSize();
		}
		public void setTitle(String titulo){
			this.titulo=titulo.split("\n",-1);
			setSize();
		}
//TEXTO
	private List<String>texto=new ArrayList<String>();
		public List<String>getTexto(){return texto;}
		public String getText(){return String.join("\n",texto);}
		public void setTexto(List<String>texto){this.texto=texto;}
		public void setTexto(String texto){setTexto(Arrays.asList(texto.split("\n",-1)));}
//POSIÇÃO DO CURSOR
	private int caret=0;
	public int getCaret(){return caret;}
		public void setCaret(int caret){this.caret=caret;}
//CONEXÕES
	private List<Conexao>conexoes=new ArrayList<Conexao>();
		public List<Conexao>getConexoes(){return conexoes;}
		public boolean addConexao(Conexao cox){
			conexoes.add(cox);
			return true;
		}
		public boolean delConexao(Conexao cox){
			return conexoes.remove(cox);
		}
//MAIN
	public Modulo(){
		super(Objeto.Tipo.MODULO);
		justSetLocationIndex(0,0);
		justSetTitle("");
	}
	public Modulo(int x,int y,String titulo){
		super(Objeto.Tipo.MODULO);
		justSetLocationIndex(x,y);
		justSetTitle(titulo);
	}
	public static Font getFont(int unit){
		return new Font(Tree.Fonte.FONTE.getName(),Tree.Fonte.FONTE.getStyle(),(int)(unit*0.1f*Tree.Fonte.FONTE.getSize()));
	}
	public Font getRelativeFont(int unit){
		final Graphics imagemEdit=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).getGraphics();
		imagemEdit.setFont(Modulo.getFont(unit));
		int width=0;
		for(String linha:titulo){
			width=Math.max(width,imagemEdit.getFontMetrics().stringWidth(linha));
		}
		int height=imagemEdit.getFontMetrics().getHeight()*titulo.length;
		while(width>getWidth()-unit||height>getHeight()){	//DIMINÚI A FONTE ATÉ O TÍTULO FICAR MENOR QUE O MOD(COM ESPAÇO EXTRA DE 1 UNIT)
			final Font oldFonte=imagemEdit.getFont();
			imagemEdit.setFont(new Font(oldFonte.getName(),oldFonte.getStyle(),oldFonte.getSize()-1));
			width=0;
			for(String linha:titulo){
				width=Math.max(width,imagemEdit.getFontMetrics().stringWidth(linha));
			}
			height=imagemEdit.getFontMetrics().getHeight()*titulo.length;
		}
		return imagemEdit.getFont();
	}
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+((state==null)?0:state.hashCode());
//		result=prime*result+Arrays.hashCode(conexoes);
		result=prime*result+((cor==null)?0:cor.hashCode());
		result=prime*result+((borda==null)?0:borda.hashCode());
//		result=prime*result+Arrays.hashCode(icones);
		result=prime*result+Arrays.hashCode(titulo);
//		result=prime*result+Arrays.hashCode(texto);
		result=prime*result+caret;
		result=prime*result+xIndex;
		result=prime*result+yIndex;
		result=prime*result+widthIndex;
		result=prime*result+heightIndex;
		return result;
	}
@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj==null)return false;
		if(!super.equals(obj))return false;
		if(getClass()!=obj.getClass())return false;
		final Modulo mod=(Modulo)obj;
		if(state!=mod.state)return false;
		if(conexoes!=mod.conexoes)return false;
		if(cor!=mod.cor)return false;
		if(borda!=mod.borda)return false;
		if(icones!=mod.icones)return false;
		if(titulo!=mod.titulo)return false;
		if(texto!=mod.texto)return false;
		if(caret!=mod.caret)return false;
		if(xIndex!=mod.xIndex)return false;
		if(yIndex!=mod.yIndex)return false;
		if(widthIndex!=mod.widthIndex)return false;
		if(heightIndex!=mod.heightIndex)return false;
		return true;
	}
//DRAW
	public void draw(Graphics2D imagemEdit,int unit){
		if(this==Tree.getGhost())return;
		final int round=Modulo.getRoundValue();
		final float borda=Tree.getBordaValue();
		drawBrilho(imagemEdit,unit,round,borda);
		drawFundo(imagemEdit,round);
		drawBorda(imagemEdit,unit,round,borda);
		if(!getTitle().isEmpty())drawTitulo(imagemEdit,unit);
		drawIcone(imagemEdit,unit);
	}
		private void drawBrilho(Graphics2D imagemEdit,int unit,int round,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(getState().equals(Modulo.State.UNSELECTED)||getState().equals(Modulo.State.HIGHLIGHTED))return;
			imagemEdit.setColor(getBrilhoCor(getState()));
			final float x=getX()-borda*2.6f;
			final float y=getY()-borda*2.6f;
			final float width=getWidth()+((borda*2.6f)*2);
			final float height=getHeight()+((borda*2.6f)*2);
			round*=1.6;
			if(this==Tree.getMestre()){
				imagemEdit.fill(new Rectangle2D.Float(x,y,width,height));
			}else imagemEdit.fill(new RoundRectangle2D.Float(x,y,width,height,round,round));
		}
		private void drawFundo(Graphics2D imagemEdit,int round){
			imagemEdit.setColor(getFundoCor(getState()));
			if(this==Tree.getMestre()){
				imagemEdit.fill(new Rectangle2D.Float(getX(),getY(),getWidth(),getHeight()));
			}else imagemEdit.fill(new RoundRectangle2D.Float(getX(),getY(),getWidth(),getHeight(),round,round));
		}
		private void drawBorda(Graphics2D imagemEdit,int unit,int round,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			imagemEdit.setStroke(getBorda().getVisual());
			imagemEdit.setColor(getBordaCor(getState()));
			if(this==Tree.getMestre()){
				imagemEdit.setStroke(new BasicStroke(Tree.getBordaValue(),BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));	//BORDAS RETAS
				imagemEdit.draw(new Rectangle2D.Float(getX(),getY(),getWidth(),getHeight()));
			}else imagemEdit.draw(new RoundRectangle2D.Float(getX(),getY(),getWidth(),getHeight(),round,round));
		}
		private void drawTitulo(Graphics2D imagemEdit,int unit){
			switch(getState()){
				case UNSELECTED:default:
				case SELECTED:
				case HIGHLIGHTED:			imagemEdit.setColor(getCor().isDark()?Tree.Fonte.LIGHT:Tree.Fonte.DARK);break;
				case TO_BE_CREATOR:		
				case TO_BE_PAI:
				case TO_BE_SON:
				case TO_BE_DELETED:			imagemEdit.setColor(Tree.Fonte.DARK);break;
			}
			if(unit<=3){		//EM ZOOM<=3, É ILEGÍVEL, TROCA POR LINHA
				imagemEdit.setStroke(new BasicStroke(1));
				final int borda=1*unit;
				final int linhasQtd=(unit<=2?Math.max(1,titulo.length/2):titulo.length);	//LINHAS MUITO JUNTAS FORMAM BLOCOS
				final float linhaHeight=(float)(getHeight())/linhasQtd;
				final int width=Math.max(1,getWidth()-borda-borda);	//DEVE APARECER PELO MENOS 1 PÍXEL
				final int x=getX()+borda;
				float y=getY()+linhaHeight/2;
				for(int i=0;i<linhasQtd;i++){
					imagemEdit.drawLine(x,(int)Math.round(y),x+width,(int)Math.round(y));
					y+=linhaHeight;
				}
			}else{
				imagemEdit.setFont(getRelativeFont(unit));
				final int height=imagemEdit.getFontMetrics().getHeight();
				int y=getY()+(isIconified()?unit*2:0)+height;
				for(String linha:titulo){
					imagemEdit.drawString(linha,getX()+(getWidth()-imagemEdit.getFontMetrics().stringWidth(linha))/2,y);
					y+=height;
				}
			}
		}
		private void drawIcone(Graphics2D imagemEdit,int unit){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(!isIconified())return;
			final int iconsWidth=Icone.getSize()*getIcones().size();
			for(int i=0,size=getIcones().size(),x=getX()+((getWidth()-iconsWidth)/2),y=getY();i<size;i++,x+=Icone.getSize())getIcones().get(i).draw(imagemEdit,x,y);
		}
	public Cor getBrilhoCor(Modulo.State state){
		final float transparency=0.4f;
		switch(state){
			case SELECTED:default:		return Cor.getTransparent(Cor.getChanged(Modulo.Cores.SELECT,0.8f),transparency);
			case TO_BE_CREATOR:			return Cor.getTransparent(Cor.getChanged(Modulo.Cores.CREATE,0.8f),transparency);
			case TO_BE_PAI:				return Cor.getTransparent(Cor.getChanged(Modulo.Cores.PAI,0.8f),transparency);
			case TO_BE_SON:				return Cor.getTransparent(Cor.getChanged(Modulo.Cores.SON,0.8f),transparency);
			case TO_BE_DELETED:			return Cor.getTransparent(Cor.getChanged(Modulo.Cores.DELETE,0.8f),transparency);
		}
	}
	public Cor getFundoCor(Modulo.State state){
		switch(state){
			case UNSELECTED:default:	return getCor();
			case SELECTED:
			case HIGHLIGHTED:
				Cor corHigh=Cor.getChanged(getCor(),60);										//FUNDO MAIS CLARO
				if(corHigh.getRed()==255&&corHigh.getGreen()==255&&corHigh.getBlue()==255){		//SE CLARO DEMAIS
					corHigh=Cor.getChanged(getCor(),-30);										//FUNDO MAIS ESCURO
				}
				return corHigh;
			case TO_BE_CREATOR:			return Modulo.Cores.CREATE;
			case TO_BE_PAI:				return Modulo.Cores.PAI;
			case TO_BE_SON:				return Modulo.Cores.SON;
			case TO_BE_DELETED:			return Modulo.Cores.DELETE;
		}
	}
	public Cor getBordaCor(Modulo.State state){
		switch(state){
			case UNSELECTED:default:
				Cor corUnSelec=Cor.getChanged(getCor(),-30);										//BORDA MAIS ESCURA QUE O FUNDO
				if(corUnSelec.getRed()==0&&corUnSelec.getGreen()==0&&corUnSelec.getBlue()==0){		//SE ESCURO DEMAIS
					corUnSelec=Cor.getChanged(getCor(),(int)((255-getCor().getBrilho())/2));		//ESCLARECE
				}
				return Cor.getContraste(Tree.FUNDO,corUnSelec,false);
			case SELECTED:				return Cor.getContraste(Tree.FUNDO,Cor.getInverted(getCor()),false);
			case HIGHLIGHTED:			return Cor.getContraste(Tree.FUNDO,getCor(),false);
			case TO_BE_CREATOR:			return Cor.getContraste(Tree.FUNDO,Cor.getChanged(Modulo.Cores.CREATE,0.8f),false);
			case TO_BE_PAI:				return Cor.getContraste(Tree.FUNDO,Cor.getChanged(Modulo.Cores.PAI,0.8f),false);
			case TO_BE_SON:				return Cor.getContraste(Tree.FUNDO,Cor.getChanged(Modulo.Cores.SON,0.8f),false);
			case TO_BE_DELETED:			return Cor.getContraste(Tree.FUNDO,Cor.getChanged(Modulo.Cores.DELETE,0.8f),false);
		}
	}
//TAG -> MOD
	public static Modulo getModulo(Element modTag)throws Exception{
		return new Modulo(){{													//MOD
			justSetTitle(modTag.getAttribute("title"));										//TITLE
			setXIndex(Integer.parseInt(modTag.getAttribute("x")));							//X
			setYIndex(Integer.parseInt(modTag.getAttribute("y")));							//Y
			setCor(Cor.HexToRGBA(modTag.getAttribute("color")));							//COLOR
			setBorda(new Borda(Integer.parseInt(modTag.getAttribute("border"))));			//BORDER
			final List<String>relLinks=Arrays.asList(modTag.getAttribute("icons").split(","));	//ICONS
			for(String relLink:relLinks){
				for(Icone icone:IconePick.ICONES){
					if(relLink.equals(icone.getRelativeLink())){
						justAddIcone(icone);
						break;
					}
				}
			}
			final Element textTag=(Element)modTag.getElementsByTagName("text").item(0);		//TEXT
			setTexto(textTag.getTextContent());
		}};
	}
//MOD -> TAG
	public void addToXML(Document xml,Element mindTag){
		final Element modTag=xml.createElement("mod");				//MOD
		modTag.setAttribute("title",getTitle());								//TITLE
		modTag.setAttribute("x",String.valueOf(getXIndex()));					//X
		modTag.setAttribute("y",String.valueOf(getYIndex()));					//Y
		modTag.setAttribute("color",getCor().toHexOpaque());					//COLOR
		modTag.setAttribute("border",String.valueOf(getBorda().getIndex()));	//BORDER
		final List<String>icons=new ArrayList<String>();
		for(Icone icone:getIcones())icons.add(icone.getRelativeLink());
		modTag.setAttribute("icons",String.join(",",icons));					//ICONS
		final Element textTag=xml.createElement("text");						//TEXT
		textTag.setTextContent(getText());
		modTag.appendChild(textTag);
		mindTag.appendChild(modTag);
	}
}