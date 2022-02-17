package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMListAtributo extends MindMarkAtributo{
//SYMBOLS
	private final static String CHEFE="* ";
	private final static String PAI="+ ";
	private final static String SON_0="- ";
	private final static String SON_1=". ";
	private final static String SON_2="  ";
//TAGS
	private final static String BULLET_0_TAG=Pattern.quote(CHEFE);	//INDEPENDENTE, ADD_SPACE_ABOVE
	private final static String BULLET_1_TAG=Pattern.quote(PAI);	//INDEPENDENTE, ADD_SPACE_ABOVE, ADD_SPACE_LEFT(SWITCH:(HAS'*')1)
	private final static String BULLET_2_TAG=Pattern.quote(SON_0);	//INDEPENDENTE, ADD_SPACE_LEFT(SWITCH:(HAS'*')1,(HAS'+')1,(HAS'*'&HAS'+')2)
	private final static String BULLET_3_TAG=Pattern.quote(SON_1);	//DEPENDENTE(NEEDS'-'),
																		//ADD_SPACE_LEFT(SWITCH:(HAS'-')1,(HAS'+'&HAS'-')2,(HAS'*'&HAS'-')2,(HAS'*'&HAS'+'&HAS'-')3)
	private final static String BULLET_4_TAG=Pattern.quote(SON_2);	//DEPENDENTE(NEEDS'.'),
																		//ADD_SPACE_LEFT(SWITCH:(HAS'-'&HAS'.')2,(HAS'+'&HAS'-'&HAS'.')3,(HAS'*'&HAS'-'&HAS'.')3,
																				//(HAS'*'&HAS'+'&HAS'-'&HAS'.')4)
//DEFINITIONS
	private final String definition=(
			//(?:(?:^|\n)(?<escapedStart>(?<!\\)\\(?:\\\\)*?)(?:\* |\+ |- |\. |  ).+)|(?:(?:^|\n)(?<tag>\* |\+ |- |\. |  )(?<text>.+))
			oneOrOther(
					pseudoGroup(
							pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
							namedGroup("escapedStart",
									notPrecededBy(ESCAPE_TAG)+
									ESCAPE_TAG+
									pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
							)+
							pseudoGroup(oneOrOther(BULLET_0_TAG,BULLET_1_TAG,BULLET_2_TAG,BULLET_3_TAG,BULLET_3_TAG,BULLET_4_TAG))+
							allExceptLineBreak()+oneOrMore()
					),
					pseudoGroup(
							pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
							namedGroup("tag",
									oneOrOther(BULLET_0_TAG,BULLET_1_TAG,BULLET_2_TAG,BULLET_3_TAG,BULLET_3_TAG,BULLET_4_TAG)
							)+
							namedGroup("text",
									allExceptLineBreak()+oneOrMore()
							)
					)
			)
	);
//VAR GLOBAIS
	public final static String LIST="list";
	public final static float SPACE_ABOVE=8f;
	public final static int LEFT_INDENT=4;
//VAR GLOBAIS: BULLET
	public final static int BULLET_0=0,BULLET_1=1,BULLET_2=2,BULLET_3=3,BULLET_4=4;
	public final static Color BULLET_COLOR=new Color(190,190,190);
	public final static int TOTAL_BULLETS=5;
	public final static int BULLET_SIZE=4;
//VAR GLOBAIS: LINE
	public final static int SPACE_INBETWEEN=2;
	public final static float LINE_WIDTH=2;
	public final static int LINE_AREA=6;
//LIST
	public static class List{
	//BULLET_TYPE
		private int bulletType;
			public int getBulletType(){return bulletType;}
	//MAIN
		public List(int bulletType){
			this.bulletType=bulletType;
		}
	}
//MAIN
	public MMListAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final boolean[]bulletsUsed=new boolean[TOTAL_BULLETS];
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
			if(match.group("escapedStart")!=null){
			//ESCAPED_START
				final int index=match.start("escapedStart")+match.group("escapedStart").length()-1;
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
				doc.setCharacterAttributes(index,1,specialAtributo,true);
			}else{
				System.out.println(match.group("tag"));
				final int bulletType=getBulletType(match.group("tag"));
				final int indentation=getIndentationByRules(bulletsUsed,bulletType);
				if(indentation==0)continue;	//INVÁLIDO
			//TAG
				final int indexTag=match.start("tag");
				final int lengthTag=match.group("tag").length();
			//LIST
				final int lengthTexto=match.group("text").length();
			//TAGS
				if(isStyled(doc,indexTag))continue;	//JÁ FOI ESTILIZADO
				bulletsUsed[bulletType]=true;
				for(int i=bulletType+1;i<bulletsUsed.length;i++)bulletsUsed[i]=false;	//A HIERARQUIA ABAIXO DELE É ZERADA
				//SPECIAL
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,indexTag,lengthTag,lengthTexto);
				doc.setCharacterAttributes(indexTag,lengthTag,specialAtributo,true);
				//TEXT
				final MindMarkAtributo atributo=new MindMarkAtributo();
				atributo.addAttribute(LIST,new List(bulletType));
				if((bulletType==BULLET_0||bulletType==BULLET_1)&&indexTag>0){
					StyleConstants.setSpaceAbove(atributo,SPACE_ABOVE);
				}else StyleConstants.setSpaceAbove(atributo,0f);
				StyleConstants.setLeftIndent(atributo,LEFT_INDENT*2*indentation);
				doc.setParagraphAttributes(indexTag,0,atributo,false);
			}
		}
	}
		private int getIndentationByRules(boolean[]bulletsUsed,int bulletAtual){
			switch(bulletAtual){
				case BULLET_0:return 1;	//*
				case BULLET_1:
					if(bulletsUsed[BULLET_0])return 2;	//*+
					return 1;	//+
				case BULLET_2:
					if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_1])return 3;	//*+-
					if(bulletsUsed[BULLET_0])return 2;	//*-
					if(bulletsUsed[BULLET_1])return 2;	//+-
					return 1;	//-
				case BULLET_3:
					if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2])return 4;	//*+-.
					if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_2])return 3;	//*-.
					if(bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2])return 3;	//+-.
					if(bulletsUsed[BULLET_2])return 2;	//-.
					return 0;
				case BULLET_4:
					if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 5;	//*+-. 
					if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 4;	//*-. 
					if(bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 4;	//+-. 
					if(bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 3;	//-. 
					return 0;
			}
			return 0;
		}
		private int getBulletType(String bullet){
			switch(bullet){
				case CHEFE:default:	return BULLET_0;
				case PAI:			return BULLET_1;
				case SON_0:			return BULLET_2;
				case SON_1:			return BULLET_3;
				case SON_2:			return BULLET_4;
			}
		}
}




//public class MMListAtributo extends MindMarkAtributo{
////SYMBOLS
//	private final static String CHEFE="* ";
//	private final static String PAI="+ ";
//	private final static String SON_0="- ";
//	private final static String SON_1=". ";
//	private final static String SON_2="  ";
////TAGS
//	private final static String BULLET_0_TAG=Pattern.quote(CHEFE);	//INDEPENDENTE, ADD_SPACE_ABOVE
//	private final static String BULLET_1_TAG=Pattern.quote(PAI);	//INDEPENDENTE, ADD_SPACE_ABOVE, ADD_SPACE_LEFT(SWITCH:(HAS'*')1)
//	private final static String BULLET_2_TAG=Pattern.quote(SON_0);	//INDEPENDENTE, ADD_SPACE_LEFT(SWITCH:(HAS'*')1,(HAS'+')1,(HAS'*'&HAS'+')2)
//	private final static String BULLET_3_TAG=Pattern.quote(SON_1);	//DEPENDENTE(NEEDS'-'),
//																//ADD_SPACE_LEFT(SWITCH:(HAS'-')1,(HAS'+'&HAS'-')2,(HAS'*'&HAS'-')2,(HAS'*'&HAS'+'&HAS'-')3)
//	private final static String BULLET_4_TAG=Pattern.quote(SON_2);	//DEPENDENTE(NEEDS'.'),
//																//ADD_SPACE_LEFT(SWITCH:(HAS'-'&HAS'.')2,(HAS'+'&HAS'-'&HAS'.')3,(HAS'*'&HAS'-'&HAS'.')3,
//																		//(HAS'*'&HAS'+'&HAS'-'&HAS'.')4)
////DEFINITIONS
//	private final String definition=(
//			//(?:^|\n)(?<!(?<!\\)\\(?:\\\\){0,99})(\* |\+ |- |\. |  )(.+)
//			pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
//			notPrecededByEscape()+
//			group(									//GROUP 1
//					oneOrOther(BULLET_0_TAG,BULLET_1_TAG,BULLET_2_TAG,BULLET_3_TAG,BULLET_3_TAG,BULLET_4_TAG)
//			)+
//			group(allExceptLineBreak()+oneOrMore())	//GROUP 2
//	);
//	private final String escapedDefinition=(
//			//(?:^|\n)((?<!\\)\\(?:\\\\)*)?(?:\* |\+ |- |\. |  ).+
//			pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
//			group(precededByEscape())+zeroOrOne()+	//GROUP 1
//			pseudoGroup(
//					oneOrOther(BULLET_0_TAG,BULLET_1_TAG,BULLET_2_TAG,BULLET_3_TAG,BULLET_3_TAG,BULLET_4_TAG)
//			)+
//			allExceptLineBreak()+oneOrMore()
//	);
////VAR GLOBAIS
//	public final static String LIST="list";
//	public final static float SPACE_ABOVE=8f;
//	public final static int LEFT_INDENT=4;
////VAR GLOBAIS: BULLET
//	public final static int BULLET_0=0,BULLET_1=1,BULLET_2=2,BULLET_3=3,BULLET_4=4;
//	public final static Color BULLET_COLOR=new Color(190,190,190);
//	public final static int TOTAL_BULLETS=5;
//	public final static int BULLET_SIZE=4;
////VAR GLOBAIS: LINE
//	public final static int SPACE_INBETWEEN=2;
//	public final static float LINE_WIDTH=2;
//	public final static int LINE_AREA=6;
////LIST
//	public static class List{
//	//BULLET_TYPE
//		private int bulletType;
//			public int getBulletType(){return bulletType;}
//	//MAIN
//		public List(int bulletType){
//			this.bulletType=bulletType;
//		}
//	}
////MAIN
//	public MMListAtributo(){}
////FUNCS
//	public void applyStyle(MindMarkDocumento doc){
//		final String texto=doc.getText();
//		applyStyle(doc,texto);
//		applyEscapedStyle(doc,texto);
//	}
//		private void applyStyle(MindMarkDocumento doc,String texto){
//			final MMListAtributo atributo=new MMListAtributo();
//			final boolean[]bulletsUsed=new boolean[TOTAL_BULLETS];
//			final Matcher match=Pattern.compile(definition).matcher(texto);
//			while(match.find()){
//				final int bulletType=getBulletType(match.group(1));
//				final int indentation=getIndentationByRules(bulletsUsed,bulletType);
//				if(indentation==0)continue;	//INVÁLIDO
//				if(isStyled(doc,match.start(1)))continue;	//JÁ FOI ESTILIZADO
//				bulletsUsed[bulletType]=true;
//				for(int i=bulletType+1;i<bulletsUsed.length;i++)bulletsUsed[i]=false;	//A HIERARQUIA ABAIXO DELE É ZERADA
//			//TAG INI
//				final int indexTag=match.start(1);
//				final int lengthTag=match.group(1).length();
//			//LIST
//				final int lengthTexto=match.group(2).length();
//				atributo.addAttribute(LIST,new List(bulletType));
//				if((bulletType==BULLET_0||bulletType==BULLET_1)&&indexTag>0){
//					StyleConstants.setSpaceAbove(atributo,SPACE_ABOVE);
//				}else StyleConstants.setSpaceAbove(atributo,0f);
//				StyleConstants.setLeftIndent(atributo,LEFT_INDENT*2*indentation);
//				doc.setParagraphAttributes(indexTag,0,atributo,false);
//			//TAGS
//				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,indexTag,lengthTag,lengthTexto);
//				doc.setCharacterAttributes(indexTag,lengthTag,specialAtributo,true);
//			}
//		}
//			private int getIndentationByRules(boolean[]bulletsUsed,int bulletAtual){
//				switch(bulletAtual){
//					case BULLET_0:return 1;	//*
//					case BULLET_1:
//						if(bulletsUsed[BULLET_0])return 2;	//*+
//						return 1;	//+
//					case BULLET_2:
//						if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_1])return 3;	//*+-
//						if(bulletsUsed[BULLET_0])return 2;	//*-
//						if(bulletsUsed[BULLET_1])return 2;	//+-
//						return 1;	//-
//					case BULLET_3:
//						if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2])return 4;	//*+-.
//						if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_2])return 3;	//*-.
//						if(bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2])return 3;	//+-.
//						if(bulletsUsed[BULLET_2])return 2;	//-.
//						return 0;
//					case BULLET_4:
//						if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 5;	//*+-. 
//						if(bulletsUsed[BULLET_0]&&bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 4;	//*-. 
//						if(bulletsUsed[BULLET_1]&&bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 4;	//+-. 
//						if(bulletsUsed[BULLET_2]&&bulletsUsed[BULLET_3])return 3;	//-. 
//						return 0;
//				}
//				return 0;
//			}
//			private int getBulletType(String bullet){
//				switch(bullet){
//					case CHEFE:default:	return BULLET_0;
//					case PAI:			return BULLET_1;
//					case SON_0:			return BULLET_2;
//					case SON_1:			return BULLET_3;
//					case SON_2:			return BULLET_4;
//				}
//			}
//		private void applyEscapedStyle(MindMarkDocumento doc,String texto){
//			final Matcher match=Pattern.compile(escapedDefinition).matcher(texto);
//			while(match.find()){
//			//TAG INI
//				if(match.group(1)!=null){
//					final int index=match.start(1)+match.group(1).length()-1;
//					final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
//					doc.setCharacterAttributes(index,1,specialAtributo,true);
//				}
//			}
//		}
//}