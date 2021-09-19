package element.tree.texto;
import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import element.tree.propriedades.Cor;
@SuppressWarnings("serial")
public class CustomEditor extends StyledEditorKit{
//QUEBRA DE LINHA
	private boolean lineWrappable=false;	//NECESSÁRIO PARA DEFINIR SE A LARGURA DEVE OU NÃO EXPANDIR COM O NO_LINE_WRAP
		public boolean isLineWrappable(){return lineWrappable;}
		public void setLineWrappable(boolean wrappable){lineWrappable=wrappable;}
	private boolean lineWrap=false;
		public boolean isLineWrapped(){return lineWrap;}
		public void setLineWrap(boolean wrap){lineWrap=wrap;}
//CARACTERES INVISÍVEIS
	private boolean viewAllChars=false;
		public boolean isAllCharsVisible(){return viewAllChars;}
		public void setViewAllChars(boolean view){viewAllChars=view;}
//TAMANHO DO TAB
	private int tabSize=40;
		public int getTabSize(){return tabSize;}
		public void setTabSize(int size){tabSize=size;}
//<GRADE_CUSTOM>
	private class CustomColumnFactory implements ViewFactory{
	//<CARACTER_CUSTOM>
		private class CustomLabelView extends LabelView{
		//MAIN
			public CustomLabelView(javax.swing.text.Element elem){super(elem);}
		//FUNCS
		@Override
			public float getMinimumSpan(int axis){	//APLICA LINE_WRAP EM PALAVRAS GRANDES, AS QUEBRANDO 
				switch(axis){
					case View.X_AXIS:	return (isLineWrappable()&&isLineWrapped()?0:super.getMinimumSpan(axis));
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
				if(isAllCharsVisible()){
					final String spaceSymbol="∙";
					final String enterSymbol="¶";
					final FontMetrics fontMetrics=imagemEdit2D.getFontMetrics();
					final String texto=getText(getStartOffset(),getEndOffset()).toString();
					final Rectangle area=((caracter instanceof Rectangle)?(Rectangle)caracter:caracter.getBounds());
					final Cor cor=new Cor(93,157,125);
					int sumOfTabs=0;
					for(int i=0;i<texto.length();i++){
						final int prevStringWidth=fontMetrics.stringWidth(texto.substring(0,i))+sumOfTabs;
						final char letra=texto.charAt(i);
						imagemEdit2D.setFont(getFont());
						final Point local=new Point((int)(prevStringWidth+area.getX()),(int)(area.getY()+area.getHeight()-4));
						switch(letra){
							case ' ':				//ESPAÇO
								imagemEdit2D.setColor(cor);
								imagemEdit2D.drawString(spaceSymbol,local.x,local.y);
							break;
							case '\t':				//TAB
								final int width=(int)(getTabExpander().nextTabStop((float)(area.getX()+prevStringWidth),i)-prevStringWidth-area.getX());
								final int borda=2;
								final int x1=(int)(prevStringWidth+borda+area.getX());
								final int x2=x1+width-borda-borda;
								final int y=(int)(area.getY()+area.getHeight()/2);
								final Stroke grossura=imagemEdit2D.getStroke();
								imagemEdit2D.setStroke(new BasicStroke(1));
								imagemEdit2D.setColor(cor);
								imagemEdit2D.drawLine(x1,y,x2,y);
								imagemEdit2D.fillPolygon(new int[]{x2,x2-4,x2-4},new int[]{y,y-3,y+4},3);
								imagemEdit2D.drawLine(x2,y-3,x2,y+3);
								sumOfTabs+=width;
								imagemEdit2D.setStroke(grossura);
							break;
							case '\n':case '\r':	//ENTER/CARRIER
								imagemEdit2D.setColor(cor);
								imagemEdit2D.drawString(enterSymbol,local.x,local.y);
							break;
						}
					}
				}
			}
		}
	//</CARACTER_CUSTOM>
	//<PARÁGRAFO_CUSTOM>
		private class CustomParagraphView extends ParagraphView{
		//MAIN
			public CustomParagraphView(Element elem){super(elem);}
		//FUNCS
		@Override
			public float nextTabStop(float x,int tabOffset){	//MUDA O TAMANHO DO TAB
				if(getTabSet()==null){
					return (float)(getTabBase()+(((int)x/getTabSize()+1)*getTabSize()));
				}else return super.nextTabStop(x,tabOffset);
			}
		@Override
			public void layout(int width,int height){	//DEFINE SE A LINHA É WRAPPED OU NÃO
				if(!isLineWrappable()||isLineWrapped()){
					super.layout(width,height);
				}else super.layout(Short.MAX_VALUE,height);
			}
		@Override
			public float getMinimumSpan(int axis){		//CONTROLA A LARGURA DA LINHA
				if(!isLineWrappable()||isLineWrapped()){
					return super.getMinimumSpan(axis);
				}else return super.getPreferredSpan(axis);
			}
		}
	//</PARÁGRAFO_CUSTOM>
	//FUNCS
	@Override
		public View create(javax.swing.text.Element elem){
			final String kind=elem.getName();
			if(kind!=null){
				switch(kind){
					case AbstractDocument.ContentElementName:	return new CustomLabelView(elem);
					case AbstractDocument.ParagraphElementName:	return new CustomParagraphView(elem);
					case AbstractDocument.SectionElementName:	return new BoxView(elem,View.Y_AXIS);
					case StyleConstants.ComponentElementName:	return new ComponentView(elem);
					case StyleConstants.IconElementName:		return new IconView(elem);
				}
			}
			return new CustomLabelView(elem);
		}
	}
//</GRADE_CUSTOM>
	private final CustomColumnFactory columnFactory=new CustomColumnFactory();
@Override
	public ViewFactory getViewFactory(){return columnFactory;}
}
