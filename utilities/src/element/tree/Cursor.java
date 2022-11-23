package element.tree;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
public class Cursor{
//ST
	private final CursorST ST=new CursorST(this);
		public CursorST getST(){return ST;}
		public int getState(){return getST().getState();}
		public void set(int stateIndex){getST().setState(stateIndex);}
//VAR GLOBAIS
	protected JFrame janela;
	public final static int CURSOR_SIZE=32;
//MAIN
	public Cursor(JFrame janela){
		this.janela=janela;
	}
//FUNCS
//COMANDOS
	public final static int NOTHING=0;
	public final static int LEFT=MouseEvent.BUTTON1_DOWN_MASK;
	public final static int MIDDLE=MouseEvent.BUTTON2_DOWN_MASK;
	public final static int RIGHT=MouseEvent.BUTTON3_DOWN_MASK;
	public final static int SHIFT=MouseEvent.SHIFT_DOWN_MASK;
	public final static int CTRL=MouseEvent.CTRL_DOWN_MASK;
	public final static int ALT=MouseEvent.ALT_DOWN_MASK;
	public static boolean match(MouseEvent m,int...keys){
		boolean result=true;
		boolean multiPress=(m.getButton()==MouseEvent.NOBUTTON);
		for(int key1:keys){
			switch(key1){
				case LEFT:case MIDDLE:case RIGHT:
					for(int key2:keys){
						if(key2!=key1)switch(key2){
							case LEFT:case MIDDLE:case RIGHT:
								multiPress=true;
							break;
						}
					}
				break;
			}
		}
		for(int k=0;k<keys.length&&result;k++){
			if(multiPress)switch(keys[k]){
				case LEFT:		result=SwingUtilities.isLeftMouseButton(m);		break;
				case MIDDLE:	result=SwingUtilities.isMiddleMouseButton(m);	break;
				case RIGHT:		result=SwingUtilities.isRightMouseButton(m);	break;
			}else switch(keys[k]){
				case LEFT:		result=(m.getButton()==MouseEvent.BUTTON1);		break;
				case MIDDLE:	result=(m.getButton()==MouseEvent.BUTTON2);		break;
				case RIGHT:		result=(m.getButton()==MouseEvent.BUTTON3);		break;
			}
			switch(keys[k]){
				case SHIFT:		result=m.isShiftDown();							break;
				case CTRL:		result=m.isControlDown();						break;
				case ALT:		result=m.isAltDown();							break;
			}
		}
		return result;
	}
//	public static boolean match(MouseEvent m,int...keys){	//MATCH EXATA, MAS EXCLUI MUITOS CASOS
//		boolean result=true;
//		boolean multiPress=(m.getButton()==MouseEvent.NOBUTTON);
//		for(int key1:keys){
//			switch(key1){
//				case LEFT:case MIDDLE:case RIGHT:
//					for(int key2:keys){
//						if(key2!=key1)switch(key2){
//							case LEFT:case MIDDLE:case RIGHT:
//								multiPress=true;
//							break;
//						}
//					}
//				break;
//			}
//		}
//		boolean left=false;
//		boolean middle=false;
//		boolean right=false;
//		boolean shift=false;
//		boolean ctrl=false;
//		boolean alt=false;
//		for(int k=0;k<keys.length&&result;k++){
//			if(multiPress)switch(keys[k]){
//				case LEFT:		left=result=SwingUtilities.isLeftMouseButton(m);		break;
//				case MIDDLE:	middle=result=SwingUtilities.isMiddleMouseButton(m);	break;
//				case RIGHT:		right=result=SwingUtilities.isRightMouseButton(m);		break;
//			}else switch(keys[k]){
//				case LEFT:		left=result=(m.getButton()==MouseEvent.BUTTON1);		break;
//				case MIDDLE:	middle=result=(m.getButton()==MouseEvent.BUTTON2);		break;
//				case RIGHT:		right=result=(m.getButton()==MouseEvent.BUTTON3);		break;
//			}
//			switch(keys[k]){
//				case SHIFT:		shift=result=m.isShiftDown();							break;
//				case CTRL:		ctrl=result=m.isControlDown();							break;
//				case ALT:		alt=result=m.isAltDown();								break;
//			}
//		}
//		if(!result)return false;	//MOUSE NÃO TEM ALGUM BOTÃO
//		//MOUSE TEM ALGUM BOTÃO QUE NÃO DEVERIA TER
//		if(multiPress){
//			if(!left)if(SwingUtilities.isLeftMouseButton(m))return false;
//			if(!middle)if(SwingUtilities.isMiddleMouseButton(m))return false;
//			if(!right)if(SwingUtilities.isRightMouseButton(m))return false;
//		}else{
//			if(!left)if(m.getButton()==MouseEvent.BUTTON1)return false;
//			if(!middle)if(m.getButton()==MouseEvent.BUTTON2)return false;
//			if(!right)if(m.getButton()==MouseEvent.BUTTON3)return false;
//		}
//		System.out.println(m);
//		if(!shift)if(m.isShiftDown())return false;
//		if(!ctrl)if(m.isControlDown())return false;
//		if(!alt)if(!middle&&m.isAltDown())return false;	//MIDDLE SEMPRE CHAMA ALT???
//		return result;
//	}
}