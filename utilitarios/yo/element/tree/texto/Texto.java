package element.tree.texto;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;
import element.tree.propriedades.Cor;
import element.tree.main.TreeUI;
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
			final int selecStart=getSelectionStart();
			int selecEnd=getSelectionEnd();
			final List<Element>linhas=new ArrayList<>();
			int i=selecStart;
		//PARA CADA LINHA
			do{
				final Element linha=getStyledDocument().getParagraphElement(i);
			//O ANOTA PARA DEL
				linhas.add(linha);
				i=linha.getEndOffset();
			}while(i<selecEnd);
		//DELETA LINHAS
			for(Element linha:linhas){
				getDocument().remove(linha.getStartOffset(),linha.getEndOffset()-linha.getStartOffset());
			}
		}catch(BadLocationException erro){}
	}
	public void addTab(KeyEvent k){
		try{
			final int selecStart=getSelectionStart();
			int selecEnd=getSelectionEnd();
			final List<Integer>tabs=new ArrayList<>();
		//PARA CADA LINHA
			for(int i=selecStart;i<selecEnd;){
				final Element linha=getStyledDocument().getParagraphElement(i);
			//ANOTA O INDEX DE SEU INÍCIO
				tabs.add(linha.getStartOffset()+tabs.size());
				i=linha.getEndOffset();
			}
			if(tabs.size()<=1)return;	//IGNORA SE APENAS 1 LINHA
		//ADICIONA TABS
			for(int tab:tabs)getDocument().insertString(tab,"\t",null);
		//IGNORA TAB
			k.consume();
		}catch(BadLocationException erro){}
	}
	public void remTab(){
		try{
			final int selecStart=getSelectionStart();
			int selecEnd=getSelectionEnd();
		//PARA CADA LINHA
			for(int i=selecStart;i<selecEnd;){
				final Element linha=getStyledDocument().getParagraphElement(i);
				final int start=linha.getStartOffset();
			//REMOVE TAB
				if(getDocument().getText(start,1).equals("\t")){
					getDocument().remove(start,1);
					selecEnd--;	//RECUA COM OS TABS
				}
				i=linha.getEndOffset();
			}
		}catch(BadLocationException erro){}
	}
	private void addTabbedEnter(KeyEvent k){
		try{
		//CONTA TABS A ADICIONAR
			int cursor=getCaretPosition();
			final Element linha=getStyledDocument().getParagraphElement(cursor);
			final int start=linha.getStartOffset();
			int tabQtd=0;
			for(int i=0;true;i++){
				if(getDocument().getText(start+i,1).equals("\t")){
					tabQtd++;
				}else break;
			}
		//REMOVE LINHAS SELECIONADAS
			final int selecStart=getSelectionStart();
			final int selecEnd=getSelectionEnd();
			final int selecLength=selecEnd-selecStart;
			if(selecLength>0)getDocument().remove(selecStart,selecLength);
		//ADICIONA ENTER E TABS
			cursor=getCaretPosition();
			getDocument().insertString(cursor,"\n",null);
			for(int i=0;i<tabQtd;i++)getDocument().insertString(cursor+1,"\t",null);
		//IGNORA ENTER
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