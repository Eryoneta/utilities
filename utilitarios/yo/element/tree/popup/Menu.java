package element.tree.popup;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import element.tree.TreeUI;
import element.tree.objeto.modulo.ModuloUI;
@SuppressWarnings("serial")
public class Menu extends JMenu{
//MAIN
	private final Menu menu=this;
	public Menu(String titulo){
		super(titulo);
		setIconTextGap(15);
		setOpaque(true);
		setBackground(TreeUI.Menu.FUNDO);
		setForeground(TreeUI.Menu.FONTE);
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
			menu.setForeground(TreeUI.Menu.FONTE);
		}else menu.setForeground(TreeUI.Menu.FONTE_DISABLED);
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
					setBackground(TreeUI.Menu.SELECT);
				}else setBackground(TreeUI.Menu.SELECT_DISABLED);
			break;
			case MouseEvent.MOUSE_EXITED:
				Popup.showTooltip(true);
				Popup.setFocus(null);
				setBackground(ModuloUI.Cores.FUNDO);
			break;
			default:
				Popup.showTooltip(true);
				super.processMouseEvent(m);
			break;
		}
	}
}