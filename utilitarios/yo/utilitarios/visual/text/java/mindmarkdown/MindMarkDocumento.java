package utilitarios.visual.text.java.mindmarkdown;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkSizeModifierAtributo;
@SuppressWarnings("serial")
public class MindMarkDocumento extends DefaultStyledDocument{
//VAR STATICS
	public final static MindMarkAtributo DEFAULT=new MindMarkAtributo();
	public static MindMarkAtributo INVISIBLE=new MindMarkAtributo(){{
		StyleConstants.setFontSize(this,0);
	}};
	public static MindMarkAtributo SPECIAL=new MindMarkAtributo(){{
		StyleConstants.setFontFamily(this,MindMarkEditor.DEFAULT_FONT.getFamily());
		StyleConstants.setForeground(this,MindMarkEditor.SPECIAL_CHARACTERS_COLOR);
	}};
//VAR STATICS: FORMAT
	public static MindMarkAtributo BOLD=new MindMarkAtributo(){{
		addDefinition(MindMarkAtributo.getDefinitionByWord("*","*"));
		addDefinition(MindMarkAtributo.getDefinitionByMultiline("***","***"));
		StyleConstants.setBold(this,true);
	}};
	public static MindMarkAtributo ITALIC=new MindMarkAtributo(){{
		addDefinition(MindMarkAtributo.getDefinitionByWord("´","´"));
		addDefinition(MindMarkAtributo.getDefinitionByMultiline("´´´","´´´"));
		StyleConstants.setItalic(this,true);
	}};
	public static MindMarkAtributo STRIKE_THROUGH=new MindMarkAtributo(){{
		addDefinition(MindMarkAtributo.getDefinitionByWord("~","~"));
		addDefinition(MindMarkAtributo.getDefinitionByMultiline("~~~","~~~"));
		StyleConstants.setStrikeThrough(this,true);
	}};
	public static MindMarkAtributo UNDERLINE=new MindMarkAtributo(){{
		addDefinition(MindMarkAtributo.getDefinitionByWord("_","_"));
		addDefinition(MindMarkAtributo.getDefinitionByMultiline("___","___"));
		StyleConstants.setUnderline(this,true);
	}};
	public static MindMarkAtributo HIGHLIGHT=new MindMarkAtributo(){{
		addDefinition(MindMarkAtributo.getDefinitionByWord("`","`"));
		addDefinition(MindMarkAtributo.getDefinitionByMultiline("```","```"));
		StyleConstants.setFontFamily(this,"Courier New");
		StyleConstants.setBackground(this,new Color(220,220,220));
	}};
	public static MindMarkSizeModifierAtributo SIZE_CHANGE=new MindMarkSizeModifierAtributo(){{
		addIncreaseDefinition(MindMarkAtributo.getDefinitionByLine("#"));
		setBiggerSizeModifiers(1.5f,2.5f,4.0f);
		addDecreaseDefinition(MindMarkAtributo.getDefinitionByLine("^"));
		setSmallerSizeModifiers(0.7f,0.4f,0.2f);
	}};
	public static MindMarkAtributo CITATION=new MindMarkAtributo(){{
		addDefinition(MindMarkAtributo.getDefinitionByWholeLine(">"));
		StyleConstants.setBackground(this,new Color(220,220,220));
	}};
//VAR STATICS: TABLE
	public static final String TableElementName="table";
	public static final String RowElementName="row";
	public static final String CellElementName="cell";
	public static final String CellParamWidth="cell-width";
	public static final String ParagraphCitationElementName="paragraph-citation";
//MAIN
	public MindMarkDocumento(){}
//FUNCS
	public void updateMarkdown(){
		setCharacterAttributes(0,getLength(),MindMarkDocumento.DEFAULT,true);
		setParagraphAttributes(0,getLength(),MindMarkDocumento.DEFAULT,true);
		styleText(MindMarkDocumento.BOLD);
		styleText(MindMarkDocumento.ITALIC);
		styleText(MindMarkDocumento.STRIKE_THROUGH);
		styleText(MindMarkDocumento.UNDERLINE);
		styleText(MindMarkDocumento.HIGHLIGHT);
		styleTextSize(MindMarkDocumento.SIZE_CHANGE);
		styleParagraph(MindMarkDocumento.CITATION);
	}
		private void styleText(MindMarkAtributo atributo){
			for(String definition:atributo.getDefinitions()){
				String texto="";
				try{
					texto=getText(0,getLength());
				}catch(BadLocationException error){}
				final Matcher match=Pattern.compile(definition).matcher(texto);
				while(match.find()){
				//TAG INI
					final int indexTagIni=match.start(1);
					final int lengthTagIni=match.group(1).length();
					setCharacterAttributes(indexTagIni,lengthTagIni,MindMarkDocumento.SPECIAL,true);
				//TEXTO
					final int indexTexto=match.start(2);
					final int lengthTexto=match.group(2).length();
					setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
				//TAG FIM
					final int indexTagFim=match.start(3);
					final int lengthTagFim=match.group(3).length();
					setCharacterAttributes(indexTagFim,lengthTagFim,MindMarkDocumento.SPECIAL,true);
				}
			}
		}
		private void styleTextSize(MindMarkSizeModifierAtributo atributoSize){
			String texto="";
			try{
				texto=getText(0,getLength());
			}catch(BadLocationException error){}
			final Matcher matchLinha=Pattern.compile(".*").matcher(texto);
			while(matchLinha.find()){					//SEGUE PARA CADA LINHA
				final List<Match>matches=new ArrayList<>();
				int indexAdjust=matchLinha.start();		//AJUSTA O INDEX(INDEX_ABSOLUTE+INDEX_OF_MATCH)
				for(String definition:atributoSize.getIncreaseDefinitions()){
					findMatch(matches,indexAdjust,matchLinha.group(),definition,true); 
				}
				indexAdjust=matchLinha.start();			//AJUSTA O INDEX(INDEX_ABSOLUTE+INDEX_OF_MATCH)
				for(String definition:atributoSize.getDecreaseDefinitions()){
					findMatch(matches,indexAdjust,matchLinha.group(),definition,false); 
				}
				Collections.sort(matches);
				int level=0;
				for(Match match:matches){
					level+=(match.increase?1:-1);
					if(level<atributoSize.getMinimumLevel()){
						level=atributoSize.getMinimumLevel();
						continue;
					}
					if(level>atributoSize.getMaximumLevel()){
						level=atributoSize.getMaximumLevel();
						continue;
					}
				//TAG INI
					setCharacterAttributes(match.indexTag,match.lengthTag,MindMarkDocumento.SPECIAL,true);
				//TEXTO
					atributoSize.updateFontSize(level);		//MODIFICA SIZE
					setCharacterAttributes(match.indexTexto,match.lengthTexto,atributoSize,false);
				}
			}
		}
			private void findMatch(List<Match>matches,int indexAdjust,String texto,String definition,boolean increase){
				final Matcher match=Pattern.compile(definition).matcher(texto);
				while(match.find()){
					final int indexTag=indexAdjust+match.start(1);
					final int lengthTag=match.group(1).length();
					final int indexTexto=indexAdjust+match.start(2);
					final int lengthTexto=match.group(2).length();
					matches.add(new Match(indexTag,lengthTag,indexTexto,lengthTexto,increase));
					findMatch(matches,indexTexto,match.group(2),definition,increase);
				}
			}
			private class Match implements Comparable<Match>{
				private int indexTag=0;
				private int lengthTag=0;
				private int indexTexto=0;
				private int lengthTexto=0;
				private boolean increase=false;
				public Match(int indexTag,int lengthTag,int indexTexto,int lengthTexto,boolean increase){
					this.indexTag=indexTag;
					this.lengthTag=lengthTag;
					this.indexTexto=indexTexto;
					this.lengthTexto=lengthTexto;
					this.increase=increase;
				}
			@Override
				public int compareTo(Match match){
					return this.indexTag-match.indexTag;
				}
			}
		private void styleParagraph(MindMarkAtributo atributo){
			for(String definition:atributo.getDefinitions()){
				String texto="";
				try{
					texto=getText(0,getLength());
				}catch(BadLocationException error){}
				final Matcher match=Pattern.compile(definition,Pattern.MULTILINE).matcher(texto);
				while(match.find()){
				//TAG INI
					final int indexTagIni=match.start(1);
					final int lengthTagIni=match.group(1).length();
					setCharacterAttributes(indexTagIni,lengthTagIni,MindMarkDocumento.SPECIAL,true);
				//TEXTO
					final SimpleAttributeSet attr=new SimpleAttributeSet();
					attr.addAttribute(atributo,true);
					setParagraphAttributes(indexTagIni,1,attr,true);
				}
			}
		}
//		private void styleParagraph(MindMarkAtributo atributo){
//			for(String definition:atributo.getDefinitions()){
//				String texto="";
//				try{
//					texto=getText(0,getLength());
//				}catch(BadLocationException error){}
//				final Matcher match=Pattern.compile(definition,Pattern.MULTILINE).matcher(texto);
//				while(match.find()){
//				//TAG INI
//					final int indexTagIni=match.start(1);
//					final int lengthTagIni=match.group(1).length();
//				//TEXTO
////					final int indexTexto=match.start(2);
//					final int lengthTexto=match.group(2).length();
//					String textoCitation="";
//					try{
//						textoCitation=getText(indexTagIni,lengthTagIni+lengthTexto);
//						remove(indexTagIni,lengthTagIni+lengthTexto);
//					}catch(BadLocationException error){
//						error.printStackTrace();//TODO
//					}
//					insertCitation(indexTagIni,textoCitation);
//					setCharacterAttributes(indexTagIni,lengthTagIni,MindMarkDocumento.SPECIAL,true);
//				}
//			}
//		}
	protected void insertCitation(int index,String texto){
		try{
			final SimpleAttributeSet attrs=new SimpleAttributeSet();
			final List<ElementSpec>citationSpecs=new ArrayList<>();
			final SimpleAttributeSet citationAttrs=new SimpleAttributeSet();
			citationAttrs.addAttribute(ElementNameAttribute,MindMarkDocumento.ParagraphCitationElementName);
		//START
			citationSpecs.add(new ElementSpec(attrs,ElementSpec.EndTagType));		//END PARAGRAPH
			citationSpecs.add(new ElementSpec(citationAttrs,ElementSpec.StartTagType));	//START CITATION
			citationSpecs.add(new ElementSpec(citationAttrs,ElementSpec.ContentType,texto.toCharArray(),0,texto.length()));	//CONTEÚDO
			citationSpecs.add(new ElementSpec(citationAttrs,ElementSpec.EndTagType));	//END CITATION
			citationSpecs.add(new ElementSpec(attrs,ElementSpec.StartTagType));		//START PARAGRAPH
			final ElementSpec[]spec=new ElementSpec[citationSpecs.size()];
			citationSpecs.toArray(spec);
			insert(index,spec);
		}catch(BadLocationException error){
			error.printStackTrace();	//TODO
		}
	}
	protected void insertTable(int index,int rows,int[]colWidths){
		try{
			final SimpleAttributeSet attrs=new SimpleAttributeSet();
			final List<ElementSpec>tableSpecs=new ArrayList<>();
			final SimpleAttributeSet tableAttrs=new SimpleAttributeSet();
			tableAttrs.addAttribute(ElementNameAttribute,MindMarkDocumento.TableElementName);
		//START
			tableSpecs.add(new ElementSpec(attrs,ElementSpec.EndTagType));			//END PARAGRAPH
			tableSpecs.add(new ElementSpec(tableAttrs,ElementSpec.StartTagType));	//START TABLE
			fillRowSpecs(tableSpecs,rows,colWidths);									//CONTEÚDO
			tableSpecs.add(new ElementSpec(tableAttrs,ElementSpec.EndTagType));		//END TABLE
			tableSpecs.add(new ElementSpec(attrs,ElementSpec.StartTagType));		//START PARAGRAPH
		//END
			final ElementSpec[]spec=new ElementSpec[tableSpecs.size()];
			tableSpecs.toArray(spec);
			insert(index,spec);
		}catch(BadLocationException error){
			error.printStackTrace();	//TODO
		}
	}
	protected void fillRowSpecs(List<ElementSpec>tableSpecs,int rowCount,int[]colWidths){
		final SimpleAttributeSet rowAttrs=new SimpleAttributeSet();
		rowAttrs.addAttribute(ElementNameAttribute,MindMarkDocumento.RowElementName);
		for(int i=0;i<rowCount;i++){
			tableSpecs.add(new ElementSpec(rowAttrs,ElementSpec.StartTagType));		//START ROW
			fillCellSpecs(tableSpecs,colWidths);										//CONTEÚDO
			tableSpecs.add(new ElementSpec(rowAttrs,ElementSpec.EndTagType));		//END ROW
		}
	}
	protected void fillCellSpecs(List<ElementSpec>tableSpecs,int[]colWidths){
		for(int i=0;i<colWidths.length;i++){
			final SimpleAttributeSet cellAttrs=new SimpleAttributeSet();
			cellAttrs.addAttribute(ElementNameAttribute,MindMarkDocumento.CellElementName);
			cellAttrs.addAttribute(MindMarkDocumento.CellParamWidth,new Integer(colWidths[i]));
			final SimpleAttributeSet attrs=new SimpleAttributeSet();
		//START
			tableSpecs.add(new ElementSpec(cellAttrs,ElementSpec.StartTagType));	//START CELL
			tableSpecs.add(new ElementSpec(attrs,ElementSpec.StartTagType));			//START PARAGRAPH
			tableSpecs.add(new ElementSpec(attrs,ElementSpec.ContentType,"\n".toCharArray(),0,1));	//CONTEÚDO
			tableSpecs.add(new ElementSpec(attrs,ElementSpec.EndTagType));				//END PARAGRAPH
			tableSpecs.add(new ElementSpec(cellAttrs,ElementSpec.EndTagType));		//END CELL
		//END
		}
	}
}