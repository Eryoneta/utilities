package utilitarios.visual.text.java.mindmarkdown;
import java.awt.Font;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo.AtributeList;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMCitationAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMHighlightAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMItalicAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMLineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMListAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMSizeModifierAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMStrikeThroughAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMImageAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMLinkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.variable.MMTextStylerAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSubscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMSuperscriptAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMUnderlineAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MMAnchorAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMBoldAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.simple.MMHiddenAtributo;
@SuppressWarnings({"serial","unchecked"})
public class MindMarkDocumento extends DefaultStyledDocument{
//ATRIBUTO BY WORD
	public MMBoldAtributo bold=new MMBoldAtributo();
	public MMItalicAtributo italic=new MMItalicAtributo();
	public MMStrikeThroughAtributo strike_through=new MMStrikeThroughAtributo();
	public MMUnderlineAtributo underline=new MMUnderlineAtributo();
	public MMHighlightAtributo highlight=new MMHighlightAtributo();
	public MMHiddenAtributo hidden=new MMHiddenAtributo();
	public MMSuperscriptAtributo superscript=new MMSuperscriptAtributo();
	public MMSubscriptAtributo subscript=new MMSubscriptAtributo();
	public MMLinkAtributo link=new MMLinkAtributo();
	public MMTextStylerAtributo text_styler=new MMTextStylerAtributo();
//ATRIBUTO BY LINE
	public MMSizeModifierAtributo size_change=new MMSizeModifierAtributo();
//ATRIBUTO BY PARAGRAPH
	public MMCitationAtributo citation=new MMCitationAtributo();
	public MMLineAtributo line=new MMLineAtributo();
	public MMListAtributo list=new MMListAtributo();
	public MMAnchorAtributo anchor=new MMAnchorAtributo();
	public MMImageAtributo image=new MMImageAtributo();
//DEFAULT
	public MindMarkAtributo defaultFormat=new MindMarkAtributo();
	public MindMarkAtributo visible=new MindMarkAtributo();
	public MindMarkAtributo invisible=new MindMarkAtributo();
//SECTION
	public static class FormatSection{
	//START_TAG
		private int startTagIndex=-1;
			public int getStartTagIndex(){return startTagIndex;}
		private int startTagLength=-1;
			public int getStartTagLength(){return startTagLength;}
	//END_TAG
		private int endTagIndex=-1;
			public int getEndTagIndex(){return endTagIndex;}
		private int endTagLength=-1;
			public int getEndTagLength(){return endTagLength;}
	//MAIN
		public FormatSection(int startTagIndex,int startTagLength,int endTagIndex,int endTagLength){
			this.startTagIndex=startTagIndex;
			this.startTagLength=startTagLength;
			this.endTagIndex=endTagIndex;
			this.endTagLength=endTagLength;
		}
	}
//MAIN
	public MindMarkDocumento(){}
//FUNCS
	public void setFont(Font font){
		defaultFormat=new MindMarkAtributo(){{
			MindMarkAtributo.setDefaultFormat(this);
			StyleConstants.setFontFamily(this,font.getFamily());
			StyleConstants.setFontSize(this,font.getSize());
		}};
		visible=new MindMarkAtributo(){{
			MindMarkAtributo.setHiddenFormat(this,false);
			StyleConstants.setFontSize(this,font.getSize());
		}};
		invisible=new MindMarkAtributo(){{
			MindMarkAtributo.setHiddenFormat(this,true);
			StyleConstants.setFontSize(this,0);
		}};
		size_change.setDefaultFontSize(font.getSize());
	}
	public synchronized void setAllMarkdownToVisible(){
		setCharacterAttributes(0,getLength()+1,visible,false);
	}
	public synchronized void setAllMarkdownToInvisible(){
		setCharacterAttributes(0,getLength()+1,invisible,false);
	}
	public synchronized void clearAllMarkdown(){
		setCharacterAttributes(0,getLength()+1,defaultFormat,true);	//O '+1' LIMPA A ÚLTIMA LETRA
		setParagraphAttributes(0,getLength()+1,defaultFormat,true);	//O '+1' LIMPA A ÚLTIMA LINHA
	}
	public synchronized void updateAllMarkdown(int viewMode,int indexStart,int indexEnd){
		
//		System.out.println("MMD UPDATE!");
		
		clearAllMarkdown();
//		long timeIni=System.currentTimeMillis();
	//BY PARAGRAPH
		citation.applyStyle(this);			//>TEXTO,|>TEXTO
		line.applyStyle(this);				//===,=0=,=1=,=2=,...,=9=,=00=, =01=, =02=,...,=99=
		list.applyStyle(this);				//* TEXTO,+ TEXTO,- TEXTO,. TEXTO,  TEXTO		//ANTES DE BOLD
		anchor.applyStyle(this);			//:-TEXTO,:-TEXTO-:,TEXTO-:
	//TODO: IMAGE_ATRIBUTO
//		image.applyStyle(this);				//![TEXTO](LINK){FwWIDTHhHEIGHT}				//ANTES DE LINK, UNDERSCORE
	//BY WORD
		link.applyStyle(this);				//[TEXTO](LINK)									//ANTES DE UNDERSCORE, SIZE_MODIFIER, E TEXT_STYLER
		text_styler.applyStyle(this);		//:[TEXTO]{:FONTE#COR##COR###COR`@*´~_$%}		//ANTES DE SIZE_MODIFIER, UNDERSCORE
		highlight.applyStyle(this);			//`TEXTO`
		hidden.applyStyle(this);			//@TEXTO@
		bold.applyStyle(this);				//*TEXTO*
		italic.applyStyle(this);			//´TEXTO´
		strike_through.applyStyle(this);	//~TEXTO~
		underline.applyStyle(this);			//_TEXTO_
		superscript.applyStyle(this);		//$TEXTO$
		subscript.applyStyle(this);			//%TEXTO%
	//BY LINE
		size_change.applyStyle(this);		//#TEXTO,^TEXTO
		
		updateAllMarkdownVisibility(viewMode,indexStart,indexEnd);
		
//		System.out.println(System.currentTimeMillis()-timeIni);

	}
	public synchronized void updateAllMarkdownVisibility(int viewMode,int selecStart,int selecEnd){
		
//		System.out.println("MMD-SHOW UPDATE!");

		int index=selecStart;
		final Element rootElement=getDefaultRootElement();
		final int startElemIndex=rootElement.getElementIndex(index);
		int endElemIndex=rootElement.getElementIndex(selecEnd);
		if(endElemIndex<startElemIndex)endElemIndex=startElemIndex;
		switch(viewMode){
			case MindMarkTexto.SHOW_ALL_FORMAT:default:
				setAllMarkdownToVisible();
			break;
			case MindMarkTexto.SHOW_FORMAT_ON_LINE:
				setAllMarkdownToInvisible();
				for(int i=startElemIndex;i<=endElemIndex;i++){
					final Element paragraph=rootElement.getElement(i);
					final AtributeList<MindMarkDocumento.FormatSection>sections=
					(AtributeList<MindMarkDocumento.FormatSection>)paragraph.getAttributes().getAttribute(MindMarkAtributo.SECTION_FORMAT);
					if(sections!=null){
						for(MindMarkDocumento.FormatSection section:sections)setToVisible(section);
					}
				}
			break;
			case MindMarkTexto.SHOW_FORMAT_ON_WORD:
				setAllMarkdownToInvisible();
				for(int i=startElemIndex;i<=endElemIndex;i++){
					final Element paragraph=rootElement.getElement(i);
					final AtributeList<MindMarkDocumento.FormatSection>sections=
					(AtributeList<MindMarkDocumento.FormatSection>)paragraph.getAttributes().getAttribute(MindMarkAtributo.SECTION_FORMAT);
					if(sections!=null){
						for(MindMarkDocumento.FormatSection section:sections){
							final int formatStartIndex=section.getStartTagIndex();
							final int formatRealEndIndex=section.getEndTagIndex()+section.getEndTagLength();
						//TS SS-SE TE: O SELEC FICA ENTRE TAG_START E TAG_END
							if(formatStartIndex<=selecStart&&selecEnd<=formatRealEndIndex)setToVisible(section);
						//SS-TS-SE TE: A TAG_START FICA ENTRE SELEC_START E SELEC_END
							if(selecStart<=formatStartIndex&&formatStartIndex<=selecEnd&&selecEnd<=formatRealEndIndex)setToVisible(section);
						//TS SS-TE-SE: A TAG_END FICA ENTRE SELEC_START E SELEC_END
							if(formatStartIndex<=selecStart&&selecStart<=formatRealEndIndex&&formatRealEndIndex<=selecEnd)setToVisible(section);
						//SS-TS-TE-SE: A TAG_START E TAG_END FICAM ENTRE SELEC_START E SELEC_END
							if(selecStart<=formatStartIndex&&formatRealEndIndex<=selecEnd)setToVisible(section);
						}
					}
				}
			break;
			case MindMarkTexto.SHOW_FORMAT_ON_CHAR:
				setAllMarkdownToInvisible();
				for(int i=startElemIndex;i<=endElemIndex;i++){
					final Element paragraph=rootElement.getElement(i);
					final AtributeList<MindMarkDocumento.FormatSection>sections=
					(AtributeList<MindMarkDocumento.FormatSection>)paragraph.getAttributes().getAttribute(MindMarkAtributo.SECTION_FORMAT);
					if(sections!=null){
						for(MindMarkDocumento.FormatSection section:sections){
							final int formatStartIndex=section.getStartTagIndex();
							final int formatStartLength=section.getStartTagLength();
							final int formatEndIndex=section.getEndTagIndex();
							final int formatEndLength=section.getEndTagLength();
							final int formatRealEndIndex=section.getEndTagIndex()+section.getEndTagLength();
							final boolean hasStartTag=(formatStartLength>0);
							final boolean hasEndTag=(formatEndLength>0);
						//SS-TS-SE TE: A TAG_START FICA ENTRE SELEC_START E SELEC_END
							if(hasStartTag)if(selecStart<formatStartIndex&&formatStartIndex<selecEnd&&selecEnd<formatRealEndIndex)setToVisible(section);
						//TS SS-TE-SE: A TAG_END FICA ENTRE SELEC_START E SELEC_END
							if(hasEndTag)if(formatStartIndex<selecStart&&selecStart<formatRealEndIndex&&formatRealEndIndex<selecEnd)setToVisible(section);
						//SS-TS-TE-SE: A TAG_START E TAG_END FICAM ENTRE SELEC_START E SELEC_END
							if(hasStartTag&&hasEndTag)if(selecStart<formatStartIndex&&formatRealEndIndex<selecEnd)setToVisible(section);
						//SS-SE|TS TE: O SELEC_END FICA LOGO ANTES DE TAG_START
							if(hasStartTag)if(selecEnd>=formatStartIndex&&formatStartIndex+formatStartLength>=selecEnd)setToVisible(section);
						//TS|SS-SE TE: O SELEC_START FICA LOGO DEPOIS DE TAG_START
							if(hasStartTag)if(selecStart>=formatStartIndex&&formatStartIndex+formatStartLength>=selecStart)setToVisible(section);
						//TS SS-SE|TE: O SELEC_END FICA LOGO ANTES DE TAG_END
							if(hasEndTag)if(formatEndLength>0)if(selecEnd>=formatEndIndex&&formatEndIndex+formatEndLength>=selecEnd)setToVisible(section);
						//TS TE|SS-SE: O SELEC_START FICA LOGO DEPOIS DE TAG_END
							if(hasEndTag)if(selecStart>=formatEndIndex&&formatEndIndex+formatEndLength>=selecStart)setToVisible(section);
						}
					}
				}
			break;
			case MindMarkTexto.SHOW_NO_FORMAT:
				setAllMarkdownToInvisible();
			break;
		}
	}
		private void setToVisible(MindMarkDocumento.FormatSection section){
			setCharacterAttributes(section.getStartTagIndex(),section.getStartTagLength(),visible,false);
			setCharacterAttributes(section.getEndTagIndex(),section.getEndTagLength(),visible,false);
		}
	public String getText(){
		try{
			return getText(0,getLength());
		}catch(BadLocationException error){
			return "";
		}
	}
@Override
	public void setCharacterAttributes(int index,int length,AttributeSet attributes,boolean replace){
		if(length==0)return;	//NENHUM CHAR SELECIONADO
		try{
			writeLock();		//PARA TUDO
			final DefaultDocumentEvent evento=new DefaultDocumentEvent(index,length,DocumentEvent.EventType.CHANGE);
			buffer.change(index,length,evento);
			final boolean isDefaultFormat=attributes.containsAttribute(MindMarkAtributo.DEFAULT_FORMAT,true);
			final boolean isHiddenFormat=(attributes.containsAttribute(MindMarkAtributo.HIDDEN_FORMAT,true)||attributes.containsAttribute(MindMarkAtributo.HIDDEN_FORMAT,false));
			final int end=index+length;
			Element character;
			for(int pos=index;pos<end;pos=character.getEndOffset()){
				character=getCharacterElement(pos);
				if(pos==character.getEndOffset())break;
				final MutableAttributeSet charAtributos=(MutableAttributeSet)character.getAttributes();
				final boolean charIsSpecialFormat=charAtributos.containsAttribute(MindMarkAtributo.SPECIAL_FORMAT,true);
				if(isHiddenFormat&&!charIsSpecialFormat)continue;					//HIDDEN SÓ PODE AFETAR SPECIAL
				if(charIsSpecialFormat&&!isDefaultFormat&&!isHiddenFormat)continue;	//SPECIAL SÓ PODE SER AFETADO POR DEFAULT E HIDDEN
//				evento.addEdit(new AttributeUndoableEdit(character,attributes,replace));
				if(replace)charAtributos.removeAttributes(charAtributos);
				charAtributos.addAttributes(attributes);
			}
			fireChangedUpdate(evento);
//			fireUndoableEditUpdate(new UndoableEditEvent(this,evento));
		}finally{
			writeUnlock();
		}
	}
@Override
	public void setParagraphAttributes(int index,int length,AttributeSet attributes,boolean replace){
		try{
			writeLock();
			final DefaultDocumentEvent evento=new DefaultDocumentEvent(index,length,DocumentEvent.EventType.CHANGE);
			final Element rootElement=getDefaultRootElement();
			final int startElement=rootElement.getElementIndex(index);
			int endElement=rootElement.getElementIndex(index+length-1);
			if(endElement<startElement)endElement=startElement;
			for(int i=startElement;i<=endElement;i++){
				final Element paragraph=rootElement.getElement(i);
				final MutableAttributeSet paragrAtributos=(MutableAttributeSet)paragraph.getAttributes();
//				evento.addEdit(new AttributeUndoableEdit(par,attributes,replace));
				if(replace)paragrAtributos.removeAttributes(paragrAtributos);
				paragrAtributos.addAttributes(attributes);
			}
			fireChangedUpdate(evento);
//			fireUndoableEditUpdate(new UndoableEditEvent(this,evento));
		}finally{
			writeUnlock();
		}
	}
}