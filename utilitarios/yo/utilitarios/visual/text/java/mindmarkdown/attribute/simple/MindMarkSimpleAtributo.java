package utilitarios.visual.text.java.mindmarkdown.attribute.simple;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MindMarkSimpleAtributo extends MindMarkAtributo{
//DEFINITION
	private String definition="";
		public void buildDefinition(String tag){
			definition=(//	(?:(?<escapedStart>(?<!\\)\\(?:\\\\)*?)tag(?=.+?tag))
						//		|(?:tag(?:(?!tag).)+?(?<escapedEnd>(?<!\\)\\(?:\\\\)*?)tag)
						//		|(?:(?<startTag>tag)(?<text>.+?)(?<endTag>tag))
					oneOrOther(
							pseudoGroup(
									namedGroup("escapedStart",
											notPrecededBy(ESCAPE_TAG)+
											ESCAPE_TAG+
											pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
									)+
									tag+		//CONSOME TAG
									followedBy(	//DEIXA O RESTO PARA OUTRO MATCH
											allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()+
											tag
									)
							),
							pseudoGroup(
									tag+		//NÃO IMPORTA MAIS, CONSOME TAG
									pseudoGroup(
											notFollowedBy(tag)+
											allExceptLineBreak()
									)+oneOrMore()+butInTheSmallestAmount()+
									namedGroup("escapedEnd",
											notPrecededBy(ESCAPE_TAG)+
											ESCAPE_TAG+
											pseudoGroup(ESCAPE_TAG+ESCAPE_TAG)+zeroOrMore()+butInTheSmallestAmount()
									)+
									tag			//CONSOME TAG
							),
							pseudoGroup(		//MATCH POR COMPLETO
									namedGroup("startTag",tag)+
									namedGroup("text",
											allExceptLineBreak()+oneOrMore()+butInTheSmallestAmount()
									)+
									namedGroup("endTag",tag)
							)
					)
			);
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
		while(match.find()){
			if(match.group("escapedStart")!=null){
			//ESCAPED_START
				final int index=match.start("escapedStart")+match.group("escapedStart").length()-1;
				final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
				doc.setCharacterAttributes(index,1,specialAtributo,true);
			}else if(match.group("escapedEnd")!=null){
			//ESCAPED_END
				final int index=match.start("escapedEnd")+match.group("escapedEnd").length()-1;
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
				doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
			}
		}
	}
}