package component.text.java_based.mindmarkdown.attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import component.text.java_based.mindmarkdown.MindMarkdownDocumento;
@SuppressWarnings("serial")
public class MMAnchorAtributo extends MindMarkdownAtributo{
//TAGS
	private final static String LEFT_TAG=Pattern.quote(":-");
	private final static String RIGHT_TAG=Pattern.quote("-:");
//DEFINITIONS
	private final String definition=(
			/*(?<!^|\n)
				(?:escapeDefinition("Start")(?<startTag>:-))?
				(?<text>.*?)
				(?:escapeDefinition("End")(?<endTag>-:))?
				(?=\n|$)*/
			precededBy(oneOrOther(startOfText(),lineBreak()))+
			pseudoGroup(
					escapeDefinition("Start")+
					namedGroup("startTag",LEFT_TAG)
			)+zeroOrOne()+
			namedGroup("text",
					allExceptLineBreak()+zeroOrMore()+butInTheSmallestAmount()
			)+
			pseudoGroup(
					escapeDefinition("End")+
					namedGroup("endTag",RIGHT_TAG)
			)+zeroOrOne()+
			followedBy(oneOrOther(lineBreak(),endOfText()))
	);
//MAIN
	public MMAnchorAtributo(){}
//FUNCS
	public void applyStyle(MindMarkdownDocumento doc){
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
			int indexTag=-1;
			int anchor=-1;
		//START_TAG
			int indexTagLeft=-1;
			int lengthTagLeft=0;
			if(match.group("startTag")!=null){
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
					indexTagLeft=match.start("startTag");
					lengthTagLeft=match.group("startTag").length();
					if(indexTag==-1)indexTag=indexTagLeft;
					if(match.group("nonEscapeStart").isEmpty()){	//NÃO DEVE HAVER TEXTO ANTES DA TAG
						anchor=StyleConstants.ALIGN_LEFT;
					}
				}
			}
		//TEXT
			final int indexTexto=match.start("text");
			final int lengthTexto=match.group("text").length();
		//END_TAG
			int indexTagRight=-1;
			int lengthTagRight=0;
			if(match.group("endTag")!=null){
				if(MindMarkdownAtributo.isStyledSpecial(doc,match.start("endTag")))continue;	//JÁ FOI ESTILIZADO
			//ESCAPED_TAG
				if(match.group("escapeEnd")!=null){
					MindMarkdownAtributo.styleEscape(doc,match.start("escapeEnd"),match.group("escapeEnd").length(),match.group("endTag").length());
				}else{
				//NON_ESCAPED_TAG
					if(match.group("nonEscapeEnd")!=null){
						MindMarkdownAtributo.styleNonEscape(doc,match.start("nonEscapeEnd"),match.group("nonEscapeEnd").length(),match.group("endTag").length());
					}
				//END_TAG
					indexTagRight=match.start("endTag");
					lengthTagRight=match.group("endTag").length();
					if(indexTag==-1)indexTag=indexTagRight;
					if(indexTagRight!=-1)if(anchor==StyleConstants.ALIGN_LEFT){
						anchor=StyleConstants.ALIGN_CENTER;
					}else anchor=StyleConstants.ALIGN_RIGHT;
				}
			}
		//FINISH
			if(anchor==-1)continue;		//É APENAS TEXTO
			if(indexTagLeft==-1)indexTagLeft=indexTexto;
			if(indexTagRight==-1)indexTagRight=indexTexto+lengthTexto;
			//ANCHOR
			final MindMarkdownAtributo atributo=new MindMarkdownAtributo();
			StyleConstants.setAlignment(atributo,anchor);
			MindMarkdownAtributo.styleLine(doc,indexTagLeft,lengthTagLeft,atributo,indexTagRight,lengthTagRight);
		}
	}
}