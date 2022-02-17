package utilitarios.visual.text.java.mindmarkdown.attribute.variable;
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
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import element.tree.popup.Popup;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.MindMarkTexto;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MMLinkAtributo extends MindMarkVariableAtributo{
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
		buildDefinitionWithURL("",false);
	}
//FUNCS
@Override
	protected void setURLAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String url){
		if(!isValid(url))return;
		StyleConstants.setUnderline(atributo,true);
		StyleConstants.setForeground(atributo,COLOR);
		atributo.addAttribute(LINK,new Link(url,indexTexto,texto.length()));
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
				if(SHOW_TOOLTIP){
					Popup.showTooltip(true);
					texto.setToolTipText(link.getLink());
				}
				if(texto.getCursor().getType()!=Cursor.HAND_CURSOR)texto.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}else{
				if(SHOW_TOOLTIP){
					Popup.showTooltip(false);
					texto.setToolTipText(null);
				}
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