package component.text.java_based.mindmarkdown.attribute.variable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;

import component.text.java_based.mindmarkdown.MindMarkdownDocumento;
import component.text.java_based.mindmarkdown.attribute.MindMarkdownAtributo;
@SuppressWarnings("serial")
public class MindMarkdownVariableAtributo extends MindMarkdownAtributo{
//VARS GLOBAIS
	private boolean isMultiline=false;
//TAGS
	private final static String START_TAG=Pattern.quote("["),END_TAG=Pattern.quote("]");
	private final static String START_URL_TAG=Pattern.quote("("),BAD_URL=Pattern.quote("\""),END_URL_TAG=Pattern.quote(")");
	private final static String START_VAR_TAG=Pattern.quote("{"),END_VAR_TAG=Pattern.quote("}");
//DEFINITIONS
	private String definition="";
		public void buildDefinitionWithURL(String startCondition,String specialTag,boolean isMultiline,boolean isURLOptional,String endCondition){
			buildDefinition(startCondition,specialTag,isMultiline,isURLOptional,null,endCondition);
		}
		public void buildDefinitionWithVars(String startCondition,String specialTag,boolean isMultiline,boolean isVarsOptional,String endCondition){
			buildDefinition(startCondition,specialTag,isMultiline,null,isVarsOptional,endCondition);
		}
		public void buildDefinitionWithURLAndVars(String startCondition,String specialTag,boolean isMultiline,boolean isURLOptional,boolean isVarsOptional,String endCondition){
			buildDefinition(startCondition,specialTag,isMultiline,isURLOptional,isVarsOptional,endCondition);
		}
		private void buildDefinition(String startCondition,String specialTag,boolean isMultiline,Boolean isURLOptional,Boolean isVarsOptional,String endCondition){
			this.isMultiline=isMultiline;
			definition=(
					/*	(?<newLine>\n)
						|(?:startCondition+escapeDefinition("Start")+(?<startTag>specialTag+\[))
						|(?:escapeDefinition("End")+(?<endTag>\](?:\((?<url>.+?)\))?(?:\{(?<vars>.+?)\})?)+endCondition)*/
					oneOrOther(
							namedGroup("newLine",lineBreak()),
							pseudoGroup(
									startCondition+
									escapeDefinition("Start")+
									namedGroup("startTag",specialTag+START_TAG)
							),
							pseudoGroup(
									escapeDefinition("End")+
									namedGroup("endTag",
											END_TAG+
											(isURLOptional==null?
													namedGroup("url","")	//VAZIO
													:pseudoGroup(
														START_URL_TAG+
														namedGroup("badURL",BAD_URL+zeroOrOne())+
														namedGroup("url",
																allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
														)+
														matchOfGroup("badURL")+
														END_URL_TAG
													)+(isURLOptional?zeroOrOne():"")
											)+
											(isVarsOptional==null?
													namedGroup("vars","")	//VAZIO
													:pseudoGroup(
														START_VAR_TAG+
														namedGroup("vars",
																allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
														)+
														END_VAR_TAG
													)+(isVarsOptional?zeroOrOne():"")
											)
									)+
									endCondition
							)
					)
			);
		}
//MAIN
	public MindMarkdownVariableAtributo(){}
//FUNCS
	public void applyStyle(MindMarkdownDocumento doc){
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(definition).matcher(texto);
		final Match matchedTag=new Match();
		while(match.find()){
		//NEW_LINE
			if(match.group("newLine")!=null){
				if(!isMultiline)matchedTag.reset();	//RECOMEÇA
		//START_TAG
			}else if(match.group("startTag")!=null){
				if(MindMarkdownAtributo.isStyledSpecial(doc,match.start("startTag")))continue;	//JÁ FOI ESTILIZADO
			//ESCAPED_TAG
				if(match.group("escapeStart")!=null){
					MindMarkdownAtributo.styleEscape(doc,match.start("escapeStart"),match.group("escapeStart").length(),match.group("startTag").length());
				}else{
				//NON_ESCAPED_TAG
					if(match.group("nonEscapeStart")!=null){
						MindMarkdownAtributo.styleNonEscape(doc,match.start("nonEscapeStart"),match.group("nonEscapeStart").length(),match.group("startTag").length());
					}
				//START_TAG
					final int indexStartTag=match.start("startTag");
					final int lengthStartTag=match.group("startTag").length();
				//FINISH
					if(matchedTag.isEmpty()){
						matchedTag.setIndex(indexStartTag);
						matchedTag.setLength(lengthStartTag);
					}
				}
		//END_TAG
			}else{
				if(MindMarkdownAtributo.isStyledSpecial(doc,match.start("endTag")))continue;	//JÁ FOI ESTILIZADO
				final String url=match.group("url");
				final String vars=match.group("vars");
				if((url==null||url.isEmpty())&&(vars==null||vars.isEmpty()))continue;		//NÃO HÁ URL E VARS
			//ESCAPED_TAG
				if(match.group("escapeEnd")!=null){
					MindMarkdownAtributo.styleEscape(doc,match.start("escapeEnd"),match.group("escapeEnd").length(),match.group("escapeEnd").length());
				}else{
				//NON_ESCAPED_TAG
					if(match.group("nonEscapeEnd")!=null){
						MindMarkdownAtributo.styleNonEscape(doc,match.start("nonEscapeEnd"),match.group("nonEscapeEnd").length(),match.group("nonEscapeEnd").length());
					}
				//END_TAG
					final int indexEndTag=match.start("endTag");
					final int lengthEndTag=match.group("endTag").length();
				//FINISH
					if(!matchedTag.isEmpty()){
						final int indexTagIni=matchedTag.getIndex();
						final int lengthTagIni=matchedTag.getLength();
						final int indexTexto=indexTagIni+lengthTagIni;
						final int lengthTexto=indexEndTag-indexTexto;
						final int indexTagFim=indexEndTag;
						final int lengthTagFim=lengthEndTag;
						if(indexTagIni+lengthTagIni==indexTagFim){
							matchedTag.reset();	//RECOMEÇA
							continue;	//NÃO HÁ TEXTO
						}
						final MindMarkdownAtributo atributo=new MindMarkdownAtributo();
						try{
							final String text=doc.getText(indexTexto,lengthTexto);
							if(url!=null&&!url.isEmpty())setURLAtributos(atributo,indexTexto,text,url);
							if(vars!=null&&!vars.isEmpty())setVarsAtributos(atributo,indexTexto,text,vars);
						}catch(BadLocationException error){}
						MindMarkdownAtributo.styleText(doc,indexTagIni,lengthTagIni,atributo,indexTagFim,lengthTagFim);
						matchedTag.reset();	//RECOMEÇA
					}
				}
			}
		}
	}
	protected void setURLAtributos(MindMarkdownAtributo atributo,int indexTexto,String texto,String url){}
	protected void setVarsAtributos(MindMarkdownAtributo atributo,int indexTexto,String texto,String vars){}
}