package element.tree.popup;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;

import element.tree.Tree;
import element.tree.objeto.modulo.Modulo;
@SuppressWarnings("serial")
public class Menu extends JMenu{
//MAIN
	private final Menu menu=this;
	public Menu(String titulo){
		super(titulo);
		setIconTextGap(15);
		setOpaque(true);
		setBackground(Tree.Menu.FUNDO);
		setForeground(Tree.Menu.FONTE);
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent m){
				focus(menu);
			}
		});
	}
//FUNCS
@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		if(enabled){
			menu.setForeground(Tree.Menu.FONTE);
		}else menu.setForeground(Tree.Menu.FONTE_DISABLED);
	}
//FOCO
	private Runnable focusDelayed(Menu menu){
		final Runnable focus=new Runnable(){
			public void run(){		//FOCA NO MENU_A_FOCAR APÓS UM TEMPO
				try{
					Thread.sleep(Popup.DELAY);
					if(this!=Popup.getFocus())return;		//FOI SUBSTITUÍDO
					focus(menu);
				}catch(InterruptedException erro){}
			}
		};
		new Thread(focus).start();
		return focus;
	}
	private void focus(Menu menu){
		Popup.setGoingToFocus(true);
		menu.processMouseEvent(new MouseEvent(menu,MouseEvent.MOUSE_ENTERED,0,0,menu.getX(),menu.getY(),1,true,0));
		Popup.setGoingToFocus(false);
	}
@Override
	protected void processMouseEvent(MouseEvent m){
		switch(m.getID()){
			case MouseEvent.MOUSE_ENTERED:
				if(Popup.getGoingToFocus()){
					super.processMouseEvent(m);
				}else{
					Popup.setFocus(focusDelayed(menu));
				}
				if(isEnabled()){
					setBackground(Tree.Menu.SELECT);
				}else setBackground(Tree.Menu.SELECT_DISABLED);
			break;
			case MouseEvent.MOUSE_EXITED:
				Popup.showTooltip(true);
				Popup.setFocus(null);
				setBackground(Modulo.Cores.FUNDO);
			break;
			default:
				Popup.showTooltip(true);
				super.processMouseEvent(m);
			break;
		}
	}
}