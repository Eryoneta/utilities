package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.MindMarkTexto;
@SuppressWarnings("serial")
public class MMLinkAtributo extends MindMarkAtributo{
//TAGS
	private final static String START_TAG=Pattern.quote("[");
	private final static String END_TAG=Pattern.quote("]");
	private final static String START_VAR_TAG=Pattern.quote("(");
	private final static String END_VAR_TAG=Pattern.quote(")");
//DEFINITIONS
	private final static String DEFINITION=(
			notPrecededBy(ESCAPE)+
			group(START_TAG)+								//GROUP 1
			group(allExceptLineBreak()+oneOrMore())+		//GROUP 2
			group(											//GROUP 3
					notPrecededBy(ESCAPE)+
					END_TAG+
					START_VAR_TAG+
					group(allExceptLineBreak()+oneOrMore())+//GROUP 4
					notPrecededBy(ESCAPE)+
					END_VAR_TAG
			)
	);
//VAR GLOBAIS
	public final static String LINK="link";
	public final static Color COLOR=new Color(0,116,204);
	public final static Color HOVER_COLOR=new Color(10,149,255);
	public final static boolean SHOW_TOOLTIP=true;
//LINK
	public static class Link{
	//LINK
		private String link;
			public String getLink(){return link;}
	//INDEX
		private int index;
			public int getIndex(){return index;}
	//LENGHT
		private int lenght;
			public int getLenght(){return lenght;}
	//MAIN
		public Link(String link,int index,int lenght){
			this.link=link;
			this.index=index;
			this.lenght=lenght;
		}
	}
//MAIN
	public MMLinkAtributo(){
		StyleConstants.setUnderline(this,true);
		StyleConstants.setForeground(this,COLOR);
	}
//FUNCS
	public static void applyStyle(MindMarkDocumento doc){
		final MMLinkAtributo atributo=new MMLinkAtributo();
		final String texto=doc.getText();
		final Matcher textMatch=Pattern.compile(DEFINITION).matcher(texto);
		while(textMatch.find()){
		//TAG INI
			final int indexTextoTagIni=textMatch.start(1);
			final int lengthTextoTagIni=textMatch.group(1).length();
			doc.setCharacterAttributes(indexTextoTagIni,lengthTextoTagIni,MindMarkAtributo.SPECIAL,true);
		//TEXTO
			final int indexTexto=textMatch.start(2);
			final int lengthTexto=textMatch.group(2).length();
			final String link=textMatch.group(4);
			atributo.addAttribute(LINK,new Link(link,indexTexto,lengthTexto));
			if(isValid(link))doc.setCharacterAttributes(indexTexto,lengthTexto,atributo,false);
		//TAG FIM
			final int indexTextoTagFim=textMatch.start(3);
			final int lengthTextoTagFim=textMatch.group(3).length();
			doc.setCharacterAttributes(indexTextoTagFim,lengthTextoTagFim,MindMarkAtributo.SPECIAL,true);
		}
	}
	private static boolean isValid(String link){
		try{
			new URL(link).toURI();
			return true;
		}catch(MalformedURLException|URISyntaxException erro){
			return false;
		}
	}
	public static void setListeners(MindMarkTexto texto){
		texto.addMouseMotionListener(new MouseMotionAdapter(){
			private Link lastLink=null;
			public void mouseMoved(MouseEvent m){
				final Point mouse=m.getPoint();
				final int index=texto.viewToModel(mouse);
				if(index>=0){
					final MindMarkDocumento doc=(MindMarkDocumento)texto.getDocument();
					final Element elem=doc.getCharacterElement(index);
					final AttributeSet atributos=elem.getAttributes();
					final Link link=(Link)atributos.getAttribute(LINK);
					if(link==lastLink)return;	//JÁ ESTÁ HOVER
					if(lastLink!=null){
						final MMLinkAtributo prevAtributo=new MMLinkAtributo();
						doc.setCharacterAttributes(lastLink.getIndex(),lastLink.getLenght(),prevAtributo,false);
					}
					if(link!=null){
						setHovered(texto,link);
						final MindMarkAtributo atributo=new MindMarkAtributo();
						StyleConstants.setForeground(atributo,HOVER_COLOR);
						doc.setCharacterAttributes(link.getIndex(),link.getLenght(),atributo,false);
					}else setHovered(texto,null);
					lastLink=link;	//MARCADO PARA SER UNHOVER
				}else setHovered(texto,null);
			} 
		});
		texto.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent m){
				final Point mouse=m.getPoint();
				final int index=texto.viewToModel(mouse);
				if(index>=0){
					final MindMarkDocumento doc=(MindMarkDocumento)texto.getDocument();
					final Element elem=doc.getCharacterElement(index);
					final AttributeSet atributos=elem.getAttributes();
					final Link link=(Link)atributos.getAttribute(LINK);
					if(link!=null)openLink(link.getLink());
					setHovered(texto,null);
				}else setHovered(texto,null);
			}
		});
	}
		private static void setHovered(MindMarkTexto texto,Link link){
			if(link!=null){
				if(SHOW_TOOLTIP)texto.setToolTipText(link.getLink());
				if(texto.getCursor().getType()!=Cursor.HAND_CURSOR)texto.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}else{
				if(SHOW_TOOLTIP)texto.setToolTipText(null);
				if(texto.getCursor().getType()!=Cursor.DEFAULT_CURSOR)texto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		private static void openLink(String link){
			try{
				Desktop.getDesktop().browse(new URL(link).toURI());
			}catch(Exception error){
				//TODO
			}
		}
}