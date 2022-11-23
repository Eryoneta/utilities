package element.tree.popup.borda;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import element.tree.propriedades.Borda;
import element.tree.propriedades.Cor;
import element.tree.Cursor;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.modulo.Modulo;
import element.tree.objeto.modulo.ModuloUI;
import element.tree.popup.Bloco;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class BordaBloco extends Bloco{
//BORDA
	private Borda borda;
		public Borda getBorda(){return borda;}
		public void setBorda(Borda borda){this.borda=borda;}
//MAIN
	public BordaBloco(BordaPick pick,int x,int y,int size,Borda borda){
		setBounds(x,y,size,size);
		setBorda(borda);
		setFocus(false);
		set(pick);
	}
	public BordaBloco(BordaPick pick){
		setBounds(0,0,0,0);
		setBorda(new Borda(Borda.SOLID));
		setFocus(false);
		set(pick);
	}
	private void set(BordaPick pick){
		final BordaBloco bloco=this;
		pick.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent m){
				if(pick.getSelectedMods().isEmpty()&&pick.getSelectedCoxs().isEmpty())return;
				final Point mouse=m.getPoint();
				if(getBounds().contains(mouse)){
					if(isFocused())return;
					setFocus(true);
					pick.setToolTipBloco(bloco);
					pick.repaint();
				}else{
					if(!isFocused())return;
					setFocus(false);
					pick.setToolTipBloco(null);
					pick.repaint();
				}
			}
		});
		pick.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent m){
				if(pick.getSelectedMods().isEmpty()&&pick.getSelectedCoxs().isEmpty())return;
				final Point mouse=m.getPoint();
				if(Cursor.match(m,Cursor.LEFT)){
					if(!getBounds().contains(mouse))return;
					boolean changed=false;
					final Borda[]oldBordas=new Borda[pick.getSelectedMods().size()+pick.getSelectedCoxs().size()];
					for(int i=0,size=pick.getSelectedMods().size();i<size;i++){
						final Objeto obj=pick.getSelectedMods().get(i);
						final Modulo mod=(Modulo)obj;
						if(!mod.getBorda().equals(bloco.getBorda()))changed=true;
						oldBordas[i]=mod.getBorda();
						mod.setBorda(bloco.getBorda());
					}
					final int sizeMods=pick.getSelectedMods().size();
					for(int i=0,size=pick.getSelectedCoxs().size();i<size;i++){
						final Objeto obj=pick.getSelectedCoxs().get(i);
						final Conexao cox=(Conexao)obj;
						if(!cox.getBorda().equals(bloco.getBorda()))changed=true;
						oldBordas[sizeMods+i]=cox.getBorda();
						cox.setBorda(bloco.getBorda());
					}
					if(changed)pick.triggerBordaPickListener(oldBordas,bloco.getBorda());
					pick.getTree().draw();
				}
			}
		});
	}
//DRAW
	public void draw(Graphics imagemEdit){
		if(isFocused()){
			imagemEdit.setColor(Cor.getChanged(ModuloUI.Cores.FUNDO,1.5f));
			imagemEdit.fillRect((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
		}
		imagemEdit.setColor(Color.BLACK);
		((Graphics2D)imagemEdit).setStroke(getBorda().getStaticVisual());
		final float borda=Popup.BLOCO_SPACE/2;
		final int x1=(int)(getX()+borda);
		final int y2=(int)(getY()+borda);
		final int y1=y2+(int)(getHeight()-Popup.BLOCO_SPACE);
		final int x2=x1+(int)(getWidth()-Popup.BLOCO_SPACE);
		((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		imagemEdit.drawLine(x1,y1,x2,y2);
		((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}