package element.tree.popup.grossura;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import element.tree.propriedades.Cor;
import element.tree.Cursor;
import element.tree.propriedades.Grossura;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.modulo.ModuloUI;
import element.tree.popup.Bloco;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class GrossuraBloco extends Bloco{
//GROSSURA
	private Grossura grossura;
		public Grossura getGrossura(){return grossura;}
		public void setGrossura(Grossura grossura){this.grossura=grossura;}
//MAIN
	public GrossuraBloco(GrossuraPick pick,int x,int y,int size,Grossura grossura){
		setBounds(x,y,size*3,size);
		setGrossura(grossura);
		setFocus(false);
		set(pick);
	}
	public GrossuraBloco(GrossuraPick pick){
		setBounds(0,0,0,0);
		setGrossura(new Grossura(Grossura.MEDIUM));
		setFocus(false);
		set(pick);
	}
	private void set(GrossuraPick pick){
		final GrossuraBloco bloco=this;
		pick.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent m){
				if(pick.getSelectedCoxs().isEmpty())return;
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
				if(pick.getSelectedCoxs().isEmpty())return;
				final Point mouse=m.getPoint();
				if(Cursor.match(m,Cursor.LEFT)){
					if(!getBounds().contains(mouse))return;
					boolean changed=false;
					final Grossura[]oldGrossuras=new Grossura[pick.getSelectedCoxs().size()];
					for(int i=0,size=pick.getSelectedCoxs().size();i<size;i++){
						final Conexao cox=pick.getSelectedCoxs().get(i);
						if(!cox.getGrossura().equals(bloco.getGrossura()))changed=true;
						oldGrossuras[i]=cox.getGrossura();
						cox.setGrossura(bloco.getGrossura());
					}
					if(changed)pick.triggerGrossuraPickListener(oldGrossuras,bloco.getGrossura());
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
		((Graphics2D)imagemEdit).setStroke(new BasicStroke(getGrossura().getStaticValor(),BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
		final float borda=Popup.BLOCO_SPACE/2;
		final int x1=(int)(getX()+borda);
		final int y=(int)(getY()+borda+(getHeight()/2-Popup.BLOCO_SPACE));
		final int x2=x1+(int)(getWidth()-Popup.BLOCO_SPACE);
		((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		imagemEdit.drawLine(x1,y,x2,y);
		((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}