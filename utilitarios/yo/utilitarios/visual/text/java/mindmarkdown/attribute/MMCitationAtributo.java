package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMCitationAtributo extends MindMarkAtributo{
//SYMBOLS
	private final static String START=">";
	private final static String EXTEND="|>";
//VAR GLOBAIS
	public final static Color BACKGROUND_COLOR_1=new Color(230,230,230);
	public final static Color BACKGROUND_COLOR_2=new Color(220,220,220);
	public final static Color BACKGROUND_COLOR_3=new Color(210,210,210);
	public final static int MAX_LEVEL=3;
//TAGS
	private final static String START_TAG=Pattern.quote(START);
	private final static String EXTEND_TAG=Pattern.quote(EXTEND);
//DEFINITIONS
	private final static String DEFINITION=(
			pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
			group(
					notPrecededBy(ESCAPE)+
					pseudoGroup(oneOrOther(START_TAG,EXTEND_TAG))+occurs(0,MAX_LEVEL)
			)+
			group(allExceptLineBreak()+zeroOrMore())
			
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
	public static void applyStyle(MindMarkDocumento doc){
		final MMCitationAtributo atributo=new MMCitationAtributo();
		final String texto=doc.getText();
		Boolean[]pastLevel=null;
		final Matcher match=Pattern.compile(DEFINITION).matcher(texto);
		while(match.find()){
			final String citation=match.group(1);
			final boolean hasContent=!match.group(2).isEmpty();
			final Boolean[]currentLevel=getCitationType(pastLevel,citation,hasContent);
			if(currentLevel==null){
				pastLevel=null;
				continue;		//LINHA SEM CITATION OU INVÁLIDO
			}
			pastLevel=currentLevel;
			final int level=getLevel(currentLevel);
			final boolean isSpacedAbove=isSpacedAbove(currentLevel);
		//TAG INI
			final int index=match.start(1);
			int length=0;
			for(boolean type:currentLevel)length+=(type?START.length():EXTEND.length());
			doc.setCharacterAttributes(index,length,MindMarkAtributo.SPECIAL,true);
		//TEXTO
			StyleConstants.setSpaceAbove(atributo,isSpacedAbove?SPACE_ABOVE:0f);
			StyleConstants.setLeftIndent(atributo,LEFT_INDENT*level);
			atributo.addAttribute(CITATION,new Citation(level));
			doc.setParagraphAttributes(index,0,atributo,true);
		}
	}
		private static Boolean[]getCitationType(Boolean[]pastLevel,String citation,boolean hasContent){
			if(pastLevel==null)pastLevel=new Boolean[0];
		//LEVEL_3
			if(citation.startsWith(START+START+START))												//>>>
				if(hasContent)return new Boolean[]{true,true,true};											//NEEDS(CONTENT), ADD_SPACE_ABOVE, SEPARATE
			if(citation.startsWith(EXTEND+START+START))												//|>>>
				if(hasContent&&(pastLevel.length==1||pastLevel.length==2||pastLevel.length==3)){			//NEEDS(CONTENT), FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3), ADD_SPACE_ABOVE
					return new Boolean[]{false,true,true};
				}
			if(citation.startsWith(EXTEND+EXTEND+START))											//|>|>>
				if(hasContent&&(pastLevel.length==2||pastLevel.length==3)){									//NEEDS(CONTENT), FOLLOWS(LEVEL_2,LEVEL_3), ADD_SPACE_ABOVE
					return new Boolean[]{false,false,true};
				}
			if(citation.startsWith(EXTEND+EXTEND+EXTEND))											//|>|>|>
				if(pastLevel.length==3)return new Boolean[]{false,false,false};								//FOLLOWS(LEVEL_3)
		//LEVEL_2
			if(citation.startsWith(START+START))													//>>
				if(hasContent)return new Boolean[]{true,true};												//NEEDS(CONTENT), ADD_SPACE_ABOVE, SEPARATE
			if(citation.startsWith(EXTEND+START))													//|>>
				if(hasContent&&(pastLevel.length==1||pastLevel.length==2||pastLevel.length==3)){			//NEEDS(CONTENT), FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3), ADD_SPACE_ABOVE
					return new Boolean[]{false,true};
				}
			if(citation.startsWith(EXTEND+EXTEND))													//|>|>
				if(pastLevel.length==2||pastLevel.length==3)return new Boolean[]{false,false};				//FOLLOWS(LEVEL_2,LEVEL_3)
		//LEVEL_1
			if(citation.startsWith(START))															//>
				if(hasContent)return new Boolean[]{true};													//NEEDS(CONTENT), ADD_SPACE_ABOVE, SEPARATE
			if(citation.startsWith(EXTEND))															//|>
				if(pastLevel.length==1||pastLevel.length==2||pastLevel.length==3){							//FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3)
					return new Boolean[]{false};
				}
			return null;
		}
		private static int getLevel(Boolean[]currentLevel){return currentLevel.length;}
		private static boolean isSpacedAbove(Boolean[]currentLevel){	//OS QUE ACABAM COM '>' SÃO ESPAÇADOS
			return currentLevel[currentLevel.length-1];
		}
}