package utilitarios.visual.text.java.mindmarkdown.view;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMImageAtributo;
public class MMParagraphImageView{
//VIEW_PAI
	private MMParagraphView paragraphView;
//EDITOR
	private MindMarkEditor editor;
//MAIN
	public MMParagraphImageView(MMParagraphView paragraphView,MindMarkEditor editor){
		this.paragraphView=paragraphView;
		this.editor=editor;
	}
//DRAW
	public void draw(Graphics g,Shape s,MMImageAtributo.Image image){
		final Rectangle area=s.getBounds();
	//IMAGEM
		editor.getLoadedImages().containsKey(image.getLink());
		if(editor.getLoadedImages().containsKey(image.getLink())){
			final BufferedImage img=editor.getLoadedImages().get(image.getLink());
			final int width=Math.max(img.getWidth(),image.getWidth());
			final int height=Math.max(img.getHeight(),image.getHeight());
			g.drawImage(img,area.x,area.y,width,height,null);
		}else try{
			final BufferedImage img=ImageIO.read(new URL(image.getLink()));
			final int width=Math.max(img.getWidth(),image.getWidth());
			final int height=Math.max(img.getHeight(),image.getHeight());
			g.drawImage(img,area.x,area.y,width,height,null);
			editor.getLoadedImages().put(image.getLink(),img);
		}catch(IOException error){}
	//TEXTO
		paragraphView.paintContent(g,s);
	}
}