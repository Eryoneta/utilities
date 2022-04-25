package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMCitationAtributo extends MindMarkAtributo{
//VAR GLOBAIS
	public final static Color[]BACKGROUND_COLOR=new Color[]{
			new Color(230,230,230),
			new Color(220,220,220),
			new Color(210,210,210)
	};
	public final static int MAX_LEVEL=3;	//SE QUISER MUDAR: ADICIONAR COR EM BACKGROUND_COLOR, E ADICIONAR CAMADA EM getCitationType()
//SYMBOLS
	public final static String START=">";
	public final static String EXTEND="|>";
//TAGS
	public final static String START_TAG=Pattern.quote(START);
	public final static String EXTEND_TAG=Pattern.quote(EXTEND);
//DEFINITIONS
	private String definition="";
	private void buildDefinition(int maxLevel){
		final StringBuilder def=new StringBuilder();
		/*(?<=^|\n)
			(?:escapeDefinition("Level1")(?<tagLevel1>>|\|>))?
			(?:escapeDefinition("Level2")(?<tagLevel2>>|\|>))?
			...
			(?<text>.*)*/
		def.append(
				precededBy(oneOrOther(startOfText(),lineBreak()))
		);
		for(int level=1;level<=MAX_LEVEL;level++){
			def.append(
					pseudoGroup(
							escapeDefinition("Level"+level)+
							namedGroup("tagLevel"+level,oneOrOther(START_TAG,EXTEND_TAG))
					)+zeroOrOne()
			);
		}
		def.append(
				namedGroup("text",
						allExceptLineBreak()+zeroOrMore()
				)
		);
		definition=def.toString();
	}
//VAR GLOBAIS
	public final static String CITATION="citation";
	public final static float SPACE_ABOVE=4f;
	public final static int LEFT_INDENT=8;
//VAR GLOBAIS: LINE
	public final static Color LINE_COLOR=new Color(190,190,190);
	public final static float LINE_WIDTH=4;
//CITATION
	public static class Citation{
	//LEVEL
		public static class Level{
		//TYPES
			public enum Type{HEAD,EXTENSION}
		//TYPE
			private Type type;
				public Type getType(){return type;}
		//MAIN
			public Level(Type type){this.type=type;}
		}
	//LEVELS
		private Level[]levels;
			public Level[]getLevels(){return levels;}
			public int getTotalLevel(){return levels.length;}
			public void setLevels(Level[]levels){this.levels=levels;}
	//MAIN
		public Citation(Level[]levels){
			this.levels=levels;
		}
		public Citation(){
			this.levels=new Level[0];
		}
	}
//MAIN
	public MMCitationAtributo(){
		buildDefinition(MAX_LEVEL);
	}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		Citation pastLevel=null;
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
		//TAG
			final StringBuilder citationTags=new StringBuilder();	//TAGS EMPILHADAS
			final int indexTag=match.start("tagLevel1");
			boolean isStillValid=true;
			for(int i=1;i<=MAX_LEVEL;i++){
				if(match.group("tagLevel"+i)==null||match.group("tagLevel"+i).isEmpty())break;	//ACABOU TAGS
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("tagLevel"+i)))break;		//JÁ FOI ESTILIZADO
			//ESCAPE
				if(match.group("escapeLevel"+i)!=null){
					MindMarkAtributo.styleEscape(doc,match.start("escapeLevel"+i),match.group("escapeLevel"+i).length(),match.group("tagLevel"+i).length());
				}
			//NON-ESCAPE
				if(match.group("nonEscapeLevel"+i)!=null){
					MindMarkAtributo.styleNonEscape(doc,match.start("nonEscapeLevel"+i),match.group("nonEscapeLevel"+i).length(),match.group("tagLevel"+i).length());
				}
				if(match.group("escapeLevel"+i)!=null&&!match.group("escapeLevel"+i).isEmpty())isStillValid=false;			//CONSIDERA ATÉ O ESCAPE
				if(match.group("nonEscapeLevel"+i)!=null&&!match.group("nonEscapeLevel"+i).isEmpty())isStillValid=false;	//CONSIDERA ATÉ O NON-ESCAPE
			//TAG
				if(isStillValid)citationTags.append(match.group("tagLevel"+i));
			}
			final boolean hasContent=!match.group("text").isEmpty();
			final Citation currentLevel=getCitationType(pastLevel,citationTags.toString(),hasContent);
			if(currentLevel==null){
				pastLevel=null;
				continue;		//LINHA SEM CITATION OU INVÁLIDO
			}
			pastLevel=currentLevel;
			final boolean isSpacedAbove=isSpacedAbove(currentLevel);
			int lengthTag=0;
			for(Citation.Level level:currentLevel.getLevels()){
				switch(level.getType()){
					case HEAD:		lengthTag+=START.length();	break;
					case EXTENSION:	lengthTag+=EXTEND.length();	break;
				}
			}
		//TEXTO
			final int indexText=match.start("text");
			final int lengthText=match.group("text").length();
		//FINISH
			if(MindMarkAtributo.isStyledSpecial(doc,indexTag))continue;	//JÁ FOI ESTILIZADO
			final MindMarkAtributo atributo=new MindMarkAtributo();
			StyleConstants.setSpaceAbove(atributo,isSpacedAbove?SPACE_ABOVE:0f);
			StyleConstants.setLeftIndent(atributo,LEFT_INDENT*currentLevel.getTotalLevel());
			atributo.addAttribute(CITATION,currentLevel);
			MindMarkAtributo.styleLine(doc,indexTag,lengthTag,atributo,indexText+lengthText,0);
		}
	}
		private Citation getCitationType(Citation pastLevel,String citationTags,boolean hasContent){
			if(pastLevel==null)pastLevel=new Citation();
			if(citationTags==null||citationTags.isEmpty())return null;
			final int pastTotalLevel=pastLevel.getTotalLevel();
			final Citation.Level head=new Citation.Level(Citation.Level.Type.HEAD);
			final Citation.Level extension=new Citation.Level(Citation.Level.Type.EXTENSION);
			switch(citationTags){
			//LEVEL_3
				case START+START+START:															//>>>
					if(hasContent){																//NEEDS(CONTENT) = LEVEL_3, SPACE_ABOVE, SEPARATION
						return new Citation(new Citation.Level[]{head,head,head});
					}else return null;
				case EXTEND+START+START:														//|>>>
					if(hasContent&&(pastTotalLevel==1||pastTotalLevel==2||pastTotalLevel==3)){	//NEEDS(CONTENT), FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3) = LEVEL_3, SPACE_ABOVE
						return new Citation(new Citation.Level[]{extension,head,head});
					}else return null;
				case EXTEND+EXTEND+START:														//|>|>>
					if(hasContent&&(pastTotalLevel==2||pastTotalLevel==3)){						//NEEDS(CONTENT), FOLLOWS(LEVEL_2,LEVEL_3) = LEVEL_3, SPACE_ABOVE
						return new Citation(new Citation.Level[]{extension,extension,head});
					}else return null;
				case EXTEND+EXTEND+EXTEND:														//|>|>|>
					if(pastTotalLevel==3){														//FOLLOWS(LEVEL_3) = LEVEL_3
						return new Citation(new Citation.Level[]{extension,extension,extension});
					}else return null;
			//LEVEL_2
				case START+START:																//>>
					if(hasContent){																//NEEDS(CONTENT) = LEVEL_2, SPACE_ABOVE, SEPARATION
						return new Citation(new Citation.Level[]{head,head});
					}else return null;
				case EXTEND+START:																//|>>
					if(hasContent&&(pastTotalLevel==1||pastTotalLevel==2||pastTotalLevel==3)){	//NEEDS(CONTENT), FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3) = LEVEL_2, SPACE_ABOVE
						return new Citation(new Citation.Level[]{extension,head});
					}else return null;
				case EXTEND+EXTEND:																//|>|>
					if(pastTotalLevel==2||pastTotalLevel==3) {									//FOLLOWS(LEVEL_2,LEVEL_3) = LEVEL_2
						return new Citation(new Citation.Level[]{extension,extension});
					}else return null;
			//LEVEL_1
				case START:																		//>
					if(hasContent){																//NEEDS(CONTENT) = LEVEL_1, SPACE_ABOVE, SEPARATION
						return new Citation(new Citation.Level[]{head});
					}else return null;
				case EXTEND:																	//|>
					if(pastTotalLevel==1||pastTotalLevel==2||pastTotalLevel==3){				//FOLLOWS(LEVEL_1,LEVEL_2,LEVEL_3) = LEVEL_1
						return new Citation(new Citation.Level[]{extension});
					}else return null;
			}
			return null;
		}
		private boolean isSpacedAbove(Citation currentLevel){	//OS QUE ACABAM COM '>' SÃO ESPAÇADOS
			final Citation.Level.Type citationLastType=currentLevel.getLevels()[currentLevel.getTotalLevel()-1].getType();
			return (citationLastType==Citation.Level.Type.HEAD);
		}
}