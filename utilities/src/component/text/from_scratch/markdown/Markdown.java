package component.text.from_scratch.markdown;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComponent;
@SuppressWarnings("serial")
public class Markdown extends JComponent{
//TEXTO
	private String texto="";
		public String getText(){return texto;}
		public void setText(String texto){
			this.texto=texto;
			setLinhas();
		}
//WRAP
	private boolean wrap=false;
		public boolean isWrapped(){return wrap;}
		public void setWrap(boolean wrap){
			this.wrap=wrap;
//			setPreferredSize(size);
		}
//DIMENSIONS
	private int width=0;
		public int getWidth(){return width;}
		public void updateWidth(){
			width=0;
			for(Linha linha:linhas){
				width=Math.max(width,linha.getWidth());
			}
		}
	private int height=0;
		public int getHeight(){return height;}
		public void updateHeight(){
			height=0;
			for(int i=0,size=linhas.size();i<size;i++){
				final Linha linha=linhas.get(i);
				height+=linha.getHeight();
			}
		}
//LINHAS
	private List<Linha>linhas=new ArrayList<>();
		public int getLenght(){return linhas.size();}
		private void setLinhas(){
			linhas.clear();
			final String texto=getText();
			final Matcher match=Pattern.compile(Linha.DEFINITION).matcher(texto);
			while(match.find()){
				final int index=match.start();
				final int length=match.group().length();
				if(index<0||length<=0)continue;
				final Linha linha=new Linha(this);
				linhas.add(linha);
				linha.setContent(match.group());
			}
			updateWidth();
			updateHeight();
			setPreferredSize(new Dimension(getWidth(),getHeight()));
		}
//MAIN
	public Markdown(){}
//FUNCS
	public int getRelativeIndexOf(Linha linha){
		int index=0;
		for(Linha l:linhas){
			if(l==linha)return index;
			index+=linha.getLenght();
		}
		return -1;
	}
@Override
	public void paintComponent(Graphics g){
	//CONFIG TEXTO
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);	//ADICIONA BORDA, CONTRASTE
		g.setFont(getFont());
		for(int i=0;i<linhas.size();i++){
			final Linha linha=linhas.get(i);
			if(isWrapped()){
//				while(){
					g.translate(0,linha.getHeight());
					linha.draw(g);
//				}
			}else{
				g.translate(0,linha.getHeight());
				linha.draw(g);
			}
		}
	}
}
//-----------------------------------------------------------------------------------------------
class Linha{
//VAR GLOBAIS
	public static String DEFINITION="(.*\n?)";	//TEXTO ENTRE LINE_BREAKS, SEGUIDO OU NÃO POR 1 LINE_BREAK
//PAI
	private Markdown pai;
		public Markdown getTextParent(){return pai;}
//DIMENSIONS
	private int width=0;
		public int getWidth(){return width;}
		public void updateWidth(){
			width=0;
			for(Caractere caractere:caracteres){
				width+=caractere.getWidth();
			}
		}
	private int height=0;
		public int getHeight(){return height;}
		public void updateHeight(){
			height=0;
			for(Caractere caractere:caracteres){
				height=Math.max(height,caractere.getHeight());
			}
		}
//CARACTERES
	private List<Caractere>caracteres=new ArrayList<>();
		public int getLenght(){return caracteres.size();}
		private void setCaracters(String linha){
			caracteres.clear();
			for(int i=0;i<linha.length();i++){
				final Caractere caractere=new Caractere(this);
				caracteres.add(caractere);
			}
			updateWidth();
			updateHeight();
		}
//MAIN
	public Linha(Markdown texto){
		this.pai=texto;
	}
	protected void setContent(String linha){
		setCaracters(linha);
	}
//FUNCS
	public int getAbsoluteIndex(){
		return getTextParent().getRelativeIndexOf(this);
	}
	public int getRelativeIndexOf(Caractere caractere){
		int index=0;
		for(Caractere c:caracteres){
			if(c==caractere)return index;
			index+=1;
		}
		return -1;
	}
//DRAW
	public void draw(Graphics g){
		for(Caractere caractere:caracteres)caractere.draw(g);
	}
}
//-----------------------------------------------------------------------------------------------
class Caractere{
//PAI
	private Linha pai;
		public Linha getLineParent(){return pai;}
//CHAR
	private char caractere;
		public char getCaractere(){return caractere;}
//SIZE
	public int getWidth(){
		final Markdown texto=getLineParent().getTextParent();
		final Graphics g=texto.getGraphics();
		final int width=g.getFontMetrics(texto.getFont()).charWidth(getCaractere());
		return width;
	}
	public int getHeight(){
		final Markdown texto=getLineParent().getTextParent();
		final Graphics g=texto.getGraphics();
		final int height=g.getFontMetrics(texto.getFont()).getHeight();//TODO: CONSIDERAR CUSTOM ATTR
		return height;
	}
//MAIN
	public Caractere(Linha linha){
		this.pai=linha;
	}
//FUNCS
	public int getAbsoluteIndex(){
		return getLineParent().getAbsoluteIndex()+getLineParent().getRelativeIndexOf(this);
	}
//DRAW
	public void draw(Graphics g){
		g.setColor(Color.BLACK);//TODO:DEVE SER CUSTOM
		g.drawString(String.valueOf(getCaractere()),0,getHeight());
	}
}