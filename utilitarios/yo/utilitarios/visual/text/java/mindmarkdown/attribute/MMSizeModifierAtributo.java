package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMSizeModifierAtributo extends MindMarkAtributo{
//VAR GLOBAIS: SIZES
	private static float[]BIGGER_SIZES=new float[]{1.5f,2.5f,4.0f};
	private static float[]SMALLER_SIZES=new float[]{0.7f,0.4f,0.2f};
//SYMBOLS
	private final static String INCREASE="#";
	private final static String DECREASE="^";
//TAGS
	private final static String INCREASE_TAG=Pattern.quote(INCREASE);
	private final static String DECREASE_TAG=Pattern.quote(DECREASE);
//DEFINITIONS
	private final String definition=(
			/*(?<newLine>\n|$)
				|(?:escapeDefinition()(?<tag>#|\^))
			*/
			oneOrOther(
					namedGroup("newLine",oneOrOther(lineBreak(),endOfText())),
					pseudoGroup(
							escapeDefinition()+
							namedGroup("tag",oneOrOther(INCREASE_TAG,DECREASE_TAG))
					)
			)
	);
//FONTE_SIZE
	public int DEFAULT_FONT_SIZE=12;
		public void setDefaultFontSize(int size){DEFAULT_FONT_SIZE=size;}
//MAIN
	public MMSizeModifierAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(definition).matcher(texto);
		final Match matchedTag=new Match();
		int sizeIndex=0;
		boolean hasHittedLimit=false;
		while(match.find()){
		//NEW_LINE
			if(match.group("newLine")!=null){
				if(!matchedTag.isEmpty()){
					final int indexNewLine=match.start("newLine");
					final Match matchIndex=new Match(matchedTag.getIndex(),matchedTag.getLength());
					final Match matchTexto=new Match();
					matchTexto.setIndex(matchIndex.getIndex()+matchIndex.getLength());
					matchTexto.setLength(indexNewLine-matchTexto.getIndex());
					applyStyle(doc,hasHittedLimit,sizeIndex,matchIndex,matchTexto);
				}
				matchedTag.reset();
				sizeIndex=0;
		//ESCAPED_TAG
			}else if(match.group("escape")!=null){
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("tag")))continue;	//JÁ FOI ESTILIZADO
				MindMarkAtributo.styleEscape(doc,match.start("escape"),match.group("escape").length(),match.group("tag").length());
		//TAG
			}else{
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("tag")))continue;	//JÁ FOI ESTILIZADO
			//NON_ESCAPED_TAG
				if(match.group("nonEscape")!=null){
					MindMarkAtributo.styleNonEscape(doc,match.start("nonEscape"),match.group("nonEscape").length(),match.group("tag").length());
				}
			//TAG
				final int indexTag=match.start("tag");
				final int lengthTag=match.group("tag").length();
			//FINISH
				final int newSizeIndex=(isIncreaseTag(match.group("tag"))?increaseSizeIndex(sizeIndex):decreaseSizeIndex(sizeIndex));
				if(newSizeIndex==sizeIndex)continue;	//NÃO MUDA DE TAMANHO
				if(!matchedTag.isEmpty()){
					final Match matchIndex=new Match(matchedTag.getIndex(),matchedTag.getLength());
					final Match matchTexto=new Match();
					matchTexto.setIndex(matchIndex.getIndex()+matchIndex.getLength());
					matchTexto.setLength(indexTag-matchTexto.getIndex());
					applyStyle(doc,hasHittedLimit,sizeIndex,matchIndex,matchTexto);
				}
				matchedTag.setIndex(indexTag);
				matchedTag.setLength(lengthTag);
				hasHittedLimit=(newSizeIndex==sizeIndex);
				sizeIndex=newSizeIndex;
			}
		}
	}
		private void applyStyle(MindMarkDocumento doc,boolean hasHittedLimit,int sizeIndex,Match matchIndex,Match matchTexto){
			final int endIndex=doc.getParagraphElement(matchIndex.getIndex()).getEndOffset();
			final MindMarkAtributo atributo=new MindMarkAtributo();
			final int newSize=(int)(DEFAULT_FONT_SIZE*getFontSizeModifier(sizeIndex));
			StyleConstants.setFontSize(atributo,newSize);
			if(!hasHittedLimit){
				MindMarkAtributo.styleText(doc,matchIndex.getIndex(),matchIndex.getLength(),atributo,endIndex,0);
			}else MindMarkAtributo.styleText(doc,matchIndex.getIndex(),0,atributo,endIndex,0);
		}
		private boolean isIncreaseTag(String tag){
			switch(tag){
				case INCREASE:default:	return true;
				case DECREASE:			return false;
			}
		}
		private int increaseSizeIndex(int sizeIndex){
			if(sizeIndex+1>BIGGER_SIZES.length)return sizeIndex;
			return sizeIndex+1;
		}
		private int decreaseSizeIndex(int sizeIndex){
			if(-sizeIndex+1>SMALLER_SIZES.length)return sizeIndex;
			return sizeIndex-1;
		}
		private float getFontSizeModifier(int sizeIndex){	//..., -2, -1, 0, 1, 2, ...
			if(sizeIndex<0){		//MENOR
				return SMALLER_SIZES[-sizeIndex-1];
			}else if(sizeIndex>0){	//MAIOR
				return BIGGER_SIZES[sizeIndex-1];
			}else return 1.0f;		//IGUAL
		}
}