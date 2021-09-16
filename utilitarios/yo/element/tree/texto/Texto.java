package element.tree.texto;
import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
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
import javax.swing.undo.UndoManager;
import element.tree.propriedades.Cor;
import element.tree.TreeUI;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.modulo.Modulo;
@SuppressWarnings("serial")
public class Texto extends JTextPane{
//OBJETO
	private Objeto obj;
		public Objeto getObjeto(){return obj;}
		public void setObjeto(Objeto obj){
			triggerObjetoSetListener(this.obj,obj);
			this.obj=obj;
		}
		public void clearObjeto(){setObjeto(null);}
//LISTENER: DISPARA COM O MUDAR DE OBJETO
	private List<ObjetoSetListener>objetoSetListeners=new ArrayList<ObjetoSetListener>();
		public void addObjetoSetListener(ObjetoSetListener objetoSetListener){objetoSetListeners.add(objetoSetListener);}
		public void triggerObjetoSetListener(Objeto oldObj,Objeto newObj){
			for(ObjetoSetListener objetoSetListener:objetoSetListeners)objetoSetListener.objetoModified(oldObj,newObj);
		}
//UNDO-REDO
	private UndoManager undoManager;
		public UndoManager getUndoManager(){return undoManager;}
		public void setUndoManager(UndoManager undoManager){
			this.undoManager=undoManager;
			getDocument().addUndoableEditListener(undoManager);
		}
		public boolean undo(){
			final boolean undo=getUndoManager().canUndo();
			if(undo)getUndoManager().undo();
			return undo;
		}
		public boolean redo(){
			final boolean redo=getUndoManager().canRedo();
			if(redo)getUndoManager().redo();
			return redo;
		}
		public void crearUndo(){undoManager.discardAllEdits();}
		public void setUndoLimit(int limite){undoManager.setLimit(limite);}
//EDITOR CUSTOM
//<EDITOR_CUSTOM>
	private class CustomEditor extends StyledEditorKit{
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
//</EDITOR_CUSTOM>
	private CustomEditor editor=new CustomEditor();
		public boolean isLineWrappable(){return editor.isLineWrappable();}
		public void setLineWrappable(boolean wrappable){editor.setLineWrappable(wrappable);}
		public boolean isLineWrapped(){return editor.isLineWrapped();}
		public void setLineWrap(boolean wrap){
			editor.setLineWrap(wrap);
			setText(getText());		//ATUALIZA O TEXTO
		}
		public boolean isAllCharsVisible(){return editor.isAllCharsVisible();}
		public void setViewAllChars(boolean hide){
			editor.setViewAllChars(hide);
			repaint();				//ATUALIZA O VISUAL
		}
//MAIN
	public Texto(){
		final Cor cor=Cor.getChanged(TreeUI.FUNDO,0.7f);
		setSelectionColor(cor);
		setSelectedTextColor(cor.isDark()?TreeUI.Fonte.LIGHT:TreeUI.Fonte.DARK);
		setEditorKit(editor);
		editor.setTabSize(20);
		setUndoManager(new UndoManager());
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent k){
				final boolean ctrl=(k.isControlDown());
				final boolean shift=(k.isShiftDown());
				if(ctrl){
					switch(k.getKeyCode()){
						case KeyEvent.VK_Z:		undo();				break;	//DESFAZER
						case KeyEvent.VK_Y:		redo();				break;	//REFAZER
						case KeyEvent.VK_D:		deleteLine();		break;	//REMOVER LINHA
					};
				}else if(shift){
					switch(k.getKeyCode()){
						case KeyEvent.VK_TAB:	remTab();			break;	//REMOVE TAB
					};
				}else{
					switch(k.getKeyCode()){
						case KeyEvent.VK_TAB:	addTab(k);			break;	//ADICIONAR TAB
						case KeyEvent.VK_ENTER:	addTabbedEnter(k);	break;	//ADICIONAR TAB E ENTER
					};
				}
			}
		});
		addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent c){
				if(getObjeto()!=null){
					if(getObjeto().getTipo().is(Objeto.Tipo.MODULO)){
						final Modulo mod=(Modulo)getObjeto();
						mod.setCaret(c.getDot());
					}else if(getObjeto().getTipo().is(Objeto.Tipo.CONEXAO)){
						final Conexao cox=(Conexao)getObjeto();
						cox.setCaret(c.getDot());
					}
				}
			}
		});
		setVisible(true);
	}
//FUNCS
	public void addEditorListener(DocumentListener listener){getDocument().addDocumentListener(listener);}
	public void deleteLine(){
		try{
			final int ini=getSelectionStart();
			int end=getSelectionEnd();
			final List<Element>linhas=new ArrayList<>();
			int i=ini;
			do{
				final Element linha=getStyledDocument().getParagraphElement(i);
				linhas.add(linha);
				i=linha.getEndOffset();
			}while(i<end);
			for(Element linha:linhas){
				getDocument().remove(linha.getStartOffset(),linha.getEndOffset()-linha.getStartOffset());
			}
		}catch(BadLocationException erro){}
	}
	public void addTab(KeyEvent k){
		try{
			final int ini=getSelectionStart();
			int end=getSelectionEnd();
			final List<Integer>tabs=new ArrayList<>();
			for(int i=ini;i<end;){
				final Element linha=getStyledDocument().getParagraphElement(i);
				tabs.add(linha.getStartOffset()+tabs.size());
				i=linha.getEndOffset();
			}
			if(tabs.size()<=1)return;	//IGNORA SE APENAS 1 LINHA
			for(int tab:tabs){
				getDocument().insertString(tab,"\t",null);
			}
			k.consume();
		}catch(BadLocationException erro){}
	}
	public void remTab(){
		try{
			final int ini=getSelectionStart();
			int end=getSelectionEnd();
			for(int i=ini;i<end;){
				final Element linha=getStyledDocument().getParagraphElement(i);
				final int start=linha.getStartOffset();
				if(getDocument().getText(start,1).equals("\t")){
					getDocument().remove(start,1);
					end--;	//RECUA COM OS TABS
				}
				i=linha.getEndOffset();
			}
		}catch(BadLocationException erro){}
	}
	private void addTabbedEnter(KeyEvent k){
		try{
			final int cursor=getCaretPosition();
			final Element linha=getStyledDocument().getParagraphElement(cursor);
			final int start=linha.getStartOffset();
			int caretQtd=0;
			for(int i=0;true;i++){
				if(getDocument().getText(start+i,1).equals("\t")){
					caretQtd++;
				}else break;
			}
			getDocument().insertString(cursor,"\n",null);
			for(int i=0;i<caretQtd;i++)getDocument().insertString(cursor+1,"\t",null);
			k.consume();
		}catch(BadLocationException erro){}
	}
//DRAW
@Override
	public void paint(Graphics imagemEdit){
	//CONFIG TEXTO
		final Rectangle area=imagemEdit.getClipBounds();
		final BufferedImage buffer=new BufferedImage(area.width,area.height,BufferedImage.TYPE_INT_RGB);	//A ÚNICA FORMA DE LCD FUNCIONAR
		final Graphics2D bufferEdit=(Graphics2D)buffer.getGraphics();
		bufferEdit.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);	//ADICIONA BORDA, CONTRASTE
		bufferEdit.translate(-area.x,-area.y);	//ALINHA O QUE SEGUIR COM A JANELA
	//TEXTO
		super.paint(bufferEdit);
	//END
		imagemEdit.drawImage(buffer,area.x,area.y,null);
	}
}