package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
@SuppressWarnings("serial")
public class MMAnchorAtributo extends MindMarkAtributo{
//TAGS
	private final static String LEFT_TAG=Pattern.quote(":-");
	private final static String RIGHT_TAG=Pattern.quote("-:");
//DEFINITIONS
	private final String definition=(
			/*		(?:(?:^|\n)(?<escapedStart>(?<!\\)\\(?:\\\\)*?)(?<escapedStartText>:-.+?)(?=\n|$))
			 * 			|(?:(?:^|\n)(?<escapedEndText>.+?)(?<escapedEnd>(?<!\\)\\(?:\\\\)*?)-:(?=\n|$))
			 * 			|(?:(?:^|\n)(?<startTag>:-)?(?<text>.+?)(?<endTag>-:)?(?=\n|$))
			 * 
			 * 	PS: O INÍCIO PODE TER VÁRIAS FAILS ATÉ ENTRAR NA OPÇÃO CORRETA.
			 * 		MESMO COMEÇO, DIFERENTES CONCLUSÕES?
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * */
			
			//(?<!(?<!\\)\\(?:\\\\){0,99})(:-)?((?:(?!-:(?:$|\n)).)*)?(?<!(?<!\\)\\(?:\\\\){0,99})(-:)?
			notPrecededByEscape()+
			group(LEFT_TAG)+zeroOrOne()+			//GROUP 1
			group(									//GROUP 2
					pseudoGroup(
							notFollowedBy(
									RIGHT_TAG+
									pseudoGroup(oneOrOther(endOfText(),lineBreak()))
							)+
							allExceptLineBreak()
					)+zeroOrMore()
			)+zeroOrOne()+
			notPrecededByEscape()+
			group(RIGHT_TAG)+zeroOrOne()			//GROUP 3
	);
	private final String escapedDefinition=(
			//((?<!\\)\\(?:\\\\)*)?(?::-)?(?:(?:(?!(?:(?<!\\)\\(?:\\\\)*)?-:(?:$|\n)).)*)?((?<!\\)\\(?:\\\\)*)?(?:-:)?
			group(precededByEscape())+zeroOrOne()+	//GROUP 1
			pseudoGroup(LEFT_TAG)+zeroOrOne()+
			pseudoGroup(
					pseudoGroup(
							notFollowedBy(
									pseudoGroup(precededByEscape())+zeroOrOne()+
									RIGHT_TAG+
									pseudoGroup(oneOrOther(endOfText(),lineBreak()))
							)+
							allExceptLineBreak()
					)+zeroOrMore()
			)+zeroOrOne()+
			group(precededByEscape())+zeroOrOne()+	//GROUP 2
			pseudoGroup(RIGHT_TAG)+zeroOrOne()
	);
//MAIN
	public MMAnchorAtributo(){}
//FUNCS
	public void applyStyle(MindMarkDocumento doc){
		final String texto=doc.getText();
		applyStyle(doc,texto);
		applyEscapedStyle(doc,texto);
	}
		private void applyStyle(MindMarkDocumento doc,String texto){
			final MMAnchorAtributo atributo=new MMAnchorAtributo();
			final Matcher match=Pattern.compile(definition).matcher(texto);
			while(match.find()){
				if(match.group(1)!=null)if(isStyled(doc,match.start(1)))continue;	//JÁ FOI ESTILIZADO
				if(match.group(3)!=null)if(isStyled(doc,match.start(3)))continue;	//JÁ FOI ESTILIZADO
				int index=-1;
				int anchor=-1;
			//TAG INI
				final int indexTagLeft=(match.group(1)!=null?match.start(1):-1);
				final int lengthTagLeft=(match.group(1)!=null?match.group(1).length():0);
				if(index==-1)index=indexTagLeft;
				if(match.group(1)!=null)anchor=StyleConstants.ALIGN_LEFT;
			//TAG FIM
				final int indexTagRight=(match.group(3)!=null?match.start(3):-1);
				final int lengthTagRight=(match.group(3)!=null?match.group(3).length():0);
				if(index==-1)index=indexTagRight;
				if(match.group(3)!=null)if(anchor==StyleConstants.ALIGN_LEFT){
					anchor=StyleConstants.ALIGN_CENTER;
				}else anchor=StyleConstants.ALIGN_RIGHT;
			//ANCHOR
				final int indexTagTexto=(match.group(2)!=null?match.start(2):-1);
				final int lengthTagTexto=(match.group(2)!=null?match.group(2).length():0);
				if(index==-1)index=indexTagTexto;
				if(anchor==-1)continue;
				StyleConstants.setAlignment(atributo,anchor);
				doc.setParagraphAttributes(index,0,atributo,false);
			//TAGS
				if(indexTagLeft!=-1){
					if(indexTagRight!=-1){		//CENTER
						final MindMarkAtributo specialAtributo=getSpecialAtributo_TwoTags(doc,indexTagLeft,lengthTagLeft,indexTagRight,lengthTagRight);
						doc.setCharacterAttributes(indexTagLeft,lengthTagLeft,specialAtributo,true);
						doc.setCharacterAttributes(indexTagRight,lengthTagRight,specialAtributo,true);
					}else{						//LEFT
						final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,indexTagLeft,lengthTagLeft,lengthTagTexto);
						doc.setCharacterAttributes(indexTagLeft,lengthTagLeft,specialAtributo,true);
					}
				}else if(indexTagRight!=-1){	//RIGHT
					final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnRight(doc,indexTagRight,lengthTagRight,lengthTagTexto);
					doc.setCharacterAttributes(indexTagRight,lengthTagRight,specialAtributo,true);
				}
			}
		}
		private  void applyEscapedStyle(MindMarkDocumento doc,String texto){
			final Matcher match=Pattern.compile(escapedDefinition).matcher(texto);
			while(match.find()){
			//TAG INI
				if(match.group(1)!=null){
					final int index=match.start(1)+match.group(1).length()-1;
					final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
					doc.setCharacterAttributes(index,1,specialAtributo,true);
				}
			//TAG FIM
				if(match.group(2)!=null){
					final int index=match.start(2)+match.group(2).length()-1;
					final MindMarkAtributo specialAtributo=getSpecialAtributo_OneTagOnLeft(doc,index,1,0);
					doc.setCharacterAttributes(index,1,specialAtributo,true);
				}
			}
		}
}