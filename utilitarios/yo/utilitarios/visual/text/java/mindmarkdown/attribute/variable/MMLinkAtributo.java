package utilitarios.visual.text.java.mindmarkdown.attribute.variable;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import element.tree.popup.Popup;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.MindMarkTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MMLinkAtributo extends MindMarkVariableAtributo{
	//SYMBOLS
		private final static String BAD_URL="'";
	//TAGS
		public final static String BAD_URL_TAG=Pattern.quote(BAD_URL);
	//DEFINITION
		private final String paramsDefinition=(
			//	(?:(?:(?<badURL>')(?<paramJoined>[^']+)\k<badURL>)|(?<param>[^\s]+))
				pseudoGroup(oneOrOther(
						pseudoGroup(
								namedGroup("badURL",BAD_URL_TAG)+
								namedGroup("paramJoined",allExcept(BAD_URL_TAG)+oneOrMore())+
								matchOfGroup("badURL")
						),
						namedGroup("param",allExcept(space())+oneOrMore())
				))
		);
//VAR GLOBAIS
	public final static String LINK="link";
	public final static Color COLOR=new Color(0,116,204);
	public final static Color HOVER_COLOR=new Color(10,149,255);
	public final static boolean SHOW_TOOLTIP=true;
//LINK
	public static class Link{
	//LINK
		private String[]link;
			public String[]getLink(){return link;}
			public String getCompleteLink(){return String.join(" ",link);}
	//INDEX
		private int index;
			public int getIndex(){return index;}
	//LENGHT
		private int lenght;
			public int getLenght(){return lenght;}
	//MAIN
		public Link(String[]link,int index,int lenght){
			this.link=link;
			this.index=index;
			this.lenght=lenght;
		}
	}
//MAIN
	public MMLinkAtributo(){
		//(?<!:|!)...
		buildDefinitionWithURL(notPrecededBy(oneOrOther(MMTextStylerAtributo.TAG,MMImageAtributo.TAG)),"",false,false,"");
	}
//FUNCS
@Override
	protected void setURLAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String link){
		StyleConstants.setUnderline(atributo,true);
		final String[]formatedLink=formatLink(link);
		if(!isURLValid(formatedLink)&&!isPATHValid(formatedLink))return;
		StyleConstants.setForeground(atributo,COLOR);
		atributo.addAttribute(LINK,new Link(formatedLink,indexTexto,texto.length()));
	}
	private static boolean isURLValid(String[]link){
		try{
			new URL(link[0]).toURI();
			return true;
		}catch(MalformedURLException|URISyntaxException erro){
			return false;
		}
	}
	private static boolean isPATHValid(String[]link){return new File(link[0]).exists();}
	private String[]formatLink(String link){
		final List<String>formatedLink=new ArrayList<>();
		final Matcher match=Pattern.compile(paramsDefinition).matcher(link);
		while(match.find()){
			if(match.group("paramJoined")!=null)formatedLink.add(match.group("paramJoined"));
			if(match.group("param")!=null)formatedLink.add(match.group("param"));
		}
		return formatedLink.toArray(new String[0]);
	}
	public static void setListeners(MindMarkTexto texto){
		texto.addMouseMotionListener(new MouseMotionAdapter(){
			private Link lastLink=null;
			private Color lastLinkColor=null;
			public void mouseMoved(MouseEvent m){
				final Point mouse=m.getPoint();
				SwingUtilities.invokeLater(new Runnable(){
					@Override public void run(){
						updateInterface(mouse);
					}
				});
			}
			private synchronized void updateInterface(Point mouse){
				final int index=texto.viewToModel(mouse);
				if(index>=0){
					final MindMarkDocumento doc=(MindMarkDocumento)texto.getDocument();
					final Element character=doc.getCharacterElement(index);
					final Link link=(Link)character.getAttributes().getAttribute(LINK);
					final Color cor=(Color)character.getAttributes().getAttribute(StyleConstants.Foreground);
					if(link==lastLink)return;	//JÁ ESTÁ HOVER
					if(lastLink!=null){
						final MindMarkAtributo atributo=new MindMarkAtributo();
						if(lastLinkColor!=null) {
							StyleConstants.setForeground(atributo,lastLinkColor);
						}else StyleConstants.setForeground(atributo,COLOR);
						doc.setCharacterAttributes(lastLink.getIndex(),lastLink.getLenght(),atributo,false);
					}
					if(link!=null){
						final MindMarkAtributo atributo=new MindMarkAtributo();
						if(cor!=null){
							final Color newCor=getBrighterColor(cor);
							StyleConstants.setForeground(atributo,newCor);
						}else StyleConstants.setForeground(atributo,HOVER_COLOR);
						doc.setCharacterAttributes(link.getIndex(),link.getLenght(),atributo,false);
						setTooltip(texto,link);
						setCursorToHand(texto,true);
					}else{
						setTooltip(texto,null);
						setCursorToHand(texto,false);
					}
					lastLink=link;			//MARCADO PARA SER UNHOVER
					lastLinkColor=cor;		//MARCADO PARA REDEFINIR COR
				}else{
					setTooltip(texto,null);
					setCursorToHand(texto,false);
				}
			}
		});
		texto.addMouseListener(new MouseAdapter(){
			private int index=0;
			public void mousePressed(MouseEvent m){
				final Point mouse=m.getPoint();
				SwingUtilities.invokeLater(new Runnable(){
					@Override public void run(){
						index=findIndex(mouse);
					}
				});
			}
			public void mouseReleased(MouseEvent m){
				final Point mouse=m.getPoint();
				SwingUtilities.invokeLater(new Runnable(){
					@Override public void run(){
						updateInterface(mouse);
					}
				});
			}
			private synchronized int findIndex(Point mouse){return texto.viewToModel(mouse);}
			private synchronized void updateInterface(Point mouse){
				final int index=findIndex(mouse);
				if(index>=0&&index==this.index){
					final MindMarkDocumento doc=(MindMarkDocumento)texto.getDocument();
					final Element character=doc.getCharacterElement(index);
					final Link link=(Link)character.getAttributes().getAttribute(LINK);
					if(link!=null)openLink(link.getLink());
					setTooltip(texto,null);
					setCursorToHand(texto,false);
				}
			}
		});
	}
		private static void setTooltip(MindMarkTexto texto,Link link){
			if(!SHOW_TOOLTIP)return;
			if(link!=null){
				Popup.showTooltip(true);
				texto.setToolTipText(link.getCompleteLink());
			}else{
				Popup.showTooltip(false);
				texto.setToolTipText(null);
			}
		}
		private static void setCursorToHand(MindMarkTexto texto,boolean hand){
			if(hand){
				if(texto.getCursor().getType()!=Cursor.HAND_CURSOR)texto.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}else {
				if(texto.getCursor().getType()!=Cursor.DEFAULT_CURSOR)texto.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		private static Color getBrighterColor(Color cor){
			final int corBrilho=(int)Math.sqrt((
					cor.getRed()*cor.getRed()*0.241)
					+(cor.getGreen()*cor.getGreen()*0.691)
					+(cor.getBlue()*cor.getBlue()*0.068));
			final boolean isBright=(corBrilho>177);
			final float brilho=(isBright?0.70f:1.30f);
			return new Color(	//CADA CANAL DE COR É CALIBRADO PARA SER BASTANTE AFETADO PELA MUDANÇA DE BRILHO.
					Math.min(255,(int)(Math.max(1,cor.getRed())*brilho*(cor.getRed()<10?100:(cor.getRed()<100?10:1)))),
					Math.min(255,(int)(Math.max(1,cor.getGreen())*brilho*(cor.getGreen()<10?100:(cor.getGreen()<100?10:1)))),
					Math.min(255,(int)(Math.max(1,cor.getBlue())*brilho*(cor.getBlue()<10?100:(cor.getBlue()<100?10:1)))),
					cor.getAlpha());
		}
		private static void openLink(String[]link){
			try{
				if(isURLValid(link)){
					Desktop.getDesktop().browse(new URL(link[0]).toURI());
				}else if(isPATHValid(link)){
					Runtime.getRuntime().exec(link);
				}
			}catch(Exception error){}//TODO
		}
}