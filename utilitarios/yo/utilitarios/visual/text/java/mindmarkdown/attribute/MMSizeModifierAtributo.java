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
	private final static String DEFINITION=(
			notPrecededBy(ESCAPE)+
			group(oneOrOther(INCREASE_TAG,DECREASE_TAG,lineBreak()))+
			group(allExcept(INCREASE_TAG,DECREASE_TAG)+zeroOrMore())
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
	public static void applyStyle(MindMarkDocumento doc){
		final MMSizeModifierAtributo atributo=new MMSizeModifierAtributo();
		final String texto=doc.getText();
		final Matcher matchLinha=Pattern.compile(".*").matcher(texto);
		while(matchLinha.find()){
			int sizeIndex=0;
			final int linhaIndex=matchLinha.start();
			final Matcher textMatch=Pattern.compile(DEFINITION).matcher(matchLinha.group());
			while(textMatch.find()){
				if(isIncreaseTag(textMatch.group(1))){
					sizeIndex++;
				}else sizeIndex--;
			//TAG
				final int indexTag=linhaIndex+textMatch.start(1);
				final int lengthTag=textMatch.group(1).length();
				if(!isBelowLimit(sizeIndex)&&!isAboveLimit(sizeIndex))doc.setCharacterAttributes(indexTag,lengthTag,MindMarkAtributo.SPECIAL,true);
			//TEXTO
				final int indexTexto=linhaIndex+textMatch.start(2);
				final int lengthTexto=textMatch.group(2).length();
				final int newSize=(int)(DEFAULT_FONT_SIZE*getFontSizeModifier(sizeIndex));
				StyleConstants.setFontSize(atributo,newSize);
				if(!isBelowLimit(sizeIndex)&&!isAboveLimit(sizeIndex)){
					doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
				}else doc.setCharacterAttributes(indexTag,lengthTag+lengthTexto,atributo,false);
			//END
				if(isBelowLimit(sizeIndex)){
					sizeIndex++;
				}else if(isAboveLimit(sizeIndex))sizeIndex--;
			}
		}
	}
		private static boolean isIncreaseTag(String tag){
			switch(tag){
				case INCREASE:default:	return true;
				case DECREASE:			return false;
			}
		}
		private static boolean isBelowLimit(int sizeIndex){
			sizeIndex*=-1;//INVERTE DE - PARA +
			if(sizeIndex>SMALLER_SIZES.length)return true;
			return false;
		}
		private static boolean isAboveLimit(int sizeIndex){
			if(sizeIndex>BIGGER_SIZES.length)return true;
			return false;
		}
		private static float getFontSizeModifier(int sizeIndex){	//..., -2, -1, 0, 1, 2, ...
			if(sizeIndex<0){		//MENOR
				if(isBelowLimit(sizeIndex))return SMALLER_SIZES[SMALLER_SIZES.length-1];
				sizeIndex*=-1;//INVERTE DE - PARA +
				return SMALLER_SIZES[sizeIndex-1];
			}else if(sizeIndex>0){	//MAIOR
				if(isAboveLimit(sizeIndex))return BIGGER_SIZES[BIGGER_SIZES.length-1];
				return BIGGER_SIZES[sizeIndex-1];
			}else return 1.0f;		//IGUAL
		}
}