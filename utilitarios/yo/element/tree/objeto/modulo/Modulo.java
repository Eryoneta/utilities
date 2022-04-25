package element.tree.objeto.modulo;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
import element.tree.main.Tree;
import element.tree.main.TreeUI;
public class Modulo extends Objeto{
//ST
	private final ModuloST ST=new ModuloST(this);
		public ModuloST getST(){return ST;}
		public ModuloST.State getState(){return getST().getState();}
		public void setState(ModuloST.State state){getST().setState(state);}
//UI
	private final ModuloUI UI=new ModuloUI(this);
		public ModuloUI getUI(){return UI;}
		public void draw(Graphics2D imagemEdit,int unit){
			getUI().draw(imagemEdit,unit);
		}
//LOCAL
	private int xIndex;
		public int getXIndex(){return xIndex;}
		public int getX(int unit){return (Tree.getLocalX()+xIndex)*unit;}
		public int getMeioXIndex(){return (getXIndex()+(getWidthIndex()/2))-(getWidthIndex()%2==0?0:1);}		//(X+(WIDTH/2))-(AJUSTE PARA WIDTHS ÍMPARES)
		public int getMeioX(int unit){return (getX(unit)+(getWidth(unit)/2))-(getWidthIndex()%2==0?0:unit/2);}	//(X+(WIDTH/2))-(AJUSTE PARA WIDTHS ÍMPARES)
		public void setXIndex(int x){xIndex=x;}
	private int yIndex;
		public int getYIndex(){return yIndex;}
		public int getY(int unit){return (Tree.getLocalY()+yIndex)*unit;}
		public int getMeioYIndex(){return (getYIndex()+(getHeightIndex()/2))-(getHeightIndex()%2==0?0:1);}		//(Y+(HEIGHT/2))-(AJUSTE PARA HEIGHTS ÍMPARES)
		public int getMeioY(int unit){return (getY(unit)+(getHeight(unit)/2))-(getHeightIndex()%2==0?0:unit/2);}//(Y+(HEIGHT/2))-(AJUSTE PARA HEIGHTS ÍMPARES)
		public void setYIndex(int y){yIndex=y;}
	public Point getLocationIndex(){return new Point(getXIndex(),getYIndex());}
	public Point getLocation(int unit){return new Point(getX(unit),getY(unit));}
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
		public int getWidth(int unit){return widthIndex*unit;}
	private int heightIndex;
		public int getHeightIndex(){return heightIndex;}
		public int getHeight(int unit){return heightIndex*unit;}
	public Dimension getSizeIndex(){return new Dimension(getWidthIndex(),getHeightIndex());}
	public Rectangle getFormIndex(){return new Rectangle(getXIndex(),getYIndex(),getWidthIndex(),getHeightIndex());}
	public Rectangle getForm(int unit){return new Rectangle(getX(unit),getY(unit),getWidth(unit),getHeight(unit));}
	public void justSetSize(){
		final int unit=8;	//O TAMANHO É FIXO, INDEPENDENTE DO ZOOM ATUAL
		final Graphics imagemEdit=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).getGraphics();
	//WIDTH
		widthIndex=0;
		if(TreeUI.Fonte.FONTE!=null)imagemEdit.setFont(Modulo.getFont(unit));
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
			final int newWidthIndex=(int)Math.ceil(((float)Icone.getSize(unit)*getIcones().size())/unit);
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
	public boolean contains(Point mouse){return getForm(Tree.UNIT).contains(mouse);}
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
	private Cor cor=ModuloUI.Cores.FUNDO;
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
	protected String[]titulo;
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
		return new Font(TreeUI.Fonte.FONTE.getName(),TreeUI.Fonte.FONTE.getStyle(),(int)(unit*0.1f*TreeUI.Fonte.FONTE.getSize()));
	}
	public Font getRelativeFont(int unit){
		final Graphics imagemEdit=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).getGraphics();
		imagemEdit.setFont(Modulo.getFont(unit));
		int width=0;
		for(String linha:titulo){
			width=Math.max(width,imagemEdit.getFontMetrics().stringWidth(linha));
		}
		int height=imagemEdit.getFontMetrics().getHeight()*titulo.length;
		while(width>getWidth(unit)-unit||height>getHeight(unit)){	//DIMINÚI A FONTE ATÉ O TÍTULO FICAR MENOR QUE O MOD(COM ESPAÇO EXTRA DE 1 UNIT)
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
		result=prime*result+((ST==null)?0:ST.hashCode());
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
		if(ST!=mod.ST)return false;
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