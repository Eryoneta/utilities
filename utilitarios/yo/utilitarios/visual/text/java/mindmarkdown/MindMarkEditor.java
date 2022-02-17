package utilitarios.visual.text.java.mindmarkdown;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.ViewFactory;
import utilitarios.visual.text.java.SimpleEditor;
import utilitarios.visual.text.java.SimpleTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
import utilitarios.visual.text.java.mindmarkdown.view.MMViewFactory;
@SuppressWarnings("serial")
public class MindMarkEditor extends SimpleEditor{
//INTERFACE
	private final MMViewFactory viewFactory=new MMViewFactory(this);
	@Override
		public ViewFactory getViewFactory(){return viewFactory;}
//TEXTO
	private MindMarkTexto editorTexto;
//DOCUMENTO
	private MindMarkDocumento documento=new MindMarkDocumento();
		public MindMarkDocumento getMindMarkDocumento(){return documento;}
//SECTION
	public class Section{
	//INDEXES
		private int index1=0;
			public int getIndex1(){return index1;}
		private int index2=0;
			public int getIndex2(){return index2;}
	//SHOW_MODE
		private int showMode=0;
			public int getShowMode(){return showMode;}
	//MAIN
		public Section(int index1,int index2,int showMode){
			this.index1=index1;
			this.index2=index2;
			this.showMode=showMode;
		}
	}
//MAIN
	public MindMarkEditor(MindMarkTexto editorTexto){
		this.editorTexto=editorTexto;
	}
	public void build(){
		editorTexto.setDocument(documento);
//		editorTexto.getDocument().addDocumentListener(new DocumentListener(){
//		@Override public void removeUpdate(DocumentEvent d){update();}
//		@Override public void insertUpdate(DocumentEvent d){update();}
//		@Override public void changedUpdate(DocumentEvent d){}
//			private void update(){
//				SwingUtilities.invokeLater(new Runnable(){
//				@Override
//					public void run(){
//						updateMarkdown();
//					}
//				});
//			}
//		});
		editorTexto.addCaretListener(new CaretListener(){
		@Override
			public void caretUpdate(CaretEvent c){
				final int indexStart=Math.min(c.getDot(),c.getMark());
				final int indexEnd=Math.max(c.getDot(),c.getMark());
				if(indexStart>=0)update(indexStart,indexEnd);
			}
			private void update(int indexStart,int indexEnd){
				SwingUtilities.invokeLater(new Runnable(){
				@Override
					public void run(){
						final MMUndoManager undoRedo=(MMUndoManager)((SimpleTexto)editorTexto).getUndoManager();
						undoRedo.setDisabled(true);
						documento.clearMarkdown();
						setHighlightedParagraph();
						documento.updateMarkdown();
						undoRedo.setDisabled(false);
					}
					private void setHighlightedParagraph(){
						final int viewMode=editorTexto.getFormatViewMode();
						final MindMarkDocumento doc=(MindMarkDocumento)editorTexto.getDocument();
						final MindMarkAtributo atributoFormat=new MindMarkAtributo();
						final int length=indexEnd-indexStart;
						switch(viewMode){
							case MindMarkTexto.SHOW_ALL_FORMAT:default:
								MindMarkAtributo.setShowFormat(atributoFormat,new Section(0,doc.getLength(),MindMarkTexto.SHOW_ALL_FORMAT));
								doc.setParagraphAttributes(0,doc.getLength(),atributoFormat,false);	//TUDO
							break;
							case MindMarkTexto.SHOW_FORMAT_ON_LINE:
								MindMarkAtributo.setShowFormat(atributoFormat,new Section(indexStart,indexEnd,MindMarkTexto.SHOW_FORMAT_ON_LINE));
								doc.setParagraphAttributes(indexStart,length,atributoFormat,false);	//APENAS LINHA
							break;
							case MindMarkTexto.SHOW_FORMAT_ON_WORD:
								MindMarkAtributo.setShowFormat(atributoFormat,new Section(indexStart,indexEnd,MindMarkTexto.SHOW_FORMAT_ON_WORD));
								doc.setParagraphAttributes(indexStart,length,atributoFormat,false);	//APENAS PALAVRA
							break;
							case MindMarkTexto.SHOW_FORMAT_ON_CHAR:
								MindMarkAtributo.setShowFormat(atributoFormat,new Section(indexStart,indexEnd,MindMarkTexto.SHOW_FORMAT_ON_CHAR));
								doc.setParagraphAttributes(indexStart,length,atributoFormat,false);	//APENAS LETRA
							break;
							case MindMarkTexto.SHOW_NO_FORMAT:
								//NADA
							break;
						}
					}
				});
			}
		});
	}
}