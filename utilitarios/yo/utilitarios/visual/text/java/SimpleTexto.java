package utilitarios.visual.text.java;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;
@SuppressWarnings("serial")
public class SimpleTexto extends JTextPane{
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
	protected SimpleEditor editor;
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
	public SimpleTexto(){
		setEditorKit(new SimpleEditor());
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
		setVisible(true);
	}
	public void setEditorKit(SimpleEditor editor){
		this.editor=editor;
		super.setEditorKit(editor);
		editor.setTabSize(20);
	}
//FUNCS
	public void deleteLine(){
		try{
			final int selecStart=getSelectionStart();
			final int selecEnd=getSelectionEnd();
			final int startDel=getStyledDocument().getParagraphElement(selecStart).getStartOffset();
			int endDel=getStyledDocument().getParagraphElement(selecEnd).getEndOffset();
			if(endDel>=getStyledDocument().getLength())endDel--;
		//DELETA LINHAS
			getDocument().remove(startDel,endDel-startDel);
		}catch(BadLocationException erro){}
	}
	public void addTab(KeyEvent k){
		try{
			final int selecStart=getSelectionStart();
			int selecEnd=getSelectionEnd();
			final List<Integer>tabs=new ArrayList<>();
		//PARA CADA LINHA
			int prevIndex=-1;
			int index=selecStart;
			do{
				final Element linha=getStyledDocument().getParagraphElement(index);
			//ANOTA O INDEX DE SEU INÍCIO
				tabs.add(linha.getStartOffset()+tabs.size());
				prevIndex=index;
				index=linha.getEndOffset();
			}while(index<selecEnd&&index!=prevIndex);
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
			int prevIndex=-1;
			int index=selecStart;
			do{
				final Element linha=getStyledDocument().getParagraphElement(index);
				final int start=linha.getStartOffset();
			//REMOVE TAB
				if(getDocument().getText(start,1).equals("\t")){
					getDocument().remove(start,1);
					selecEnd--;	//RECUA COM OS TABS
				}
				prevIndex=index;
				index=linha.getEndOffset()+1;
			}while(index<selecEnd&&index!=prevIndex);
		}catch(BadLocationException erro){}
	}
	private void addTabbedEnter(KeyEvent k){
		try{
		//CONTA TABS A ADICIONAR
			int cursor=getCaretPosition();
			final Element linha=getStyledDocument().getParagraphElement(cursor);
			final int start=linha.getStartOffset();
			int tabQtd=0;
			for(int i=0;i<linha.getEndOffset()+1;i++){
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