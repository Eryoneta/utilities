package element.tree.popup;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import element.tree.main.Tree;
import element.tree.main.TreeUI;
import element.tree.objeto.modulo.ModuloUI;
@SuppressWarnings("serial")
public class Item extends JMenuItem{
//TREE
	private Tree tree;
		public Tree getTree(){return tree;}
		public void setTree(Tree tree){this.tree=tree;}
//BLOCOS
	public void unfocusAll(){}	//USADO PELOS EXTENDS
//MAIN
	private final Item item=this;
	public Item(String titulo,Tree tree){
		super(titulo);
		setTree(tree);
		setOpaque(true);
		setBackground(TreeUI.Menu.FUNDO);
		setForeground(TreeUI.Menu.FONTE);
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent m){
				focus(item);
			}
		});
	}
//FUNCS
@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		if(enabled){
			item.setForeground(TreeUI.Menu.FONTE);
		}else item.setForeground(TreeUI.Menu.FONTE_DISABLED);
	}
//FOCO
	private Runnable focusDelayed(Item item){
		final Runnable focus=new Runnable(){
			public void run(){		//FOCA NO MENU_A_FOCAR APÓS UM TEMPO
				try{
					Thread.sleep(Popup.DELAY);
					if(this!=Popup.getFocus())return;		//FOI SUBSTITUÍDO
					focus(item);
				}catch(InterruptedException erro){}
			}
		};
		new Thread(focus).start();
		return focus;
	}
	private void focus(Item item){
		Popup.setGoingToFocus(true);
		item.processMouseEvent(new MouseEvent(item,MouseEvent.MOUSE_ENTERED,0,0,item.getX(),item.getY(),1,true,0));
		Popup.setGoingToFocus(false);
	}
@Override
	protected void processMouseEvent(MouseEvent m){
		switch(m.getID()){
			case MouseEvent.MOUSE_ENTERED:
				if(Popup.getGoingToFocus()){
					super.processMouseEvent(m);
				}else{
					Popup.setFocus(focusDelayed(item));
				}
				if(isEnabled()){
					setBackground(TreeUI.Menu.SELECT);
				}else setBackground(TreeUI.Menu.SELECT_DISABLED);
			break;
			case MouseEvent.MOUSE_EXITED:
				Popup.showTooltip(true);
				unfocusAll();
				repaint();
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