package element.tree;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
public class CursorST{
//STATES
	public final static int NORMAL=1;			//[0-9]
	public final static int SELECT=2;			//[0-9]
	public final static int AUTODRAG=3;			//[0-9]
	public final static int MOVE=10;			//[0-9]0
	public final static int CREATE=20;			//[0-9]0
	public final static int DELETE=30;			//[0-9]0
	public final static int SON=40;				//[0-9]0
	public final static int PAI=50;				//[0-9]0
	public final static int EDIT_TITLE=60;		//[0-9]0
	public final static int AREA_SELECT=100;	//[0-9]00
	public final static int AREA_CREATE=200;	//[0-9]00
	public final static int AREA_DELETE=300;	//[0-9]00
	public final static int AREA_SON=400;		//[0-9]00
	public final static int AREA_PAI=500;		//[0-9]00
	public final static int DRAG=1000;			//[0-9]000
//STATE
	private int state=NORMAL;
		public int getState(){return state;}
		public void setState(int state){
			final int camada1=(state%1000%100%10);
			final int camada2=(state%1000%100)-camada1;
			final int camada3=(state%1000)-camada1-camada2;
			final int camada4=(state)-camada1-camada2-camada3;
			final BufferedImage cursorImg=new BufferedImage(Cursor.CURSOR_SIZE,Cursor.CURSOR_SIZE,BufferedImage.TYPE_INT_ARGB);
			final Graphics editCursor=cursorImg.getGraphics();
			switch(camada1){
				case NORMAL:		editCursor.drawImage(getCursor("NORMAL"),0,0,null);			break;
				case SELECT:		editCursor.drawImage(getCursor("SELECT"),0,0,null);			break;
				case AUTODRAG:		editCursor.drawImage(getCursor("AUTODRAG"),0,0,null);		break;
			}
			switch(camada2){
				case MOVE:			editCursor.drawImage(getCursor("MOVE"),0,0,null);			break;
				case CREATE:		editCursor.drawImage(getCursor("CREATE"),0,0,null);			break;
				case DELETE:		editCursor.drawImage(getCursor("DELETE"),0,0,null);			break;
				case SON:			editCursor.drawImage(getCursor("SON"),0,0,null);			break;
				case PAI:			editCursor.drawImage(getCursor("PAI"),0,0,null);			break;
				case EDIT_TITLE:	editCursor.drawImage(getCursor("EDIT_TITLE"),0,0,null);		break;
			}
			switch(camada3){
				case AREA_SELECT:	editCursor.drawImage(getCursor("AREA_SELECT"),0,0,null);	break;
				case AREA_CREATE:	editCursor.drawImage(getCursor("AREA_CREATE"),0,0,null);	break;
				case AREA_DELETE:	editCursor.drawImage(getCursor("AREA_DELETE"),0,0,null);	break;
				case AREA_SON:		editCursor.drawImage(getCursor("AREA_SON"),0,0,null);		break;
				case AREA_PAI:		editCursor.drawImage(getCursor("AREA_PAI"),0,0,null);		break;
			}
			switch(camada4){
				case DRAG:			editCursor.drawImage(getCursor("DRAG"),0,0,null);			break;
			}
			Point ponto=new Point(0,0);
			if(camada1==AUTODRAG)ponto=new Point(16,16);
			editCursor.dispose();
			cursor.janela.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImg,ponto,""));
		}
			private Image getCursor(String nome){
//				Image cursor=Toolkit.getDefaultToolkit().getImage(Cursor.class.getResource("/element/tree/cursores/"+nome+".png"));	//PRECISA DE UM LISTENER, ESPERANDO CARREGAR???
				final Image cursor=new ImageIcon(Cursor.class.getResource("/element/tree/cursores/"+nome+".png")).getImage();
				return cursor;
			}
//CURSOR
	private Cursor cursor;
//MAIN
	public CursorST(Cursor cursor){
		this.cursor=cursor;
	}
}