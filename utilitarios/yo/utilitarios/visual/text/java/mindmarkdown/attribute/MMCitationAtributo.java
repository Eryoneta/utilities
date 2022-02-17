package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMCitationAtributo extends MindMarkAtributo{
//VAR GLOBAIS
	public final static Color BACKGROUND_COLOR_1=new Color(230,230,230);
	public final static Color BACKGROUND_COLOR_2=new Color(220,220,220);
	public final static Color BACKGROUND_COLOR_3=new Color(210,210,210);
	public final static int MAX_LEVEL=3;
//SYMBOLS
	private final static String START=">";
	private final static String EXTEND="|>";
//TAGS
	private final static String START_TAG=Pattern.quote(START);
	private final static String EXTEND_TAG=Pattern.quote(EXTEND);
//DEFINITIONS
	private final String definition=(
			//(?:(?:^|\n)(?<escapedStart>(?<!\\)\\(?:\\\\)*?)(?:>|\|>){1,3}.*)|(?:(?:^|\n)(?<tag>(?:>|\|>){0,3})(?<text>.*))
			oneOrOther(
					pseudoGroup(
							pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
							namedGroup("escapedStart",
									notPrecededBy(ESCAPE_TAG)+
									ESCAPE_TAG+
									pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
							)+
							pseudoGroup(oneOrOther(START_TAG,EXTEND_TAG))+occursBetween(1,MAX_LEVEL)+
							allExceptLineBreak()+zeroOrMore()
					),
					pseudoGroup(
							pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
							namedGroup("tag",
									pseudoGroup(oneOrOther(START_TAG,EXTEND_TAG))+occursBetween(0,MAX_LEVEL)
							)+
							namedGroup("text",
									allExceptLineBreak()+zeroOrMore()
							)
					)
			)
	);
//VAR GLOBAIS
	public final static String CITATION="citation";
	public final static float SPACE_ABOVE=4f;
	public final static int LEFT_INDENT=8;
//VAR GLOBAIS: LINE
	public final static Color LINE_COLOR=new Color(190,190,190);
	public final static float LINE_WIDTH=4;
//CITATION
	public static class Citation{
	//BULLET_TYPE
		private int level;
			public int getLevel(){return level;}
	//MAIN
		public Citation(int level){
			this.level=level;
		}
	}
//MAIN
	public MMCitationAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		Boolean[]pastLevel=null;
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
			if(match.group("escapedStart")!=null){
			//ESCAPED_START
				final int index=match.start("escapedStart")+match.group("escapedStart").length()-1;
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
				doc.setCharacterAttributes(index,1,specialAtributo,true);
				pastLevel=null;		//LINHA INVALIDADA
			}else{
				final String citation=match.group("tag");
				final boolean hasContent=!match.group("text").isEmpty();
				final Boolean[]currentLevel=getCitationType(pastLevel,citation,hasContent);
				if(currentLevel==null){
					pastLevel=null;
					continue;		//LINHA SEM CITATION OU INVÁLIDO
				}
				pastLevel=currentLevel;
				final int level=getLevel(currentLevel);
				final boolean isSpacedAbove=isSpacedAbove(currentLevel);
			//TAG
				final int indexTag=match.start("tag");
				int lengthTag=0;
				for(boolean type:currentLevel)lengthTag+=(type?START.length():EXTEND.length());
			//TEXTO
				final int lengthTexto=match.group("text").length();
			//TAGS
				if(isStyled(doc,indexTag))continue;	//JÁ FOI ESTILIZADO
				//SPECIAL
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,indexTag,lengthTag,lengthTexto);
				doc.setCharacterAttributes(indexTag,lengthTag,specialAtributo,true);
				//TEXT
				final MindMarkAtributo atributo=new MindMarkAtributo();
				StyleConstants.setSpaceAbove(atributo,isSpacedAbove?SPACE_ABOVE:0f);
				StyleConstants.setLeftIndent(atributo,LEFT_INDENT*level);
				atributo.addAttribute(CITATION,new Citation(level));
				doc.setParagraphAttributes(indexTag,0,atributo,false);
			}
		}
	}
		private Boolean[]getCitationType(Boolean[]pastLevel,String citation,boolean hasContent){
			if(pastLevel==null)pastLevel=new Boolean[0];
			if(citation==null)return null;
		//LEVEL_3
			if(citation.startsWith(START+START+START))												//>>>
				if(hasContent)return new Boolean[]{true,true,true};										//NEEDS(CONTENT), ADD_SPACE_ABOVE, SEPARATE
			if(citation.startsWith(EXTEND+START+START))												//|>>>
				if(hasContent&&(pastLevel.length==1||pastLevel.length==2||pastLevel.length==3)){		//NEEDS(CONTENT), FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3), ADD_SPACE_ABOVE
					return new Boolean[]{false,true,true};
				}
			if(citation.startsWith(EXTEND+EXTEND+START))											//|>|>>
				if(hasContent&&(pastLevel.length==2||pastLevel.length==3)){								//NEEDS(CONTENT), FOLLOWS(LEVEL_2,LEVEL_3), ADD_SPACE_ABOVE
					return new Boolean[]{false,false,true};
				}
			if(citation.startsWith(EXTEND+EXTEND+EXTEND))											//|>|>|>
				if(pastLevel.length==3)return new Boolean[]{false,false,false};							//FOLLOWS(LEVEL_3)
		//LEVEL_2
			if(citation.startsWith(START+START))													//>>
				if(hasContent)return new Boolean[]{true,true};											//NEEDS(CONTENT), ADD_SPACE_ABOVE, SEPARATE
			if(citation.startsWith(EXTEND+START))													//|>>
				if(hasContent&&(pastLevel.length==1||pastLevel.length==2||pastLevel.length==3)){		//NEEDS(CONTENT), FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3), ADD_SPACE_ABOVE
					return new Boolean[]{false,true};
				}
			if(citation.startsWith(EXTEND+EXTEND))													//|>|>
				if(pastLevel.length==2||pastLevel.length==3)return new Boolean[]{false,false};			//FOLLOWS(LEVEL_2,LEVEL_3)
		//LEVEL_1
			if(citation.startsWith(START))															//>
				if(hasContent)return new Boolean[]{true};												//NEEDS(CONTENT), ADD_SPACE_ABOVE, SEPARATE
			if(citation.startsWith(EXTEND))															//|>
				if(pastLevel.length==1||pastLevel.length==2||pastLevel.length==3){						//FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3)
					return new Boolean[]{false};
				}
			return null;
		}
		private int getLevel(Boolean[]currentLevel){return currentLevel.length;}
		private boolean isSpacedAbove(Boolean[]currentLevel){	//OS QUE ACABAM COM '>' SÃO ESPAÇADOS
			return currentLevel[currentLevel.length-1];
		}
}