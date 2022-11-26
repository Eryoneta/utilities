package element;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import architecture.rrf_vp.View;

public abstract class ElementoView<R> extends View<R> {
//LOCAL
	protected int x=0;
		public int getX(){return x;}
		public void setX(int x){
			final Elemento<?,?,?,?> root=(Elemento<?,?,?,?>)this.root;
			this.x=root.getPainel().getJanela().getInsets().left+x;
		}
	protected int y=0;
		public int getY(){return y;}
		public void setY(int y){
			final Elemento<?,?,?,?> root=(Elemento<?,?,?,?>)this.root;
			this.y=root.getPainel().getJanela().getInsets().top+y;
		}
	public Point getLocation(){return new Point(getX(),getY());}
	public void setLocation(int x,int y){setX(x);setY(y);}
//FORM
	protected int width=1;
		public int getWidth(){return width;}
		public void setWidth(int width){this.width=width;sizeChanged=true;}
	protected int height=1;
		public int getHeight(){return height;}
		public void setHeight(int height){this.height=height;sizeChanged=true;}
	public Dimension getSize(){return new Dimension(getWidth(),getHeight());}
	public void setSize(int width,int height){setWidth(width);setHeight(height);}
	public Rectangle getBounds(){return new Rectangle(getX(),getY(),getWidth(),getHeight());}
	public Rectangle getRelativeBounds(){return new Rectangle(0,0,getWidth(),getHeight());}
	public void setBounds(int x,int y,int width,int height){setX(x);setY(y);setWidth(width);setHeight(height);}
//IMAGEM
	protected Image print=new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		protected Image getPrint(){return print;}
	private Image buffer=new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
	private boolean sizeChanged=false;
		private void resizePrint(){
			print.flush();
			buffer=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
			print=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
			sizeChanged=false;
		}
//VISIBILIDADE
	protected boolean visible=false;
		public boolean isVisible(){return visible;}
		public void setVisible(boolean visible){
			this.visible=visible;
			draw();
		}
//MAIN
	public ElementoView(R root) {
		super(root);
	}
//CLEAR
	public void clearDraw(){
		clear((Graphics2D)buffer.getGraphics());
		clear((Graphics2D)getPrint().getGraphics());
	}
		private void clear(Graphics2D imagemEdit){
			imagemEdit.setComposite(AlphaComposite.Clear);
			imagemEdit.fillRect(0,0,getWidth(),getHeight());
			imagemEdit.setComposite(AlphaComposite.SrcOver);
		}
//DRAW
	public synchronized void draw(){
		if(!isVisible())return;
		if(sizeChanged)resizePrint();		//RECRIA TELA
		draw((Graphics2D)buffer.getGraphics());											//DESENHA COMPONENTES EM BUFFER
		getPrint().getGraphics().drawImage(buffer,0,0,getWidth(),getHeight(),null);		//DESENHA TUDO EM IMAGEM
		final Elemento<?,?,?,?> root=(Elemento<?,?,?,?>)this.root;
		root.getPainel().getJanela().repaint();
	}
	protected abstract void draw(Graphics2D imagemEdit);
//FUNCS
	@Override
	protected abstract void init();
}