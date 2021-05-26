package element.tree.popup.icon;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import element.tree.Cor;
import element.tree.Cursor;
import element.tree.Icone;
import element.tree.objeto.modulo.Modulo;
import element.tree.popup.Bloco;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class IconeBloco extends Bloco{
//ICONE
	private Icone icone;
		public Icone getIcone(){return icone;}
		public void setIcone(Icone icone){this.icone=icone;}
//MAIN
	public IconeBloco(IconePick pick,int x,int y,int size,Icone icone){
		super();
		setBounds(x,y,size,size);
		setIcone(icone);
		setFocus(false);
		set(pick);
	}
	public IconeBloco(IconePick pick){
		super();
		setBounds(0,0,0,0);
		setIcone(null);
		setFocus(false);
		set(pick);
	}
	private void set(IconePick pick){
		final IconeBloco bloco=this;
		pick.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseMoved(MouseEvent m){
				if(pick.getSelectedMods().isEmpty())return;
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
				if(pick.getSelectedMods().isEmpty())return;
				final Point mouse=m.getPoint();
				if(Cursor.match(m,Cursor.LEFT)){
					if(!getBounds().contains(mouse))return;
					boolean changed=false;
					for(Modulo mod:pick.getSelectedMods()){
						if(mod.addIcone(getIcone()))changed=true;
					}
					if(changed)pick.triggerIconePickListener(getIcone(),true);
					pick.getTree().draw();
				}else if(Cursor.match(m,Cursor.RIGHT)){
					if(!getBounds().contains(mouse))return;
					boolean changed=false;
					for(Modulo mod:pick.getSelectedMods()){
						if(mod.delIcone(getIcone())!=null)changed=true;
					}
					if(changed)pick.triggerIconePickListener(getIcone(),false);
					pick.getTree().draw();
				}
			}
		});
	}
//DRAW
	public void draw(Graphics imagemEdit){
		if(isFocused()){
			imagemEdit.setColor(Cor.getChanged(Modulo.Cores.FUNDO,1.5f));
			imagemEdit.fillRect((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
		}
		imagemEdit.drawImage(	//INSERE ICONE CENTRALIZADO DO BLOCO
				getIcone().getIcone(),
				(int)(getX()+(getWidth()-Popup.BLOCO_SIZE)/2),
				(int)(getY()+(getHeight()-Popup.BLOCO_SIZE)/2),
				Popup.BLOCO_SIZE,
				Popup.BLOCO_SIZE
		,null);
	}
}