package element.tree.main;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import element.tree.Chunk;
import element.tree.SelecaoST;
import element.tree.objeto.ListaObjeto;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.propriedades.Icone;
import element.tree.objeto.nodulo.Nodulo;
import element.tree.objeto.conexao.segmento.Segmento;
import element.tree.objeto.modulo.Modulo;
import element.tree.undoRedo.UndoRedoObjeto;
public class Actions{
//VAR GLOBAIS
	private Tree tree;
	protected Point oldMouse=new Point();
//MOUSES
	public Point mousePressed=new Point();
	public Point mouseReleased=new Point();
	public Point mouseDragged=new Point();
	public Point mouseMoved=new Point();
	public Point mouseNonGrid=new Point();
//MAIN
	public Actions(Tree tree){
		this.tree=tree;
	}
//INSERIR AÇÕES
	public void putIntputs(){
		final JFrame janela=(JFrame)tree.getPainel().getJanela();
		janela.addMouseWheelListener(new MouseWheelListener(){
			public void mouseWheelMoved(MouseWheelEvent w){
				if(!tree.getRelativeBounds().contains(getPosition(w.getPoint())))return;
				mouseMoved=getGridPosition(w.getPoint());
				setZoom(w.getPoint(),w.getWheelRotation());
				final Point newLocal=getGridPosition(w.getPoint());
				final Point diff=new Point(mouseMoved.x-newLocal.x,mouseMoved.y-newLocal.y);
				mousePressed.x-=diff.x;
				mousePressed.y-=diff.y;
				mouseDragged.x-=diff.x;
				mouseDragged.y-=diff.y;
				mouseReleased.x-=diff.x;
				mouseReleased.y-=diff.y;
				mouseMoved.x-=diff.x;
				mouseMoved.y-=diff.y;
				tree.draw();
				updateTituloBounds();
				if(tree.getUI().getTitulo().getObjeto()!=null){		//ATUALIZA O TAMANHO DA FONTE DE TÍTULO
					tree.getUI().updateTituloFont();
				}
			}
		});
		janela.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent m){
				if(!tree.isEnabled())return;
				final Point mouseAtual=getGridPosition(m.getPoint());
				mousePressed=new Point(mouseAtual);
				mouseDragged=new Point(mouseAtual);
				mouseMoved=new Point(mouseAtual);
				final TreeST.State state=tree.getStateContent(tree.getState());
				if(state.getMousePressedAction()!=null)state.getMousePressedAction().run(m);
			}
			public void mouseReleased(MouseEvent m){
				if(!tree.isEnabled())return;
				final Point mouseAtual=getGridPosition(m.getPoint());
				mouseReleased=new Point(mouseAtual);
				mouseDragged=new Point(mouseAtual);
				mouseMoved=new Point(mouseAtual);
				final TreeST.State state=tree.getStateContent(tree.getState());
				if(state.getMouseReleasedAction()!=null)state.getMouseReleasedAction().run(m);
			}
			public void mouseClicked(MouseEvent m){
				if(!tree.isEnabled())return;
				final Point mouseAtual=getGridPosition(m.getPoint());
				mousePressed=new Point(mouseAtual);
				mouseReleased=new Point(mouseAtual);
				mouseDragged=new Point(mouseAtual);
				mouseMoved=new Point(mouseAtual);
				final TreeST.State state=tree.getStateContent(tree.getState());
				if(state.getMouseCliquedAction()!=null)state.getMouseCliquedAction().run(m);
			}
		});
		janela.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent m){
				if(!tree.isEnabled())return;
				final Point mouseDraggedAtual=getGridPosition(m.getPoint());
				if(!mouseDraggedAtual.equals(mouseDragged)){
					final TreeST.State state=tree.getStateContent(tree.getState());
					if(state.getMouseDraggedAction()!=null)state.getMouseDraggedAction().run(m);
					mouseDragged=new Point(mouseDraggedAtual);
					mouseMoved=new Point(mouseDraggedAtual);
				}
			}
			public void mouseMoved(MouseEvent m){
				if(!tree.isEnabled())return;	//IGNORA SE DESFOCADO
				final Point mouseMovedAtual=getGridPosition(m.getPoint());
				if(!mouseMovedAtual.equals(mouseMoved)){
					final TreeST.State state=tree.getStateContent(tree.getState());
					if(state.getMouseMovedAction()!=null)state.getMouseMovedAction().run(m);
					mouseMoved=new Point(mouseMovedAtual);
				}
			}
		});
		janela.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent k){
				if(!tree.isEnabled())return;
				final TreeST.State state=tree.getStateContent(tree.getState());
				if(state.getKeyPressedAction()!=null)state.getKeyPressedAction().run(k);
			}
			public void keyReleased(KeyEvent k){
				if(!tree.isEnabled())return;
				final TreeST.State state=tree.getStateContent(tree.getState());
				if(state.getKeyReleasedAction()!=null)state.getKeyReleasedAction().run(k);
			}
		});
		janela.addWindowListener(new WindowListener(){
			public void windowOpened(WindowEvent w){}
			public void windowIconified(WindowEvent w){}
			public void windowDeiconified(WindowEvent w){}
			public void windowClosing(WindowEvent w){}
			public void windowClosed(WindowEvent w){}
			public void windowActivated(WindowEvent w){
				tree.setEnabled(true);
				if(tree.getUI().getTitulo().isVisible())tree.getUI().getTitulo().requestFocus();
			}
			public void windowDeactivated(WindowEvent w){
				if(!janela.isUndecorated())tree.setEnabled(false);
			}
		});
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
		final Point telaMeio=getGridPosition(new Point(tree.getPainel().getJanela().getWidth()/2,tree.getPainel().getJanela().getHeight()/2));
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
						tree.draw();
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
						tree.draw();
						if(animation!=this)return;	//OUTRA ANIMAÇÃO FOI CHAMADA
						try{
							Thread.sleep((tempoIni-System.nanoTime()+tempoAlvo)/1000000);
						}catch(Exception erro){/*EVITA ERRO DE VALOR NEGATIVO*/}
					}
				}
				Tree.setLocal(localFim.x,localFim.y);
				tree.draw();
			}
		}).start();
	}
	protected void startAutoDrag(){
		new Thread(new Runnable(){
			public void run(){
				final int restRadius=40;
				final int speedControl=4;
				final int fps=30;
				final long tempoAlvo=1000000000/fps;
				double acumX=0;
				double acumY=0;
				while(tree.getState()==TreeST.AUTO_DRAG_ALL+TreeST.NORMAL){
					final long tempoIni=System.nanoTime();
					if(!tree.isEnabled()){	//INTERROMPE SE DESFOCADO
						try{
							Thread.sleep((tempoIni-System.nanoTime()+tempoAlvo)/1000000);
						}catch(Exception erro){/*EVITA ERRO DE VALOR NEGATIVO*/}
						continue;
					}
					final Point mouseAtual=getNonGridPosition(mouseMoved);
					final Point mouseAncora=getNonGridPosition(mousePressed);
					double difX=(mouseAtual.x-mouseAncora.x);
					double difY=(mouseAtual.y-mouseAncora.y);
					final double distancia=Math.sqrt(difX*difX+difY*difY);
					if(distancia>restRadius){
						double moveX=difX/Tree.UNIT/speedControl;
						double moveY=difY/Tree.UNIT/speedControl;
						acumX+=Math.abs(moveX-(int)moveX);
						acumY+=Math.abs(moveY-(int)moveY);
						if(acumX>=Tree.UNIT){
							acumX-=(int)acumX;
							if(moveX>0){
								moveX+=acumX;
							}else moveX-=acumX;
						}
						if(acumY>=Tree.UNIT){
							acumY-=(int)acumY;
							if(moveY>0){
								moveY+=acumY;
							}else moveY-=acumY;
						}
						Tree.setLocal(Tree.getLocalX()-(int)moveX,Tree.getLocalY()-(int)moveY);
					}
					tree.draw();
					try{
						Thread.sleep((tempoIni-System.nanoTime()+tempoAlvo)/1000000);
					}catch(Exception erro){/*EVITA ERRO DE VALOR NEGATIVO*/}
				}
				tree.draw();	//RETIRA A SETA DO AUTO_DRAG
			}
		}).start();
	}
//GET OBJETO HOVER
	protected Objeto getObjetoHover(Point mouse){
		Objeto objHover=null;
		if(objHover==null)for(Modulo mod:tree.getVisibleMods().values()){
			if(mod.contains(mouse)&&mod!=Tree.getGhost())objHover=mod;
		}
		if(objHover==null)for(Nodulo nod:tree.getVisibleNods().values()){
			if(nod.contains(mouse))objHover=nod;
		}
		if(objHover==null)for(Conexao cox:tree.getVisibleCoxs().values()){
			if(cox.contains(mouse))objHover=cox;
		}
		return objHover;
	}
	protected Modulo getModuloHover(Point mouse,Modulo...modsToIgnore){
		Modulo modHover=null;
		for(Modulo mod:tree.getVisibleMods().values()){
			boolean toIgnore=false;
			if(modsToIgnore.length>0)for(Modulo modToIgnore:modsToIgnore){
				if(mod==modToIgnore){
					toIgnore=true;
					break;
				}
			}
			if(toIgnore||mod==Tree.getGhost())continue;
			if(mod.contains(mouse))modHover=mod;
		}
		return modHover;
	}
	protected Conexao getConexaoHover(Point mouse){
		Conexao coxHover=null;
		for(Conexao cox:tree.getVisibleCoxs().values()){
			if(cox.contains(mouse))coxHover=cox;
		}
		return coxHover;
	}
	protected Nodulo getNoduloHover(Point mouse){
		Nodulo nodHover=null;
		for(Nodulo nod:tree.getVisibleNods().values()){
			if(nod.contains(mouse))nodHover=nod;
		}
		return nodHover;
	}
//GET MOUSE POSITION
	protected Point getPosition(Point mouse){
		final JFrame janela=(JFrame)tree.getPainel().getJanela();
		final int menu=(janela.getJMenuBar()!=null?janela.getJMenuBar().getHeight():0);
		return new Point(mouse.x-tree.getX()-janela.getInsets().left,mouse.y-tree.getY()-janela.getInsets().top-menu);
	}
	protected Point getGridPosition(Point mouse){
		mouse=getPosition(mouse);
		mouse.x/=Tree.UNIT;
		mouse.y/=Tree.UNIT;
		return mouse;
	}
	protected Point getNonGridPosition(Point mouse){
		mouse=new Point(mouse);
		mouse.x=(mouse.x*Tree.UNIT)+(Tree.UNIT/2);
		mouse.y=(mouse.y*Tree.UNIT)+(Tree.UNIT/2);
		return mouse;
	}
//ZOOM
	private int setZoom(Point mouse,int rotation){
		final int zoom=Math.max(1,Math.min(24,Tree.UNIT-rotation));		//ZOOM(VALOR DE UNIT): 1-24
		final Point mouse1=getGridPosition(mouse);
		Tree.UNIT=zoom;
		final Point mouse2=getGridPosition(mouse);
		Tree.setLocal(Tree.getLocalX()+(mouse2.x-mouse1.x),Tree.getLocalY()+(mouse2.y-mouse1.y));
		return zoom;
	}
//TÍTULO
	protected void updateTituloBounds(){
		if(tree.getUI().getTitulo().isVisible()){
			final Modulo mod=(Modulo)tree.getUI().getTitulo().getObjeto();
			final int iconSize=(mod.isIconified()?Icone.getSize(Tree.UNIT):0);
			tree.getUI().getTitulo().setBounds(
					mod.getX(Tree.UNIT),
					mod.getY(Tree.UNIT)+iconSize,
					mod.getWidth(Tree.UNIT),
					mod.getHeight(Tree.UNIT)-iconSize
			);
		}
	}
//ATALHOS
	public void setModo(int modo){
		if(tree.getUI().getTitulo().isVisible())return;
		final TreeST.State state=tree.getStateContent(tree.getState());
		switch(modo){
			case TreeST.TO_CREATE:
				if(state.getCreateAction()!=null)state.getCreateAction().run();
			break;
			case TreeST.TO_CONNECT:
				if(state.getConnectAction()!=null)state.getConnectAction().run();
			break;
			case TreeST.TO_DELETE:
				if(state.getDeleteAction()!=null)state.getDeleteAction().run();
			break;
		}
	}
	//EXIBIR
		public void centralizar(){
			if(tree.getUI().getTitulo().isVisible())return;
			final Point oldLocal=Tree.getLocal();
			if(tree.getSelectedObjetos().isEmpty()){
				setFocusOn(new Objeto[]{Tree.getMestre()});
			}else{
				setFocusOn(tree.getSelectedObjetos().getAll().values().toArray(new Objeto[0]));
			}
			animate(oldLocal,Tree.getLocal());
			resetState();
		}
		public void zoom(int rotation){
			setZoom(new Point(tree.getWidth()/2,tree.getHeight()/2),-rotation);
			tree.draw();
		}
	//EDITAR
		public void undo(){
			if(tree.getUI().getTitulo().isVisible())return;
			tree.getUndoRedoManager().undo();
			resetState();
		}
		public void redo(){
			if(tree.getUI().getTitulo().isVisible())return;
			tree.getUndoRedoManager().redo();
			resetState();
		}
		public void cut(){
			if(tree.getUI().getTitulo().isVisible())return;
			final String cut=String.join("\n",tree.getText(tree.getSelectedObjetos()));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(cut),null);
			delete();
		}
		public void copy(){
			if(tree.getUI().getTitulo().isVisible())return;
			final String copy=String.join("\n",tree.getText(tree.getSelectedObjetos()));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copy),null);
			resetState();
		}
		private class TransferableImage implements Transferable{
			private Image imagem;
			public TransferableImage(Image img){this.imagem=img;}
			public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException,IOException{
				if(flavor.equals(DataFlavor.imageFlavor)&&imagem!=null)return imagem;
					else throw new UnsupportedFlavorException(flavor);
			}
			public DataFlavor[]getTransferDataFlavors(){
				final DataFlavor[]flavors=new DataFlavor[1];
				flavors[0]=DataFlavor.imageFlavor;
				return flavors;
			}
			public boolean isDataFlavorSupported(DataFlavor flavor){
				final DataFlavor[]flavors=getTransferDataFlavors();
				for(int i=0;i<flavors.length;i++){
					if(flavor.equals(flavors[i]))return true;
				}
				return false;
			}
		}
		public void copyAsImg(){
			if(tree.getUI().getTitulo().isVisible())return;
			final Image imagem=tree.getImage(new ListaObjeto(tree.getSelectedObjetos()));
			if(imagem==null)return;
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new TransferableImage(imagem),null);
			resetState();
		}
		public void paste(){
			if(tree.getUI().getTitulo().isVisible())return;
			tree.unSelectAll();
			try{
				final String texto=Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
				if(!texto.startsWith("<mind "))return;
				final Document tags=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(texto)));
				final Element mindTag=tags.getDocumentElement();
				final List<Objeto>lista=tree.addTree(mindTag,false);
				for(Objeto obj:lista)tree.select(obj);
			}catch(Exception erro){
				TreeUI.mensagem(TreeUI.getLang().get("T_Err2","Error: Couldn't paste objects!")+"\n"+erro,TreeUI.Options.ERRO);
			}
			if(tree.getSelectedObjetos().isEmpty())return;
			final Point local=Tree.getLocal();
			setFocusOn(tree.getSelectedObjetos().getAll().values().toArray(new Objeto[0]));	//MOVE FOCO PARA O CENTRO DO GRUPO
			final Point telaMeio=getGridPosition(new Point(tree.getPainel().getJanela().getWidth()/2,tree.getPainel().getJanela().getHeight()/2));
				//A DIFERENÇA ENTRE OS LOCAIS ALINHA TUDO PARA O CENTRO DA JANELA, E A DIFERENÇA ENTRE O MOUSE E O CENTRO ALINHA TUDO PARA O CURSOR
			final Point diff=new Point(local.x-Tree.getLocalX()-(mouseMoved.x-telaMeio.x),local.y-Tree.getLocalY()-(mouseMoved.y-telaMeio.y));
			for(Modulo mod:tree.getSelectedObjetos().getModulos()){
				mod.setLocationIndex(mod.getXIndex()-diff.x,mod.getYIndex()-diff.y);
			}
			for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
				nod.setLocationIndex(nod.getXIndex()-diff.x,nod.getYIndex()-diff.y);
			}
			Tree.setLocal(local.x,local.y);
			deleteGhostCoxs();
			tree.getUI().getSelecao().setEmpty();
			if(tree.getST().stateContains(TreeST.TO_CREATE)){
				final TreeST.State state=tree.getStateContent(tree.getState());
				if(state.getModulo()!=null){
					addUndoable(state.getModulo(),false);
				}else if(state.getNodulo()!=null){
					addUndoable(state.getNodulo(),false);
				}
			}
			tree.draw();
			tree.setState(TreeST.MOVE+TreeST.NORMAL);
		}
		public void delete(){
			if(tree.getUI().getTitulo().isVisible())return;
			tree.unSelect(Tree.getMestre());
			if(tree.getSelectedObjetos().isEmpty())return;
			delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
			resetState();
		}
		public void moveUp(boolean saltar){
			if(tree.getUI().getTitulo().isVisible())return;
			if(tree.getUI().getPopup().isShowing())return;
			final int salto=(saltar?8:1);
			if(tree.getSelectedObjetos().isEmpty()){
				Tree.setLocal(Tree.getLocalX(),Tree.getLocalY()+salto);
			}else{
				for(Modulo mod:tree.getSelectedObjetos().getModulos()){
					mod.setLocationIndex(mod.getXIndex(),mod.getYIndex()-salto);
				}
				for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
					nod.setLocationIndex(nod.getXIndex(),nod.getYIndex()-salto);
				}
				final List<Objeto>savedObjs=new ArrayList<>();
				savedObjs.addAll(tree.getSelectedObjetos().getModulos());
				savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
				tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(0,+salto));
			}
			tree.draw();
		}
		public void moveRight(boolean saltar){
			if(tree.getUI().getTitulo().isVisible())return;
			if(tree.getUI().getPopup().isShowing())return;
			final int salto=(saltar?8:1);
			if(tree.getSelectedObjetos().isEmpty()){
				Tree.setLocal(Tree.getLocalX()-salto,Tree.getLocalY());
			}else{
				for(Modulo mod:tree.getSelectedObjetos().getModulos()){
					mod.setLocationIndex(mod.getXIndex()+salto,mod.getYIndex());
				}
				for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
					nod.setLocationIndex(nod.getXIndex()+salto,nod.getYIndex());
				}
				final List<Objeto>savedObjs=new ArrayList<>();
				savedObjs.addAll(tree.getSelectedObjetos().getModulos());
				savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
				tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(-salto,0));
			}
			tree.draw();
		}
		public void moveLeft(boolean saltar){
			if(tree.getUI().getTitulo().isVisible())return;
			if(tree.getUI().getPopup().isShowing())return;
			final int salto=(saltar?8:1);
			if(tree.getSelectedObjetos().isEmpty()){
				Tree.setLocal(Tree.getLocalX()+salto,Tree.getLocalY());
			}else{
				for(Modulo mod:tree.getSelectedObjetos().getModulos()){
					mod.setLocationIndex(mod.getXIndex()-salto,mod.getYIndex());
				}
				for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
					nod.setLocationIndex(nod.getXIndex()-salto,nod.getYIndex());
				}
				final List<Objeto>savedObjs=new ArrayList<>();
				savedObjs.addAll(tree.getSelectedObjetos().getModulos());
				savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
				tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(+salto,0));
			}
			tree.draw();
		}
		public void moveDown(boolean saltar){
			if(tree.getUI().getTitulo().isVisible())return;
			if(tree.getUI().getPopup().isShowing())return;
			final int salto=(saltar?8:1);
			if(tree.getSelectedObjetos().isEmpty()){
				Tree.setLocal(Tree.getLocalX(),Tree.getLocalY()-salto);
			}else{
				for(Modulo mod:tree.getSelectedObjetos().getModulos()){
					mod.setLocationIndex(mod.getXIndex(),mod.getYIndex()+salto);
				}
				for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
					nod.setLocationIndex(nod.getXIndex(),nod.getYIndex()+salto);
				}
				final List<Objeto>savedObjs=new ArrayList<>();
				savedObjs.addAll(tree.getSelectedObjetos().getModulos());
				savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
				tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(0,-salto));
			}
			tree.draw();
		}
	//SELECIONAR
		public void selectAll(){
			if(tree.getUI().getTitulo().isVisible())return;
			tree.selectAll();
			resetState();
		}
		public void unSelectAll(){
			if(tree.getUI().getTitulo().isVisible()){
				tree.getUI().setTitulo(null);
			}
			tree.unSelectAll();
			resetState();
		}
		public void selectSons(){
			if(tree.getUI().getTitulo().isVisible())return;
			final List<Objeto>treesSelec=new ArrayList<>();
			treesSelec.addAll(tree.getSelectedObjetos().getModulos());
			treesSelec.addAll(tree.getSelectedObjetos().getConexoes());
			for(Objeto obj:treesSelec)tree.selectTree(obj);
			resetState();
		}
		public void invertSelection(){
			if(tree.getUI().getTitulo().isVisible())return;
			final List<Objeto>objs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
			tree.selectAll();
			for(Objeto obj:objs)tree.unSelect(obj);
			resetState();
		}
		public void selectModSemPai(){
			if(tree.getUI().getTitulo().isVisible())return;
			tree.unSelectAll();
			for(Objeto obj:tree.getObjetos().getAll().values()){
				if(obj.getTipo().is(Objeto.Tipo.MODULO)){
					final Modulo mod=(Modulo)obj;
					if(mod==Tree.getMestre())continue;
					boolean hasPai=false;
					for(Conexao cox:mod.getConexoes()){
						if(cox.getPai()!=mod){
							hasPai=true;
							break;
						}
					}
					if(!hasPai)tree.select(mod);
				}
			}
			resetState();
		}
	//MÓDULO
		public void editTitulo(){
			if(tree.getState()!=TreeST.NORMAL)return;
			if(tree.getSelectedObjetos().getModulos().size()!=1)return;
			tree.getUI().setTitulo(tree.getSelectedObjetos().getModulos().get(0));
			tree.setState(TreeST.EDIT_TITLE);
		}
		public void createModRelacionado(){
			if(tree.getUI().getTitulo().isVisible())return;
			deleteGhostCoxs();
			tree.getUI().getSelecao().setEmpty();
			if(!tree.getSelectedObjetos().getModulos().isEmpty()){
				final Modulo newMod=createModRelacionado(
						mouseMoved.x-Tree.getLocalX(),mouseMoved.y-Tree.getLocalY(),
						tree.getSelectedObjetos().getModulos());
				tree.setState(TreeST.MOVE+TreeST.TO_CREATE,newMod);
			}else{
				tree.unSelectAll();
				tree.draw();
				tree.setState(TreeST.NORMAL);
			}
		}
		public void startRelation(){
			if(tree.getUI().getTitulo().isVisible())return;
			deleteGhostCoxs();
			tree.getUI().getSelecao().setEmpty();
			Tree.getGhost().setLocationIndex(mouseMoved.x-Tree.getLocalX(),mouseMoved.y-Tree.getLocalY());
			relateToGhost(tree.getSelectedObjetos().getModulos());
			tree.unSelectAll();
			tree.draw();
			if(!Tree.getGhost().getConexoes().isEmpty()){
				tree.setState(TreeST.MOVE+TreeST.TO_CONNECT);
			}else tree.setState(TreeST.NORMAL);
		}
		public void deleteMods(){
			if(tree.getUI().getTitulo().isVisible())return;
			deleteGhostCoxs();
			tree.getUI().getSelecao().setEmpty();
			tree.unSelect(Tree.getMestre());
			if(tree.getSelectedObjetos().getModulos().isEmpty())return;
			final List<Modulo>selectedMods=new ArrayList<>(tree.getSelectedObjetos().getModulos());
			tree.unSelectAll();		//DESELECIONA COXS E NODS NÃO CONECTADOS
			for(Modulo mod:selectedMods){
				tree.select(mod);
				for(Conexao cox:mod.getConexoes()){
					tree.select(cox);
				}
			}
			delete();
		}
	//CONEXÃO
		public void invertCox(){
			if(tree.getUI().getTitulo().isVisible())return;
			final List<Objeto>newObjs=new ArrayList<>();
			for(Conexao cox:tree.getSelectedObjetos().getConexoes()){
				if(cox.getPai()==Tree.getMestre())continue;
				final Conexao newCox=new Conexao(cox.getSon(),cox.getPai());
				newCox.setBorda(cox.getBorda());
				newCox.setGrossura(cox.getGrossura());
				newCox.setTexto(cox.getTexto());
				final List<Nodulo>newNods=new ArrayList<>();
				for(Nodulo nod:cox.getNodulos()){
					final Nodulo newNod=new Nodulo(newCox,nod.getXIndex(),nod.getYIndex());
					newNods.add(newNod);
				}
				newObjs.add(newCox);
				Collections.reverse(newNods);
				for(Nodulo newNod:newNods){
					newObjs.add(newNod);
				}
			}
			addUndoable(newObjs,true);
			resetState();
		}
//RESET
	public void resetState(){
		deleteGhostCoxs();
		tree.getUI().getSelecao().setEmpty();
		if(tree.getST().stateContains(TreeST.TO_CREATE)){
			final TreeST.State state=tree.getStateContent(tree.getState());
			if(state.getModulo()!=null){
				addUndoable(state.getModulo(),false);
			}else if(state.getNodulo()!=null){
				addUndoable(state.getNodulo(),false);
			}
		}
		tree.draw();
		tree.setState(TreeST.NORMAL);
	}
//REPETIÇÕES
	//ADD/DEL
		public void addUndoable(List<Objeto>objs,boolean addToTree){
			final List<Objeto>allObjs=ListaObjeto.toList(objs).getAllOrdened();	//SEPARA EM MOD(1,2),COX(1,2,3),NOD(1,2,3,4)
			for(Objeto obj:allObjs)switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					for(Conexao cox:mod.getConexoes()){
						objs.add(cox);										//INCLUI COXS, QUE SERÃO ADICIONADOS JUNTOS
						for(Nodulo nod:cox.getNodulos())objs.add(nod);		//INCLUI NODS, QUE SERÃO ADICIONADOS JUNTOS
					}
				break;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					for(Nodulo nod:cox.getNodulos())objs.add(nod);			//INCLUI NODS, QUE SERÃO ADICIONADOS JUNTOS
				break;
				case NODULO:break;
				case SEGMENTO:break;
			}
			if(addToTree)for(Objeto obj:allObjs)tree.add(obj);
			Collections.reverse(allObjs);						//INVERTE PARA NOD(4,3,2,1),COX(3,2,1),MOD(2,1)
			tree.getUndoRedoManager().addUndoObjeto(allObjs.toArray(new Objeto[0]),UndoRedoObjeto.Modo.DEL);
		}
		public void addUndoable(Objeto obj,boolean addToTree){
			final List<Objeto>objs=new ArrayList<>();
			objs.add(obj);
			addUndoable(objs,addToTree);
		}
		public void delUndoable(List<Objeto>objs){
			final ListaObjeto lista=ListaObjeto.toList(objs);
			for(Conexao cox:lista.getConexoes()){
				for(Nodulo nod:cox.getNodulos())lista.add(nod);			//INCLUI NODS, QUE SERÃO DELETADOS JUNTOS
			}
			for(Modulo mod:lista.getModulos()){
				for(Conexao cox:mod.getConexoes()){
					lista.add(cox);										//INCLUI COXS, QUE SERÃO DELETADOS JUNTOS
					for(Nodulo nod:cox.getNodulos())lista.add(nod); 	//INCLUI NODS, QUE SERÃO DELETADOS JUNTOS
				}
			}
			objs=lista.getAllOrdenedInverted();		//SEPARA EM NOD(1,2,3,4),COX(1,2,3),MOD(1,2)
			for(Objeto obj:objs)tree.del(obj);
			Collections.reverse(objs);				//INVERTE PARA MOD(2,1),COX(3,2,1),NOD(4,3,2,1)
			tree.getUndoRedoManager().addUndoObjeto(objs.toArray(new Objeto[0]),UndoRedoObjeto.Modo.ADD);
		}
		public void delUndoable(Objeto obj){
			final List<Objeto>objs=new ArrayList<>();
			objs.add(obj);
			delUndoable(objs);
		}
	//SET
		protected void setHover(SelecaoST.State state){
			final Point mouse=getNonGridPosition(mouseMoved);
			tree.unSelectAll();
			tree.getUI().getPopup().close();
			Modulo modHover=getModuloHover(mouse);
			switch(state){
				case TO_UNSELECT:default:break;
				case TO_CREATE:
					if(modHover!=null){
						tree.selectToBeCreator(modHover);
					}else{
						final Conexao coxHover=getConexaoHover(mouse);
						if(coxHover!=null){
							final Segmento seg=coxHover.segmentContains(mouse);
							tree.selectToBeCreator(seg);
						}
					}
				break;
				case TO_CREATE_SON:
					if(modHover==Tree.getMestre())modHover=null;
					if(modHover!=null){
						tree.selectToBeSon(modHover);
					}
				break;
				case TO_CREATE_PAI:
					if(modHover!=null){
						tree.selectToBePai(modHover);
					}
				break;
				case TO_DELETE:
					if(modHover!=null){
						tree.selectToBeDeleted(modHover);
					}else{
						final Nodulo nodHover=getNoduloHover(mouse);
						if(nodHover!=null){
							tree.selectToBeDeleted(nodHover);
						}else{
							final Conexao coxHover=getConexaoHover(mouse);
							if(coxHover!=null){
								tree.selectToBeDeleted(coxHover);
							}
						}
					}
				break;
			}
			tree.draw();
		}
		protected void setArea(SelecaoST.State state,boolean addTo,int...locations){
			Rectangle allChunksIndexes=null;
			Rectangle newChunksIndexes=null;
			switch(locations.length){
				default:	//MANTÉM UMA ÁREA
					if(!addTo)switch(state){
						case TO_SELECT:
						case TO_DELETE:	tree.unSelectAll();			break;
						default:		tree.unSelectAllMods();		break;
					}
					tree.getUI().getSelecao().setState(state);
					allChunksIndexes=tree.getChunksIndexes(tree.getUI().getSelecao().getFormIndex());
					newChunksIndexes=allChunksIndexes;
				break;
				case 4:		//CRIA UMA ÁREA
					if(!addTo)switch(state){
						case TO_SELECT:
						case TO_DELETE:	tree.unSelectAll();			break;
						default:		tree.unSelectAllMods();		break;
					}
					tree.getUI().getSelecao().setState(state);
					tree.getUI().getSelecao().setAncoraIndex(locations[0],locations[1]);
					tree.getUI().getSelecao().setAreaIndex(locations[2],locations[3]);
					allChunksIndexes=tree.getChunksIndexes(tree.getUI().getSelecao().getFormIndex());
					newChunksIndexes=allChunksIndexes;
				break;
				case 2:		//EXPANDE UMA ÁREA
					final Rectangle areaOld=tree.getUI().getSelecao().getFormIndex();
					tree.getUI().getSelecao().setAreaIndex(locations[0],locations[1]);
					final Rectangle areaNew=tree.getUI().getSelecao().getFormIndex();
					final Rectangle areaAll=new Rectangle(		//ÁREA TOTAL
							Math.min(areaOld.x,areaNew.x),
							Math.min(areaOld.y,areaNew.y),
							Math.max(areaOld.width,areaNew.width),
							Math.max(areaOld.height,areaNew.height));
					allChunksIndexes=tree.getChunksIndexes(areaAll);
					newChunksIndexes=tree.getChunksIndexes(areaNew);	//ONDE TUDO É SELEC
				break;
			}
			for(int y=allChunksIndexes.y;y-allChunksIndexes.y<allChunksIndexes.height;y++){
				for(int x=allChunksIndexes.x;x-allChunksIndexes.x<allChunksIndexes.width;x++){
					final Chunk chunk=tree.getChunks().get(new Point(x,y));
					if(chunk!=null){
						if(y>newChunksIndexes.y&&y-newChunksIndexes.y<newChunksIndexes.height-1&&	//CHUNK NÃO FICA NAS BORDAS VERTICAIS
								x>newChunksIndexes.x&&x-newChunksIndexes.x<newChunksIndexes.width-1){	//CHUNK NÃO FICA NAS BORDAS HORIZONTAIS
							for(Objeto obj:chunk.getObjetos().getAll().values()){	//TODOS OS OBJS DENTRO DA ÁREA SÃO AUTOMATICAMENTE SELEC
								switch(state){
									case TO_SELECT:default:	tree.select(obj);				break;
									case TO_CREATE:			tree.selectToBeCreator(obj);	break;
									case TO_CREATE_SON:		tree.selectToBeSon(obj);		break;
									case TO_CREATE_PAI:		tree.selectToBePai(obj);		break;
									case TO_DELETE:			tree.selectToBeDeleted(obj);	break;
								}
							}
						}else{
							for(Modulo mod:chunk.getObjetos().getModulos()){	//SELEC MODS
								if(tree.getUI().getSelecao().intersects(mod.getForm(Tree.UNIT))){
									switch(state){
										case TO_SELECT:default:	tree.select(mod);				break;
										case TO_CREATE:			tree.selectToBeCreator(mod);	break;
										case TO_CREATE_SON:		tree.selectToBeSon(mod);		break;
										case TO_CREATE_PAI:		tree.selectToBePai(mod);		break;
										case TO_DELETE:			tree.selectToBeDeleted(mod);	break;
									}
								}else if(!addTo)tree.unSelect(mod);
							}
							for(Conexao cox:chunk.getObjetos().getConexoes()){	//SELEC COXS
								if(tree.getUI().getSelecao().intersects(cox.getForm(Tree.UNIT))){
									switch(state){
										case TO_SELECT:default:	tree.select(cox);				break;
										case TO_CREATE:			break;
										case TO_CREATE_SON:		break;
										case TO_CREATE_PAI:		break;
										case TO_DELETE:			tree.selectToBeDeleted(cox);	break;
									}
								}else if(!addTo)switch(state){
									case TO_SELECT:
									case TO_DELETE:	tree.unSelect(cox);	break;
									default:		break;
								}
							}
							for(Nodulo nod:chunk.getObjetos().getNodulos()){	//SELEC NODS
								if(tree.getUI().getSelecao().intersects(nod.getForm(Tree.UNIT))){
									switch(state){
										case TO_SELECT:default:	tree.select(nod);				break;
										case TO_CREATE:			break;
										case TO_CREATE_SON:		break;
										case TO_CREATE_PAI:		break;
										case TO_DELETE:			tree.selectToBeDeleted(nod);	break;
									}
								}else if(!addTo)switch(state){
									case TO_SELECT:
									case TO_DELETE:	tree.unSelect(nod);	break;
									default:		break;
								}
							}
						}
					}
				}
			}
			tree.draw();
		}
		protected void addAndSelect(Objeto obj){
			if(obj==null)return;
			switch(obj.getTipo()){
				case MODULO:
					final Modulo mod=(Modulo)obj;
					final List<Objeto>savedObjs=new ArrayList<>();
					savedObjs.add(mod);
					for(Conexao cox:mod.getConexoes())savedObjs.add(cox);
					addUndoable(savedObjs,false);
					tree.unSelectAll();
					tree.select(mod);
					for(Conexao cox:mod.getConexoes())tree.select(cox);
				break;
				case CONEXAO:break;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					addUndoable(nod,false);
					tree.unSelectAll();
					tree.select(nod);
				break;
				case SEGMENTO:break;
			}
			tree.draw();
		}
	//CREATE
		public Modulo createMod(int x,int y){
			final Modulo newMod=new Modulo(x,y,"");
			addUndoable(newMod,true);
			tree.unSelectAll();
			tree.select(newMod);
			tree.draw();
			return newMod;
		}
		public Modulo createModRelacionado(int x,int y,List<Modulo>modsPais){
			final Modulo newMod=new Modulo(x,y,"");
			if(modsPais.size()==1){
				final Modulo mod=modsPais.get(0);
				newMod.setCor(mod.getCor());
				newMod.setBorda(mod.getBorda());
			}
			tree.add(newMod);
			relateMods(modsPais,newMod);
			tree.draw();
			return newMod;
		}
		public Nodulo createNod(Segmento seg,int x,int y,boolean undoable){
			final int index=seg.getConexao().getSegmentos().indexOf(seg);
			final Nodulo newNod=new Nodulo(seg.getConexao(),x,y);
			newNod.setIndexOnCox(index);
			if(undoable){
				addUndoable(newNod,true);
			}else tree.add(newNod);
			tree.unSelectAll();
			if(undoable)tree.select(newNod);
			tree.draw();
			return newNod;
		}
		protected void createNodOnGhostCoxs(int x,int y){
			for(Conexao cox:Tree.getGhost().getConexoes()){
				final Nodulo newNod=new Nodulo(cox,x,y);
				tree.add(newNod);
			}
			tree.draw();
		}
	//RELATE
		protected void relateMods(List<Modulo>modsPais,Modulo modSon){
			for(Modulo modPai:modsPais){
				if(modPai==modSon)continue;
				final Conexao newCox=new Conexao(modPai,modSon);
				newCox.setBorda(newCox.getSon().getBorda());
				tree.add(newCox);
				tree.select(newCox);
			}
			tree.unSelectAll();
			tree.draw();
		}
		protected void relateToMods(List<Modulo>mods){
			if(Tree.getGhost().getConexoes().isEmpty())return;
			final List<Objeto>savedObjs=new ArrayList<>();
			for(Modulo mod:mods){
				for(Conexao cox:Tree.getGhost().getConexoes()){
					final Conexao newCox=new Conexao(mod,cox.getSon());
					newCox.setBorda(cox.getBorda());
					tree.add(newCox);
					savedObjs.add(newCox);
					for(Nodulo nod:cox.getNodulos()){
						final Nodulo newNod=new Nodulo(newCox,nod.getXIndex(),nod.getYIndex());
						tree.add(newNod);
						savedObjs.add(newNod);
					}
				}
			}
			addUndoable(savedObjs,false);
			tree.unSelectAll();
			for(Objeto obj:savedObjs)tree.select(obj);
			tree.draw();
		}
		protected void relateToMods(Modulo mod){relateToMods(Arrays.asList(mod));}
		public void relateToGhost(List<Modulo>mods){
			for(Modulo mod:mods){
				final Conexao newCox=new Conexao(Tree.getGhost(),mod);
				newCox.setBorda(newCox.getSon().getBorda());
				tree.add(newCox);
			}
			tree.unSelectAll();
			tree.draw();
		}
		public void relateToGhost(Modulo mod){relateToGhost(Arrays.asList(mod));}
	//DELETE
		public void deleteGhostCoxs(){
			while(!Tree.getGhost().getConexoes().isEmpty()){
				tree.del(Tree.getGhost().getConexoes().get(0));
			}
			tree.draw();
		}
}