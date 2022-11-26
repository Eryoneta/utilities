package element;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import architecture.rrf_vp.Root;

public abstract class Elemento<R,F,V,P> extends Root<R,F,V,P>{
//RRF-VP
	protected ElementoView<?> getThisView() {return (ElementoView<?>)view;}
//PAINEL(PAI)
	protected Painel painel;
		public Painel getPainel(){return painel;}
//LOCAL
	public int getX(){return getThisView().getX();}
	public void setX(int x){getThisView().setX(x);}
	public int getY(){return getThisView().getY();}
	public void setY(int y){getThisView().setY(y);}
	public Point getLocation(){return getThisView().getLocation();}
	public void setLocation(int x,int y){getThisView().setLocation(x, y);}
//FORM
	public int getWidth(){return getThisView().getWidth();}
	public void setWidth(int width){getThisView().setWidth(width);}
	public int getHeight(){return getThisView().getHeight();}
	public void setHeight(int height){getThisView().setHeight(height);}
	public Dimension getSize(){return getThisView().getSize();}
	public void setSize(int width,int height){getThisView().setSize(width, height);}
	public Rectangle getBounds(){return getThisView().getBounds();}
	public Rectangle getRelativeBounds(){return getThisView().getRelativeBounds();}
	public void setBounds(int x,int y,int width,int height){getThisView().setBounds(x, y, width, height);}
	public void setBounds(Rectangle r){getThisView().setBounds(r.x,r.y,r.width,r.height);}
//IMAGEM
	protected Image getPrint(){return getThisView().getPrint();}
//VISIBILIDADE
	public boolean isVisible(){return getThisView().isVisible();}
	public void setVisible(boolean visible){getThisView().setVisible(visible);}
//CLEAR
	public void clearDraw(){getThisView().clearDraw();}
//DRAW
	public synchronized void draw(){getThisView().draw();}
	protected abstract void draw(Graphics2D imagemEdit);
//MAIN
	public Elemento(Painel painel){
		this.painel=painel;
		painel.setLayout(null);
	}
}