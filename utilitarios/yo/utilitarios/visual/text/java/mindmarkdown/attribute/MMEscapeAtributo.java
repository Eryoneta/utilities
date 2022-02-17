package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMEscapeAtributo extends MindMarkAtributo{
//DEFINITION
	private final String definition=(
			//(?<tag>\\)(?<text>\\)
			namedGroup("tag",ESCAPE_TAG)+
			namedGroup("text",ESCAPE_TAG)
	);
//VAR GLOBAIS
	public final static String ESCAPED="escaped";
//MAIN
	public MMEscapeAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		final Matcher match=Pattern.compile(definition).matcher(texto);
		while(match.find()){
		//TAG INI
			final int indexTag=match.start("tag");
			final int lengthTag=match.group("tag").length();
		//TEXTO
			final int indexTexto=match.start("text");
			final int lengthTexto=match.group("text").length();
		//TAGS
			if(isStyled(doc,indexTag))continue;	//JÁ FOI ESTILIZADO
			//SPECIAL
			final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,indexTag,lengthTag,lengthTexto);
			doc.setCharacterAttributes(indexTag,lengthTag,specialAtributo,true);
			//TEXT
			final MindMarkAtributo atributo=new MindMarkAtributo();
			atributo.addAttribute(ESCAPED,true);
			doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
		}
	}
}