package element.tree.objeto.modulo;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import architecture.rrf_vp.view.ViewJoint;

public class ModuloView implements ViewJoint<ModuloView,Modulo> {
//ROOT
	private Modulo root;
		@Override public Modulo getRoot() {return root;}
//LOCAL
	private int xIndex;
		public int getXIndex(){return xIndex;}
		public int getX(){return getX(getRoot().getTree().getViewUnit());}
		public int getX(byte unit){return (getRoot().getTree().getViewX()+xIndex)*unit;}
		public void setXIndex(int x){xIndex=x;}
	private int yIndex;
		public int getYIndex(){return yIndex;}
		public int getY(){return getY(getRoot().getTree().getViewUnit());}
		public int getY(byte unit){return (getRoot().getTree().getViewY()+yIndex)*unit;}
		public void setYIndex(byte y){yIndex=y;}
	public Point getLocationIndex(){return new Point(getXIndex(),getYIndex());}
	public Point getLocation(){return getLocation(getRoot().getTree().getViewUnit());}
	public Point getLocation(byte unit){return new Point(getX(unit),getY(unit));}
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
		public int getWidth(){return getWidth(getRoot().getTree().getViewUnit());}
		public int getWidth(byte unit){return widthIndex*unit;}
	private int heightIndex;
		public int getHeightIndex(){return heightIndex;}
		public int getHeight(){return getHeight(getRoot().getTree().getViewUnit());}
		public int getHeight(byte unit){return heightIndex*unit;}
	public Dimension getSizeIndex(){return new Dimension(getWidthIndex(),getHeightIndex());}
	public Dimension getSize(){return new Dimension(getWidth(),getHeight());}
	public Rectangle getFormIndex(){return new Rectangle(getXIndex(),getYIndex(),getWidthIndex(),getHeightIndex());}
	public Rectangle getForm(byte unit){return new Rectangle(getX(unit),getY(unit),getWidth(unit),getHeight(unit));}
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
//MAIN
	public ModuloView(Modulo root) {
		this.root=root;
	}
//FUNCS
	@Override public void init() {}
}