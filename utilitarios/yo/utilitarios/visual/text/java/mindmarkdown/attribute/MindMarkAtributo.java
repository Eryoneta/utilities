package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.text.SimpleAttributeSet;
@SuppressWarnings("serial")
public class MindMarkAtributo extends SimpleAttributeSet{
//FUNCS STATICS
	public static String getDefinitionByWord(String startTag,String endTag){
		startTag=Pattern.quote(startTag);
		endTag=Pattern.quote(endTag);
		//(?<!\\)(startTag)((?:(?!startTag)(?!endTag).)+)(?<!\\)(endTag) =
			//NÃO_É_PRECEDIDO_POR(\)
			//GRUPO_1({1} * startTag)
				//GRUPO_2({1 OU MAIS} * PSEUDO_GRUPO(
						//NÃO_É_SEGUIDO_POR(startTag)
						//NÃO_É_SEGUIDO_POR(endTag)
						//TUDO_EXCETO_LINE_BREAK
				//))
			//NÃO_É_PRECEDIDO_POR(\)
			//GRUPO_3({1} * endTag)
		return "(?<!\\\\)("+startTag+")((?:(?!"+startTag+")(?!"+endTag+").)+)(?<!\\\\)("+endTag+")";
	}
	public static String getDefinitionByLine(String tag){
		tag=Pattern.quote(tag);
		//(?<!\\)(tag)(.+) =
			//NÃO_É_PRECEDIDO_POR(\)
			//GRUPO_1({1} * tag)
				//GRUPO_2({1 OU MAIS} * TUDO_EXCETO_LINE_BREAK)
		return "(?<!\\\\)("+tag+")(.+)";
	}
	public static String getDefinitionByWholeLine(String tag){
		tag=Pattern.quote(tag);
		//^(?<!\\)(tag)(.+) =
			//{1} * TEXT_START
			//NÃO_É_PRECEDIDO_POR(\)
			//GRUPO_1({1} * tag)
				//GRUPO_2({1 OU MAIS} * TUDO_EXCETO_LINE_BREAK)
		return "^(?<!\\\\)("+tag+")(.+)";
	}
	public static String getDefinitionByMultiline(String startTag,String endTag){
		startTag=Pattern.quote(startTag);
		endTag=Pattern.quote(endTag);
		//(?<!\\)(startTag)((?:(?!startTag)(?!endTag).|\n)+)(?<!\\)(endTag) =
			//NÃO_É_PRECEDIDO_POR(\)
			//GRUPO_1({1} * startTag)
				//GRUPO_2({1 OU MAIS} * PSEUDO_GRUPO(
						//NÃO_É_SEGUIDO_POR(startTag)
						//NÃO_É_SEGUIDO_POR(endTag)
						//TUDO_EXCETO_LINE_BREAK OU LINE_BREAK
				//))
			//NÃO_É_PRECEDIDO_POR(\)
			//GRUPO_3({1} * endTag)
		return "(?<!\\\\)("+startTag+")((?:(?!"+startTag+")(?!"+endTag+").|\\n)+)(?<!\\\\)("+endTag+")";
	}
//DEFINITIONS
	private List<String>definitions=new ArrayList<>();
		public List<String>getDefinitions(){return definitions;}
		public void addDefinition(String regex){definitions.add(regex);}
		public void delDefinition(String regex){definitions.remove(regex);}
//MAIN
	public MindMarkAtributo(){}
}