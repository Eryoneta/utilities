package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.MindMarkTexto;
@SuppressWarnings("serial")
public class MMImageAtributo extends MindMarkAtributo{
//TAGS
	private final static String START_TAG=Pattern.quote("![");
	private final static String END_TAG=Pattern.quote("]");
	private final static String START_VAR_TAG=Pattern.quote("(");
	private final static String END_VAR_TAG=Pattern.quote(")");
//DEFINITIONS
	private final static String DEFINITION=(
			pseudoGroup(oneOrOther(startOfText(),lineBreak()))+
			group(											//GROUP 1
					notPrecededBy(ESCAPE)+
					START_TAG+
					allExceptLineBreak()+zeroOrMore()+
					notPrecededBy(ESCAPE)+
					END_TAG+
					START_VAR_TAG+
					group(allExceptLineBreak()+oneOrMore())+//GROUP 2
					notPrecededBy(ESCAPE)+
					END_VAR_TAG
			)+
			pseudoGroup(oneOrOther(lineBreak(),endOfText()))
	);
//VAR GLOBAIS
	public final static String IMAGE="image";
	public final static boolean SHOW_TOOLTIP=true;
//LINK
	public static class Image{
	//LINK
		private String link;
			public String getLink(){return link;}
	//MAIN
		public Image(String link){
			this.link=link;
		}
	}
//MAIN
	public MMImageAtributo(){
		StyleConstants.setAlignment(this,StyleConstants.ALIGN_CENTER);
	}
//FUNCS
	public static void applyStyle(MindMarkDocumento doc){
		final MMImageAtributo atributo=new MMImageAtributo();
		final String texto=doc.getText();
		final Matcher textMatch=Pattern.compile(DEFINITION).matcher(texto);
		while(textMatch.find()){
		//TAG
			final int indexTag=textMatch.start(1);
			final int lengthTag=textMatch.group(1).length();
			doc.setCharacterAttributes(indexTag,lengthTag,MindMarkAtributo.SPECIAL,true);
		//IMAGE
			final String link=textMatch.group(2);
			atributo.addAttribute(IMAGE,new Image(link));
			doc.setParagraphAttributes(indexTag,0,atributo,true);
		}
	}
	public static void setListeners(MindMarkTexto texto){
		texto.addMouseMotionListener(new MouseMotionAdapter(){
			private Image lastImage=null;
			public void mouseMoved(MouseEvent m){
				final Point mouse=m.getPoint();
				final int index=texto.viewToModel(mouse);
				if(index>=0){
					final MindMarkDocumento doc=(MindMarkDocumento)texto.getDocument();
					final Element elem=doc.getCharacterElement(index);
					final AttributeSet atributos=elem.getAttributes();
					final Image image=(Image)atributos.getAttribute(IMAGE);
					if(image==lastImage)return;	//JÁ ESTÁ HOVER
					if(image!=null){
						setHovered(texto,image);
					}else setHovered(texto,null);
					lastImage=image;
				}else setHovered(texto,null);
			} 
		});
	}
		private static void setHovered(MindMarkTexto texto,Image image){
			if(image!=null){
				if(SHOW_TOOLTIP)texto.setToolTipText(image.getLink());
				if(texto.getCursor().getType()!=Cursor.HAND_CURSOR)texto.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}else{
				if(SHOW_TOOLTIP)texto.setToolTipText(null);
				if(texto.getCursor().getType()!=Cursor.DEFAULT_CURSOR)texto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
}