package element.tree.main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import element.tree.Chunk;
import element.tree.Cursor;
import element.tree.Selecao;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.ConexaoST;
import element.tree.objeto.modulo.Modulo;
import element.tree.objeto.modulo.ModuloUI;
import element.tree.objeto.nodulo.Nodulo;
import element.tree.objeto.nodulo.NoduloST;
import element.tree.popup.Popup;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Icone;
import element.tree.texto.ObjetoSetListener;
import element.tree.texto.Texto;
import element.tree.texto.Titulo;
import utilitarios.ferramenta.language.LanguagePackage;
@SuppressWarnings("serial")
public class TreeUI{
//TREE
	private Tree tree;
//VAR GLOBAIS
	public static float getBordaValue(int unit){return unit*0.3f;}
//LANG
	private static LanguagePackage LANG=new LanguagePackage();
		public static LanguagePackage getLang(){return LANG;}
		public static void addLanguage(File link,String idiomaFiltro,String prefixFiltro){
			LANG.add(link,idiomaFiltro,prefixFiltro);
		}
//FONTE
	public static class Fonte{
		public static Font FONTE;
		public static Cor FUNDO=new Cor(255,255,255);
		public static Cor FUNDO_SELECT=new Cor(155,155,155);
		public static Cor FUNDO_UNSELECT=new Cor(230,230,230);
		public static Cor DARK=new Cor(10,10,10);
		public static Cor LIGHT=new Cor(245,245,245);
		public static Cor SELECTED=new Cor(255,255,255);
		public static Cor UNSELECTED=new Cor(80,80,80);
	}
		public void setFonte(Font fonte){
			Fonte.FONTE=fonte;
			getTitulo().setFont(fonte);
			getTexto().setFont(fonte);
			for(Modulo mod:tree.getObjetos().getModulos())mod.setSize();
		}
		public static Font getFonte(){return Fonte.FONTE;}
//COR DE FUNDO
	public static Cor FUNDO=new Cor(240,240,240);
//MENU
	public static class Menu{
		public static Cor FUNDO=new Cor(220,220,220);
		public static Cor SELECT=new Cor(154,210,255);
		public static Cor SELECT_DISABLED=new Cor(240,240,240);
		public static Cor FONTE=new Cor(10,10,10);
		public static Cor FONTE_DISABLED=new Cor(109,109,109);
	}
//POPUP
	private Popup popup;
		public Popup getPopup(){return popup;}
//SELEÇÃO
	private Selecao selecao=new Selecao();
		public Selecao getSelecao(){return selecao;}
//CURSOR
	private Cursor cursor;
		public Cursor getCursor(){return cursor;}
//TITULO
	private Titulo titulo;
		public Titulo getTitulo(){return titulo;}
		public void buildTitulo(){
			titulo=new Titulo(tree){{
				setOpaque(false);
				setEnabled(true);
				setFont(TreeUI.Fonte.FONTE);
				setForeground(Cor.BLACK);
				setLineWrappable(false);
				final SimpleAttributeSet center=new SimpleAttributeSet();
				StyleConstants.setAlignment(center,StyleConstants.ALIGN_CENTER);
				getStyledDocument().setParagraphAttributes(0,getStyledDocument().getLength(),center,false);
				getDocument().addDocumentListener(new DocumentListener(){
				@Override public void removeUpdate(DocumentEvent d){updateObjTitle();}
				@Override public void insertUpdate(DocumentEvent d){updateObjTitle();}
				@Override public void changedUpdate(DocumentEvent d){updateObjTitle();}
					private void updateObjTitle(){		//ALTERA IMEDIATAMENTE PARA PODER SALVAR COM O TEXTO ABERTO
						if(titulo==null)return;
						if(titulo.getObjeto()==null)return;
						if(!titulo.getObjeto().getTipo().is(Objeto.Tipo.MODULO))return;
						final Modulo mod=(Modulo)titulo.getObjeto();
						mod.setTitle(getText());
						final int iconSize=(mod.isIconified()?Icone.getSize(Tree.UNIT):0);
						titulo.setBounds(mod.getX(Tree.UNIT),mod.getY(Tree.UNIT)+iconSize,mod.getWidth(Tree.UNIT),mod.getHeight(Tree.UNIT)-iconSize);
//									titulo.setFont(mod.getRelativeFont(Tree.UNIT));	//CAUSA ERRO
						tree.draw();
						final JFrame janela=(JFrame)tree.getPainel().getJanela();
						if(!janela.getTitle().startsWith("*")){
							janela.setTitle("*"+janela.getTitle());
						}
					}
				});
				addKeyListener(new KeyAdapter(){
				@Override public void keyPressed(KeyEvent k){
						updateTituloFont();
					}
				});
				addObjetoSetListener(new ObjetoSetListener(){
					private Modulo mod=null;
					private String tituloTexto=null;
				@Override public void objetoModified(Objeto oldObj,Objeto newObj){
						if(oldObj!=null&&!oldObj.getTipo().is(Objeto.Tipo.MODULO))return;
						if(newObj!=null&&!newObj.getTipo().is(Objeto.Tipo.MODULO))return;
						if(oldObj==newObj)return;
						if(mod!=null&&tituloTexto!=null){
							if(getUndoManager().canUndo()){
								tree.getUndoRedoManager().addUndoTitulo(mod,tituloTexto);
							}
						}
						final Modulo mod=(Modulo)newObj;
						if(mod==null){
							this.mod=null;
							tituloTexto=null;
						}else{
							this.mod=mod;
							tituloTexto=mod.getTitle();
						}
					}
				});
				addKeyListener(new KeyAdapter(){
				@Override public void keyPressed(KeyEvent k){
						switch(k.getKeyCode()){
							case KeyEvent.VK_TAB:		k.consume();	break;
						}
					}
				});
				setVisible(false);
			}};
		}
		public void setTitulo(Modulo mod){
			final int borda=(int)(TreeUI.getBordaValue(Tree.UNIT));
			titulo.setBorder(BorderFactory.createEmptyBorder(borda,borda,borda,borda));
			if(mod==null||mod==Tree.getGhost()){
				tree.triggerObjetoListener(titulo.getObjeto(),true);	//ATUALIZA TITULO DE JANELA-TEXTO
				titulo.setVisible(false);
				titulo.clearObjeto();		//NÃO MODIFICAR MOD/COX ANTERIOR
				titulo.setText("");
				titulo.crearUndo();
				tree.getPainel().getJanela().requestFocus();
				return;
			}
			titulo.clearObjeto();		//NÃO MODIFICAR MOD/COX ANTERIOR
			titulo.setText(mod.getTitle());
			final int iconSize=(mod.isIconified()?Icone.getSize(Tree.UNIT):0);
			titulo.setBounds(mod.getX(Tree.UNIT),mod.getY(Tree.UNIT)+iconSize,mod.getWidth(Tree.UNIT),mod.getHeight(Tree.UNIT)-iconSize);
			titulo.setObjeto(mod);
			updateTituloFont();
			titulo.crearUndo();
			titulo.setVisible(true);
			titulo.setSelectionStart(0);
			titulo.setSelectionEnd(titulo.getText().length());
			titulo.requestFocus();
		}
		public void updateTituloFont(){
			final Modulo mod=(Modulo)titulo.getObjeto();
			titulo.setFont(mod.getRelativeFont(Tree.UNIT));
		//NÃO É PRECISO O BASTANTE, E NÃO ATUALIZA DE FORMA UNIFORME
//					final Graphics imagemEdit=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB).getGraphics();
//					imagemEdit.setFont(titulo.getFont());
//					final int fonteHeight=imagemEdit.getFontMetrics().getHeight();
//					final double linhaHeight=((double)mod.getHeight()/mod.getTitle().split("\n").length);
//					final double space=(double)((linhaHeight-fonteHeight)/linhaHeight);
//					final MutableAttributeSet set=new SimpleAttributeSet(titulo.getParagraphAttributes());
//					StyleConstants.setLineSpacing(set,(float)space);
//					titulo.setParagraphAttributes(set,false);
		}
//TEXTO
	private Texto texto;
		public Texto getTexto(){return texto;}
		public void buildTexto(){
			texto=new Texto(){{
				setEnabled(false);
				setFont(TreeUI.Fonte.FONTE);
				setLineWrappable(true);
				getDocument().addDocumentListener(new DocumentListener(){
				@Override public void removeUpdate(DocumentEvent d){updateObjText();}
				@Override public void insertUpdate(DocumentEvent d){updateObjText();}
				@Override public void changedUpdate(DocumentEvent d){updateObjText();}
					private void updateObjText(){		//ALTERA IMEDIATAMENTE PARA PODER SALVAR COM O TEXTO ABERTO
						if(texto==null)return;
						if(texto.getObjeto()==null)return;
						if(!texto.getObjeto().getTipo().is(Objeto.Tipo.MODULO,Objeto.Tipo.CONEXAO))return;
						if(texto.getObjeto().getTipo().is(Objeto.Tipo.MODULO)){
							final Modulo mod=(Modulo)texto.getObjeto();
							mod.setTexto(Arrays.asList(getText().split("\n",-1)));
						}else if(texto.getObjeto().getTipo().is(Objeto.Tipo.CONEXAO)){
							final Conexao cox=(Conexao)texto.getObjeto();
							cox.setTexto(Arrays.asList(getText().split("\n",-1)));
						}
						final JFrame janela=(JFrame)tree.getPainel().getJanela();
						if(!janela.getTitle().startsWith("*")){
							janela.setTitle("*"+janela.getTitle());
						}
					}
				});
				addObjetoSetListener(new ObjetoSetListener(){
					private Objeto obj=null;
					private List<String>textoTexto=null;
				@Override public void objetoModified(Objeto oldObj,Objeto newObj){
						if(oldObj!=null&&!oldObj.getTipo().is(Objeto.Tipo.MODULO,Objeto.Tipo.CONEXAO))return;
						if(newObj!=null&&!newObj.getTipo().is(Objeto.Tipo.MODULO,Objeto.Tipo.CONEXAO))return;
						if(oldObj==newObj)return;
						if(obj!=null&&textoTexto!=null){
							if(getUndoManager().canUndo()){
								tree.getUndoRedoManager().addUndoTexto(obj,textoTexto);
							}
						}
						if(newObj==null){
							obj=null;
							textoTexto=null;
						}else{
							obj=newObj;
							if(newObj.getTipo().is(Objeto.Tipo.MODULO)){
								final Modulo mod=(Modulo)newObj;
								textoTexto=new ArrayList<String>(mod.getTexto());
							}else if(newObj.getTipo().is(Objeto.Tipo.CONEXAO)){
								final Conexao cox=(Conexao)newObj;
								textoTexto=new ArrayList<String>(cox.getTexto());
							}
						}
					}
				});
			}};
		}
		public void setTexto(Objeto obj){
			texto.setEnabled(false);
			if(obj==Tree.getGhost()){
				texto.clearObjeto();
				texto.setText("");
				texto.crearUndo();
				texto.setCaretPosition(0);
				return;
			}
			texto.clearObjeto();	//NÃO MODIFICAR MOD/COX ANTERIOR
			if(obj.getTipo().is(Objeto.Tipo.MODULO)){
				final Modulo mod=(Modulo)obj;
				texto.setText(mod.getText());
				texto.setObjeto(mod);
				texto.crearUndo();
				texto.setCaretPosition(Math.min(mod.getCaret(),Math.max(texto.getText().length(),0)));
			}else if(obj.getTipo().is(Objeto.Tipo.CONEXAO)){
				final Conexao cox=(Conexao)obj;
				texto.setText(cox.getText());
				texto.setObjeto(cox);
				texto.crearUndo();
				texto.setCaretPosition(Math.min(cox.getCaret(),Math.max(texto.getText().length(),0)));
			}
			texto.setEnabled(true);
		}
//MAIN
	public TreeUI(Tree tree){this.tree=tree;}
	public void build(){
		buildTitulo();
		buildTexto();
		popup=new Popup(tree);
		popup.update();
		cursor=new Cursor(((JFrame)tree.getPainel().getJanela()));
	}
//DRAW
	public void draw(Graphics2D imagemEdit){
		final int unit=Tree.UNIT;
		final float borda=TreeUI.getBordaValue(unit);
		if(((JFrame)tree.getPainel().getJanela()).isUndecorated()){
			tree.clearDraw();
		}
		final boolean antialias=(tree.isAntialias()&&(tree.getObjetos().size()<tree.getObjetosLimite()||tree.getObjetosLimite()==-1));	//ANTIALIAS
		imagemEdit.setRenderingHint(RenderingHints.KEY_ANTIALIASING,(antialias?RenderingHints.VALUE_ANTIALIAS_ON:RenderingHints.VALUE_ANTIALIAS_OFF));
		final Rectangle tela=new Rectangle(
				tree.getRelativeBounds().x-unit,
				tree.getRelativeBounds().y-unit,
				tree.getRelativeBounds().width+(unit*2),
				tree.getRelativeBounds().height+(unit*2)
		);
		if(!((JFrame)tree.getPainel().getJanela()).isUndecorated()){
			drawFundo(imagemEdit,tela,unit);
		}
		drawObjetos(imagemEdit,tela,unit,borda);
		drawFrente(imagemEdit);
		imagemEdit.dispose();
	}
		private void drawFundo(Graphics2D imagemEdit,Rectangle tela,int unit){
			if(unit<=2){	//COR DA GRADE, COBRINDO TUDO
				imagemEdit.setColor(Cor.getChanged(TreeUI.FUNDO,0.95f));
			}else imagemEdit.setColor(TreeUI.FUNDO);
			imagemEdit.fillRect(0,0,tree.getWidth(),tree.getHeight());						//FUNDO
			final boolean showChunks=false;
			if(showChunks){		//DEBUG
				final Rectangle chunksInd=tree.getChunksIndexes(-Tree.getLocalX(),-Tree.getLocalY(),tela.width/unit,tela.height/unit);
				for(int y=chunksInd.y;y-chunksInd.y<chunksInd.height;y++){
					for(int x=chunksInd.x;x-chunksInd.x<chunksInd.width;x++){
						imagemEdit.setColor(y%2==0&&x%2==0||y%2!=0&&x%2!=0?ModuloUI.Cores.CREATE:ModuloUI.Cores.PAI);	//XAND: 1001
						final Chunk chunk=tree.getChunks().get(new Point(x,y));
						if(chunk!=null)imagemEdit.setColor(ModuloUI.Cores.SON);
						final int size=unit*Chunk.SIZE;
						final int X=(x*size)+(Tree.getLocalX()*unit);
						final int Y=(y*size)+(Tree.getLocalY()*unit);
						imagemEdit.fillRect(X,Y,size,size);
						imagemEdit.setColor(Color.RED);
						imagemEdit.drawString(x+","+y,X,Y+size);
					}
				}
			}
			if(tree.isShowingGrid()&&unit>2){													//GRADE
				imagemEdit.setColor(Cor.getChanged(TreeUI.FUNDO,0.95f));
				for(int x=0,size=tree.getWidth()/unit;x<=size;x++)imagemEdit.drawLine(unit*x,tree.getY(),unit*x,tree.getHeight());
				for(int y=0,size=tree.getHeight()/unit;y<=size;y++)imagemEdit.drawLine(tree.getX(),unit*y,tree.getWidth(),unit*y);					
			}
		}
		private void drawObjetos(Graphics2D imagemEdit,Rectangle tela,int unit,float borda){
			tree.getVisibleMods().clear();
			tree.getVisibleCoxs().clear();
			tree.getVisibleNods().clear();
			final Rectangle chunksIndexes=tree.getChunksIndexes(-Tree.getLocalX(),-Tree.getLocalY(),tela.width/unit,tela.height/unit);
			for(int y=chunksIndexes.y;y-chunksIndexes.y<chunksIndexes.height;y++){
				for(int x=chunksIndexes.x;x-chunksIndexes.x<chunksIndexes.width;x++){
					final Chunk chunk=tree.getChunks().get(new Point(x,y));
					if(chunk==null)continue;
					for(Conexao cox:chunk.getObjetos().getConexoes()){
						tree.getVisibleCoxs().put(cox.getIndex(),cox);
					}
					for(Nodulo nod:chunk.getObjetos().getNodulos()){
						tree.getVisibleNods().put(nod.getIndex(),nod);
					}
					for(Modulo mod:chunk.getObjetos().getModulos()){
						tree.getVisibleMods().put(mod.getIndex(),mod);
					}
				}
			}
			imagemEdit.setStroke(new BasicStroke((int)borda));	//BORDA DOS OBJS
		//DRAW CONEXÕES
			for(Conexao cox:tree.getVisibleCoxs().values()){
				if(cox.getState().is(ConexaoST.State.UNSELECTED))cox.draw(imagemEdit,unit);	//COXS DESELECIONADOS
			}
			for(Conexao cox:tree.getVisibleCoxs().values()){
				if(!cox.getState().is(ConexaoST.State.UNSELECTED))cox.draw(imagemEdit,unit);	//COXS SELECIONADOS FICAM NA FRENTE
			}
		//DRAW NÓDULOS
			for(Nodulo nod:tree.getVisibleNods().values()){
				if(nod.getState().is(NoduloST.State.UNSELECTED))nod.draw(imagemEdit,unit);	//NODS DESELECIONADOS
			}
			for(Nodulo nod:tree.getVisibleNods().values()){
				if(!nod.getState().is(NoduloST.State.UNSELECTED))nod.draw(imagemEdit,unit);	//NODS SELECIONADOS FICAM NA FRENTE
			}
		//DRAW MÓDULOS
			for(Modulo mod:tree.getVisibleMods().values())mod.draw(imagemEdit,unit);				//MODS
		}
		private void drawFrente(Graphics2D imagemEdit){
			if(!getSelecao().isEmpty())getSelecao().draw(imagemEdit);								//ÁREA DE SELEÇÃO
			if(((JFrame)tree.getPainel().getJanela()).isUndecorated()){							//BORDA PARA PERMITIR DRAG
				imagemEdit.setStroke(new BasicStroke(10));
				final Color f=TreeUI.FUNDO;
				imagemEdit.setColor(new Color((float)f.getRed()/255,(float)f.getGreen()/255,(float)f.getBlue()/255,0.6f));
				imagemEdit.drawRect(0,0,tree.getWidth(),tree.getHeight());
			}
			if(tree.getState()==TreeST.AUTO_DRAG_ALL+TreeST.NORMAL){			//SETA DO AUTODRAG
				drawAutoDragLine(imagemEdit);
			}
			if(!tree.isEnabled()){															//EFEITO DE ACINZENTADO
				final Color f=TreeUI.FUNDO;
				imagemEdit.setColor(new Color((float)f.getRed()/255,(float)f.getGreen()/255,(float)f.getBlue()/255,0.6f));
				imagemEdit.fillRect(0,0,tree.getWidth(),tree.getHeight());
			}
		}
			private void drawAutoDragLine(Graphics2D imagemEdit){
				imagemEdit.setStroke(new BasicStroke(1));
			//PONTOS
				final Point ponto1=tree.getActions().getNonGridPosition(tree.getActions().mouseReleased);
				final Point ponto2=tree.getActions().getNonGridPosition(tree.getActions().mouseMoved);
				final int difX=ponto2.x-ponto1.x;
				final int difY=ponto2.y-ponto1.y;
			//ANGLES
				double angle=Math.atan2(difY,difX);
				double angleReto1=angle+Math.toRadians(90);
				double angleReto2=angle-Math.toRadians(90);
			//SIZES
				final double size=(Math.abs(difX)+Math.abs(difY))/4;
				final double pontaWidth=size/2;
				final double pontaBaseWidth=size/4;
				Point2D pontaBase=null;
			//AJUSTES
				if(angle>Math.toRadians(90)){			//90º -> 180º
					angle=Math.abs(Math.toRadians(180)-angle);
					pontaBase=new Point2D.Double(ponto2.x+(size*Math.cos(angle)),ponto2.y-(size*Math.sin(angle)));
				}else if(angle>Math.toRadians(0)){		//0º -> 90º
					pontaBase=new Point2D.Double(ponto2.x-(size*Math.cos(angle)),ponto2.y-(size*Math.sin(angle)));
				}else if(angle>Math.toRadians(-90)){	//-90º -> 0º
					angle=Math.abs(Math.toRadians(180)-angle);
					pontaBase=new Point2D.Double(ponto2.x+(size*Math.cos(angle)),ponto2.y-(size*Math.sin(angle)));
				}else if(angle>Math.toRadians(-180)){	//-180º -> -90º
					pontaBase=new Point2D.Double(ponto2.x-(size*Math.cos(angle)),ponto2.y-(size*Math.sin(angle)));
				}
			//SETA
				final Point2D arrowPonta1=new Point2D.Double(pontaBase.getX()-(pontaWidth*Math.cos(angleReto1)),pontaBase.getY()-(pontaWidth*Math.sin(angleReto1)));
				final Point2D arrowReta1=new Point2D.Double(pontaBase.getX()-(pontaBaseWidth*Math.cos(angleReto1)),pontaBase.getY()-(pontaBaseWidth*Math.sin(angleReto1)));
				final Point2D arrowPonta2=new Point2D.Double(pontaBase.getX()-(pontaWidth*Math.cos(angleReto2)),pontaBase.getY()-(pontaWidth*Math.sin(angleReto2)));
				final Point2D arrowReta2=new Point2D.Double(pontaBase.getX()-(pontaBaseWidth*Math.cos(angleReto2)),pontaBase.getY()-(pontaBaseWidth*Math.sin(angleReto2)));
				final Path2D.Float seta=new Path2D.Float();
				seta.moveTo(ponto1.x,ponto1.y);							//BASE
				seta.lineTo(arrowReta1.getX(),arrowReta1.getY());		//MEIO DIREITO
				seta.lineTo(arrowPonta1.getX(),arrowPonta1.getY());		//PONTA DIREITA
				seta.lineTo(ponto2.getX(),ponto2.getY());				//PONTA
				seta.lineTo(arrowPonta2.getX(),arrowPonta2.getY());		//PONTA ESQUERDA
				seta.lineTo(arrowReta2.getX(),arrowReta2.getY());		//MEIO ESQUERDO
				seta.closePath();										//BASE
			//DRAW
				final int XRadius=10;
				imagemEdit.setColor(Cor.getChanged(ModuloUI.Cores.FUNDO,ModuloUI.Cores.FUNDO.isDark()?1.1f:0.8f));
				imagemEdit.drawLine(ponto1.x-XRadius,ponto1.y,ponto1.x+XRadius,ponto1.y);
				imagemEdit.drawLine(ponto1.x,ponto1.y-XRadius,ponto1.x,ponto1.y+XRadius);
				imagemEdit.setColor(Cor.getTransparent(Cor.getChanged(ModuloUI.Cores.FUNDO,ModuloUI.Cores.FUNDO.isDark()?1.1f:0.8f),0.6f));
				imagemEdit.fill(seta);
				imagemEdit.setColor(Cor.getChanged(ModuloUI.Cores.FUNDO,ModuloUI.Cores.FUNDO.isDark()?1.1f:0.8f));
				imagemEdit.draw(seta);
			}
//MENSAGEM
	public enum Options{
		ERRO,
		AVISO;
	}
	public static void mensagem(String mensagem,Options tipo){
		Toolkit.getDefaultToolkit().beep();
		switch(tipo){
			case AVISO:	JOptionPane.showMessageDialog(null,mensagem,TreeUI.getLang().get("T_Av","Warning!"),JOptionPane.WARNING_MESSAGE);break;
			case ERRO:	JOptionPane.showMessageDialog(null,mensagem,TreeUI.getLang().get("T_Err","Error...!"),JOptionPane.ERROR_MESSAGE);break;
		}
	}
}