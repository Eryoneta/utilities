package utilitarios.visual.text.java.view;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.text.Element;
import javax.swing.text.LabelView;
import javax.swing.text.View;
import utilitarios.visual.text.java.SimpleEditor;
public class SimpleLabelView extends LabelView{
//EDITOR_KIT
	private SimpleEditor editorKit;
//MAIN
	public SimpleLabelView(Element elem,SimpleEditor editor){
		super(elem);
		editorKit=editor;
	}
//FUNCS
@Override
	public float getMinimumSpan(int axis){	//APLICA LINE_WRAP EM PALAVRAS GRANDES, AS QUEBRANDO 
		switch(axis){
			case View.X_AXIS:	return (editorKit.isLineWrappable()&&editorKit.isLineWrapped()?0:super.getMinimumSpan(axis));
			case View.Y_AXIS:	return super.getMinimumSpan(axis);
			default:			throw new IllegalArgumentException("Invalid axis: " +axis);
		}
	}
//DRAW
@Override
	public void paint(Graphics imagemEdit,Shape caracter){	//DRAW CARACTERES INVISÍVEIS
		final Graphics2D imagemEdit2D=(Graphics2D)imagemEdit;
	//LETRAS
		super.paint(imagemEdit2D,caracter);
	//LETRAS ESCONDIDAS
		if(editorKit.isAllCharsVisible()){
			final FontMetrics fontMetrics=imagemEdit2D.getFontMetrics();
			final String texto=getText(getStartOffset(),getEndOffset()).toString();
			final Rectangle area=((caracter instanceof Rectangle)?(Rectangle)caracter:caracter.getBounds());
			final Font fonte=new Font(SimpleEditor.DEFAULT_FONT.getFamily(),SimpleEditor.DEFAULT_FONT.getStyle(),imagemEdit2D.getFont().getSize());
			imagemEdit2D.setFont(fonte);
			final FontMetrics fontMetricsDefault=imagemEdit2D.getFontMetrics();
			int sumOfTabs=0;
			for(int i=0;i<texto.length();i++){
				final int prevStringWidth=fontMetrics.stringWidth(texto.substring(0,i))+sumOfTabs;
				final char letra=texto.charAt(i);
				final Point local=new Point((int)(prevStringWidth+area.getX()),(int)(area.getY()+area.getHeight()*0.8));
				switch(letra){
					case ' ':				//ESPAÇO
						final int adjustSpaceX=(fontMetrics.stringWidth(SimpleEditor.SPACE_SYMBOL)-fontMetricsDefault.stringWidth(SimpleEditor.SPACE_SYMBOL))/2;
						imagemEdit2D.setColor(SimpleEditor.SPECIAL_CHARACTERS_COLOR);
						imagemEdit2D.drawString(SimpleEditor.SPACE_SYMBOL,local.x+adjustSpaceX,local.y);
					break;
					case '\t':				//TAB
						final int width=(int)(getTabExpander().nextTabStop((float)(area.getX()+prevStringWidth),i)-prevStringWidth-area.getX());
						final int borda=2;
						final int x1=(int)(prevStringWidth+borda+area.getX());
						final int x2=x1+width-borda-borda;
						final int y=(int)(area.getY()+area.getHeight()/2);
						final Stroke grossura=imagemEdit2D.getStroke();
						imagemEdit2D.setStroke(new BasicStroke(1));
						imagemEdit2D.setColor(SimpleEditor.SPECIAL_CHARACTERS_COLOR);
						imagemEdit2D.drawLine(x1,y,x2,y);
						imagemEdit2D.fillPolygon(new int[]{x2,x2-4,x2-4},new int[]{y,y-3,y+4},3);
						imagemEdit2D.drawLine(x2,y-3,x2,y+3);
						sumOfTabs+=width;
						imagemEdit2D.setStroke(grossura);
					break;
					case '\n':case '\r':	//ENTER/CARRIER
						final int adjustEnterX=(fontMetrics.stringWidth(SimpleEditor.ENTER_SYMBOL)-fontMetricsDefault.stringWidth(SimpleEditor.ENTER_SYMBOL))/2;
						imagemEdit2D.setColor(SimpleEditor.SPECIAL_CHARACTERS_COLOR);
						imagemEdit2D.drawString(SimpleEditor.ENTER_SYMBOL,local.x+adjustEnterX,local.y);
					break;
				}
			}
		}
	}
}