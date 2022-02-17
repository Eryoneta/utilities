package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMSizeModifierAtributo extends MindMarkAtributo{
//SYMBOLS
	private final static String INCREASE="#";
	private final static String DECREASE="^";
//TAGS
	private final static String INCREASE_TAG=Pattern.quote(INCREASE);
	private final static String DECREASE_TAG=Pattern.quote(DECREASE);
//DEFINITIONS
	private final String definition=(
			//(?<!(?<!\\)\\(?:\\\\){0,99})(#|\^|\n)((?:(?!#|\^|\n).)*)
			notPrecededByEscape()+
			group(oneOrOther(INCREASE_TAG,DECREASE_TAG,lineBreak()))+	//GROUP 1
			group(														//GROUP 2
					pseudoGroup(
							notFollowedBy(
									oneOrOther(INCREASE_TAG,DECREASE_TAG,lineBreak())
							)+
							allExceptLineBreak()
					)+zeroOrMore()
			)
	);
	private final String escapedDefinition=(
			//((?<!\\)\\(?:\\\\)*)(?:#|\^|\n)(?:(?!(?<!\\)\\(?:\\\\)*(?:#|\^)).)*
			group(precededByEscape())+	//GROUP 1
			pseudoGroup(oneOrOther(INCREASE_TAG,DECREASE_TAG,lineBreak()))+
			pseudoGroup(
					notFollowedBy(
							notPrecededByEscape()+
							pseudoGroup(oneOrOther(INCREASE_TAG,DECREASE_TAG,lineBreak()))
					)+
					allExceptLineBreak()
			)+zeroOrMore()
	);
//FONTE_SIZE
	public static int DEFAULT_FONT_SIZE=12;
		public static void setDefaultFontSize(int size){DEFAULT_FONT_SIZE=size;}
//SIZES
	private static float[]BIGGER_SIZES=new float[]{1.5f,2.5f,4.0f};
	private static float[]SMALLER_SIZES=new float[]{0.7f,0.4f,0.2f};
//MAIN
	public MMSizeModifierAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final Matcher matchLinha=Pattern.compile(allExceptLineBreak()+zeroOrMore()).matcher(texto);
		while(matchLinha.find()){
			final int linhaIndex=matchLinha.start();
			applyStyle(doc,matchLinha.group(),linhaIndex);
			applyEscapedStyle(doc,matchLinha.group(),linhaIndex);
		}
	}
		private void applyStyle(MindMarkDocumento doc,String texto,int linhaIndex){
			final MMSizeModifierAtributo atributo=new MMSizeModifierAtributo();
			int sizeIndex=0;
			final Matcher match=Pattern.compile(definition).matcher(texto);
			while(match.find()){
				if(isStyled(doc,linhaIndex+match.start(1)))continue;	//JÁ FOI ESTILIZADO
			//TAG
				final int indexTag=linhaIndex+match.start(1);
				final int lengthTag=match.group(1).length();
			//TEXTO
				if(isIncreaseTag(match.group(1))){
					sizeIndex++;
				}else sizeIndex--;
				final int indexTexto=linhaIndex+match.start(2);
				final int lengthTexto=match.group(2).length();
				final int newSize=(int)(DEFAULT_FONT_SIZE*getFontSizeModifier(sizeIndex));
				StyleConstants.setFontSize(atributo,newSize);
				if(!isBelowLimit(sizeIndex)&&!isAboveLimit(sizeIndex)){
					doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
				}else doc.setCharacterAttributes(indexTag,lengthTag+lengthTexto,atributo,false);
			//TAGS
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,indexTag,lengthTag,lengthTexto);
				if(!isBelowLimit(sizeIndex)&&!isAboveLimit(sizeIndex))doc.setCharacterAttributes(indexTag,lengthTag,specialAtributo,true);
			//END
				if(isBelowLimit(sizeIndex)){
					sizeIndex++;
				}else if(isAboveLimit(sizeIndex))sizeIndex--;
			}
		}
			private boolean isIncreaseTag(String tag){
				switch(tag){
					case INCREASE:default:	return true;
					case DECREASE:			return false;
				}
			}
			private boolean isBelowLimit(int sizeIndex){
				sizeIndex*=-1;//INVERTE DE - PARA +
				if(sizeIndex>SMALLER_SIZES.length)return true;
				return false;
			}
			private boolean isAboveLimit(int sizeIndex){
				if(sizeIndex>BIGGER_SIZES.length)return true;
				return false;
			}
			private float getFontSizeModifier(int sizeIndex){	//..., -2, -1, 0, 1, 2, ...
				if(sizeIndex<0){		//MENOR
					if(isBelowLimit(sizeIndex))return SMALLER_SIZES[SMALLER_SIZES.length-1];
					sizeIndex*=-1;//INVERTE DE - PARA +
					return SMALLER_SIZES[sizeIndex-1];
				}else if(sizeIndex>0){	//MAIOR
					if(isAboveLimit(sizeIndex))return BIGGER_SIZES[BIGGER_SIZES.length-1];
					return BIGGER_SIZES[sizeIndex-1];
				}else return 1.0f;		//IGUAL
			}
		private void applyEscapedStyle(MindMarkDocumento doc,String texto,int linhaIndex){
			final Matcher match=Pattern.compile(escapedDefinition).matcher(texto);
			while(match.find()){
			//TAG INI
				if(match.group(1)!=null){
					final int index=linhaIndex+match.start(1)+match.group(1).length()-1;
					final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
					doc.setCharacterAttributes(index,1,specialAtributo,true);
				}
			}
		}
}