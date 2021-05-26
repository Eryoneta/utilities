package element.tree;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import element.tree.Cor;
import element.tree.Icone;
import element.Elemento;
import element.Painel;
import element.tree.objeto.ListaObjeto;
import element.tree.objeto.Objeto;
import element.tree.objeto.ObjetoBoundsListener;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.Nodulo;
import element.tree.objeto.conexao.Segmento;
import element.tree.objeto.modulo.Modulo;
import element.tree.popup.Popup;
import element.tree.texto.ObjetoSetListener;
import element.tree.texto.Texto;
import element.tree.undoRedo.UndoRedo;
@SuppressWarnings("serial")
public class Tree extends Elemento{
//LOCAL
	private static int X=0;
	public static int getLocalX(){return X;}
	private static int Y=0;
		public static int getLocalY(){return Y;}
	public static Point getLocal(){return new Point(X,Y);}
	public static void setLocal(int x,int y){X=x;Y=y;}
//VAR GLOBAIS
	public static int UNIT=8;
	public static float getBordaValue(){return UNIT*0.3f;}
//MÓDULOS ESPECIAIS
	private static Modulo MESTRE=new Modulo(0,0,"Novo Mind"){{
		setCor(new Cor(0,255,255));
	}};
		public static Modulo getMestre(){return MESTRE;}
	private static Modulo GHOST=new Modulo(0,0,""){{
		setTexto("Texto");
		setCor(new Cor(0,0,0));
	}};
		public static Modulo getGhost(){return GHOST;}
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
			titulo.setFont(fonte);
			texto.setFont(fonte);
			for(Modulo mod:getObjetos().getModulos())mod.setSize();
		};
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
//CONFIGURAÇÕES
	private int objetosLimite=100;
		public int getObjetosLimite(){return objetosLimite;}
		public void setObjetosLimite(int limite){objetosLimite=limite;}
	private boolean showGrid=false;
		public boolean isShowingGrid(){return showGrid;}
		public void setShowGrid(boolean show){showGrid=show;}	
	private boolean antialias=true;
		public boolean isAntialias(){return antialias;}
		public void setAntialias(boolean antialias){this.antialias=antialias;}
	private boolean enabled=true;
		public void setEnabled(boolean enabled){
			this.enabled=enabled;
			draw();
		}
		public boolean isEnabled(){return enabled;}
//AÇÕES
	private Actions actions;
		public Actions getActions(){return actions;}
//POPUP
	private Popup popup;
		public Popup getPopup(){return popup;}
//SELEÇÃO
	private Selecao selecao=new Selecao();
		public Selecao getSelecao(){return selecao;}
//UNDO-REDO
	private UndoRedo undoRedo;
		public UndoRedo getUndoRedoManager(){return undoRedo;}
//CURSOR
	private Cursor cursor;
		public Cursor getCursor(){return cursor;}
//LISTENER: DISPARA COM O MUDAR DE FOCO DE UM OBJETO
	private List<ObjetoFocusListener>objetoListeners=new ArrayList<ObjetoFocusListener>();
		public void addObjetoListener(ObjetoFocusListener objetoListener){objetoListeners.add(objetoListener);}
		public void triggerObjetoListener(Objeto obj,boolean isFocused){
			if(isFocused){
				for(ObjetoFocusListener objetoListener:objetoListeners)objetoListener.objetoFocused(obj);
			}else{
				for(ObjetoFocusListener objetoListener:objetoListeners)objetoListener.objetoUnFocused(obj);
			}
		}
//TITULO
	private Texto titulo=new Texto(){
		{
			setOpaque(false);
			setEnabled(true);
			setFont(Tree.Fonte.FONTE);
			setForeground(Cor.BLACK);
			setLineWrappable(false);
			final SimpleAttributeSet center=new SimpleAttributeSet();
			StyleConstants.setAlignment(center,StyleConstants.ALIGN_CENTER);
			getStyledDocument().setParagraphAttributes(0,getStyledDocument().getLength(),center,false);
			addEditorListener(new DocumentListener(){
				public void removeUpdate(DocumentEvent d){run();}
				public void insertUpdate(DocumentEvent d){run();}
				public void changedUpdate(DocumentEvent d){run();}
				private void run(){		//ALTERA IMEDIATAMENTE PARA PODER SALVAR COM O TEXTO ABERTO
					if(titulo.getObjeto()==null)return;
					if(!titulo.getObjeto().getTipo().is(Objeto.Tipo.MODULO))return;
					final Modulo mod=(Modulo)titulo.getObjeto();
					mod.setTitle(getText());
					final int iconSize=(mod.isIconified()?Icone.getSize():0);
					titulo.setBounds(mod.getX(),mod.getY()+iconSize,mod.getWidth(),mod.getHeight()-iconSize);
					titulo.setFont(mod.getRelativeFont(Tree.UNIT));
					draw();
					if(!((JFrame)painel.getJanela()).getTitle().startsWith("*")){
						((JFrame)painel.getJanela()).setTitle("*"+((JFrame)painel.getJanela()).getTitle());
					}
				}
			});
			addObjetoSetListener(new ObjetoSetListener(){
				private Modulo mod=null;
				private String tituloTexto=null;
				public void objetoModified(Objeto oldObj,Objeto newObj){
					if(oldObj!=null&&!oldObj.getTipo().is(Objeto.Tipo.MODULO))return;
					if(oldObj!=newObj){		//MOD É MUDADO
						if(mod!=null&&tituloTexto!=null){
							if(getUndoManager().canUndo()){
								undoRedo.addUndoTitulo(mod,tituloTexto);
							}
						}
						final Modulo mod=(Modulo)newObj;
						this.mod=mod;
						if(mod==null){
							tituloTexto=null;
						}else{
							tituloTexto=mod.getTitle();
						}
					}
				}
			});
			addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_TAB:		k.consume();	break;
					}
				}
			});
			setVisible(false);
		}
		protected void paintComponent(Graphics imagemEdit){
			config(imagemEdit);
			imagemEdit.setColor(getBackground());
			final int round=Modulo.getRoundValue();
			imagemEdit.fillRoundRect(0,0,getWidth(),getHeight(),round,round);
			super.paintComponent(imagemEdit);	//TODO: TEM CHANCE DE GERAR UM ERROR EM LOOP(SE O TEXTO SE DUPLICAR, NÃO O DELETE!)
		}
		protected void paintBorder(Graphics imagemEdit){
			config(imagemEdit);
			if(getObjeto()!=null){
				final Modulo mod=(Modulo)getObjeto();
				imagemEdit.setColor(Cor.getInverted(mod.getCor()));
				((Graphics2D)imagemEdit).setStroke(mod.getBorda().getVisual());
			}else imagemEdit.setColor(getForeground());
			final int round=Modulo.getRoundValue();
			imagemEdit.drawRoundRect(0,0,getWidth(),getHeight(),round,round);
		}
		private void config(Graphics imagemEdit){
			final boolean antialias=(isAntialias()&&(getObjetos().getAll().size()<getObjetosLimite()));
			((Graphics2D)imagemEdit).setRenderingHint(RenderingHints.KEY_ANTIALIASING,(antialias?RenderingHints.VALUE_ANTIALIAS_ON:RenderingHints.VALUE_ANTIALIAS_OFF));
			((Graphics2D)imagemEdit).setStroke(new BasicStroke(Tree.getBordaValue()));
		}
	};
		public Texto getTitulo(){return titulo;}
		public void setTitulo(Modulo mod){
			final int borda=(int)(Tree.getBordaValue());
			titulo.setBorder(BorderFactory.createEmptyBorder(borda,borda,borda,borda));
			if(mod==null||mod==getGhost()){
				triggerObjetoListener(titulo.getObjeto(),true);	//ATUALIZA TITULO DE JANELA-TEXTO
				titulo.setVisible(false);
				titulo.clearObjeto();		//NÃO MODIFICAR MOD/COX ANTERIOR
				titulo.setText("");
				titulo.crearUndo();
				painel.getJanela().requestFocus();
				return;
			}
			titulo.clearObjeto();		//NÃO MODIFICAR MOD/COX ANTERIOR
			titulo.setText(mod.getTitle());
			final int iconSize=(mod.isIconified()?Icone.getSize():0);
			titulo.setBounds(mod.getX(),mod.getY()+iconSize,mod.getWidth(),mod.getHeight()-iconSize);
			titulo.setObjeto(mod);
			titulo.setFont(mod.getRelativeFont(Tree.UNIT));
			titulo.crearUndo();
			titulo.setVisible(true);
			titulo.setSelectionStart(0);
			titulo.setSelectionEnd(titulo.getText().length());
			titulo.requestFocus();
		}
//TEXTO
	private Texto texto=new Texto(){{
		setEnabled(false);
		setFont(Tree.Fonte.FONTE);
		setLineWrappable(true);
		addEditorListener(new DocumentListener(){
			public void removeUpdate(DocumentEvent d){run();}
			public void insertUpdate(DocumentEvent d){run();}
			public void changedUpdate(DocumentEvent d){run();}
			private void run(){		//ALTERA IMEDIATAMENTE PARA PODER SALVAR COM O TEXTO ABERTO
				if(texto.getObjeto()==null)return;
				if(!texto.getObjeto().getTipo().is(Objeto.Tipo.MODULO,Objeto.Tipo.CONEXAO))return;
				if(texto.getObjeto().getTipo().is(Objeto.Tipo.MODULO)){
					final Modulo mod=(Modulo)texto.getObjeto();
					mod.setTexto(Arrays.asList(getText().split("\n",-1)));
				}else if(texto.getObjeto().getTipo().is(Objeto.Tipo.CONEXAO)){
					final Conexao cox=(Conexao)texto.getObjeto();
					cox.setTexto(Arrays.asList(getText().split("\n",-1)));
				}
				if(!((JFrame)painel.getJanela()).getTitle().startsWith("*")){
					((JFrame)painel.getJanela()).setTitle("*"+((JFrame)painel.getJanela()).getTitle());
				}
			}
		});
		addObjetoSetListener(new ObjetoSetListener(){
			private Objeto obj;
			private List<String>textoTexto;
			public void objetoModified(Objeto oldObj,Objeto newObj){
				if(oldObj!=null&&!oldObj.getTipo().is(Objeto.Tipo.MODULO,Objeto.Tipo.CONEXAO))return;
				if(oldObj!=newObj){		//MOD/COX É MUDADO
					if(obj!=null&&textoTexto!=null){
						if(getUndoManager().canUndo()){
							undoRedo.addUndoTexto(obj,textoTexto);
						}
					}
					obj=newObj;
					if(newObj==null){
						textoTexto=null;
					}else{
						if(newObj.getTipo().is(Objeto.Tipo.MODULO)){
							final Modulo mod=(Modulo)newObj;
							textoTexto=new ArrayList<String>(mod.getTexto());
						}else if(newObj.getTipo().is(Objeto.Tipo.CONEXAO)){
							final Conexao cox=(Conexao)newObj;
							textoTexto=new ArrayList<String>(cox.getTexto());
						}
					}
				}
			}
		});
	}};
		public Texto getTexto(){return texto;}
		public void setTexto(Objeto obj){
			texto.setEnabled(false);
			if(obj==getGhost()){
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
//OBJS
	private int objsListaMaxSize=0;
	private ListaObjeto objetos=new ListaObjeto();
		public ListaObjeto getObjetos(){return objetos;}
	//ADD
		public boolean add(int index,Objeto obj){
			if(obj==null)return false;
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					if(!getObjetos().contains(mod)){
						if(index==-1){
							mod.setIndex(objsListaMaxSize++);
							getObjetos().add(mod);
						}else getObjetos().add(index,mod);
					}
					addToChunk(mod,getChunksIndexes(mod.getFormIndex()));
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					if(cox.getPai()==null||cox.getSon()==null)return false;	//COX DEVE TER MODS
					if(cox.getPai()==cox.getSon())return false;				//COX DEVE TER MODS DIFERENTES
					if(cox.getSon()==getMestre())return false;				//MESTRE NÃO PODE SER SON
					if(cox.getPai()!=getGhost()&&cox.getSon()!=getGhost()){
						if(!getObjetos().contains(cox.getPai())||!getObjetos().contains(cox.getSon())){
							return false;									//PAI E SON DEVEM ESTAR PRESENTES OU SER GHOST
						}
					}
					boolean didReplace=false;
					for(Conexao cox2:getObjetos().getConexoes()){
						if(cox2.getPai()==cox.getPai()&&cox2.getSon()==cox.getSon()||	//COX DEVE SER ÚNICO
								cox2.getPai()==cox.getSon()&&cox2.getSon()==cox.getPai()){
							getActions().delUndoable(cox2);
							didReplace=true;
							break;
						}
					}
					cox.getPai().addConexao(cox);
					cox.getSon().addConexao(cox);
					if(!getObjetos().contains(cox)){
						if(index==-1){
							cox.setIndex(objsListaMaxSize++);
							getObjetos().add(cox);
						}else getObjetos().add(index,cox);
					}
					addToChunk(cox,getChunksIndexes(cox.getFormIndex().getBounds()));
					if(didReplace){
						return false;
					}else return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					if(!getObjetos().contains(nod.getConexao()))return false;
					nod.getConexao().addNodulo(nod);
					if(!getObjetos().contains(nod)){
						if(index==-1){
							nod.setIndex(objsListaMaxSize++);
							getObjetos().add(nod);
						}else getObjetos().add(index,nod);
					}
					addToChunk(nod,getChunksIndexes(nod.getFormIndex()));
					addToChunk(nod.getConexao(),getChunksIndexes(nod.getConexao().getFormIndex().getBounds()));
					return true;
				case SEGMENTO:break;
			}
			return false;
		}
		public boolean add(Objeto obj){return add(-1,obj);}
	//DEL
		public boolean del(Objeto obj){
			if(obj==null)return false;
			unSelect(obj);
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					if(mod==getMestre())return false;		//MESTRE NÃO PODE SER DEL
					while(!mod.getConexoes().isEmpty()){
						del(mod.getConexoes().get(0));
					}
					getObjetos().del(mod);
					delDeChunk(mod,getChunksIndexes(mod.getFormIndex()));
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					unSelect(cox.getPai());
					unSelect(cox.getSon());
					cox.getPai().delConexao(cox);
					cox.getSon().delConexao(cox);
					delDeChunk(cox,getChunksIndexes(cox.getFormIndex().getBounds()));
					getObjetos().del(cox);		//RETIRA, IMPEDINDO QUE OS NODS O RECOLOQUEM
					while(!cox.getNodulos().isEmpty()){
						del(cox.getNodulos().get(0));
					}
					return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					delDeChunk(nod.getConexao(),getChunksIndexes(nod.getConexao().getFormIndex().getBounds()));
					unSelect(nod.getConexao());
					nod.getConexao().delNodulo(nod);
					if(getObjetos().contains(nod.getConexao())){
						addToChunk(nod.getConexao(),getChunksIndexes(nod.getConexao().getFormIndex().getBounds()));	//ATUALIZA CHUNKS COM O NOVO BOUNDS DE COX
					}
					getObjetos().del(nod);
					delDeChunk(nod,getChunksIndexes(nod.getFormIndex()));
					return true;
				case SEGMENTO:break;
			}
			return false;
		}
//SELECTED OBJS
	private ListaObjeto selectedObjetos=new ListaObjeto();
		public ListaObjeto getSelectedObjetos(){return selectedObjetos;}
	//SELECT
		public boolean select(Objeto obj){
			if(obj==null)return false;
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					if(mod==getGhost())return false;
					mod.setState(Modulo.State.SELECTED);
					if(selectedObjetos.contains(mod))return true;
					setTexto(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==0?mod:getGhost());
					selectedObjetos.add(mod);
					triggerObjetoListener(mod,true);
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					cox.setState(Conexao.State.SELECTED);
					if(selectedObjetos.contains(cox))return true;
					setTexto(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==0?cox:getGhost());
					selectedObjetos.add(cox);
					triggerObjetoListener(cox,true);
					return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					nod.setState(Nodulo.State.SELECTED);
					if(selectedObjetos.contains(nod))return true;
					selectedObjetos.add(nod);
					triggerObjetoListener(nod,true);
					return true;
				case SEGMENTO:break;
			}
			return false;
		}
		public void selectTree(Objeto obj){
			select(obj);
			switch (obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					for(Conexao cox:mod.getConexoes()){
						if(!cox.getSon().equals(mod)){
							if(cox.getState().is(Conexao.State.SELECTED))continue;
							if(cox.getSon().getState().is(Modulo.State.SELECTED))continue;
							selectTree(cox);
							select(cox.getSon());
						}
					}
				break;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					for(Nodulo nod:cox.getNodulos())select(nod);
				break;
				case NODULO:break;
				case SEGMENTO:break;
			}
		}
		public void selectAllMods(){for(Modulo mod:getObjetos().getModulos())select(mod);}
		public void selectAllCoxs(){for(Conexao cox:getObjetos().getConexoes())select(cox);}
		public void selectAllNods(){for(Nodulo nod:getObjetos().getNodulos())select(nod);}
		public void selectAll(){for(Objeto obj:getObjetos().getAll().values())select(obj);}
		public boolean selectToBeCreator(Objeto obj){
			if(obj==null)return false;
			unSelect(obj);						//DEVE ESTAR UNSELECTED PARA SER TO_BE_CREATOR
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					if(mod==getGhost())return false;
					mod.setState(Modulo.State.TO_BE_CREATOR);
					if(selectedObjetos.contains(mod))return true;
					selectedObjetos.add(mod);
					return true;
				case CONEXAO:	return false;	//COX PODE APENAS SER CHAMADO CREATOR PELO SEG
				case NODULO:	return false;	//NOD PODE APENAS SER CHAMADO CREATOR PELO COX
				case SEGMENTO:
					final Segmento seg=(Segmento)obj;
					seg.setState(Segmento.State.TO_BE_CREATOR);
					if(selectedObjetos.contains(seg.getConexao()))return true;
					selectedObjetos.add(seg.getConexao());
					return true;
			}
			return false;
		}
		public boolean selectToBeSon(Objeto obj){
			if(obj==null)return false;
			if(!obj.getTipo().is(Objeto.Tipo.MODULO))return false;
			final Modulo mod=(Modulo)obj;
			if(mod==getGhost())return false;	//GHOST NÃO PODE SER SON
			if(mod==getMestre())return false;	//MESTRE NÃO PODE SER SON
			unSelect(mod);				//DEVE ESTAR UNSELECTED PARA SER TO_BE_SON
			mod.setState(Modulo.State.TO_BE_SON);
			if(selectedObjetos.contains(mod))return true;
			selectedObjetos.add(mod);
			return true;
		}
		public boolean selectToBePai(Objeto obj){
			if(obj==null)return false;
			if(!obj.getTipo().is(Objeto.Tipo.MODULO))return false;
			final Modulo mod=(Modulo)obj;
			if(mod==getGhost())return false;	//GHOST NÃO PODE SER PAI
			if(!getGhost().getConexoes().isEmpty()){	//É GHOST QUE ESTÁ RELACIONANDO SONS A SI
				boolean allAreConectados=true;
				for(Conexao cox:getGhost().getConexoes()){
					if(cox.getSon()==mod)return false;	//PAI NÃO PODE SER SEU PRÓPRIO SON
					boolean coxExiste=false;
					for(Conexao sonCox:cox.getSon().getConexoes()){
						if(sonCox.getPai()==mod){
							coxExiste=true;
							break;
						}
					}
					if(!coxExiste)allAreConectados=false;
				}
				if(allAreConectados)return false;		//NÃO HÁ CONEXÃO QUE POSSA SER CRIADA
			}
			unSelect(mod);				//DEVE ESTAR UNSELECTED PARA SER TO_BE_PAI
			mod.setState(Modulo.State.TO_BE_PAI);
			if(selectedObjetos.contains(mod))return true;
			selectedObjetos.add(mod);
			return true;
		}
		public boolean selectToBeDeleted(Objeto obj){
			if(obj==null)return false;
			unSelect(obj);						//DEVE ESTAR UNSELECTED PARA SER TO_BE_DELETED
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					if(mod==getGhost())return false;
					if(mod==getMestre())return false;	//MESTRE NÃO PODE SER DEL
					mod.setState(Modulo.State.TO_BE_DELETED);
					for(Conexao cox:mod.getConexoes()){
						selectToBeDeleted(cox);
					}
					if(selectedObjetos.contains(mod))return true;
					selectedObjetos.add(mod);
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					cox.setState(Conexao.State.TO_BE_DELETED);
					for(Nodulo nod:cox.getNodulos()){
						selectToBeDeleted(nod);
					}
					if(selectedObjetos.contains(cox))return true;
					selectedObjetos.add(cox);
					return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					nod.setState(Nodulo.State.TO_BE_DELETED);
					if(selectedObjetos.contains(nod))return true;
					selectedObjetos.add(nod);
					return true;
				case SEGMENTO:break;
			}
			return false;
		}
	//UNSELECT
		public boolean unSelect(Objeto obj){
			if(obj==null)return false;
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					if(mod==getGhost())return false;
					mod.setState(Modulo.State.UNSELECTED);
					if(!selectedObjetos.contains(mod))return true;
					selectedObjetos.del(mod);
					if(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==1){
						if(selectedObjetos.getModulos().size()==1){
							setTexto(selectedObjetos.getModulos().get(0));
						}else if(selectedObjetos.getConexoes().size()==1){
							setTexto(selectedObjetos.getConexoes().get(0));
						}
					}else if(getTexto().getObjeto()!=getGhost()){
						setTexto(getGhost());
					}
					triggerObjetoListener(mod,false);
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					cox.setState(Conexao.State.UNSELECTED);
					if(!selectedObjetos.contains(cox))return true;
					selectedObjetos.del(cox);
					if(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==1){
						if(selectedObjetos.getModulos().size()==1){
							setTexto(selectedObjetos.getModulos().get(0));
						}else if(selectedObjetos.getConexoes().size()==1){
							setTexto(selectedObjetos.getConexoes().get(0));
						}
					}else if(getTexto().getObjeto()!=getGhost()){
						setTexto(getGhost());
					}
					triggerObjetoListener(cox,false);
					return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					nod.setState(Nodulo.State.UNSELECTED);
					if(!selectedObjetos.contains(nod))return true;
					selectedObjetos.del(nod);
					triggerObjetoListener(nod,false);
					return true;
				case SEGMENTO:break;
			}
			return false;
		}
		public void unSelectTree(Objeto obj){
			unSelect(obj);
			switch (obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					for(Conexao cox:mod.getConexoes()){
						if(!cox.getSon().equals(mod)){
							if(!cox.getState().is(Conexao.State.SELECTED))continue;
							if(!cox.getSon().getState().is(Modulo.State.SELECTED))continue;
							unSelectTree(cox);
							unSelectTree(cox.getSon());
						}
					}
				break;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					for(Nodulo nod:cox.getNodulos())unSelect(nod);
				break;
				case NODULO:break;
				case SEGMENTO:break;
			}
		}
		public void unSelectAllMods(){while(!getSelectedObjetos().getModulos().isEmpty())unSelect(getSelectedObjetos().getModulos().get(0));}
		public void unSelectAllCoxs(){while(!getSelectedObjetos().getConexoes().isEmpty())unSelect(getSelectedObjetos().getConexoes().get(0));}
		public void unSelectAllNods(){while(!getSelectedObjetos().getNodulos().isEmpty())unSelect(getSelectedObjetos().getNodulos().get(0));}
		public void unSelectAll(){while(!getSelectedObjetos().getAll().isEmpty())unSelect((Objeto)getSelectedObjetos().getAll().values().toArray()[0]);}
//VISIBLE OBJETOS
		private HashMap<Integer,Modulo>visibleMods=new HashMap<>();
			public HashMap<Integer,Modulo>getVisibleMods(){return visibleMods;}
		private HashMap<Integer,Conexao>visibleCoxs=new HashMap<>();
			public HashMap<Integer,Conexao>getVisibleCoxs(){return visibleCoxs;}
		private HashMap<Integer,Nodulo>visibleNods=new HashMap<>();
			public HashMap<Integer,Nodulo>getVisibleNods(){return visibleNods;}
		private HashMap<Point,Chunk>chunks=new HashMap<>();
			public HashMap<Point,Chunk>getChunks(){return chunks;}
			public void addToChunk(Objeto obj,Rectangle chunksIndexes){
				Rectangle areaSimples=null;
				Path2D.Float areaComplex=null;
				switch(obj.getTipo()){
					case MODULO:	areaSimples=((Modulo)obj).getForm();	break;
					case CONEXAO:	areaComplex=((Conexao)obj).getForm();	break;
					case NODULO:	areaSimples=((Nodulo)obj).getForm();	break;
					case SEGMENTO:											break;
				}
				for(int y=chunksIndexes.y;y-chunksIndexes.y<chunksIndexes.height;y++){
					for(int x=chunksIndexes.x;x-chunksIndexes.x<chunksIndexes.width;x++){
						final Point index=new Point(x,y);
						final Chunk chunkNewBounds=getChunks().get(index);
						if(chunkNewBounds==null){
							final Chunk chunkNew=new Chunk(x,y);
							if(!(areaSimples!=null?areaSimples:areaComplex).intersects(chunkNew.getForm()))continue;
							chunkNew.add(obj);
							getChunks().put(index,chunkNew);
						}else{
							if(!(areaSimples!=null?areaSimples:areaComplex).intersects(chunkNewBounds.getForm()))continue;
							chunkNewBounds.add(obj);
						}
					}
				}
			}
			public void delDeChunk(Objeto obj,Rectangle chunksIndexes){
				for(int y=chunksIndexes.y;y-chunksIndexes.y<chunksIndexes.height;y++){
					for(int x=chunksIndexes.x;x-chunksIndexes.x<chunksIndexes.width;x++){
						final Point index=new Point(x,y);
						final Chunk chunkOldBounds=getChunks().get(index);
						if(chunkOldBounds!=null){
							chunkOldBounds.del(obj);
							if(chunkOldBounds.isEmpty())getChunks().remove(index);
						}
					}
				}
			}
			public Rectangle getChunksIndexes(int xIndex,int yIndex,int widthIndex,int heightIndex){
				final int x=(int)Math.floor((float)xIndex/Chunk.SIZE);										//ROUND_TO_MIN(X/CHUNK)
				final int y=(int)Math.floor((float)yIndex/Chunk.SIZE);										//ROUND_TO_MIN(Y/CHUNK)
				int width=(int)Math.ceil(((float)xIndex/Chunk.SIZE)+((float)widthIndex/Chunk.SIZE)-x);		//ROUND_TO_MAX((X/CHUNK)+(WIDTH/CHUNK)-ROUND_TO_MIN(X/CHUNK))
				int height=(int)Math.ceil(((float)yIndex/Chunk.SIZE)+((float)heightIndex/Chunk.SIZE)-y);	//ROUND_TO_MAX((Y/CHUNK)+(HEIGHT/CHUNK)-ROUND_TO_MIN(Y/CHUNK))
				return new Rectangle(x,y,width,height);
			}
			public Rectangle getChunksIndexes(Rectangle areaIndex){return getChunksIndexes(areaIndex.x,areaIndex.y,areaIndex.width,areaIndex.height);}
//MAIN
	public Tree(Painel painel){
		super(painel);
		final ObjetoBoundsListener boundsListener=new ObjetoBoundsListener(){
			public void boundsChanged(Objeto obj,Rectangle oldBoundsIndex,Rectangle newBoundsIndex){
				if(obj==getGhost())return;		//GHOST NÃO APARECE
				final Rectangle chunksOldIndexes=getChunksIndexes(oldBoundsIndex);
				final Rectangle chunksNewIndexes=getChunksIndexes(newBoundsIndex);
				if(obj.getTipo().is(Objeto.Tipo.CONEXAO)||!chunksNewIndexes.equals(chunksOldIndexes)){	//COXS MUDAM DE CHUNK, MAS NÃO DE ÁREA
					delDeChunk(obj,chunksOldIndexes);
					if(getObjetos().contains(obj)){
						addToChunk(obj,chunksNewIndexes);
					}
				}
			}
		};
		Modulo.addBoundsListener(boundsListener);
		Conexao.addBoundsListener(boundsListener);
		Nodulo.addBoundsListener(boundsListener);
		Tree.MESTRE=new Modulo(0,0,"Novo Mind");
		getMestre().setCor(new Cor(0,255,255));
		Tree.GHOST=new Modulo(0,0,"Texto");
		undoRedo=new UndoRedo(this);
		popup=new Popup(this);
		popup.update();
		painel.add(titulo);
		cursor=new Cursor(((JFrame)painel.getJanela()));
		actions=new Actions(this,((JFrame)painel.getJanela()));
		clear();
		actions.putIntputs();
	}
//FUNCS
	public void clear(){
		Tree.UNIT=8;
		setLocal(0,0);
		Tree.Fonte.FONTE=new Font("Courier New",Font.PLAIN,15);
		selecao.setEmpty();
		undoRedo.clear();
		getObjetos().clear();
		getSelectedObjetos().clear();
		getVisibleMods().clear();
		getVisibleCoxs().clear();
		getVisibleNods().clear();
		getChunks().clear();
		objsListaMaxSize=0;
		Tree.MESTRE=new Modulo(0,0,"Novo Mind");
		getMestre().setCor(new Cor(0,255,255));
		Tree.GHOST=new Modulo(0,0,"Texto");
		add(getMestre());
		setTitulo(getGhost());
		setTexto(getGhost());
		setAntialias(true);
		setEnabled(true);
		getActions().setState(Actions.NORMAL);
	}
	public void setFocusOn(Objeto[]objs){
		if(objs==null)return;
		int xIndexMin=0;
		int yIndexMin=0;
		int xIndexMax=0;
		int yIndexMax=0;
		boolean firstRun=true;
		for(Objeto obj:objs){
			if(obj==null)continue;
			int x=0;
			int y=0;
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					x=mod.getMeioXIndex();
					y=mod.getMeioYIndex();
				break;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					final Rectangle coxForm=cox.getFormIndex().getBounds();
					x=coxForm.x+(coxForm.width/2);
					y=coxForm.y+(coxForm.height/2);
				break;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					x=nod.getXIndex();
					y=nod.getYIndex();
				break;
				case SEGMENTO:break;
			}
			if(firstRun){
				xIndexMin=x;
				yIndexMin=y;
				xIndexMax=x;
				yIndexMax=y;
				firstRun=false;
			}else{
				xIndexMin=Math.min(xIndexMin,x);
				yIndexMin=Math.min(yIndexMin,y);
				xIndexMax=Math.max(xIndexMax,x);
				yIndexMax=Math.max(yIndexMax,y);
			}
		}
		final int meioX=(xIndexMax+xIndexMin)/2;
		final int meioY=(yIndexMax+yIndexMin)/2;
		final Point telaMeio=getActions().getGridPosition(new Point(painel.getJanela().getWidth()/2,painel.getJanela().getHeight()/2));
		Tree.setLocal(telaMeio.x-meioX,telaMeio.y-meioY);	//ALINHA O MEIO DA JANELA COM O MEIO DOS OBJS FOCADOS
	}
	private Runnable animation;
	public void animate(Point localIni,Point localFim){
		new Thread(animation=new Runnable(){
			public void run(){
				final double diffX=localFim.x-localIni.x;
				final double diffY=localFim.y-localIni.y;
				if(diffX==0&&diffY==0)return;
				final int passos=20;
				final Point quadrante=new Point(localIni.x<=localFim.x?1:-1,localIni.y<localFim.y?1:-1);	//INF-DIR
				final int fps=60;
				final long tempoAlvo=1000000000/fps;
				if(Math.abs(diffX)>=Math.abs(diffY)){
					final double slope=diffY/diffX;
					final double speed=Math.abs(diffX/passos);
					for(double x=localIni.x;(quadrante.x==1?x<=localFim.x:x>localFim.x);x+=speed*quadrante.x){
						final long tempoIni=System.nanoTime();
						final double y=slope*(x-localIni.x)+localIni.y;	//y=m*x+b
						Tree.setLocal((int)x,(int)y);
						draw();
						if(animation!=this)return;	//OUTRA ANIMAÇÃO FOI CHAMADA
						try{
							Thread.sleep((tempoIni-System.nanoTime()+tempoAlvo)/1000000);
						}catch(Exception erro){/*EVITA ERRO DE VALOR NEGATIVO*/}
					}
				}else{
					final double slope=diffX/diffY;
					final double speed=Math.abs(diffY/passos);
					for(double y=localIni.y;(quadrante.y==1?y<=localFim.y:y>localFim.y);y+=speed*quadrante.y){
						final long tempoIni=System.nanoTime();
						final double x=slope*(y-localIni.y)+localIni.x;	//x=m*y+b
						Tree.setLocal((int)x,(int)y);
						draw();
						if(animation!=this)return;	//OUTRA ANIMAÇÃO FOI CHAMADA
						try{
							Thread.sleep((tempoIni-System.nanoTime()+tempoAlvo)/1000000);
						}catch(Exception erro){/*EVITA ERRO DE VALOR NEGATIVO*/}
					}
				}
				Tree.setLocal(localFim.x,localFim.y);
				draw();
			}
		}).start();
	}
//DRAW
@Override
	public synchronized void draw(Graphics2D imagemEdit){
		final int unit=Tree.UNIT;
		if(((JFrame)painel.getJanela()).isUndecorated()){
			clearDraw();
		}
		final boolean antialias=(isAntialias()&&(getObjetos().size()<getObjetosLimite()||getObjetosLimite()==-1));	//ANTIALIAS
		imagemEdit.setRenderingHint(RenderingHints.KEY_ANTIALIASING,(antialias?RenderingHints.VALUE_ANTIALIAS_ON:RenderingHints.VALUE_ANTIALIAS_OFF));
		final Rectangle tela=new Rectangle(
				getRelativeBounds().x-unit,
				getRelativeBounds().y-unit,
				getRelativeBounds().width+(unit*2),
				getRelativeBounds().height+(unit*2)
		);
		if(!((JFrame)painel.getJanela()).isUndecorated()){
			drawFundo(imagemEdit,tela,unit);
		}
		drawObjetos(imagemEdit,tela,unit);
		drawFrente(imagemEdit);
		imagemEdit.dispose();
	}
		private void drawFundo(Graphics2D imagemEdit,Rectangle tela,int unit){
			if(unit<=2){	//COR DA GRADE, COBRINDO TUDO
				imagemEdit.setColor(Cor.getChanged(Tree.FUNDO,0.95f));
			}else imagemEdit.setColor(Tree.FUNDO);
			imagemEdit.fillRect(0,0,getWidth(),getHeight());						//FUNDO
			final boolean showChunks=false;
			if(showChunks){		//DEBUG
				final Rectangle chunksInd=getChunksIndexes(-Tree.getLocalX(),-Tree.getLocalY(),tela.width/unit,tela.height/unit);
				for(int y=chunksInd.y;y-chunksInd.y<chunksInd.height;y++){
					for(int x=chunksInd.x;x-chunksInd.x<chunksInd.width;x++){
						imagemEdit.setColor(y%2==0&&x%2==0||y%2!=0&&x%2!=0?Modulo.Cores.CREATE:Modulo.Cores.PAI);	//XAND: 1001
						final Chunk chunk=getChunks().get(new Point(x,y));
						if(chunk!=null)imagemEdit.setColor(Modulo.Cores.SON);
						final int size=unit*Chunk.SIZE;
						final int X=(x*size)+(Tree.getLocalX()*unit);
						final int Y=(y*size)+(Tree.getLocalY()*unit);
						imagemEdit.fillRect(X,Y,size,size);
						imagemEdit.setColor(Color.RED);
						imagemEdit.drawString(x+","+y,X,Y+size);
					}
				}
			}
			if(isShowingGrid()&&unit>2){													//GRADE
				imagemEdit.setColor(Cor.getChanged(Tree.FUNDO,0.95f));
				for(int x=0,size=getWidth()/unit;x<=size;x++)imagemEdit.drawLine(unit*x,getY(),unit*x,getHeight());
				for(int y=0,size=getHeight()/unit;y<=size;y++)imagemEdit.drawLine(getX(),unit*y,getWidth(),unit*y);					
			}
		}
		private void drawObjetos(Graphics2D imagemEdit,Rectangle tela,int unit){
			getVisibleMods().clear();
			getVisibleCoxs().clear();
			getVisibleNods().clear();
			final Rectangle chunksIndexes=getChunksIndexes(-Tree.getLocalX(),-Tree.getLocalY(),tela.width/unit,tela.height/unit);
			for(int y=chunksIndexes.y;y-chunksIndexes.y<chunksIndexes.height;y++){
				for(int x=chunksIndexes.x;x-chunksIndexes.x<chunksIndexes.width;x++){
					final Chunk chunk=getChunks().get(new Point(x,y));
					if(chunk==null)continue;
					for(Conexao cox:chunk.getObjetos().getConexoes()){
						getVisibleCoxs().put(cox.getIndex(),cox);
					}
					for(Nodulo nod:chunk.getObjetos().getNodulos()){
						getVisibleNods().put(nod.getIndex(),nod);
					}
					for(Modulo mod:chunk.getObjetos().getModulos()){
						getVisibleMods().put(mod.getIndex(),mod);
					}
				}
			}
			imagemEdit.setStroke(new BasicStroke((int)(Tree.getBordaValue())));	//BORDA DOS OBJS
		//DRAW CONEXÕES
			for(Conexao cox:getVisibleCoxs().values()){
				if(cox.getState().is(Conexao.State.UNSELECTED))cox.draw(imagemEdit,unit);	//COXS DESELECIONADOS
			}
			for(Conexao cox:getVisibleCoxs().values()){
				if(!cox.getState().is(Conexao.State.UNSELECTED))cox.draw(imagemEdit,unit);	//COXS SELECIONADOS FICAM NA FRENTE
			}
		//DRAW NÓDULOS
			for(Nodulo nod:getVisibleNods().values()){
				if(nod.getState().is(Nodulo.State.UNSELECTED))nod.draw(imagemEdit,unit);		//NODS DESELECIONADOS
			}
			for(Nodulo nod:getVisibleNods().values()){
				if(!nod.getState().is(Nodulo.State.UNSELECTED))nod.draw(imagemEdit,unit);	//NODS SELECIONADOS FICAM NA FRENTE
			}
		//DRAW MÓDULOS
			for(Modulo mod:getVisibleMods().values())mod.draw(imagemEdit,unit);				//MODS
		}
		private void drawFrente(Graphics2D imagemEdit){
			if(!selecao.isEmpty())selecao.draw(imagemEdit);								//ÁREA DE SELEÇÃO
			if(((JFrame)painel.getJanela()).isUndecorated()){							//BORDA PARA PERMITIR DRAG
				imagemEdit.setStroke(new BasicStroke(10));
				final Color f=Tree.FUNDO;
				imagemEdit.setColor(new Color((float)f.getRed()/255,(float)f.getGreen()/255,(float)f.getBlue()/255,0.6f));
				imagemEdit.drawRect(0,0,getWidth(),getHeight());
			}
			if(getActions().getState()==Actions.AUTO_DRAG_ALL+Actions.NORMAL){			//SETA DO AUTODRAG
				drawAutoDragLine(imagemEdit);
			}
			if(!isEnabled()){															//EFEITO DE ACINZENTADO
				final Color f=Tree.FUNDO;
				imagemEdit.setColor(new Color((float)f.getRed()/255,(float)f.getGreen()/255,(float)f.getBlue()/255,0.6f));
				imagemEdit.fillRect(0,0,getWidth(),getHeight());
			}
		}
			private void drawAutoDragLine(Graphics2D imagemEdit){
				imagemEdit.setStroke(new BasicStroke(1));
				//PONTOS
				final Point ponto1=getActions().getNonGridPosition(getActions().mouseReleased);
				final Point ponto2=getActions().getNonGridPosition(getActions().mouseMoved);
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
				imagemEdit.setColor(Cor.getChanged(Modulo.Cores.FUNDO,Modulo.Cores.FUNDO.isDark()?1.1f:0.8f));
				imagemEdit.drawLine(ponto1.x-XRadius,ponto1.y,ponto1.x+XRadius,ponto1.y);
				imagemEdit.drawLine(ponto1.x,ponto1.y-XRadius,ponto1.x,ponto1.y+XRadius);
				imagemEdit.setColor(Cor.getTransparent(Cor.getChanged(Modulo.Cores.FUNDO,Modulo.Cores.FUNDO.isDark()?1.1f:0.8f),0.6f));
				imagemEdit.fill(seta);
				imagemEdit.setColor(Cor.getChanged(Modulo.Cores.FUNDO,Modulo.Cores.FUNDO.isDark()?1.1f:0.8f));
				imagemEdit.draw(seta);
			}
//TAG -> TREE
	public List<Objeto>addTree(Element mindTag,boolean replaceMestre)throws Exception{
		final List<Objeto>lista=new ArrayList<Objeto>();
		final HashMap<Integer,Modulo>mods=new HashMap<Integer,Modulo>();
		final String fontNome=mindTag.getAttribute("fontName");			//SET FONT
		final int fontStyle=Integer.parseInt(mindTag.getAttribute("fontStyle"));
		final int fontSize=Integer.parseInt(mindTag.getAttribute("fontSize"));
		setFonte(new Font(fontNome,fontStyle,fontSize));
		final NodeList modsTag=mindTag.getElementsByTagName("mod");		//SET MODS
		for(int m=0,size=modsTag.getLength();m<size;m++){
			final Element modTag=(Element)modsTag.item(m);
			Modulo mod=Modulo.getModulo(modTag);
			if(replaceMestre&&getObjetos().getModulos().size()==1&&m==0){
				Tree.getMestre().setTitle(mod.getTitle());
				Tree.getMestre().setCor(mod.getCor());
				for(Icone icone:mod.getIcones())Tree.getMestre().justAddIcone(icone);
				Tree.getMestre().setTexto(mod.getTexto());
				Tree.getMestre().setLocationIndex(mod.getXIndex(),mod.getYIndex());		//ATIVA BOUNDS_LISTENER
				mod=Tree.getMestre();
			}else{
				add(mod);
			}
			mods.put(m,mod);
			lista.add(mod);
		}
		final NodeList coxs=mindTag.getElementsByTagName("cox");		//SET COXS
		for(int c=0,sizeC=coxs.getLength();c<sizeC;c++){
			final Element coxTag=(Element)coxs.item(c);
			final Conexao cox=Conexao.getConexao(coxTag,mods);
			add(cox);
			lista.add(cox);
			final NodeList nods=coxTag.getElementsByTagName("nod");		//SET NODS
			for(int n=0,sizeN=nods.getLength();n<sizeN;n++){
				final Element nodTag=(Element)nods.item(n);
				final Nodulo nod=Nodulo.getNodulo(nodTag,cox);
				add(nod);
				lista.add(nod);
			}
		}
		return lista;
	}
//TREE -> TAG
	public String getText(ListaObjeto lista){
		final List<Modulo>listaMods=lista.getModulos();
		final HashMap<Modulo,Integer>mods=new HashMap<Modulo,Integer>();
		final LinkedHashSet<Conexao>coxs=new LinkedHashSet<Conexao>();
		try{
			final Document xml=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element mindTag=xml.createElement("mind");			//MIND
			mindTag.setAttribute("fontName",Tree.Fonte.FONTE.getName());					//FONT_NAME
			mindTag.setAttribute("fontStyle",String.valueOf(Tree.Fonte.FONTE.getStyle()));	//FONT_STYLE
			mindTag.setAttribute("fontSize",String.valueOf(Tree.Fonte.FONTE.getSize()));	//FONT_SIZE
			xml.appendChild(mindTag);									//XML DE MIND
			for(int m=0,size=listaMods.size();m<size;m++){
				final Modulo mod=listaMods.get(m);
				mod.addToXML(xml,mindTag);								//XML DE MODS
				mods.put(mod,m);
				for(Conexao cox:mod.getConexoes())coxs.add(cox);
			}
			for(Conexao cox:coxs)cox.addToXML(xml,mindTag,mods);		//XML DE COXS
			final TransformerFactory transformerFactory=TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number",new Integer(4));		//4 ESPAÇOS
			final Transformer transformer=transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");	//TIRA XML HEADER
			transformer.setOutputProperty(OutputKeys.INDENT,"yes");
			final StringWriter writer=new StringWriter();
			transformer.transform(new DOMSource(xml),new StreamResult(writer));
			return writer.toString()
					.replaceAll("\n    (<mod|<cox|</mod|</cox)","\t$1")				//TROCA ESPAÇOS POR TAB
					.replaceAll("\n        (<nod|<text|</nod|</text)","\t\t$1");	//TROCA ESPAÇOS POR TAB
		}catch(TransformerException|TransformerFactoryConfigurationError|ParserConfigurationException erro){
			mensagem("Erro ao traduzir para string!\n"+erro,Options.ERRO);
		}
		return null;
	}
//MENSAGEM
	public enum Options{
		ERRO,
		AVISO;
	}
	public static void mensagem(String mensagem,Options tipo){
		Toolkit.getDefaultToolkit().beep();
		switch(tipo){
			case AVISO:	JOptionPane.showMessageDialog(null,mensagem,"Aviso!",JOptionPane.WARNING_MESSAGE);break;
			case ERRO:	JOptionPane.showMessageDialog(null,mensagem,"Erro...!",JOptionPane.ERROR_MESSAGE);break;
		}
	}
}