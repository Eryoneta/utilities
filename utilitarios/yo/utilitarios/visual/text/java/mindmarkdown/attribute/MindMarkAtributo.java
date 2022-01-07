package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Font;
import java.util.regex.Pattern;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import utilitarios.visual.text.java.mindmarkdown.MindMarkEditor;
@SuppressWarnings("serial")
public class MindMarkAtributo extends SimpleAttributeSet{
//VAR STATICS
	public final static String ESCAPE=Pattern.quote("\\"); 
	public static MindMarkAtributo DEFAULT=new MindMarkAtributo();
		public static void setDefaultFont(Font fonte){
			DEFAULT=new MindMarkAtributo(){{
				StyleConstants.setFontFamily(this,fonte.getFamily());
				StyleConstants.setFontSize(this,fonte.getSize());
			}};
		}
	public final static MindMarkAtributo INVISIBLE=new MindMarkAtributo(){{
		StyleConstants.setFontSize(this,0);
	}};
	public final static MindMarkAtributo SPECIAL=new MindMarkAtributo(){{
		StyleConstants.setFontFamily(this,MindMarkEditor.DEFAULT_FONT.getFamily());
		StyleConstants.setForeground(this,MindMarkEditor.SPECIAL_CHARACTERS_COLOR);
	}};
//FUNCS STATICS
//QUANTITY
	public static String oneOrMore(){return "+";}
	public static String zeroOrMore(){return "*";}
	public static String zeroOrOne(){return "?";}
	public static String occurs(int quantity){return "{"+quantity+"}";}
	public static String occurs(int minQuantity,int maxQuantity){return "{"+minQuantity+","+maxQuantity+"}";}
//CHARACTERS
	public static String allExceptLineBreak(){return ".";}
	public static String lineBreak(){return "\\n";}
	public static String startOfText(){return "^";}
	public static String endOfText(){return "$";}
	public static String characters(String...words){
		String result="[";
		for(String word:words)result+=word;
		return result+"]";
	}
	public static String allExcept(String...words){
		String result="[^";
		for(String word:words)result+=word;
		return result+"]";
	}
//COMPUTATION
	public static String oneOrOther(String...words){
		String result="";
		for(String word:words)result+="|"+word;	//EX: |WORD|WORD|WORD
		return result.substring(1);				//EX: WORD|WORD|WORD
	}
//PRECEDED/FOLLOWED
	public static String precededBy(String word){return "(?<="+word+")";}
	public static String followedBy(String word){return "(?="+word+")";}
	public static String notPrecededBy(String word){return "(?<!"+word+")";}
	public static String notFollowedBy(String word){return "(?!"+word+")";}
//GROUPS
	public static String group(String group){return "("+group+")";}
	public static String pseudoGroup(String pseudoGroup){return "(?:"+pseudoGroup+")";}
//MAIN
	public MindMarkAtributo(){}
}