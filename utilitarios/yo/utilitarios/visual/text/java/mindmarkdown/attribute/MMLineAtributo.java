package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMLineAtributo extends MindMarkAtributo{
//VAR GLOBAIS
	public final static String LINE="line";
	public final static Color COLOR=new Color(190,190,190);
	public final static float LINE_WIDTH=4;
//SYMBOLS
	public final static String DEFAULT_OPTION="=";
	public final static String OPTION_0="0";
	public final static String OPTION_1="1";
	public final static String OPTION_2="2";
	public final static String OPTION_3="3";
	public final static String OPTION_4="4";
	public final static String OPTION_5="5";
	public final static String OPTION_6="6";
	public final static String OPTION_7="7";
	public final static String OPTION_8="8";
	public final static String OPTION_9="9";
//TAGS
	public final static String TAG=Pattern.quote(DEFAULT_OPTION+"");
	private final String COMPLETE_TAG=TAG+characters(range(0,9),TAG)+characters(range(0,9))+zeroOrOne()+TAG;	//=[0-9=][0-9]?=
//DEFINITIONS
	private final String definition=(
			/*(?<=^|\n)
				escapeDefinition()
				(?<tag>=[0-9=][0-9]?=)
				(?=\n|$)*/
			precededBy(oneOrOther(startOfText(),lineBreak()))+
			escapeDefinition()+
			namedGroup("tag",COMPLETE_TAG)+
			followedBy(oneOrOther(lineBreak(),endOfText()))
	);
//LINE
	public static class Line{
	//TYPES
		public enum Type{SOLID,SOLID_THIN,TRACED,DOTS,PENCIL_SCRATCH,ARROWS,MIDDLE_DOT,THREE_DOTS,DIAMOND,GRADIENT}
	//TYPE
		private Type type;
			public Type getType(){return type;}
	//SIZES
		public enum Size{SIZE_0,SIZE_1,SIZE_2,SIZE_3,SIZE_4,SIZE_5,SIZE_6,SIZE_7,SIZE_8,SIZE_9}
	//SIZE
		private Size size;
			public Size getSize(){return size;}
	//MAIN
		public Line(Type type,Size size){
			this.type=type;
			this.size=size;
		}
	}
//MAIN
	public MMLineAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
		//ESCAPED_START
			if(match.group("escape")!=null){
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("tag")))continue;	//JÁ FOI ESTILIZADO
				MindMarkAtributo.styleEscape(doc,match.start("escape"),match.group("escape").length(),match.group("tag").length());
		//TAG
			}else{
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("tag")))continue;	//JÁ FOI ESTILIZADO
			//NON_ESCAPED_START
				if(match.group("nonEscape")!=null){
					MindMarkAtributo.styleNonEscape(doc,match.start("nonEscape"),match.group("nonEscape").length(),match.group("tag").length());
				}
			//TAG
				final int indexTag=match.start("tag");
				final int lengthTag=match.group("tag").length();
			//LINE
				final Line.Type type=getType(match.group("tag"));
				final Line.Size size=getSize(match.group("tag"));
			//FINISH
				if(!match.group("nonEscape").isEmpty())continue;	//NÃO DEVE HAVER TEXTO ANTES DA TAG
				final MindMarkAtributo atributo=new MindMarkAtributo();
				StyleConstants.setAlignment(atributo,StyleConstants.ALIGN_CENTER);
				atributo.addAttribute(LINE,new Line(type,size));
				MindMarkAtributo.styleLine(doc,indexTag,lengthTag,atributo,indexTag+lengthTag,0);
			}
		}
	}
		private Line.Type getType(String line){
			final String type=String.valueOf(line.charAt(1));
			switch(type){
				case DEFAULT_OPTION:case OPTION_0:default:	return Line.Type.SOLID;
				case OPTION_1:		return Line.Type.SOLID_THIN;
				case OPTION_2:		return Line.Type.TRACED;
				case OPTION_3:		return Line.Type.DOTS;
				case OPTION_4:		return Line.Type.PENCIL_SCRATCH;
				case OPTION_5:		return Line.Type.ARROWS;
				case OPTION_6:		return Line.Type.MIDDLE_DOT;
				case OPTION_7:		return Line.Type.THREE_DOTS;
				case OPTION_8:		return Line.Type.DIAMOND;
				case OPTION_9:		return Line.Type.GRADIENT;
			}
		}
		private Line.Size getSize(String line){
			if(line.length()==3)return Line.Size.SIZE_0;
			final String size=String.valueOf(line.charAt(2));
			switch(size){
				case OPTION_1:		return Line.Size.SIZE_1;
				case OPTION_2:		return Line.Size.SIZE_2;
				case OPTION_3:		return Line.Size.SIZE_3;
				case OPTION_4:		return Line.Size.SIZE_4;
				case OPTION_5:		return Line.Size.SIZE_5;
				case OPTION_6:		return Line.Size.SIZE_6;
				case OPTION_7:		return Line.Size.SIZE_7;
				case OPTION_8:		return Line.Size.SIZE_8;
				case OPTION_9:		return Line.Size.SIZE_9;
				case OPTION_0:default:	return Line.Size.SIZE_0;
			}
		}
}