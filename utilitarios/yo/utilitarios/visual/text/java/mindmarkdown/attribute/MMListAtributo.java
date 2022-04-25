package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMListAtributo extends MindMarkAtributo{
//VAR GLOBAIS
	public final static int TOTAL_BULLETS=5;
//SYMBOLS
	public final static String CHEFE="*";
	public final static String PAI="+";
	public final static String SON_0="-";
	public final static String SON_1=".";
	public final static String SON_2=" ";
	public final static String EXTENSION="|";
	public final static String SPACE=" ";
//TAGS
	public final static String CHEFE_TAG=Pattern.quote(CHEFE+SPACE);	//INDEPENDENTE, ADD_SPACE_ABOVE
	public final static String PAI_TAG=Pattern.quote(PAI+SPACE);		//INDEPENDENTE, ADD_SPACE_ABOVE, ADD_SPACE_LEFT(SWITCH:(HAS'*')1)
	public final static String SON_0_TAG=Pattern.quote(SON_0+SPACE);	//INDEPENDENTE, ADD_SPACE_LEFT(SWITCH:(HAS'*')1,(HAS'+')1,(HAS'*'&HAS'+')2)
	public final static String SON_1_TAG=Pattern.quote(SON_1+SPACE);	//DEPENDENTE(NEEDS'-'),
																				//ADD_SPACE_LEFT(SWITCH:(HAS'-')1,(HAS'+'&HAS'-')2,(HAS'*'&HAS'-')2,(HAS'*'&HAS'+'&HAS'-')3)
	public final static String SON_2_TAG=Pattern.quote(SON_2+SPACE);	//DEPENDENTE(NEEDS'.'),
																				//ADD_SPACE_LEFT(SWITCH:(HAS'-'&HAS'.')2,(HAS'+'&HAS'-'&HAS'.')3,(HAS'*'&HAS'-'&HAS'.')3,
																				//(HAS'*'&HAS'+'&HAS'-'&HAS'.')4)
	public final static String EXTENSION_TAG=Pattern.quote(EXTENSION);
//DEFINITIONS
	private final String definition=(
			/*(?<=^|\n)
				escapeDefinition()
				(?<tag>(?:\|)?(?:\* |\+ |- |\. |  ))
				(?<text>.*)*/
			precededBy(oneOrOther(startOfText(),lineBreak()))+
			escapeDefinition()+
			namedGroup("tag",
					pseudoGroup(EXTENSION_TAG)+zeroOrOne()+
					pseudoGroup(oneOrOther(CHEFE_TAG,PAI_TAG,SON_0_TAG,SON_1_TAG,SON_2_TAG))
			)+
			namedGroup("text",
					allExceptLineBreak()+zeroOrMore()
			)
	);
//VAR GLOBAIS
	public final static String LIST="list";
	public final static float SPACE_ABOVE=8f;
	public final static int LEFT_INDENT=8;
//VAR GLOBAIS: BULLET
	public final static Color COLOR=new Color(190,190,190);
	public final static int BULLET_SIZE=LEFT_INDENT/2;
//VAR GLOBAIS: LINE
	public final static float LINE_WIDTH=2;
	public final static int LINE_AREA=(int)(2+LINE_WIDTH+2);	//SPACE + LINE + SPACE
//LIST
	public static class List{
	//BULLET_TYPES
		public enum BulletType{
			CHEFE(0),PAI(1),SON_0(2),SON_1(3),SON_2(4);
		//VALOR
			private int id=0;
				public int getID(){return id;}
		//MAIN
			private BulletType(int id){this.id=id;}
		}
		private BulletType bulletType;
			public BulletType getBulletType(){return bulletType;}
	//TYPES
		public enum Type{HEAD,EXTENSION;}
		private Type type;
			public Type getType(){return type;}
		private boolean isImmediateSon;
			public boolean isImmediateSon(){return isImmediateSon;}
	//INDENTATION_LEVEL
		private int indentationLevel;
			public int getIndentationLevel(){return indentationLevel;}
	//MAIN
		public List(BulletType bulletType,Type type,boolean isImmediateSon,int indentationLevel){
			this.bulletType=bulletType;
			this.type=type;
			this.isImmediateSon=isImmediateSon;
			this.indentationLevel=indentationLevel;
		}
	//FUNCS
		public boolean isExtension(){return (getType()==Type.EXTENSION);}
	}
//MAIN
	public MMListAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final List[]pastLists=new List[TOTAL_BULLETS];
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
		//ESCAPE
			if(match.group("escape")!=null){
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("tag")))continue;	//JÁ FOI ESTILIZADO
				MindMarkAtributo.styleEscape(doc,match.start("escape"),match.group("escape").length(),match.group("tag").length());
		//TAG
			}else{
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("tag")))continue;	//JÁ FOI ESTILIZADO
				final List currentList=getList(pastLists,match.group("tag"));
			//NON_ESCAPE
				if(match.group("nonEscape")!=null){
					MindMarkAtributo.styleNonEscape(doc,match.start("nonEscape"),match.group("nonEscape").length(),match.group("tag").length());
				}
				if(currentList==null)continue;	//INVÁLIDO
			//TAG
				final int indexTag=match.start("tag");
				final int lengthTag=match.group("tag").length();
			//TEXTO
				final int indexText=match.start("text");
				final int lengthText=match.group("text").length();
			//FINISH
				if(match.group("nonEscape")!=null&&!match.group("nonEscape").isEmpty())continue;	//NÃO DEVE HAVER TEXTO ANTES DA TAG
				final int currentListID=currentList.getBulletType().getID();
				pastLists[currentListID]=currentList;
				for(int i=currentListID+1;i<pastLists.length;i++)pastLists[i]=null;	//A HIERARQUIA ABAIXO DELE É ZERADA
				final MindMarkAtributo atributo=new MindMarkAtributo();
				atributo.addAttribute(LIST,currentList);
				switch(currentList.getBulletType()){
					case CHEFE:case PAI:	//"*" OU "+" E NÃO FICA NO COMEÇO DO TEXTO 
						if(indexTag>0&&!currentList.isExtension()){
							StyleConstants.setSpaceAbove(atributo,SPACE_ABOVE);
						}else StyleConstants.setSpaceAbove(atributo,0f);
					break;
					default:
						StyleConstants.setSpaceAbove(atributo,0f);
					break;
				}
				StyleConstants.setLeftIndent(atributo,LEFT_INDENT*currentList.getIndentationLevel());
				MindMarkAtributo.styleLine(doc,indexTag,lengthTag,atributo,indexText+lengthText,0);
			}
		}
	}
		private List getList(List[]pastLists,String bullet){
			final boolean hasChefe=(pastLists[List.BulletType.CHEFE.getID()]!=null);
			final boolean hasPai=(pastLists[List.BulletType.PAI.getID()]!=null);
			final boolean hasSon0=(pastLists[List.BulletType.SON_0.getID()]!=null);
			final boolean hasSon1=(pastLists[List.BulletType.SON_1.getID()]!=null);
			final boolean hasSon2=(pastLists[List.BulletType.SON_2.getID()]!=null);
			List.BulletType bulletType=null;
			List.Type type=List.Type.HEAD;
			boolean isImmediateSon=false;
			int indentation=0;
			switch(bullet){
				case CHEFE+SPACE:default:
					bulletType=List.BulletType.CHEFE;	//"*"
					indentation=1;											//"* "
				break;
				case PAI+SPACE:
					bulletType=List.BulletType.PAI;		//"+"
					if(hasChefe&&!hasSon(pastLists,List.BulletType.CHEFE))isImmediateSon=true;
					if(hasChefe)indentation=2;								//"*" -> "+"
					else indentation=1;										//"+"
				break;
				case SON_0+SPACE:
					bulletType=List.BulletType.SON_0;	//"-"
					if(hasChefe&&!hasSon(pastLists,List.BulletType.CHEFE))isImmediateSon=true;
					if(hasPai&&!hasSon(pastLists,List.BulletType.PAI))isImmediateSon=true;
					if(hasChefe&&hasPai)indentation=3;						//"*" -> "+" -> "-"
					else if(hasChefe)indentation=2;							//"*" -> "-"
					else if(hasPai)indentation=2;							//"+ " -> "-"
					else indentation=1;										//"-"
				break;
				case SON_1+SPACE:
					bulletType=List.BulletType.SON_1;	//"."
					if(hasSon0&&!hasSon(pastLists,List.BulletType.SON_0))isImmediateSon=true;
					if(hasChefe&&hasPai&&hasSon0)indentation=4;				//"*" -> "+" -> "-" -> "."
					else if(hasChefe&&hasSon0)indentation=3;				//"*" -> "-" -> "."
					else if(hasPai&&hasSon0)indentation=3;					//"+" -> "-" -> "."
					else if(hasSon0)indentation=2;							//"-" -> "."
					else indentation=0;										//"." = INVÁLIDO
				break;
				case SON_2+SPACE:
					bulletType=List.BulletType.SON_2;	//" "
					if(hasSon1&&!hasSon(pastLists,List.BulletType.SON_1))isImmediateSon=true;
					if(hasChefe&&hasPai&&hasSon0&&hasSon1)indentation=5;	//"*" -> "+" -> "-" -> "." -> " "
					else if(hasChefe&&hasSon0&&hasSon1)indentation=4;		//"*" -> "-" -> "." -> " "
					else if(hasPai&&hasSon0&&hasSon1)indentation=4;			//"+" -> "-" -> "." -> " "
					else if(hasSon0&&hasSon1)indentation=3;					//"-" -> "." -> " "
					else indentation=0;										//" " = INVÁLIDO
				break;
				case EXTENSION+CHEFE+SPACE:
					bulletType=List.BulletType.CHEFE;	//"|*"
					type=List.Type.EXTENSION;
					if(hasChefe)indentation=1;										//"*" -> "|*"
					else indentation=0;												//"|*" = INVÁLIDO
				break;
				case EXTENSION+PAI+SPACE:
					bulletType=List.BulletType.PAI;		//"|+"
					type=List.Type.EXTENSION;
					if(hasChefe&&hasPai)indentation=2;								//"*" -> "+" -> "|+"
					else if(hasPai)indentation=1;									//"+" -> "|+"
					else indentation=0;												//"|+" = INVÁLIDO
				break;
				case EXTENSION+SON_0+SPACE:
					bulletType=List.BulletType.SON_0;	//"|-"
					type=List.Type.EXTENSION;
					if(hasChefe&&hasPai&&hasSon0)indentation=3;						//"*" -> "+" -> "-" -> "|-"
					else if(hasChefe&&hasSon0)indentation=2;						//"*" -> "-" -> "|-"
					else if(hasPai&&hasSon0)indentation=2;							//"+" -> "-" -> "|-"
					else if(hasSon0)indentation=1;									//"-" -> "|-"
					else indentation=0;												//"|-" = INVÁLIDO
				break;
				case EXTENSION+SON_1+SPACE:
					bulletType=List.BulletType.SON_1;	//"|."
					type=List.Type.EXTENSION;
					if(hasChefe&&hasPai&&hasSon0&&hasSon1)indentation=4;			//"*" -> "+" -> "-" -> "." -> "|."
					else if(hasChefe&&hasSon0&&hasSon1)indentation=3;				//"*" -> "-" -> "." -> "|."
					else if(hasPai&&hasSon0&&hasSon1)indentation=3;					//"+" -> "-" -> "." -> "|."
					else if(hasSon0&&hasSon1)indentation=2;							//"-" -> "." -> "|."
					else if(hasSon1)indentation=1;									//"." -> "|."
					else indentation=0;												//"|." = INVÁLIDO
				break;
				case EXTENSION+SON_2+SPACE:
					bulletType=List.BulletType.SON_2;	//"| "
					type=List.Type.EXTENSION;
					if(hasChefe&&hasPai&&hasSon0&&hasSon1&&hasSon2)indentation=5;	//"*" -> "+" -> "-" -> "." -> " " -> "| "
					else if(hasChefe&&hasSon0&&hasSon1&&hasSon2)indentation=4;		//"*" -> "-" -> "." -> " " -> "| "
					else if(hasPai&&hasSon0&&hasSon1&&hasSon2)indentation=4;		//"+" -> "-" -> "." -> " " -> "| "
					else if(hasSon0&&hasSon1&&hasSon2)indentation=3;				//"-" -> "." -> " " -> "| "
					else if(hasSon2)indentation=1;									//" " -> "| "
					else indentation=0;												//"| " = INVÁLIDO
				break;
					
			}
			if(indentation>0){
				return new List(bulletType,type,isImmediateSon,indentation);
			}else return null;
		}
			private boolean hasSon(List[]pastLists,List.BulletType bulletType){
				for(int i=bulletType.getID()+1;i<pastLists.length;i++)if(pastLists[i]!=null)return true;
				return false;
			}
}