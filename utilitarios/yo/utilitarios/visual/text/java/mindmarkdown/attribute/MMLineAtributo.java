package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMLineAtributo extends MindMarkAtributo{
//SYMBOLS
	private final static String DEFAULT="===";		//SÓLIDO TOTAL
	private final static String SOLID_G="=0=";		//SÓLIDO TOTAL
	private final static String SOLID_M="=1=";		//SÓLIDO MÉDIO
	private final static String SOLID_S="=2=";		//SÓLIDO PEQUENO
	private final static String DOTLINE_G="=3=";	//COM PONTO CENTRAL TOTAL
	private final static String DOTLINE_M="=4=";	//COM PONTO CENTRAL MÉDIO
	private final static String DOTLINE_S="=5=";	//COM PONTO CENTRAL PEQUENO
	private final static String DIAMOND_G="=6=";	//DIAMANTE TOTAL
	private final static String DIAMOND_M="=7=";	//DIAMANTE MÉDIO
	private final static String DIAMOND_S="=8=";	//DIAMANTE PEQUENO
	private final static String GRADIENT="=9=";		//GRADIENTE TOTAL
//TAGS
	private final String TAG="="+characters(range(0,9),"=")+"=";
//DEFINITIONS
	private final String definition=(
			//(?:(?:^|\n)(?<escapedStart>(?<!\\)\\(?:\\\\)*?)=[0-9=]=(?=\n|$))|(?:(?:^|\n)(?<tag>=[0-9=]=)(?=\n|$))
			oneOrOther(
					pseudoGroup(
							pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
							namedGroup("escapedStart",
									notPrecededBy(ESCAPE_TAG)+
									ESCAPE_TAG+
									pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
							)+
							TAG+
							followedBy(oneOrOther(lineBreak(),endOfText()))
					),
					pseudoGroup(
							pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
							namedGroup("tag",TAG)+
							followedBy(oneOrOther(lineBreak(),endOfText()))
					)
			)
	);
//VAR GLOBAIS
	public final static String LINE="line";
	public final static Color LINE_COLOR=new Color(190,190,190);
	public final static float LINE_WIDTH=4;
	public final static int TYPE_0=0,TYPE_1=1,TYPE_2=2,TYPE_3=3,TYPE_4=4,TYPE_5=5,TYPE_6=6,TYPE_7=7,TYPE_8=8,TYPE_9=9;
//LINE
	public static class Line{
	//TYPE
		private int type;
			public int getType(){return type;}
	//MAIN
		public Line(int type){
			this.type=type;
		}
	}
//MAIN
	public MMLineAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
			if(match.group("escapedStart")!=null){
			//ESCAPED_START
				final int index=match.start("escapedStart")+match.group("escapedStart").length()-1;
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
				doc.setCharacterAttributes(index,1,specialAtributo,true);
			}else{
			//TAG
				final int indexTag=match.start("tag");
				final int lengthTag=match.group("tag").length();
			//LINE
				final int type=getType(match.group("tag"));
			//TAGS
				if(isStyled(doc,indexTag))continue;	//JÁ FOI ESTILIZADO
				//SPECIAL
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,indexTag,lengthTag,0);
				doc.setCharacterAttributes(indexTag,lengthTag,specialAtributo,true);
				//TEXT
				final MindMarkAtributo atributo=new MindMarkAtributo();
				StyleConstants.setAlignment(atributo,StyleConstants.ALIGN_CENTER);
				atributo.addAttribute(LINE,new Line(type));
				doc.setParagraphAttributes(indexTag,0,atributo,false);
			}
		}
	}
		private int getType(String line){
			switch(line){
				case DEFAULT:case SOLID_G:default:	return TYPE_0;
				case SOLID_M:		return TYPE_1;
				case SOLID_S:		return TYPE_2;
				case DOTLINE_G:		return TYPE_3;
				case DOTLINE_M:		return TYPE_4;
				case DOTLINE_S:		return TYPE_5;
				case DIAMOND_G:		return TYPE_6;
				case DIAMOND_M:		return TYPE_7;
				case DIAMOND_S:		return TYPE_8;
				case GRADIENT:		return TYPE_9;
			}
		}
}