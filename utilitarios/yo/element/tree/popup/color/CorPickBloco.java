package element.tree.popup.color;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import element.tree.propriedades.Cor;
import element.tree.Cursor;
import element.tree.objeto.modulo.Modulo;
import element.tree.objeto.modulo.ModuloUI;
import element.tree.popup.Bloco;
import element.tree.popup.Popup;
@SuppressWarnings("serial")
public class CorPickBloco extends Bloco{
//JANELA
	private JFrame janela=new JFrame(){{
		setAlwaysOnTop(true);
		setUndecorated(true);
		setBackground(new Cor(0,0,0,1));	//QUASE INVISÍVEL
		final Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width,screenSize.height);
		addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent m){
				final Point mouse=m.getPoint();
				if(Cursor.match(m,Cursor.LEFT)){
					janela.dispose();
					setCor(mouse);
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			private final int whitePixel=Color.WHITE.getRGB();
			private final int transparentPixel=Cor.TRANSPARENTE.getRGB();
			public void mouseMoved(MouseEvent m){
				setPickerCor(m.getPoint());
			}
			public void mouseDragged(MouseEvent m){
				setPickerCor(m.getPoint());
			}
			private void setPickerCor(Point mouse){
				final Color cor=bot.getPixelColor(mouse.x,mouse.y);
				final Graphics2D cursorImgEdit=(Graphics2D)cursorImg.getGraphics();
				cursorImgEdit.setColor(cor);
				cursorImgEdit.fillRect(0,0,Cursor.CURSOR_SIZE,Cursor.CURSOR_SIZE);		//PINTA A COR ESCOLHIDA
				for(int y=0;y<Cursor.CURSOR_SIZE;y++){
					for(int x=0;x<Cursor.CURSOR_SIZE;x++){
						final int maskPixel=mask.getRGB(x,y);
						if(maskPixel==whitePixel)cursorImg.setRGB(x,y,transparentPixel);	//BRANCO SIGNIFICA TRANSPARENTE
					}
				}
				cursorImgEdit.drawImage(cursor,0,0,null);		//DRAW O CURSOR
				setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImg,new Point(0,Cursor.CURSOR_SIZE-1),""));
				cursorImgEdit.dispose();
			}
		});
	}};
//CURSOR
	private final BufferedImage cursorImg=new BufferedImage(Cursor.CURSOR_SIZE,Cursor.CURSOR_SIZE,BufferedImage.TYPE_INT_ARGB);
	private final BufferedImage mask=new BufferedImage(Cursor.CURSOR_SIZE,Cursor.CURSOR_SIZE,BufferedImage.TYPE_INT_ARGB);
	private final Image cursor=Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/picker/Cursor.png"));
//PICKER
	private CorPick pick;
	private Robot bot;
		private void setCor(Point local){
			if(pick.getSelectedMods().isEmpty())return;
			final Cor cor=new Cor(bot.getPixelColor(local.x,local.y));
			boolean changed=false;
			final Cor[]oldCores=new Cor[pick.getSelectedMods().size()];
			for(int i=0,size=pick.getSelectedMods().size();i<size;i++){
				final Modulo mod=pick.getSelectedMods().get(i);
				if(!mod.getCor().equals(cor))changed=true;
				oldCores[i]=mod.getCor();
				mod.setCor(cor);
			}
			pick.getBlocoPrincipal().setCor(pick.getSelectedMods().get(pick.getSelectedMods().size()-1).getCor());
			pick.repaint();
			if(changed)pick.triggerCorPickListener(oldCores,cor);
			pick.getTree().draw();
		}
//MAIN
	public CorPickBloco(CorPick pick,int x,int y,int width,int height){
		setBounds(x,y,width,height);
		setFocus(false);
		this.pick=pick;
		try{
			bot=new Robot();
		}catch(AWTException erro){}
		set(pick);
		final Graphics2D maskEdit=(Graphics2D)mask.getGraphics();
		maskEdit.drawImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/picker/Mask.png")),0,0,null);
	}
	private void set(CorPick pick){
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
				final Point mouse=m.getPoint();
				if(Cursor.match(m,Cursor.LEFT)){
					if(!getBounds().contains(mouse))return;
					janela.setVisible(true);
				}
			}
		});
	}
//DRAW
	private final Image icone=new ImageIcon(Cursor.class.getResource("/icons/picker/Picker.png")).getImage();
	public void draw(Graphics imagemEdit){
		if(isFocused()){
			imagemEdit.setColor(Cor.getChanged(ModuloUI.Cores.FUNDO,1.5f));
			imagemEdit.fillRect((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
		}
		imagemEdit.drawImage(	//INSERE ICONE CENTRALIZADO DO BLOCO
				icone,
				(int)(getX()+(getWidth()-Popup.BLOCO_SIZE)/2),
				(int)(getY()+(getHeight()-Popup.BLOCO_SIZE)/2),
				Popup.BLOCO_SIZE,
				Popup.BLOCO_SIZE
		,null);
	}
}