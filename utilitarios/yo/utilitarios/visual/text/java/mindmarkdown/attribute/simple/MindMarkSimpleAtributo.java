package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MindMarkSimpleAtributo extends MindMarkAtributo{
//VARS GLOBAIS
	private boolean isMultiline=false;
	private boolean startTagEqualsEndTag=false;
//DEFINITION
	private String definition="";
		public void buildDefinition(String startTag,boolean isMultiline,String endTag){
			this.isMultiline=isMultiline;
			startTagEqualsEndTag=(startTag.equals(endTag));
			definition=(startTagEqualsEndTag?
					(/*(?<newLine>\n)
						|(?:escapeDefinition("Start")(?<startTag>startTag))*/
					oneOrOther(
							namedGroup("newLine",lineBreak()),
							pseudoGroup(
									escapeDefinition("Start")+
									namedGroup("startTag",startTag)
							)
					)
			):(		/*(?<newLine>\n)
						|(?:escapeDefinition("Start")(?<startTag>startTag))
						|(?:escapeDefinition("End")(?<endTag>endTag))*/
					oneOrOther(
							namedGroup("newLine",lineBreak()),
							pseudoGroup(
									escapeDefinition("Start")+
									namedGroup("startTag",startTag)
							),
							pseudoGroup(
									escapeDefinition("End")+
									namedGroup("endTag",endTag)
							)
					)
			));
			//NÃO É CAPAZ DE LIDAR COM A ESCAPED_TAG_END E TAG_START-TEXT-TAG_END:
				//TEXTO TAG TEXTO ESCAPED_TAG TEXTO TAG = DEVE-SE ESCOLHER ENTRE FORMAT ESCAPED_TAG_END OU FORMAT TAG_START-TEXT-TAG_END 
			/*(?:(?<escapedStart>(?<!\\)\\(?:\\\\)*?)(?<escapedStartTag>tag)(?=.+?tag))
				|(?:tag(?:(?!tag).)+?(?<escapedEnd>(?<!\\)\\(?:\\\\)*?)(?<escapedEndTag>tag))
				|(?:(?<startTag>tag)(?<text>.+?)(?<endTag>tag))*/
		};
//ATRIBUTOS
	private MindMarkAtributo atributo=new MindMarkAtributo();
		public void setAtributo(Object nome,Object valor){atributo.addAttribute(nome,valor);}
//MAIN
	public MindMarkSimpleAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(definition).matcher(texto);
		final Match matchedTag=new Match();
		while(match.find()){
		//NEW_LINE
			if(match.group("newLine")!=null){
				if(!isMultiline)matchedTag.reset();	//RECOMEÇA
		//START_TAG
			}else if(match.group("startTag")!=null){
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("startTag")))continue;	//JÁ FOI ESTILIZADO
			//ESCAPED_TAG
				if(match.group("escapeStart")!=null){
					MindMarkAtributo.styleEscape(doc,match.start("escapeStart"),match.group("escapeStart").length(),match.group("startTag").length());
				}else{
				//NON_ESCAPED_TAG
					if(match.group("nonEscapeStart")!=null){
						MindMarkAtributo.styleNonEscape(doc,match.start("nonEscapeStart"),match.group("nonEscapeStart").length(),match.group("startTag").length());
					}
				//START_TAG
					final int indexStartTag=match.start("startTag");
					final int lengthStartTag=match.group("startTag").length();
				//FINISH
					if(matchedTag.isEmpty()){
						matchedTag.setIndex(indexStartTag);
						matchedTag.setLength(lengthStartTag);
					}else if(startTagEqualsEndTag){
						final int indexTagIni=matchedTag.getIndex();
						final int lengthTagIni=matchedTag.getLength();
						final int indexTagFim=indexStartTag;
						final int lengthTagFim=lengthStartTag;
						if(indexTagIni+lengthTagIni==indexTagFim){
							matchedTag.setIndex(indexTagFim);
							matchedTag.setLength(lengthTagFim);
							continue;	//NÃO HÁ TEXTO
						}
						MindMarkAtributo.styleText(doc,indexTagIni,lengthTagIni,atributo,indexTagFim,lengthTagFim);
						matchedTag.reset();	//RECOMEÇA
					}
				}
		//END_TAG
			}else{
				if(MindMarkAtributo.isStyledSpecial(doc,match.start("endTag")))continue;	//JÁ FOI ESTILIZADO
			//ESCAPED_TAG
				if(match.group("escapeEnd")!=null){
					MindMarkAtributo.styleEscape(doc,match.start("escapeEnd"),match.group("escapeEnd").length(),match.group("escapeEnd").length());
				}else{
				//NON_ESCAPED_TAG
					if(match.group("nonEscapeEnd")!=null){
						MindMarkAtributo.styleNonEscape(doc,match.start("nonEscapeEnd"),match.group("nonEscapeEnd").length(),match.group("nonEscapeEnd").length());
					}
				//END_TAG
					final int indexEndTag=match.start("endTag");
					final int lengthEndTag=match.group("endTag").length();
				//FINISH
					if(!matchedTag.isEmpty()){
						final int indexTagIni=matchedTag.getIndex();
						final int lengthTagIni=matchedTag.getLength();
						final int indexTagFim=indexEndTag;
						final int lengthTagFim=lengthEndTag;
						if(indexTagIni+lengthTagIni==indexTagFim){
							matchedTag.setIndex(indexTagFim);
							matchedTag.setLength(lengthTagFim);
							continue;	//NÃO HÁ TEXTO
						}
						MindMarkAtributo.styleText(doc,indexTagIni,lengthTagIni,atributo,indexTagFim,lengthTagFim);
						matchedTag.reset();	//RECOMEÇA
					}
				}
			}
		}
	}
}