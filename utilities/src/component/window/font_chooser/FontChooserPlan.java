package component.window.font_chooser;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;

import architecture.rrf_vp.plan.PlanJoint;
import component.window.font_chooser.FontChooser.Option;

@SuppressWarnings("serial")
public class FontChooserPlan implements PlanJoint<FontChooserPlan, FontChooser> {
//VAR GLOBAIS
	protected static final Font DEFAULT_FONT=new Font("Arial",Font.PLAIN,12);
	protected static final String[]NAMES=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	protected static final int[]STYLES={
			Font.PLAIN,Font.BOLD,Font.ITALIC,Font.BOLD|Font.ITALIC
	};
	protected static final int[]SIZES={
			8,9,10,11,12,14,16,18,20,22,24,26,28,36,48,72
	};
//RESPOSTAS
	private Option resposta=Option.CANCEL;
		protected Option getResposta() {return resposta;}
//LISTENER: DISPARA COM O FOCO DE TEXTO
	protected class TextoFocusAction extends FocusAdapter{
	//TEXTO
		private JTextComponent texto;
	//MAIN
		public TextoFocusAction(JTextComponent texto){this.texto=texto;}
	//FUNCS
		public void focusGained(FocusEvent f){
			texto.selectAll();
		}
		public void focusLost(FocusEvent f){
			texto.select(0,0);
			getRoot().getView().updateExemplo();
		}
	}
//LISTENER: DISPARA COM O INPUT DE TECLAS
	protected class TextoKeyAction extends KeyAdapter{
	//LISTA
		private JList<String> lista;
	//MAIN
		public TextoKeyAction(JList<String> lista){this.lista=lista;}
	//FUNCS
		public void keyPressed(KeyEvent k){
			int i=lista.getSelectedIndex();
			switch(k.getKeyCode()){
				case KeyEvent.VK_UP:
					i=lista.getSelectedIndex()-1;
					if(i<0)i=0;
					lista.setSelectedIndex(i);
				break;
				case KeyEvent.VK_DOWN:
					int listaSize=lista.getModel().getSize();
					i=lista.getSelectedIndex()+1;
					if(i>=listaSize)i=listaSize-1;
					lista.setSelectedIndex(i);
				break;
			}
		}
	}
//LISTENER: DISPARA COM A EDIÇÃO DE TEXTO
	protected class ListDocumentAction implements DocumentListener{
	//LISTA
		private JList<String> lista;
	//MAIN
		public ListDocumentAction(JList<String> lista){this.lista=lista;}
	//FUNCS
		public void insertUpdate(DocumentEvent d){update(d);}
		public void removeUpdate(DocumentEvent d){update(d);}
		public void changedUpdate(DocumentEvent d){update(d);}
		private void update(DocumentEvent d){
			String texto="";
			try{
				final Document doc=d.getDocument();
				texto=doc.getText(0,doc.getLength());
			}catch(BadLocationException erro){
				//PROVAVELMENTE NÃO DEVE OCORRER
			}
			if(texto.length()>0){
				int index=lista.getNextMatch(texto,0,Position.Bias.Forward);
				if(index<0)index=0;
				lista.ensureIndexIsVisible(index);
				final String matchedName=lista.getModel().getElementAt(index).toString();
				if(texto.equalsIgnoreCase(matchedName)){
					if(index!=lista.getSelectedIndex()){
						SwingUtilities.invokeLater(new ListSelector(index));
					}
				}
			}
		}
		public class ListSelector implements Runnable{
		//INDEX
			private int index;
		//MAIN
			public ListSelector(int index){this.index=index;}
		//FUNCS
			public void run(){lista.setSelectedIndex(this.index);}
		}
	}
//LISTENER: DISPARA COM A SELEÇÃO DE ITEM
	protected class ListSelectionAction implements ListSelectionListener{
	//TEXTO
		private JTextComponent texto;
	//MAIN
		public ListSelectionAction(JTextComponent texto){this.texto=texto;}
	//FUNCS
		public void valueChanged(ListSelectionEvent l){
			if(l.getValueIsAdjusting()==false){
				@SuppressWarnings("unchecked")
				final JList<String> lista=(JList<String>)l.getSource();
				final String selecTexto=(String)lista.getSelectedValue();
				final String selecOldTexto=texto.getText();
				try {
					texto.setText(selecTexto);
				}catch (Exception error) {
					//IMPEDE ERRO DE SET_INDEX(0) ATUALIZAR TEXTO... ENQUANTO ESTE ESTÁ SENDO GERADO...?
				}
				if(!selecOldTexto.equalsIgnoreCase(selecTexto)){
					texto.selectAll();
					texto.requestFocus();
				}
				getRoot().getView().updateExemplo();
			}
		}
	}
//LISTENER: DISPARA COM AÇÃO NO BOTÃO OK
	protected class OKButtonAction extends AbstractAction{
	//VAR GLOBAIS
		private JDialog dialog;
	//MAIN
		protected OKButtonAction(JDialog dialog, String text){
			this.dialog=dialog;
			putValue(Action.DEFAULT,text);
			putValue(Action.ACTION_COMMAND_KEY,text);
			putValue(Action.NAME,text);
		}
	//FUNCS
		public void actionPerformed(ActionEvent a){
			resposta=Option.APPROVE;
			dialog.dispose();
		}
	}
//LISTENER: DISPARA COM AÇÃO NO BOTÃO CANCELAR
	protected class CancelButtonAction extends AbstractAction{
	//VAR GLOBAIS
		private JDialog dialog;
	//MAIN
		protected CancelButtonAction(JDialog dialog, String text){
			this.dialog=dialog;
			putValue(Action.DEFAULT,text);
			putValue(Action.ACTION_COMMAND_KEY,text);
			putValue(Action.NAME,text);
		}
	//FUNCS
		public void actionPerformed(ActionEvent a){
			resposta=Option.CANCEL;
			dialog.dispose();
		}
	}
//LISTENER: DISPARA COM O FECHAR DA JANELA
	protected class WindowClosingAction extends WindowAdapter{
	//MAIN
		protected WindowClosingAction(){}
	//FUNCS
		public void windowClosing(WindowEvent w){
			if(resposta==Option.ERROR)return;
			resposta=Option.CANCEL;
		}
	}
//ROOT
	private FontChooser root;
		@Override public FontChooser getRoot() {return root;}
//MAIN
	public FontChooserPlan(FontChooser root) {
		this.root=root;
	}
//FUNCS
	@Override
	public void init() {}
	public Font getSelectedFont(){
		return new Font(
				getRoot().getView().getSelectedNome(),
				getRoot().getView().getSelectedEstilo(),
				getRoot().getView().getSelectedTamanho());
	}
	public void setSelectedFont(Font fonte){
		getRoot().getView().setSelectedNome(fonte.getFamily());
		getRoot().getView().setSelectedEstilo(fonte.getStyle());
		getRoot().getView().setSelectedTamanho(fonte.getSize());
	}
}