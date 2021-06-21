package element.tree;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import element.tree.objeto.ListaObjeto;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.propriedades.Icone;
import element.tree.objeto.conexao.Nodulo;
import element.tree.objeto.conexao.Segmento;
import element.tree.objeto.modulo.Modulo;
import element.tree.undoRedo.UndoRedoObjeto;
public class Actions{
//STATES
	public final static int NORMAL=0;				//[0-9]
	public final static int TO_CREATE=1;			//[0-9]
	public final static int TO_CONNECT=2;			//[0-9]
	public final static int TO_DELETE=3;			//[0-9]
	public final static int EDIT_TITLE=4;			//[0-9]
	public final static int DRAG=10;				//[0-9]0
	public final static int MOVE=20;				//[0-9]0
	public final static int SELECT_AREA=30;			//[0-9]0
	public final static int WAITING1=100;			//[0-9]00
	public final static int WAITING2=200;			//[0-9]00
	public final static int WAITING3=300;			//[0-9]00
	public final static int LEFT=1000;				//[0-9]000
	public final static int MIDDLE=2000;			//[0-9]000
	public final static int RIGHT=3000;				//[0-9]000
	public final static int DRAG_ALL=10000;			//[0-9]0000
	public final static int AUTO_DRAG_ALL=20000;	//[0-9]0000
	private int state=NORMAL;
		public int getState(){return state;}
	private class State{
		private Action mousePressed;
			public Action getMousePressedAction(){return mousePressed;}
			public void setMousePressedAction(Action action){mousePressed=action;}
		private Action mouseReleased;
			public Action getMouseReleasedAction(){return mouseReleased;}
			public void setMouseReleasedAction(Action action){mouseReleased=action;}
		private Action mouseCliqued;
			public Action getMouseCliquedAction(){return mouseCliqued;}
			public void setMouseCliquedAction(Action action){mouseCliqued=action;}
		private Action mouseDragged;
			public Action getMouseDraggedAction(){return mouseDragged;}
			public void setMouseDraggedAction(Action action){mouseDragged=action;}
		private Action mouseMoved;
			public Action getMouseMovedAction(){return mouseMoved;}
			public void setMouseMovedAction(Action action){mouseMoved=action;}
		private Action keyPressed;
			public Action getKeyPressedAction(){return keyPressed;}
			public void setKeyPressedAction(Action action){keyPressed=action;}
		private Action keyReleased;
			public Action getKeyReleasedAction(){return keyReleased;}
			public void setKeyReleasedAction(Action action){keyReleased=action;}
		private Action created;
			public Action getCreateAction(){return created;}
			public void setCreateAction(Action action){created=action;}
		private Action connected;
			public Action getConnectAction(){return connected;}
			public void setConnectAction(Action action){connected=action;}
		private Action deleted;
			public Action getDeleteAction(){return deleted;}
			public void setDeleteAction(Action action){deleted=action;}
		private Modulo mod;
			public void setModulo(Modulo mod){this.mod=mod;}
			public Modulo getModulo(){return mod;}
		private Conexao cox;
			public void setConexao(Conexao cox){this.cox=cox;}
			public Conexao getConexao(){return cox;}
		private Nodulo nod;
			public void setNodulo(Nodulo nod){this.nod=nod;}
			public Nodulo getNodulo(){return nod;}
		private Segmento seg;
			public void setSegmento(Segmento seg){this.seg=seg;}
			public Segmento getSegmento(){return seg;}
		private Action action;
			public void setAction(Action action){this.action=action;}
			public Action getAction(){return action;}
	}
	private abstract class Action{
		public void run(){}
		public void run(MouseEvent m){}
		public void run(KeyEvent k){}
	}
	private HashMap<Integer,State>states=new HashMap<>();
		private HashMap<Integer,State>getStates(){return states;}
		public State setState(int newStateIndex,Objeto...objs){
			final State oldState=getStates().get(state);
			oldState.setModulo(null);		//ZERA MOD
			oldState.setConexao(null);		//ZERA COX
			oldState.setNodulo(null);		//ZERA NOD
			oldState.setSegmento(null);		//ZERA SEG
			state=newStateIndex;
			final State newState=getStates().get(newStateIndex);
			if(objs.length>0)for(Objeto obj:objs){
				switch(obj.getTipo()){
					case MODULO:	newState.setModulo((Modulo)obj);		break;
					case CONEXAO:	newState.setConexao((Conexao)obj);		break;
					case NODULO:	newState.setNodulo((Nodulo)obj);		break;
					case SEGMENTO:	newState.setSegmento((Segmento)obj);	break;
				}
			}
			if(newState.getAction()!=null)newState.getAction().run();
			return newState;
		}
		public boolean stateContains(int state){
			final int camada1=(state%10000%1000%100%10);
			if(state==camada1)return true;
			final int camada2=(state%10000%1000%100)-camada1;
			if(state==camada2)return true;
			final int camada3=(state%10000%1000)-camada1-camada2;
			if(state==camada3)return true;
			final int camada4=(state%10000)-camada1-camada2-camada3;
			if(state==camada4)return true;
			final int camada5=(state)-camada1-camada2-camada3-camada4;
			if(state==camada5)return true;
			return false;
		}
//VAR GLOBAIS
	private JFrame janela;
	private Tree tree;
	private Point oldMouse=new Point();
//MOUSES
	public Point mousePressed=new Point();
	public Point mouseReleased=new Point();
	public Point mouseDragged=new Point();
	public Point mouseMoved=new Point();
	public Point mouseNonGrid=new Point();
//MAIN
	public Actions(Tree tree,JFrame janela){
		this.tree=tree;
		this.janela=janela;
//NORMAL
		states.put(NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.NORMAL);
				};
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){										//HÁ MOD SELEC
							if(modSelec.getState().is(Modulo.State.SELECTED)){
								setState(LEFT+WAITING2+NORMAL,modSelec);
							}else{
								setState(LEFT+WAITING1+NORMAL,modSelec);
							}
						}else{													//NENHUM MOD FOI SELEC, VERIFICA POR NODS
							final Nodulo nodSelec=getNoduloHover(mouse);
							if(nodSelec!=null){									//HÁ NOD SELEC
								if(nodSelec.getState().is(Nodulo.State.SELECTED)){
									setState(LEFT+WAITING2+NORMAL,nodSelec);
								}else{
									setState(LEFT+WAITING1+NORMAL,nodSelec);
								}
							}else{												//NENHUM NOD FOI SELEC, VERIFICA POR COXS
								final Conexao coxSelec=getConexaoHover(mouse);
								if(coxSelec!=null){								//HÁ COX SELEC
									if(coxSelec.getState().is(Conexao.State.SELECTED)){
										setState(LEFT+WAITING2+NORMAL,coxSelec);
									}else{
										setState(LEFT+WAITING1+NORMAL,coxSelec);
									}
								}else{											//NENHUM COX FOI SELEC
									setState(LEFT+WAITING1+NORMAL);
								}
							}
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(MIDDLE+WAITING1+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseCliquedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)&&m.getClickCount()==2){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){
							tree.unSelectAll();
							tree.select(modSelec);
							tree.draw();
							tree.setTitulo(modSelec);	//TITULO ACIMA DO DRAW
							setState(EDIT_TITLE);
						}
					}
				}
			});
			setKeyPressedAction(new Action(){
				public void run(KeyEvent k){
					if(tree.getPopup().isShowing())return;
					boolean ctrl=(k.isControlDown());
					final int salto=(ctrl?8:1);
					switch(k.getKeyCode()){
						case KeyEvent.VK_UP:
						case KeyEvent.VK_KP_UP:
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
						break;
						case KeyEvent.VK_DOWN:
						case KeyEvent.VK_KP_DOWN:
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
						break;
						case KeyEvent.VK_RIGHT:
						case KeyEvent.VK_KP_RIGHT:
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
						break;
						case KeyEvent.VK_LEFT:
						case KeyEvent.VK_KP_LEFT:
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
								tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(salto,0));
							}
							tree.draw();
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//MIDDLE+WAITING1+NORMAL
		states.put(MIDDLE+WAITING1+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.SELECT);
				};
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(AUTO_DRAG_ALL+NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}
				}
			});
		}});
//DRAG_ALL+NORMAL
		states.put(DRAG_ALL+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.SELECT);
				};
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE);
					setState(DRAG_ALL+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE_SON);
					setState(DRAG_ALL+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_DELETE);
					setState(DRAG_ALL+TO_DELETE);
				}
			});
		}});
//AUTO_DRAG_ALL+NORMAL
		states.put(AUTO_DRAG_ALL+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.AUTODRAG);
					startAutoDrag();
				};
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						setState(NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(MIDDLE+WAITING1+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						setState(NORMAL);
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//LEFT+WAITING1+NORMAL
		states.put(LEFT+WAITING1+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+LEFT+WAITING1+NORMAL,getModulo(),getConexao(),getNodulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						if(getModulo()!=null){				//HÁ MOD SELEC
							if(getModulo().contains(mouse))tree.select(getModulo());
						}else if(getNodulo()!=null){		//HÁ NOD SELEC
							if(getNodulo().contains(mouse))tree.select(getNodulo());
						}else if(getConexao()!=null){		//HÁ COX SELEC
							if(getConexao().contains(mouse))tree.select(getConexao());
						}
						tree.draw();
						setState(NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.unSelectAll();
						final Point mouse=getPosition(m.getPoint());
						if(getModulo()!=null){				//HÁ MOD SELEC
							if(getModulo().contains(mouse))tree.select(getModulo());
						}else if(getNodulo()!=null){		//HÁ NOD SELEC
							if(getNodulo().contains(mouse))tree.select(getNodulo());
						}else if(getConexao()!=null){		//HÁ COX SELEC
							if(getConexao().contains(mouse))tree.select(getConexao());
						}
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.ALT,Cursor.LEFT)){
						if(getConexao()!=null){
							for(Conexao cox:tree.getSelectedObjetos().getConexoes()){
								for(Nodulo nod:cox.getNodulos()){
									tree.select(nod);
								}
							}
						}
						oldMouse=new Point(mouseDragged);
						for(Modulo mod:tree.getSelectedObjetos().getModulos()){
							mod.setLocationIndex(mod.getXIndex()-(mouseDragged.x-mouseDraggedAtual.x),mod.getYIndex()-(mouseDragged.y-mouseDraggedAtual.y));
						}
						for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
							nod.setLocationIndex(nod.getXIndex()-(mouseDragged.x-mouseDraggedAtual.x),nod.getYIndex()-(mouseDragged.y-mouseDraggedAtual.y));
						}
						tree.draw();
						setState(DRAG+NORMAL);
					
					}else if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						setArea(Selecao.State.TO_SELECT,true,
								mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT)){
						setArea(Selecao.State.TO_SELECT,false,
								mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+NORMAL);
					}
				}
			});
		}});
//DRAG_ALL+LEFT+WAITING1+NORMAL
		states.put(DRAG_ALL+LEFT+WAITING1+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						if(getModulo()!=null){				//HÁ MOD SELEC
							if(getModulo().contains(mouse))tree.select(getModulo());
						}else{
							if(getNodulo()!=null){			//HÁ NOD SELEC
								if(getNodulo().contains(mouse))tree.select(getNodulo());
							}else{
								if(getConexao()!=null){		//HÁ COX SELEC
									if(getConexao().contains(mouse))tree.select(getConexao());
								}
							}
						}
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.unSelectAll();
						final Point mouse=getPosition(m.getPoint());
						if(getModulo()!=null){				//HÁ MOD SELEC
							if(getModulo().contains(mouse))tree.select(getModulo());
						}else{
							if(getNodulo()!=null){			//HÁ NOD SELEC
								if(getNodulo().contains(mouse))tree.select(getNodulo());
							}else{
								if(getConexao()!=null){		//HÁ COX SELEC
									if(getConexao().contains(mouse))tree.select(getConexao());
								}
							}
						}
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						setState(LEFT+WAITING1+NORMAL,getModulo(),getConexao(),getNodulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						updateTituloBounds();
						tree.draw();
					}
				}
			});
		}});
//SELECT_AREA+NORMAL
		states.put(SELECT_AREA+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.AREA_SELECT+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getSelecao().setEmpty();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						setArea(Selecao.State.TO_SELECT,true,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					}else if(Cursor.match(m,Cursor.LEFT)){
						setArea(Selecao.State.TO_SELECT,false,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							setArea(Selecao.State.TO_SELECT,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE,false);
					setState(SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE_SON,false);
					setState(SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_DELETE,false);
					setState(SELECT_AREA+TO_DELETE);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+NORMAL
		states.put(DRAG_ALL+SELECT_AREA+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.AREA_SELECT+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(SELECT_AREA+NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							setArea(Selecao.State.TO_SELECT,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE_SON,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_DELETE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
				}
			});
		}});
//LEFT+WAITING2+NORMAL
		states.put(LEFT+WAITING2+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						setState(DRAG_ALL+LEFT+WAITING2+NORMAL,getModulo(),getConexao(),getNodulo());
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						if(getModulo()!=null){				//HÁ MOD SELEC
							if(getModulo().contains(mouse))tree.unSelect(getModulo());
						}else if(getNodulo()!=null){		//HÁ NOD SELEC
							if(getNodulo().contains(mouse))tree.unSelect(getNodulo());
						}else if(getConexao()!=null){		//HÁ COX SELEC
							if(getConexao().contains(mouse))tree.unSelect(getConexao());
						}
						tree.draw();
						setState(NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						if(tree.getSelectedObjetos().getAll().size()==1){
							tree.unSelectAll();
						}else{
							tree.unSelectAll();
							if(getModulo()!=null){
								if(getModulo().contains(mouse))tree.select(getModulo());
							}else if(getNodulo()!=null){
								if(getNodulo().contains(mouse))tree.select(getNodulo());
							}else if(getConexao()!=null){
								if(getConexao().contains(mouse))tree.select(getConexao());
							}
						}
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						setArea(Selecao.State.TO_SELECT,true,
								mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getConexao()!=null){
							for(Conexao cox:tree.getSelectedObjetos().getConexoes()){
								for(Nodulo nod:cox.getNodulos()){
									tree.select(nod);
								}
							}
						}
						oldMouse=new Point(mouseDragged);
						for(Modulo mod:tree.getSelectedObjetos().getModulos()){
							mod.setLocationIndex(mod.getXIndex()-(mouseDragged.x-mouseDraggedAtual.x),mod.getYIndex()-(mouseDragged.y-mouseDraggedAtual.y));
						}
						for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
							nod.setLocationIndex(nod.getXIndex()-(mouseDragged.x-mouseDraggedAtual.x),nod.getYIndex()-(mouseDragged.y-mouseDraggedAtual.y));
						}
						tree.draw();
						setState(DRAG+NORMAL);
					}
				}
			});
		}});
//DRAG_ALL+LEFT+WAITING2+NORMAL
		states.put(DRAG_ALL+LEFT+WAITING2+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						if(getModulo()!=null){				//HÁ MOD SELEC
							if(getModulo().contains(mouse))tree.unSelect(getModulo());
						}else{
							if(getNodulo()!=null){			//HÁ NOD SELEC
								if(getNodulo().contains(mouse))tree.unSelect(getNodulo());
							}else{
								if(getConexao()!=null){		//HÁ COX SELEC
									if(getConexao().contains(mouse))tree.unSelect(getConexao());
								}
							}
						}
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						if(getModulo()!=null){
							if(getModulo().contains(mouse)){
								if(tree.getSelectedObjetos().getModulos().size()>1){
									 tree.unSelectAll();
									 tree.select(getModulo());
								}else tree.unSelectAll();
							}else tree.unSelectAll();
						}else{
							if(getNodulo()!=null){
								if(getNodulo().contains(mouse)){
									if(tree.getSelectedObjetos().getNodulos().size()>1){
										 tree.unSelectAll();
										 tree.select(getNodulo());
									}else tree.unSelectAll();
								}else tree.unSelectAll();
							}else{
								if(getConexao()!=null){
									if(getConexao().contains(mouse)){
										if(tree.getSelectedObjetos().getConexoes().size()>1){
											 tree.unSelectAll();
											 tree.select(getConexao());
										}else tree.unSelectAll();
									}else tree.unSelectAll();
								}else tree.unSelectAll();	//SE CLICK NO VAZIO
							}
						}
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(LEFT+WAITING2+NORMAL,getModulo(),getConexao(),getNodulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						updateTituloBounds();
						tree.draw();
					}
				}
			});
		}});
//DRAG+NORMAL
		states.put(DRAG+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+DRAG+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						final List<Objeto>savedObjs=new ArrayList<>();
						savedObjs.addAll(tree.getSelectedObjetos().getModulos());
						savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
						tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(-(mouseMoved.x-oldMouse.x),-(mouseMoved.y-oldMouse.y)));
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final List<Objeto>savedObjs=new ArrayList<>();
						savedObjs.addAll(tree.getSelectedObjetos().getModulos());
						savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
						tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(-(mouseMoved.x-oldMouse.x),-(mouseMoved.y-oldMouse.y)));
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						for(Modulo mod:tree.getSelectedObjetos().getModulos()){
							mod.setLocationIndex(mod.getXIndex()-(mouseDragged.x-mouseDraggedAtual.x),mod.getYIndex()-(mouseDragged.y-mouseDraggedAtual.y));
						}
						for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
							nod.setLocationIndex(nod.getXIndex()-(mouseDragged.x-mouseDraggedAtual.x),nod.getYIndex()-(mouseDragged.y-mouseDraggedAtual.y));
						}
						tree.draw();
					}
				}
			});
		}});
//DRAG_ALL+DRAG+NORMAL
		states.put(DRAG_ALL+DRAG+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final List<Objeto>savedObjs=new ArrayList<>();
						savedObjs.addAll(tree.getSelectedObjetos().getModulos());
						savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
						tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(-(mouseMoved.x-oldMouse.x),-(mouseMoved.y-oldMouse.y)));
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG+NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
						if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//MOVE+NORMAL
		states.put(MOVE+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.MOVE+Cursor.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						addUndoable(savedObjs,false);
						setState(NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+MOVE+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						addUndoable(savedObjs,false);
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseMovedAtual=getGridPosition(m.getPoint());
					for(Modulo mod:tree.getSelectedObjetos().getModulos()){
						mod.setLocationIndex(mod.getXIndex()-(mouseMoved.x-mouseMovedAtual.x),mod.getYIndex()-(mouseMoved.y-mouseMovedAtual.y));
					}
					for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
						nod.setLocationIndex(nod.getXIndex()-(mouseMoved.x-mouseMovedAtual.x),nod.getYIndex()-(mouseMoved.y-mouseMovedAtual.y));
					}
					tree.draw();
				}
			});
		}});
//DRAG_ALL+MOVE+NORMAL
		states.put(DRAG_ALL+MOVE+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						addUndoable(savedObjs,false);
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						addUndoable(savedObjs,false);
						tree.unSelectAll();
						tree.draw();
						setState(DRAG_ALL+RIGHT+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(MOVE+NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
						if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//EDIT_TITLE
	states.put(EDIT_TITLE,new State(){{
		setAction(new Action(){
			public void run(){
				tree.getCursor().set(Cursor.EDIT_TITLE+Cursor.NORMAL);
			}
		});
		setMousePressedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.LEFT)){
					tree.getCursor().set(Cursor.EDIT_TITLE+Cursor.SELECT);
				}else if(Cursor.match(m,Cursor.MIDDLE)){
					setState(DRAG_ALL+EDIT_TITLE);
				}else if(Cursor.match(m,Cursor.RIGHT)){
					tree.setTitulo(null);
					tree.draw();
					setState(RIGHT+WAITING1+NORMAL);
				}
			}
		});
		setMouseReleasedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.LEFT)){
					tree.setTitulo(null);
					tree.draw();
					setState(NORMAL);
				}
			}
		});
		setMouseDraggedAction(new Action(){
			public void run(MouseEvent m){
				final Point mouseDraggedAtual=getGridPosition(m.getPoint());
				if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
					tree.setTitulo(null);
					setArea(Selecao.State.TO_SELECT,true,
							mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
							mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					setState(SELECT_AREA+NORMAL);
				}else if(Cursor.match(m,Cursor.LEFT)){
					tree.setTitulo(null);
					setArea(Selecao.State.TO_SELECT,false,
							mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
							mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					setState(SELECT_AREA+NORMAL);
				}
			}
		});
		setKeyReleasedAction(new Action(){
			public void run(KeyEvent k){
				switch(k.getKeyCode()){
					case KeyEvent.VK_ESCAPE:
						//delete() É ATIVADO
						setState(NORMAL);
					break;
				}
			}
		});
	}});
//DRAG_ALL+EDIT_TITLE
	states.put(DRAG_ALL+EDIT_TITLE,new State(){{
		setAction(new Action(){
			public void run(){
				tree.getCursor().set(Cursor.DRAG+Cursor.EDIT_TITLE+Cursor.SELECT);
			}
		});
		setMousePressedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.MIDDLE)){
					setState(DRAG_ALL+EDIT_TITLE);
				}else if(Cursor.match(m,Cursor.RIGHT)){
					tree.setTitulo(null);
					tree.draw();
					setState(RIGHT+WAITING1+NORMAL);
				}
			}
		});
		setMouseReleasedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.LEFT)){
					tree.setTitulo(null);
					tree.draw();
					setState(DRAG_ALL+NORMAL);
				}else if(Cursor.match(m,Cursor.MIDDLE)){
					setState(EDIT_TITLE);
				}
			}
		});
		setMouseDraggedAction(new Action(){
			public void run(MouseEvent m){
				final Point mouseDraggedAtual=getGridPosition(m.getPoint());
				if(Cursor.match(m,Cursor.MIDDLE)){
					Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
					updateTituloBounds();
					tree.draw();
				}
			}
		});
		setKeyReleasedAction(new Action(){
			public void run(KeyEvent k){
				switch(k.getKeyCode()){
					case KeyEvent.VK_ESCAPE:
						tree.setTitulo(null);
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					break;
				}
			}
		});
	}});
//RIGHT+WAITING1+NORMAL
		states.put(RIGHT+WAITING1+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.RIGHT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){
							if(!modSelec.getState().is(Modulo.State.SELECTED))tree.unSelectAll();
							tree.unSelect(modSelec);
							tree.select(modSelec);
							tree.getPopup().show(mousePressed,modSelec);
						}else{
							final Nodulo nodSelec=getNoduloHover(mouse);
							if(nodSelec!=null){
								if(!nodSelec.getState().is(Nodulo.State.SELECTED))tree.unSelectAll();
								tree.unSelect(nodSelec);
								tree.select(nodSelec);
								tree.getPopup().show(mousePressed,nodSelec);
							}else{
								final Conexao coxSelec=getConexaoHover(mouse);
								if(coxSelec!=null){
									if(!coxSelec.getState().is(Conexao.State.SELECTED))tree.unSelectAll();
									tree.unSelect(coxSelec);
									tree.select(coxSelec);
									final Segmento segSelec=coxSelec.segmentContains(mouse);
									tree.getPopup().show(mousePressed,segSelec);
								}else{
									tree.unSelectAll();
									tree.getPopup().show(mousePressed,null);
								}
							}
						}
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.RIGHT)){
						tree.unSelectAll();
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
						setState(DRAG_ALL+RIGHT+NORMAL);
					}
				}
			});
		}});
//DRAG_ALL+RIGHT+NORMAL
		states.put(DRAG_ALL+RIGHT+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.RIGHT)){
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.RIGHT)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//TO_CREATE
		states.put(TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.CREATE+Cursor.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modHover=getModuloHover(mouse);
						if(modHover!=null){
							tree.select(modHover);
							setState(WAITING2+TO_CREATE);
						}else{
							final Conexao coxHover=getConexaoHover(mouse);
							if(coxHover!=null){
								final Segmento segHover=coxHover.segmentContains(mouse);
								setState(WAITING2+TO_CREATE,segHover);
							}else{
								setState(WAITING1+TO_CREATE);
							}
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+TO_CREATE);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					tree.unSelectAll();
					final Point mouse=getPosition(m.getPoint());
					final Modulo modHover=getModuloHover(mouse);
					if(modHover!=null){
						tree.selectToBeCreator(modHover);
					}else{
						final Conexao coxHover=getConexaoHover(mouse);
						if(coxHover!=null){
							final Segmento segHover=coxHover.segmentContains(mouse);
							tree.selectToBeCreator(segHover);
						}
					}
					tree.draw();
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_UNSELECT);
					setState(NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//DRAG_ALL+TO_CREATE
		states.put(DRAG_ALL+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.CREATE+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(TO_CREATE);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_UNSELECT);
					setState(DRAG_ALL+NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE_SON);
					setState(DRAG_ALL+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_DELETE);
					setState(DRAG_ALL+TO_DELETE);
				}
			});
		}});
//WAITING1+TO_CREATE
		states.put(WAITING1+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getCursor().set(Cursor.MOVE+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.CREATE+Cursor.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING1+TO_CREATE,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						addAndSelect(getModulo());
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							setState(MOVE+TO_CREATE,getModulo());
						}else{
							createMod(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							setState(TO_CREATE);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.unSelectAll();
							tree.draw();
						}else{
							createMod(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
						}
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							getModulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setArea(Selecao.State.TO_CREATE_PAI,true,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+TO_CREATE,getModulo());
						}else{
							setArea(Selecao.State.TO_CREATE,true,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+TO_CREATE);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							addAndSelect(getModulo());
							setArea(Selecao.State.TO_SELECT,false,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+NORMAL);
						}else{
							setArea(Selecao.State.TO_CREATE,false,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+TO_CREATE);
						}
					}
				}
			});
		}});
//DRAG_ALL+WAITING1+TO_CREATE
		states.put(DRAG_ALL+WAITING1+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.DRAG+Cursor.CREATE+Cursor.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							createMod(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							setState(DRAG_ALL+TO_CREATE);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.unSelectAll();
							tree.draw();
						}else{
							createMod(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
						}
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(WAITING1+TO_CREATE,getModulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//SELECT_AREA+TO_CREATE
		states.put(SELECT_AREA+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getCursor().set(Cursor.AREA_PAI+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.AREA_CREATE+Cursor.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+TO_CREATE,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getSelecao().setEmpty();
						addAndSelect(getModulo());
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(getModulo()!=null){
							relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.unSelectAll();
							tree.draw();
							setState(MOVE+TO_CREATE,getModulo());
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(TO_CREATE);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(getModulo()!=null){
							addAndSelect(getModulo());
							setArea(Selecao.State.TO_SELECT,false);
							tree.getSelecao().setEmpty();
							tree.draw();
							setState(NORMAL);
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(NORMAL);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							getModulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setArea(Selecao.State.TO_CREATE_PAI,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							setArea(Selecao.State.TO_CREATE,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							addAndSelect(getModulo());
							setArea(Selecao.State.TO_SELECT,false,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+NORMAL);
						}else{
							setArea(Selecao.State.TO_CREATE,false,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(getModulo()!=null){
								addAndSelect(getModulo());
								setArea(Selecao.State.TO_SELECT,false);
								setState(SELECT_AREA+NORMAL);
							}else{
								setArea(Selecao.State.TO_CREATE,false);
							}
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					addAndSelect(getModulo());
					setArea(Selecao.State.TO_SELECT,false);
					setState(SELECT_AREA+NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					addAndSelect(getModulo());
					setArea(Selecao.State.TO_CREATE_SON,false);
					setState(SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					addAndSelect(getModulo());
					setArea(Selecao.State.TO_DELETE,false);
					setState(SELECT_AREA+TO_DELETE);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+TO_CREATE
		states.put(DRAG_ALL+SELECT_AREA+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getCursor().set(Cursor.DRAG+Cursor.AREA_PAI+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.DRAG+Cursor.AREA_CREATE+Cursor.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(getModulo()!=null){
							relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.unSelectAll();
							tree.draw();
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(DRAG_ALL+TO_CREATE);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(getModulo()!=null){
							addAndSelect(getModulo());
							setArea(Selecao.State.TO_CREATE,false);
							tree.getSelecao().setEmpty();
							tree.draw();
							setState(DRAG_ALL+NORMAL);
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(DRAG_ALL+NORMAL);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(SELECT_AREA+TO_CREATE,getModulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(getModulo()!=null){
								addAndSelect(getModulo());
								setArea(Selecao.State.TO_SELECT,false);
								setState(DRAG_ALL+SELECT_AREA+NORMAL);
							}else{
								setArea(Selecao.State.TO_CREATE,false);
							}
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					addAndSelect(getModulo());
					setArea(Selecao.State.TO_SELECT,false);
					setState(DRAG_ALL+SELECT_AREA+NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					addAndSelect(getModulo());
					setArea(Selecao.State.TO_CREATE_SON,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					addAndSelect(getModulo());
					setArea(Selecao.State.TO_DELETE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
				}
			});
		}});
//WAITING2+TO_CREATE
		states.put(WAITING2+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getCursor().set(Cursor.MOVE+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.CREATE+Cursor.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING2+TO_CREATE,getModulo(),getSegmento());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						addAndSelect(getModulo());
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.draw();
							setState(MOVE+TO_CREATE,getModulo());
						}else{
							if(getSegmento()!=null){
								createNod(getSegmento(),mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),true);
								setState(TO_CREATE);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							addAndSelect(getModulo());
							setState(NORMAL);
						}else{
							if(getSegmento()!=null){
								createNod(getSegmento(),mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),true);
								setState(NORMAL);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							getModulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							setState(DRAG+TO_CREATE,getModulo());
						}else{
							if(getSegmento()!=null){
								final Nodulo newNod=createNod(getSegmento(),mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY(),false);
								tree.unSelect(newNod);
								setState(DRAG+TO_CREATE,newNod);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(DRAG+TO_CREATE,newMod);
							}
						}
					}
				}
			});
		}});
//DRAG_ALL+WAITING2+TO_CREATE
		states.put(DRAG_ALL+WAITING2+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.DRAG+Cursor.CREATE+Cursor.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.draw();
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							if(getSegmento()!=null){
								createNod(getSegmento(),mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),true);
								setState(DRAG_ALL+TO_CREATE);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							addAndSelect(getModulo());
							setState(DRAG_ALL+NORMAL);
						}else{
							if(getSegmento()!=null){
								createNod(getSegmento(),mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),true);
								setState(DRAG_ALL+NORMAL);
							}else{
								final Modulo newMod=createModRelacionado(
										mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(WAITING2+TO_CREATE,getModulo(),getSegmento());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//DRAG+TO_CREATE
		states.put(DRAG+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+DRAG+TO_CREATE,getModulo(),getNodulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						if(getModulo()!=null){
							addAndSelect(getModulo());
						}else if(getNodulo()!=null){
							addAndSelect(getNodulo());
						}
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							setState(MOVE+TO_CREATE,getModulo());
						}else{
							addAndSelect(getNodulo());
							setState(NORMAL);
						}
					
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							addAndSelect(getModulo());
						}else if(getNodulo()!=null){
							addAndSelect(getNodulo());
						}
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							getModulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else if(getNodulo()!=null){
							getNodulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
						tree.draw();
					}
				}
			});
		}});
//DRAG_ALL+DRAG+TO_CREATE
		states.put(DRAG_ALL+DRAG+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							addAndSelect(getNodulo());
							setState(DRAG_ALL+NORMAL);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							addAndSelect(getModulo());
						}else if(getNodulo()!=null){
							addAndSelect(getNodulo());
						}
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG+TO_CREATE,getModulo(),getNodulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//MOVE+TO_CREATE
		states.put(MOVE+TO_CREATE,new State(){
			private boolean lock=false;
			{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.MOVE+Cursor.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modHover=getModuloHover(mouse,getModulo());
						if(modHover!=null){
							tree.select(modHover);
							setState(WAITING2+TO_CREATE,getModulo());
						}else{
							setState(WAITING1+TO_CREATE,getModulo());
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						addAndSelect(getModulo());
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						//NADA
					}else if(Cursor.match(m,Cursor.LEFT)){
						addAndSelect(getModulo());
						setState(NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseMovedAtual=getGridPosition(m.getPoint());
					final Point mouse=getPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL)){
						getModulo().setLocationIndex(mouseMovedAtual.x-Tree.getLocalX(),mouseMovedAtual.y-Tree.getLocalY());
						tree.unSelectAll();
						final Modulo modHover=getModuloHover(mouse,getModulo());
						if(modHover!=null){
							tree.selectToBePai(modHover);
						}
						tree.draw();
					}else{
						getModulo().setLocationIndex(mouseMovedAtual.x-Tree.getLocalX(),mouseMovedAtual.y-Tree.getLocalY());
						tree.draw();
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						addAndSelect(getModulo());
						setArea(Selecao.State.TO_SELECT,false,
								mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+NORMAL);
					}
				}
			});
			setKeyPressedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(lock)return;
							lock=true;
							final Modulo modHover=getModuloHover(getNonGridPosition(mouseMoved),getModulo());
							if(modHover!=null){
								tree.selectToBePai(modHover);
								tree.draw();
							}
							tree.getCursor().set(Cursor.PAI+Cursor.NORMAL);
						break;
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							lock=false;
							tree.unSelectAllMods();
							tree.draw();
							tree.getCursor().set(Cursor.MOVE+Cursor.NORMAL);
						break;
					}
				}
			});
		}});
//DRAG_ALL+MOVE+TO_CREATE
		states.put(DRAG_ALL+MOVE+TO_CREATE,new State(){
			private boolean lock=false;
			{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						//NADA
					}else if(Cursor.match(m,Cursor.LEFT)){
						addAndSelect(getModulo());
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(MOVE+TO_CREATE,getModulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyPressedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(lock)return;
							lock=true;
							final Modulo modHover=getModuloHover(getNonGridPosition(mouseMoved),getModulo());
							if(modHover!=null){
								tree.selectToBePai(modHover);
								tree.draw();
							}
							tree.getCursor().set(Cursor.DRAG+Cursor.PAI+Cursor.NORMAL);
						break;
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							lock=false;
							tree.unSelectAllMods();
							tree.draw();
							tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.NORMAL);
						break;
					}
				}
			});
		}});
//TO_CONNECT
		states.put(TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.SON+Cursor.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						Modulo modSelec=getModuloHover(mouse);
						if(modSelec==Tree.getMestre())modSelec=null;
						if(modSelec!=null){
							setState(WAITING2+TO_CONNECT,modSelec);
						}else{
							setState(WAITING1+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					tree.unSelectAllMods();
					final Point mouse=getPosition(m.getPoint());
					final Modulo modHover=getModuloHover(mouse);
					tree.selectToBeSon(modHover);
					tree.draw();
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_UNSELECT);
					setState(NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//DRAG_ALL+TO_CONNECT
		states.put(DRAG_ALL+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.SON+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(TO_CONNECT);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE);
					setState(DRAG_ALL+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_UNSELECT);
					setState(DRAG_ALL+NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_DELETE);
					setState(DRAG_ALL+TO_DELETE);
				}
			});
		}});
//WAITING1+TO_CONNECT
		states.put(WAITING1+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getCursor().set(Cursor.SON+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.PAI+Cursor.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING1+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						deleteGhostCoxs();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							setState(TO_CONNECT);
						}else{
							createNodOnGhostCoxs(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							setState(MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						deleteGhostCoxs();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						if(Tree.getGhost().getConexoes().isEmpty()){
							setArea(Selecao.State.TO_CREATE_SON,true,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							setArea(Selecao.State.TO_CREATE_PAI,true,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
						tree.draw();
						setState(SELECT_AREA+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						if(Tree.getGhost().getConexoes().isEmpty()){
							setArea(Selecao.State.TO_CREATE_SON,false,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							setArea(Selecao.State.TO_CREATE_PAI,false,
									mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
						tree.draw();
						setState(SELECT_AREA+TO_CONNECT);
					}
				}
			});
		}});
//DRAG_ALL+WAITING1+TO_CONNECT
		states.put(DRAG_ALL+WAITING1+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getCursor().set(Cursor.DRAG+Cursor.SON+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.DRAG+Cursor.PAI+Cursor.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							setState(DRAG_ALL+TO_CONNECT);
						}else{
							createNodOnGhostCoxs(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							setState(DRAG_ALL+MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						deleteGhostCoxs();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						setState(WAITING1+TO_CONNECT);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//SELECT_AREA+TO_CONNECT
		states.put(SELECT_AREA+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getCursor().set(Cursor.AREA_SON+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.AREA_PAI+Cursor.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						deleteGhostCoxs();
						tree.getSelecao().setEmpty();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							tree.draw();
							if(Tree.getGhost().getConexoes().isEmpty()){
								setState(TO_CONNECT);
							}else{
								setState(MOVE+TO_CONNECT);
							}
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
								relateToGhost(tree.getSelectedObjetos().getModulos());
							}else{
								relateToMods(tree.getSelectedObjetos().getModulos());
							}
							setState(MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							deleteGhostCoxs();
							setState(NORMAL);
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
								relateToGhost(tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CONNECT);
							}else{
								relateToMods(tree.getSelectedObjetos().getModulos());
								deleteGhostCoxs();
								setState(NORMAL);
							}
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							setArea(Selecao.State.TO_CREATE_SON,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setArea(Selecao.State.TO_CREATE_PAI,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							setArea(Selecao.State.TO_CREATE_SON,false,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setArea(Selecao.State.TO_CREATE_PAI,false,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(Tree.getGhost().getConexoes().isEmpty()){
								setArea(Selecao.State.TO_CREATE_SON,false);
							}else setArea(Selecao.State.TO_CREATE_PAI,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					deleteGhostCoxs();
					setArea(Selecao.State.TO_CREATE,false);
					setState(SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					deleteGhostCoxs();
					setArea(Selecao.State.TO_SELECT,false);
					setState(SELECT_AREA+NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					deleteGhostCoxs();
					setArea(Selecao.State.TO_DELETE,false);
					setState(SELECT_AREA+TO_DELETE);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+TO_CONNECT
		states.put(DRAG_ALL+SELECT_AREA+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getCursor().set(Cursor.DRAG+Cursor.AREA_SON+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.DRAG+Cursor.AREA_PAI+Cursor.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							tree.draw();
							if(Tree.getGhost().getConexoes().isEmpty()){
								setState(DRAG_ALL+TO_CONNECT);
							}else{
								setState(DRAG_ALL+MOVE+TO_CONNECT);
							}
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
								relateToGhost(tree.getSelectedObjetos().getModulos());
							}else{
								relateToMods(tree.getSelectedObjetos().getModulos());
							}
							setState(DRAG_ALL+MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							deleteGhostCoxs();
							setState(DRAG_ALL+NORMAL);
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
								relateToGhost(tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CONNECT);
							}else{
								relateToMods(tree.getSelectedObjetos().getModulos());
								deleteGhostCoxs();
								setState(DRAG_ALL+NORMAL);
							}
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(SELECT_AREA+TO_CREATE);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(Tree.getGhost().getConexoes().isEmpty()){
								setArea(Selecao.State.TO_CREATE_SON,false);
							}else setArea(Selecao.State.TO_CREATE_PAI,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					deleteGhostCoxs();
					setArea(Selecao.State.TO_CREATE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					deleteGhostCoxs();
					setArea(Selecao.State.TO_SELECT,false);
					setState(DRAG_ALL+SELECT_AREA+NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					deleteGhostCoxs();
					setArea(Selecao.State.TO_DELETE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
				}
			});
		}});
//WAITING2+TO_CONNECT
		states.put(WAITING2+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getCursor().set(Cursor.SON+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.PAI+Cursor.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING2+TO_CONNECT,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						deleteGhostCoxs();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							relateToGhost(getModulo());
						}else{
							relateToMods(getModulo());
						}
						setState(MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							relateToGhost(getModulo());
							setState(MOVE+TO_CONNECT);
						}else{
							relateToMods(getModulo());
							deleteGhostCoxs();
							setState(NORMAL);
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						relateToGhost(getModulo());
						setState(DRAG+TO_CONNECT);
					}
				}
			});
		}});
//DRAG_ALL+WAITING2+TO_CONNECT
		states.put(DRAG_ALL+WAITING2+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getCursor().set(Cursor.DRAG+Cursor.SON+Cursor.SELECT);
					}else{
						tree.getCursor().set(Cursor.DRAG+Cursor.PAI+Cursor.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							relateToGhost(getModulo());
						}else{
							relateToMods(getModulo());
						}
						setState(DRAG_ALL+MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							Tree.getGhost().setLocationIndex(mouseReleased.x-Tree.getLocalX(),mouseReleased.y-Tree.getLocalY());
							relateToGhost(getModulo());
							setState(DRAG_ALL+MOVE+TO_CONNECT);
						}else{
							relateToMods(getModulo());
							deleteGhostCoxs();
							setState(DRAG_ALL+NORMAL);
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(WAITING2+TO_CONNECT,getModulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//DRAG+TO_CONNECT
		states.put(DRAG+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+DRAG+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						deleteGhostCoxs();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){
							relateToMods(modSelec);
						}
						setState(MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){
							relateToMods(modSelec);
						}
						deleteGhostCoxs();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						tree.unSelectAll();
						final Point mouse=getPosition(m.getPoint());
						final Modulo modHover=getModuloHover(mouse);
						if(modHover!=null){
							tree.selectToBePai(modHover);
						}
						tree.draw();
					}
				}
			});
		}});
//DRAG_ALL+DRAG+TO_CONNECT
		states.put(DRAG_ALL+DRAG+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.MOVE+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){
							relateToMods(modSelec);
						}
						setState(DRAG_ALL+MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){
							relateToMods(modSelec);
						}
						deleteGhostCoxs();
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG+TO_CONNECT);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//MOVE+TO_CONNECT
		states.put(MOVE+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.PAI+Cursor.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Modulo modSelec=getModuloHover(mouse);
						if(modSelec!=null){
							setState(WAITING2+TO_CONNECT,modSelec);
						}else{
							setState(WAITING1+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						deleteGhostCoxs();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseMovedAtual=getGridPosition(m.getPoint());
					Tree.getGhost().setLocationIndex(mouseMovedAtual.x-Tree.getLocalX(),mouseMovedAtual.y-Tree.getLocalY());
					tree.unSelectAllMods();
					final Point mouse=getPosition(m.getPoint());
					final Modulo modHover=getModuloHover(mouse);
					if(modHover!=null){
						tree.selectToBePai(modHover);
					}
					tree.draw();
				}
			});
		}});
//DRAG_ALL+MOVE+TO_CONNECT
		states.put(DRAG_ALL+MOVE+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.PAI+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(MOVE+TO_CONNECT);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//TO_DELETE
		states.put(TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DELETE+Cursor.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						setState(WAITING1+TO_DELETE);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+TO_DELETE);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouse=getPosition(m.getPoint());
					tree.unSelectAll();
					final Modulo modDel=getModuloHover(mouse);
					if(modDel!=null){
						tree.selectToBeDeleted(modDel);
					}else{
						final Nodulo nodDel=getNoduloHover(mouse);
						if(nodDel!=null){
							tree.selectToBeDeleted(nodDel);
						}else{
							final Conexao coxDel=getConexaoHover(mouse);
							if(coxDel!=null){
								tree.selectToBeDeleted(coxDel);
							}
						}
					}
					tree.draw();
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_UNSELECT);
					setState(NORMAL);
				}
			});
		}});
//DRAG_ALL+TO_DELETE
		states.put(DRAG_ALL+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.DELETE+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						setState(DRAG_ALL+WAITING1+TO_DELETE);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(TO_DELETE);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE);
					setState(DRAG_ALL+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_CREATE_SON);
					setState(DRAG_ALL+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setHover(Selecao.State.TO_UNSELECT);
					setState(DRAG_ALL+NORMAL);
				}
			});
		}});
//WAITING1+TO_DELETE
		states.put(WAITING1+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DELETE+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING1+TO_DELETE);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Objeto objSelec=getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
							}
						}
						tree.draw();
						setState(TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Objeto objSelec=getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
							}
						}
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						setArea(Selecao.State.TO_DELETE,true,
								mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						setArea(Selecao.State.TO_DELETE,false,
								mouseDragged.x-Tree.getLocalX(),mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+TO_DELETE);
					}
				}
			});
		}});
//DRAG_ALL+WAITING1+TO_DELETE
		states.put(DRAG_ALL+WAITING1+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.DELETE+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Objeto objSelec=getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
							}
						}
						tree.draw();
						setState(DRAG_ALL+TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=getPosition(m.getPoint());
						final Objeto objSelec=getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
							}
						}
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(WAITING1+TO_DELETE);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//SELECT_AREA+TO_DELETE
		states.put(SELECT_AREA+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.AREA_DELETE+Cursor.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getSelecao().setEmpty();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getSelecao().setEmpty();
						tree.draw();
						setState(TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getSelecao().setEmpty();
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						setArea(Selecao.State.TO_DELETE,true,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						tree.draw();
					}else if(Cursor.match(m,Cursor.LEFT)){
						setArea(Selecao.State.TO_DELETE,false,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							setArea(Selecao.State.TO_DELETE,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE,false);
					setState(SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE_SON,false);
					setState(SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_SELECT,false);
					setState(SELECT_AREA+NORMAL);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+TO_DELETE
		states.put(DRAG_ALL+SELECT_AREA+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getCursor().set(Cursor.DRAG+Cursor.AREA_DELETE+Cursor.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getSelecao().setEmpty();
						tree.draw();
						setState(DRAG_ALL+TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getSelecao().setEmpty();
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(SELECT_AREA+TO_DELETE);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							setArea(Selecao.State.TO_DELETE,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_CREATE_SON,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					setArea(Selecao.State.TO_SELECT,false);
					setState(DRAG_ALL+SELECT_AREA+NORMAL);
				}
			});
		}});
		setState(NORMAL);
	}
//INSERIR AÇÕES
	public void putIntputs(){
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
				if(tree.getTitulo().getObjeto()!=null){		//ATUALIZA O TAMANHO DA FONTE DE TÍTULO
					tree.updateTituloFont();
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
				final State state=getStates().get(getState());
				if(state.getMousePressedAction()!=null)state.getMousePressedAction().run(m);
			}
			public void mouseReleased(MouseEvent m){
				if(!tree.isEnabled())return;
				final Point mouseAtual=getGridPosition(m.getPoint());
				mouseReleased=new Point(mouseAtual);
				mouseDragged=new Point(mouseAtual);
				mouseMoved=new Point(mouseAtual);
				final State state=getStates().get(getState());
				if(state.getMouseReleasedAction()!=null)state.getMouseReleasedAction().run(m);
			}
			public void mouseClicked(MouseEvent m){
				if(!tree.isEnabled())return;
				final Point mouseAtual=getGridPosition(m.getPoint());
				mousePressed=new Point(mouseAtual);
				mouseReleased=new Point(mouseAtual);
				mouseDragged=new Point(mouseAtual);
				mouseMoved=new Point(mouseAtual);
				final State state=getStates().get(getState());
				if(state.getMouseCliquedAction()!=null)state.getMouseCliquedAction().run(m);
			}
		});
		janela.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent m){
				if(!tree.isEnabled())return;
				final Point mouseDraggedAtual=getGridPosition(m.getPoint());
				if(!mouseDraggedAtual.equals(mouseDragged)){
					final State state=getStates().get(getState());
					if(state.getMouseDraggedAction()!=null)state.getMouseDraggedAction().run(m);
					mouseDragged=new Point(mouseDraggedAtual);
					mouseMoved=new Point(mouseDraggedAtual);
				}
			}
			public void mouseMoved(MouseEvent m){
				if(!tree.isEnabled())return;	//IGNORA SE DESFOCADO
				final Point mouseMovedAtual=getGridPosition(m.getPoint());
				if(!mouseMovedAtual.equals(mouseMoved)){
					final State state=getStates().get(getState());
					if(state.getMouseMovedAction()!=null)state.getMouseMovedAction().run(m);
					mouseMoved=new Point(mouseMovedAtual);
				}
			}
		});
		janela.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent k){
				if(!tree.isEnabled())return;
				final State state=getStates().get(getState());
				if(state.getKeyPressedAction()!=null)state.getKeyPressedAction().run(k);
			}
			public void keyReleased(KeyEvent k){
				if(!tree.isEnabled())return;
				final State state=getStates().get(getState());
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
				if(tree.getTitulo().isVisible())tree.getTitulo().requestFocus();
			}
			public void windowDeactivated(WindowEvent w){
				if(!janela.isUndecorated())tree.setEnabled(false);
			}
		});
	}
	private void startAutoDrag(){
		new Thread(new Runnable(){
			public void run(){
				final int restRadius=40;
				final int speedControl=4;
				final int fps=30;
				final long tempoAlvo=1000000000/fps;
				double acumX=0;
				double acumY=0;
				while(getState()==AUTO_DRAG_ALL+NORMAL){
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
	private Objeto getObjetoHover(Point mouse){
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
	private Modulo getModuloHover(Point mouse,Modulo...modsToIgnore){
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
	private Conexao getConexaoHover(Point mouse){
		Conexao coxHover=null;
		for(Conexao cox:tree.getVisibleCoxs().values()){
			if(cox.contains(mouse))coxHover=cox;
		}
		return coxHover;
	}
	private Nodulo getNoduloHover(Point mouse){
		Nodulo nodHover=null;
		for(Nodulo nod:tree.getVisibleNods().values()){
			if(nod.contains(mouse))nodHover=nod;
		}
		return nodHover;
	}
//GET MOUSE POSITION
	public Point getPosition(Point mouse){
		final int menu=(janela.getJMenuBar()!=null?janela.getJMenuBar().getHeight():0);
		return new Point(mouse.x-tree.getX()-janela.getInsets().left,mouse.y-tree.getY()-janela.getInsets().top-menu);
	}
	public Point getGridPosition(Point mouse){
		mouse=getPosition(mouse);
		mouse.x/=Tree.UNIT;
		mouse.y/=Tree.UNIT;
		return mouse;
	}
	public Point getNonGridPosition(Point mouse){
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
	private void updateTituloBounds(){
		if(tree.getTitulo().isVisible()){
			final Modulo mod=(Modulo)tree.getTitulo().getObjeto();
			final int iconSize=(mod.isIconified()?Icone.getSize():0);
			tree.getTitulo().setBounds(
					mod.getX(),
					mod.getY()+iconSize,
					mod.getWidth(),
					mod.getHeight()-iconSize
			);
		}
	}
//ATALHOS
	public void setModo(int modo){
		if(tree.getTitulo().isVisible())return;
		final State state=getStates().get(getState());
		switch(modo){
			case TO_CREATE:
				if(state.getCreateAction()!=null)state.getCreateAction().run();
			break;
			case TO_CONNECT:
				if(state.getConnectAction()!=null)state.getConnectAction().run();
			break;
			case TO_DELETE:
				if(state.getDeleteAction()!=null)state.getDeleteAction().run();
			break;
		}
	}
	//EXIBIR
		public void centralizar(){
			if(tree.getTitulo().isVisible())return;
			final Point oldLocal=Tree.getLocal();
			if(tree.getSelectedObjetos().isEmpty()){
				tree.setFocusOn(new Objeto[]{Tree.getMestre()});
			}else{
				tree.setFocusOn(tree.getSelectedObjetos().getAll().values().toArray(new Objeto[0]));
			}
			tree.animate(oldLocal,Tree.getLocal());
			resetState();
		}
		public void zoom(int rotation){
			setZoom(new Point(tree.getWidth()/2,tree.getHeight()/2),-rotation);
			tree.draw();
		}
	//EDITAR
		public void undo(){
			if(tree.getTitulo().isVisible())return;
			tree.getUndoRedoManager().undo();
			resetState();
		}
		public void redo(){
			if(tree.getTitulo().isVisible())return;
			tree.getUndoRedoManager().redo();
			resetState();
		}
		public void cut(){
			if(tree.getTitulo().isVisible())return;
			final String cut=String.join("\n",tree.getText(tree.getSelectedObjetos()));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(cut),null);
			delete();
		}
		public void copy(){
			if(tree.getTitulo().isVisible())return;
			final String copy=String.join("\n",tree.getText(tree.getSelectedObjetos()));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(copy),null);
			resetState();
		}
		public void paste(){
			if(tree.getTitulo().isVisible())return;
			tree.unSelectAll();
			final Font fonte=Tree.Fonte.FONTE;
			try{
				final String texto=Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
				if(!texto.startsWith("<mind "))return;
				final Document tags=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(texto)));
				final Element mindTag=tags.getDocumentElement();
				final List<Objeto>lista=tree.addTree(mindTag,false);
				for(Objeto obj:lista)tree.select(obj);
			}catch(Exception erro){
				Tree.mensagem(Tree.getLang().get("T_Err2","Error: Couldn't paste objects!")+"\n"+erro,Tree.Options.ERRO);
			}
			Tree.Fonte.FONTE=fonte;
			if(tree.getSelectedObjetos().isEmpty())return;
			final Point local=Tree.getLocal();
			tree.setFocusOn(tree.getSelectedObjetos().getAll().values().toArray(new Objeto[0]));	//MOVE FOCO PARA O CENTRO DO GRUPO
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
			tree.getSelecao().setEmpty();
			if(stateContains(TO_CREATE)){
				final State state=getStates().get(getState());
				if(state.getModulo()!=null){
					addUndoable(state.getModulo(),false);
				}else if(state.getNodulo()!=null){
					addUndoable(state.getNodulo(),false);
				}
			}
			tree.draw();
			setState(MOVE+NORMAL);
		}
		public void delete(){
			if(tree.getTitulo().isVisible())return;
			tree.unSelect(Tree.getMestre());
			if(tree.getSelectedObjetos().isEmpty())return;
			delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
			resetState();
		}
	//SELECIONAR
		public void selectAll(){
			if(tree.getTitulo().isVisible())return;
			tree.selectAll();
			resetState();
		}
		public void unSelectAll(){
			if(tree.getTitulo().isVisible()){
				tree.setTitulo(null);
			}
			tree.unSelectAll();
			resetState();
		}
		public void selectSons(){
			if(tree.getTitulo().isVisible())return;
			final List<Objeto>treesSelec=new ArrayList<>();
			treesSelec.addAll(tree.getSelectedObjetos().getModulos());
			treesSelec.addAll(tree.getSelectedObjetos().getConexoes());
			for(Objeto obj:treesSelec)tree.selectTree(obj);
			resetState();
		}
		public void invertSelection(){
			if(tree.getTitulo().isVisible())return;
			final List<Objeto>objs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
			tree.selectAll();
			for(Objeto obj:objs)tree.unSelect(obj);
			resetState();
		}
		public void selectModSemPai(){
			if(tree.getTitulo().isVisible())return;
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
			if(getState()!=NORMAL)return;
			if(tree.getSelectedObjetos().getModulos().size()!=1)return;
			tree.setTitulo(tree.getSelectedObjetos().getModulos().get(0));
			setState(Actions.EDIT_TITLE);
		}
		public void createModRelacionado(){
			if(tree.getTitulo().isVisible())return;
			deleteGhostCoxs();
			tree.getSelecao().setEmpty();
			if(!tree.getSelectedObjetos().getModulos().isEmpty()){
				final Modulo newMod=createModRelacionado(
						mouseMoved.x-Tree.getLocalX(),mouseMoved.y-Tree.getLocalY(),
						tree.getSelectedObjetos().getModulos());
				setState(MOVE+TO_CREATE,newMod);
			}else{
				tree.unSelectAll();
				tree.draw();
				setState(NORMAL);
			}
		}
		public void startRelation(){
			if(tree.getTitulo().isVisible())return;
			deleteGhostCoxs();
			tree.getSelecao().setEmpty();
			Tree.getGhost().setLocationIndex(mouseMoved.x-Tree.getLocalX(),mouseMoved.y-Tree.getLocalY());
			for(Modulo mod:tree.getSelectedObjetos().getModulos()){
				tree.add(new Conexao(Tree.getGhost(),mod));
			}
			tree.unSelectAll();
			tree.draw();
			if(!Tree.getGhost().getConexoes().isEmpty()){
				setState(MOVE+TO_CONNECT);
			}else setState(NORMAL);
		}
		public void deleteMods(){
			if(tree.getTitulo().isVisible())return;
			deleteGhostCoxs();
			tree.getSelecao().setEmpty();
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
			if(tree.getTitulo().isVisible())return;
			final List<Objeto>newObjs=new ArrayList<>();
			for(Conexao cox:tree.getSelectedObjetos().getConexoes()){
				if(cox.getPai()==Tree.getMestre())continue;
				final Conexao newCox=new Conexao(cox.getSon(),cox.getPai());
				newCox.setBorda(cox.getBorda());
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
		tree.getSelecao().setEmpty();
		if(stateContains(TO_CREATE)){
			final State state=getStates().get(getState());
			if(state.getModulo()!=null){
				addUndoable(state.getModulo(),false);
			}else if(state.getNodulo()!=null){
				addUndoable(state.getNodulo(),false);
			}
		}
		tree.draw();
		setState(NORMAL);
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
		private void setHover(Selecao.State state){
			final Point mouse=getNonGridPosition(mouseMoved);
			tree.unSelectAll();
			tree.getPopup().close();
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
		private void setArea(Selecao.State state,boolean addTo,int...locations){
			Rectangle allChunksIndexes=null;
			Rectangle newChunksIndexes=null;
			switch(locations.length){
				default:	//MANTÉM UMA ÁREA
					if(!addTo)switch(state){
						case TO_SELECT:
						case TO_DELETE:	tree.unSelectAll();			break;
						default:		tree.unSelectAllMods();		break;
					}
					tree.getSelecao().setState(state);
					allChunksIndexes=tree.getChunksIndexes(tree.getSelecao().getFormIndex());
					newChunksIndexes=allChunksIndexes;
				break;
				case 4:		//CRIA UMA ÁREA
					if(!addTo)switch(state){
						case TO_SELECT:
						case TO_DELETE:	tree.unSelectAll();			break;
						default:		tree.unSelectAllMods();		break;
					}
					tree.getSelecao().setState(state);
					tree.getSelecao().setAncoraIndex(locations[0],locations[1]);
					tree.getSelecao().setAreaIndex(locations[2],locations[3]);
					allChunksIndexes=tree.getChunksIndexes(tree.getSelecao().getFormIndex());
					newChunksIndexes=allChunksIndexes;
				break;
				case 2:		//EXPANDE UMA ÁREA
					final Rectangle areaOld=tree.getSelecao().getFormIndex();
					tree.getSelecao().setAreaIndex(locations[0],locations[1]);
					final Rectangle areaNew=tree.getSelecao().getFormIndex();
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
								if(tree.getSelecao().intersects(mod.getForm())){
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
								if(tree.getSelecao().intersects(cox.getForm())){
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
								if(tree.getSelecao().intersects(nod.getForm())){
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
		private void addAndSelect(Objeto obj){
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
		private void createNodOnGhostCoxs(int x,int y){
			for(Conexao cox:Tree.getGhost().getConexoes()){
				final Nodulo newNod=new Nodulo(cox,x,y);
				tree.add(newNod);
			}
			tree.draw();
		}
	//RELATE
		private void relateMods(List<Modulo>modsPais,Modulo modSon){
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
		private void relateToMods(List<Modulo>mods){
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
		private void relateToMods(Modulo mod){relateToMods(Arrays.asList(mod));}
		public void relateToGhost(List<Modulo>mods){
			for(Modulo mod:mods){
				final Conexao newCox=new Conexao(Tree.getGhost(),mod);
				newCox.setBorda(newCox.getSon().getBorda());
				tree.add(newCox);
			}
			tree.unSelectAll();
			tree.draw();
		}
		private void relateToGhost(Modulo mod){relateToGhost(Arrays.asList(mod));}
	//DELETE
		private void deleteGhostCoxs(){
			while(!Tree.getGhost().getConexoes().isEmpty()){
				tree.del(Tree.getGhost().getConexoes().get(0));
			}
			tree.draw();
		}
}