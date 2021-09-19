package element.tree.popup.color;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JColorChooser;
import element.tree.propriedades.Cor;
import element.tree.Cursor;
import element.tree.main.TreeUI;
import element.tree.objeto.modulo.Modulo;
import element.tree.objeto.modulo.ModuloUI;
import element.tree.popup.Bloco;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class CorBloco extends Bloco{
//COR
	private Cor cor;
		public Cor getCor(){return cor;}
		public void setCor(Cor cor){this.cor=cor;}
		public void setCor(Color cor){this.cor=new Cor(cor);}
//MAIN
	public CorBloco(CorPick pick,int x,int y,int size,Cor cor){
		setBounds(x,y,size,size);
		setCor(cor);
		setFocus(false);
		set(pick);
	}
	public CorBloco(CorPick pick){
		setBounds(0,0,0,0);
		setCor(Color.WHITE);
		setFocus(false);
		set(pick);
	}
	private void set(CorPick pick){
		final CorBloco bloco=this;
		pick.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent m){
				if(pick.getSelectedMods().isEmpty())return;
				final Point mouse=m.getPoint();
				if(getBounds().contains(mouse)){
					if(isFocused())return;
					setFocus(true);
					pick.repaint();
				}else{
					if(!isFocused())return;
					setFocus(false);
					pick.repaint();
				}
			}
		});
		pick.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent m){
				if(pick.getSelectedMods().isEmpty())return;
				final Point mouse=m.getPoint();
				if(Cursor.match(m,Cursor.LEFT)){
					if(!getBounds().contains(mouse))return;
					boolean changed=false;
					final Cor[]oldCores=new Cor[pick.getSelectedMods().size()];
					for(int i=0,size=pick.getSelectedMods().size();i<size;i++){
						final Modulo mod=pick.getSelectedMods().get(i);
						if(!mod.getCor().equals(getCor()))changed=true;
						oldCores[i]=mod.getCor();
						mod.setCor(getCor());
					}
					pick.getBlocoPrincipal().setCor(pick.getSelectedMods().get(pick.getSelectedMods().size()-1).getCor());
					pick.repaint();
					if(changed)pick.triggerCorPickListener(oldCores,getCor());
					pick.getTree().draw();
				}else if(Cursor.match(m,Cursor.RIGHT)){
					if(!getBounds().contains(mouse))return;
					final Color cor=JColorChooser.showDialog(null,
							TreeUI.getLang().get("T_Pop_C_Es","Choose a color"),
							getCor());
					if(cor==null)return;
					if(bloco==pick.getBlocoPrincipal()){
						pick.getBlocoPrincipal().setCor(cor);
						boolean changed=false;
						final Cor[]oldCores=new Cor[pick.getSelectedMods().size()];
						for(int i=0,size=pick.getSelectedMods().size();i<size;i++){
							final Modulo mod=pick.getSelectedMods().get(i);
							if(!mod.getCor().equals(getCor()))changed=true;
							oldCores[i]=mod.getCor();
							mod.setCor(getCor());
						}
						pick.getBlocoPrincipal().setCor(pick.getSelectedMods().get(pick.getSelectedMods().size()-1).getCor());
						pick.repaint();
						if(changed)pick.triggerCorPickListener(oldCores,getCor());
					}else{
						CorPick.PALETA_DEFAULT[y][x]=new Cor(cor);
						setCor(cor);
						pick.repaint();
					}
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
		((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		final float borda=Popup.BLOCO_SPACE/2;
	//FUNDO
		if(isFocused()){	//HIGHLIGHTED
			Cor corHigh=Cor.getChanged(getCor(),60);										//FUNDO MAIS CLARO
			if(corHigh.getRed()==255&&corHigh.getGreen()==255&&corHigh.getBlue()==255){		//SE CLARO DEMAIS
				corHigh=Cor.getChanged(getCor(),-30);										//FUNDO MAIS ESCURO
			}
			imagemEdit.setColor(corHigh);
		}else{				//UNSELECTED
			imagemEdit.setColor(getCor());
		}
		imagemEdit.fillRoundRect(
				(int)(getX()+borda),
				(int)(getY()+borda),
				(int)(getWidth()-Popup.BLOCO_SPACE),
				(int)(getHeight()-Popup.BLOCO_SPACE)
		,7,7);
	//BORDA
		if(isFocused()){	//HIGHLIGHTED
			imagemEdit.setColor(getCor());
		}else{				//UNSELECTED
			Cor corUnSelec=Cor.getChanged(getCor(),-30);									//BORDA MAIS ESCURA QUE O FUNDO
			if(corUnSelec.getRed()==0&&corUnSelec.getGreen()==0&&corUnSelec.getBlue()==0){	//SE ESCURO DEMAIS
				corUnSelec=Cor.getChanged(getCor(),(int)((255-getCor().getBrilho())/2));	//ESCLARECE
			}
			imagemEdit.setColor(corUnSelec);
		}
		final Stroke linha=((Graphics2D)imagemEdit).getStroke();
		((Graphics2D)imagemEdit).setStroke(new BasicStroke(2));
		imagemEdit.drawRoundRect(
				(int)(getX()+borda),
				(int)(getY()+borda),
				(int)(getWidth()-Popup.BLOCO_SPACE),
				(int)(getHeight()-Popup.BLOCO_SPACE)
		,7,7);
		((Graphics2D)imagemEdit).setStroke(linha);
		((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}