package utilitarios.visual.text.java.mindmarkdown.attribute.variable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MindMarkVariableAtributo extends MindMarkAtributo{
//TAGS
	private final static String START_TAG=Pattern.quote("["),END_TAG=Pattern.quote("]");
	private final static String START_URL_TAG=Pattern.quote("("),END_URL_TAG=Pattern.quote(")");
	private final static String START_VAR_TAG=Pattern.quote("{"),END_VAR_TAG=Pattern.quote("}");
//DEFINITIONS
	private String definition="";
		public void buildDefinitionWithURL(String specialTag,boolean isMultiline){
			definition=(//	(?:(?<escapedStart>(?<!\\)\\(?:\\\\)*?)\((?=.+?\]\(.+?\)))|(?:(?<startTag>\[)(?<text>.+?)(?<endTag>\]\((?<url>.+?)\)))
					oneOrOther(
							pseudoGroup(
									namedGroup("escapedStart",
											notPrecededBy(ESCAPE_TAG)+
											ESCAPE_TAG+
											pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
									)+
									specialTag+START_TAG+		//CONSOME TAG
									followedBy(		//DEIXA O RESTO PARA OUTRO MATCH
											(isMultiline?
													characters(space(),nonSpace())+oneOrMore()+butInTheSmallestAmount()
													:allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
											)+
											END_TAG+
											START_URL_TAG+
											allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()+
											END_URL_TAG
									)
							),
							pseudoGroup(			//MATCH POR COMPLETO
									namedGroup("startTag",specialTag+START_TAG)+
									namedGroup("text",
											(isMultiline?
													characters(space(),nonSpace())+oneOrMore()+butInTheSmallestAmount()
													:allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
											)
									)+
									namedGroup("endTag",
											END_TAG+
											START_URL_TAG+
											namedGroup("url",
													allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
											)+
											END_URL_TAG+
											namedGroup("vars","")	//VAZIO
									)
							)
					)
			);
		}
		public void buildDefinitionWithVars(String specialTag,boolean isMultiline){
			definition=(//	(?:(?<escapedStart>(?<!\\)\\(?:\\\\)*?)\[(?=.+?\]\{.*?\}))|(?:(?<startTag>\[)(?<text>.+?)(?<endTag>\]\{(?<vars>.*?)\}))
					oneOrOther(
							pseudoGroup(
									namedGroup("escapedStart",
											notPrecededBy(ESCAPE_TAG)+
											ESCAPE_TAG+
											pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
									)+
									specialTag+START_TAG+		//CONSOME TAG
									followedBy(		//DEIXA O RESTO PARA OUTRO MATCH
											(isMultiline?
													characters(space(),nonSpace())+oneOrMore()+butInTheSmallestAmount()
													:allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
											)+
											END_TAG+
											START_VAR_TAG+
											allExceptLineBreak()+zeroOrMore()+butInTheSmallestAmount()+
											END_VAR_TAG
									)
							),
							pseudoGroup(			//MATCH POR COMPLETO
									namedGroup("startTag",specialTag+START_TAG)+
									namedGroup("text",
											(isMultiline?
													characters(space(),nonSpace())+oneOrMore()+butInTheSmallestAmount()
													:allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
											)
									)+
									namedGroup("endTag",
											END_TAG+
											namedGroup("url","")+	//VAZIO
											START_VAR_TAG+
											namedGroup("vars",
													allExceptLineBreak()+zeroOrMore()+butInTheSmallestAmount()
											)+
											END_VAR_TAG
									)
							)
					)
			);
		}
		public void buildDefinitionWithURLAndVars(String specialTag,boolean isMultiline,boolean isURLOptional,boolean isVarsOptional){
			definition=(//	(?:(?<escapedStart>(?<!\\)\\(?:\\\\)*?)specialTag\[(?=.+?\](?:\(.+?\))?(?:\{.*?\})?))
						//		|(?:(?<startTag>\[)(?<text>.+?)(?<endTag>\]\((?<url>.+?)\)\{(?<vars>.*?)\}))
					oneOrOther(
							pseudoGroup(
									namedGroup("escapedStart",
											notPrecededBy(ESCAPE_TAG)+
											ESCAPE_TAG+
											pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
									)+
									specialTag+START_TAG+		//CONSOME TAG
									followedBy(		//DEIXA O RESTO PARA OUTRO MATCH
											(isMultiline?
													characters(space(),nonSpace())+oneOrMore()+butInTheSmallestAmount()
													:allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
											)+
											END_TAG+
											pseudoGroup(
												START_URL_TAG+
												allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()+
												END_URL_TAG
											)+(isURLOptional?zeroOrOne():"")+
											pseudoGroup(
												START_VAR_TAG+
												allExceptLineBreak()+zeroOrMore()+butInTheSmallestAmount()+
												END_VAR_TAG
											)+(isVarsOptional?zeroOrOne():"")
									)
							),
							pseudoGroup(			//MATCH POR COMPLETO
									namedGroup("startTag",specialTag+START_TAG)+
									namedGroup("text",
											(isMultiline?
													characters(space(),nonSpace())+oneOrMore()+butInTheSmallestAmount()
													:allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
											)
									)+
									namedGroup("endTag",
											END_TAG+
											pseudoGroup(
												START_URL_TAG+
												namedGroup("url",
														allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
												)+
												END_URL_TAG
											)+(isURLOptional?zeroOrOne():"")+
											pseudoGroup(
												START_VAR_TAG+
												namedGroup("vars",
														allExceptLineBreak()+zeroOrMore()+butInTheSmallestAmount()
												)+
												END_VAR_TAG
											)+(isVarsOptional?zeroOrOne():"")
									)
							)
					)
			);
		}
//MAIN
	public MindMarkVariableAtributo(){}
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
			//TAG_START
				final int indexTagIni=match.start("startTag");
				final int lengthTagIni=match.group("startTag").length();
			//TEXT
				final int indexTexto=match.start("text");
				final int lengthTexto=match.group("text").length();
			//TAG_END
				final int indexTagFim=match.start("endTag");
				final int lengthTagFim=match.group("endTag").length();
			//TAGS
				if(isStyled(doc,indexTagIni))continue;	//JÁ FOI ESTILIZADO
				if(isStyled(doc,indexTagFim))continue;	//JÁ FOI ESTILIZADO
				//SPECIAL
				final MindMarkAtributo specialAtributo=getSpecialAtributo_TwoTags(doc,indexTagIni,lengthTagIni,indexTagFim,lengthTagFim);
				doc.setCharacterAttributes(indexTagIni,lengthTagIni,specialAtributo,true);
				doc.setCharacterAttributes(indexTagFim,lengthTagFim,specialAtributo,true);
				//TEXT
				final MindMarkAtributo atributo=new MindMarkAtributo();
				setURLAtributos(atributo,indexTexto,match.group("text"),match.group("url"));
				setVarsAtributos(atributo,indexTexto,match.group("text"),match.group("vars"));
				doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
			}
		}
	}
	protected void setURLAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String url){}
	protected void setVarsAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String vars){}
}