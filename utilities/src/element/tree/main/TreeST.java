package element.tree.main;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import element.tree.Cursor;
import element.tree.CursorST;
import element.tree.SelecaoST;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.ConexaoST;
import element.tree.objeto.conexao.segmento.Segmento;
import element.tree.objeto.modulo.Modulo;
import element.tree.objeto.modulo.ModuloST;
import element.tree.objeto.nodulo.Nodulo;
import element.tree.objeto.nodulo.NoduloST;
public class TreeST{
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
//STATE
	private int state=NORMAL;
		public int getState(){return state;}
//STATE
	public class State{
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
		public abstract class Action{
			public void run(){}
			public void run(MouseEvent m){}
			public void run(KeyEvent k){}
		}
//STATES
	private HashMap<Integer,State>states=new HashMap<>();
		public State getStateContent(int state){return states.get(state);}
		public State setState(int newStateIndex,Objeto...objs){
			final State oldState=getStateContent(state);
			oldState.setModulo(null);		//ZERA MOD
			oldState.setConexao(null);		//ZERA COX
			oldState.setNodulo(null);		//ZERA NOD
			oldState.setSegmento(null);		//ZERA SEG
			state=newStateIndex;
			final State newState=getStateContent(newStateIndex);
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
//TREE
	private Tree tree;
//MAIN
	public TreeST(Tree tree){
		this.tree=tree;
	}
//FLUXO
	public void loadStates(){
//NORMAL
		states.put(NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.NORMAL);
				};
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){										//HÁ MOD SELEC
							if(modSelec.getState().is(ModuloST.State.SELECTED)){
								setState(LEFT+WAITING2+NORMAL,modSelec);
							}else{
								setState(LEFT+WAITING1+NORMAL,modSelec);
							}
						}else{													//NENHUM MOD FOI SELEC, VERIFICA POR NODS
							final Nodulo nodSelec=tree.getActions().getNoduloHover(mouse);
							if(nodSelec!=null){									//HÁ NOD SELEC
								if(nodSelec.getState().is(NoduloST.State.SELECTED)){
									setState(LEFT+WAITING2+NORMAL,nodSelec);
								}else{
									setState(LEFT+WAITING1+NORMAL,nodSelec);
								}
							}else{												//NENHUM NOD FOI SELEC, VERIFICA POR COXS
								final Conexao coxSelec=tree.getActions().getConexaoHover(mouse);
								if(coxSelec!=null){								//HÁ COX SELEC
									if(coxSelec.getState().is(ConexaoST.State.SELECTED)){
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){
							tree.unSelectAll();
							tree.select(modSelec);
							tree.draw();
							tree.getUI().setTitulo(modSelec);	//TITULO ACIMA DO DRAW
							setState(EDIT_TITLE);
						}
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//MIDDLE+WAITING1+NORMAL
		states.put(MIDDLE+WAITING1+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.SELECT);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.SELECT);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE);
					setState(DRAG_ALL+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE_SON);
					setState(DRAG_ALL+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_DELETE);
					setState(DRAG_ALL+TO_DELETE);
				}
			});
		}});
//AUTO_DRAG_ALL+NORMAL
		states.put(AUTO_DRAG_ALL+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.AUTODRAG);
					tree.getActions().startAutoDrag();
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
					tree.getActions().setHover(SelecaoST.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//LEFT+WAITING1+NORMAL
		states.put(LEFT+WAITING1+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.SELECT);
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.ALT,Cursor.LEFT)){
						if(getConexao()!=null){
							for(Conexao cox:tree.getSelectedObjetos().getConexoes()){
								for(Nodulo nod:cox.getNodulos()){
									tree.select(nod);
								}
							}
						}
						tree.getActions().oldMouse=new Point(tree.getActions().mouseDragged);
						for(Modulo mod:tree.getSelectedObjetos().getModulos()){
							mod.setLocationIndex(mod.getXIndex()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),mod.getYIndex()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						}
						for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
							nod.setLocationIndex(nod.getXIndex()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),nod.getYIndex()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						}
						tree.draw();
						setState(DRAG+NORMAL);
					
					}else if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_SELECT,true,
								tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_SELECT,false,
								tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.getActions().updateTituloBounds();
						tree.draw();
					}
				}
			});
		}});
//SELECT_AREA+NORMAL
		states.put(SELECT_AREA+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.AREA_SELECT+CursorST.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getUI().getSelecao().setEmpty();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_SELECT,true,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_SELECT,false,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
					setState(SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
					setState(SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
					setState(SELECT_AREA+TO_DELETE);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+NORMAL
		states.put(DRAG_ALL+SELECT_AREA+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.AREA_SELECT+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(SELECT_AREA+NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
				}
			});
		}});
//LEFT+WAITING2+NORMAL
		states.put(LEFT+WAITING2+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.SELECT);
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_SELECT,true,
								tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
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
						tree.getActions().oldMouse=new Point(tree.getActions().mouseDragged);
						for(Modulo mod:tree.getSelectedObjetos().getModulos()){
							mod.setLocationIndex(mod.getXIndex()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),mod.getYIndex()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						}
						for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
							nod.setLocationIndex(nod.getXIndex()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),nod.getYIndex()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.getActions().updateTituloBounds();
						tree.draw();
					}
				}
			});
		}});
//DRAG+NORMAL
		states.put(DRAG+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.MOVE+CursorST.SELECT);
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
						tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(-(tree.getActions().mouseMoved.x-tree.getActions().oldMouse.x),-(tree.getActions().mouseMoved.y-tree.getActions().oldMouse.y)));
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
						tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(-(tree.getActions().mouseMoved.x-tree.getActions().oldMouse.x),-(tree.getActions().mouseMoved.y-tree.getActions().oldMouse.y)));
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						for(Modulo mod:tree.getSelectedObjetos().getModulos()){
							mod.setLocationIndex(mod.getXIndex()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),mod.getYIndex()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						}
						for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
							nod.setLocationIndex(nod.getXIndex()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),nod.getYIndex()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final List<Objeto>savedObjs=new ArrayList<>();
						savedObjs.addAll(tree.getSelectedObjetos().getModulos());
						savedObjs.addAll(tree.getSelectedObjetos().getNodulos());
						tree.getUndoRedoManager().addUndoLocal(savedObjs.toArray(new Objeto[0]),new Point(-(tree.getActions().mouseMoved.x-tree.getActions().oldMouse.x),-(tree.getActions().mouseMoved.y-tree.getActions().oldMouse.y)));
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG+NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
						if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//MOVE+NORMAL
		states.put(MOVE+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.MOVE+CursorST.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						tree.getActions().addUndoable(savedObjs,false);
						setState(NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+MOVE+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						tree.getActions().addUndoable(savedObjs,false);
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseMovedAtual=tree.getActions().getGridPosition(m.getPoint());
					for(Modulo mod:tree.getSelectedObjetos().getModulos()){
						mod.setLocationIndex(mod.getXIndex()-(tree.getActions().mouseMoved.x-mouseMovedAtual.x),mod.getYIndex()-(tree.getActions().mouseMoved.y-mouseMovedAtual.y));
					}
					for(Nodulo nod:tree.getSelectedObjetos().getNodulos()){
						nod.setLocationIndex(nod.getXIndex()-(tree.getActions().mouseMoved.x-mouseMovedAtual.x),nod.getYIndex()-(tree.getActions().mouseMoved.y-mouseMovedAtual.y));
					}
					tree.draw();
				}
			});
		}});
//DRAG_ALL+MOVE+NORMAL
		states.put(DRAG_ALL+MOVE+NORMAL,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						tree.getActions().addUndoable(savedObjs,false);
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						final List<Objeto>savedObjs=new ArrayList<>(tree.getSelectedObjetos().getAll().values());
						tree.getActions().addUndoable(savedObjs,false);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
						if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//EDIT_TITLE
	states.put(EDIT_TITLE,new State(){{
		setAction(new Action(){
			public void run(){
				tree.getUI().getCursor().set(CursorST.EDIT_TITLE+CursorST.NORMAL);
			}
		});
		setMousePressedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.LEFT)){
					tree.getUI().getCursor().set(CursorST.EDIT_TITLE+CursorST.SELECT);
				}else if(Cursor.match(m,Cursor.MIDDLE)){
					setState(DRAG_ALL+EDIT_TITLE);
				}else if(Cursor.match(m,Cursor.RIGHT)){
					tree.getUI().setTitulo(null);
					tree.draw();
					setState(RIGHT+WAITING1+NORMAL);
				}
			}
		});
		setMouseReleasedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.LEFT)){
					tree.getUI().setTitulo(null);
					tree.draw();
					setState(NORMAL);
				}
			}
		});
		setMouseDraggedAction(new Action(){
			public void run(MouseEvent m){
				final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
				if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
					tree.getUI().setTitulo(null);
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,true,
							tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
							mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					setState(SELECT_AREA+NORMAL);
				}else if(Cursor.match(m,Cursor.LEFT)){
					tree.getUI().setTitulo(null);
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,false,
							tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
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
				tree.getUI().getCursor().set(CursorST.DRAG+CursorST.EDIT_TITLE+CursorST.SELECT);
			}
		});
		setMousePressedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.MIDDLE)){
					setState(DRAG_ALL+EDIT_TITLE);
				}else if(Cursor.match(m,Cursor.RIGHT)){
					tree.getUI().setTitulo(null);
					tree.draw();
					setState(RIGHT+WAITING1+NORMAL);
				}
			}
		});
		setMouseReleasedAction(new Action(){
			public void run(MouseEvent m){
				if(Cursor.match(m,Cursor.LEFT)){
					tree.getUI().setTitulo(null);
					tree.draw();
					setState(DRAG_ALL+NORMAL);
				}else if(Cursor.match(m,Cursor.MIDDLE)){
					setState(EDIT_TITLE);
				}
			}
		});
		setMouseDraggedAction(new Action(){
			public void run(MouseEvent m){
				final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
				if(Cursor.match(m,Cursor.MIDDLE)){
					Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
					tree.getActions().updateTituloBounds();
					tree.draw();
				}
			}
		});
		setKeyReleasedAction(new Action(){
			public void run(KeyEvent k){
				switch(k.getKeyCode()){
					case KeyEvent.VK_ESCAPE:
						tree.getUI().setTitulo(null);
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
					tree.getUI().getCursor().set(CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.RIGHT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){
							if(!modSelec.getState().is(ModuloST.State.SELECTED))tree.unSelectAll();
							tree.unSelect(modSelec);
							tree.select(modSelec);
							tree.getUI().getPopup().show(tree.getActions().mousePressed,modSelec);
						}else{
							final Nodulo nodSelec=tree.getActions().getNoduloHover(mouse);
							if(nodSelec!=null){
								if(!nodSelec.getState().is(NoduloST.State.SELECTED))tree.unSelectAll();
								tree.unSelect(nodSelec);
								tree.select(nodSelec);
								tree.getUI().getPopup().show(tree.getActions().mousePressed,nodSelec);
							}else{
								final Conexao coxSelec=tree.getActions().getConexaoHover(mouse);
								if(coxSelec!=null){
									if(!coxSelec.getState().is(ConexaoST.State.SELECTED))tree.unSelectAll();
									tree.unSelect(coxSelec);
									tree.select(coxSelec);
									final Segmento segSelec=coxSelec.segmentContains(mouse);
									tree.getUI().getPopup().show(tree.getActions().mousePressed,segSelec);
								}else{
									tree.unSelectAll();
									tree.getUI().getPopup().show(tree.getActions().mousePressed,null);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.RIGHT)){
						tree.unSelectAll();
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.SELECT);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.RIGHT)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//TO_CREATE
		states.put(TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.CREATE+CursorST.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modHover=tree.getActions().getModuloHover(mouse);
						if(modHover!=null){
							tree.select(modHover);
							setState(WAITING2+TO_CREATE);
						}else{
							final Conexao coxHover=tree.getActions().getConexaoHover(mouse);
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
					final Point mouse=tree.getActions().getPosition(m.getPoint());
					final Modulo modHover=tree.getActions().getModuloHover(mouse);
					if(modHover!=null){
						tree.selectToBeCreator(modHover);
					}else{
						final Conexao coxHover=tree.getActions().getConexaoHover(mouse);
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
					tree.getActions().setHover(SelecaoST.State.TO_UNSELECT);
					setState(NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//DRAG_ALL+TO_CREATE
		states.put(DRAG_ALL+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.CREATE+CursorST.SELECT);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_UNSELECT);
					setState(DRAG_ALL+NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE_SON);
					setState(DRAG_ALL+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_DELETE);
					setState(DRAG_ALL+TO_DELETE);
				}
			});
		}});
//WAITING1+TO_CREATE
		states.put(WAITING1+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getUI().getCursor().set(CursorST.MOVE+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.CREATE+CursorST.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING1+TO_CREATE,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().addAndSelect(getModulo());
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
							tree.getActions().createMod(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							setState(TO_CREATE);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.unSelectAll();
							tree.draw();
						}else{
							tree.getActions().createMod(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
						}
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							getModulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,true,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+TO_CREATE,getModulo());
						}else{
							tree.getActions().setArea(SelecaoST.State.TO_CREATE,true,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+TO_CREATE);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
							tree.getActions().setArea(SelecaoST.State.TO_SELECT,false,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+NORMAL);
						}else{
							tree.getActions().setArea(SelecaoST.State.TO_CREATE,false,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
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
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.CREATE+CursorST.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							tree.getActions().createMod(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							setState(DRAG_ALL+TO_CREATE);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.unSelectAll();
							tree.draw();
						}else{
							tree.getActions().createMod(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
						}
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(WAITING1+TO_CREATE,getModulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
						tree.getUI().getCursor().set(CursorST.AREA_PAI+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.AREA_CREATE+CursorST.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+TO_CREATE,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getUI().getSelecao().setEmpty();
						tree.getActions().addAndSelect(getModulo());
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(getModulo()!=null){
							tree.getActions().relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.unSelectAll();
							tree.draw();
							setState(MOVE+TO_CREATE,getModulo());
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(TO_CREATE);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
							tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
							tree.getUI().getSelecao().setEmpty();
							tree.draw();
							setState(NORMAL);
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(NORMAL);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							getModulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							tree.getActions().setArea(SelecaoST.State.TO_CREATE,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
							tree.getActions().setArea(SelecaoST.State.TO_SELECT,false,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							setState(SELECT_AREA+NORMAL);
						}else{
							tree.getActions().setArea(SelecaoST.State.TO_CREATE,false,
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
								tree.getActions().addAndSelect(getModulo());
								tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
								setState(SELECT_AREA+NORMAL);
							}else{
								tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
							}
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().addAndSelect(getModulo());
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
					setState(SELECT_AREA+NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().addAndSelect(getModulo());
					tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
					setState(SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().addAndSelect(getModulo());
					tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
					setState(SELECT_AREA+TO_DELETE);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+TO_CREATE
		states.put(DRAG_ALL+SELECT_AREA+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.AREA_PAI+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.AREA_CREATE+CursorST.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(getModulo()!=null){
							tree.getActions().relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.unSelectAll();
							tree.draw();
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(DRAG_ALL+TO_CREATE);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
							tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
							tree.getUI().getSelecao().setEmpty();
							tree.draw();
							setState(DRAG_ALL+NORMAL);
						}else{
							if(tree.getSelectedObjetos().getModulos().isEmpty()){
								tree.draw();
								setState(DRAG_ALL+NORMAL);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(getModulo()!=null){
								tree.getActions().addAndSelect(getModulo());
								tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
								setState(DRAG_ALL+SELECT_AREA+NORMAL);
							}else{
								tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
							}
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().addAndSelect(getModulo());
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
					setState(DRAG_ALL+SELECT_AREA+NORMAL);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().addAndSelect(getModulo());
					tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().addAndSelect(getModulo());
					tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
				}
			});
		}});
//WAITING2+TO_CREATE
		states.put(WAITING2+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					if(getModulo()!=null){
						tree.getUI().getCursor().set(CursorST.MOVE+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.CREATE+CursorST.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING2+TO_CREATE,getModulo(),getSegmento());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().addAndSelect(getModulo());
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.draw();
							setState(MOVE+TO_CREATE,getModulo());
						}else{
							if(getSegmento()!=null){
								tree.getActions().createNod(getSegmento(),tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),true);
								setState(TO_CREATE);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
							setState(NORMAL);
						}else{
							if(getSegmento()!=null){
								tree.getActions().createNod(getSegmento(),tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),true);
								setState(NORMAL);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CREATE,newMod);
							}
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							getModulo().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							tree.getActions().relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							setState(DRAG+TO_CREATE,getModulo());
						}else{
							if(getSegmento()!=null){
								final Nodulo newNod=tree.getActions().createNod(getSegmento(),mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY(),false);
								tree.unSelect(newNod);
								setState(DRAG+TO_CREATE,newNod);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
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
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.CREATE+CursorST.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().relateMods(tree.getSelectedObjetos().getModulos(),getModulo());
							tree.draw();
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							if(getSegmento()!=null){
								tree.getActions().createNod(getSegmento(),tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),true);
								setState(DRAG_ALL+TO_CREATE);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
										tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CREATE,newMod);
							}
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
							setState(DRAG_ALL+NORMAL);
						}else{
							if(getSegmento()!=null){
								tree.getActions().createNod(getSegmento(),tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),true);
								setState(DRAG_ALL+NORMAL);
							}else{
								final Modulo newMod=tree.getActions().createModRelacionado(
										tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY(),
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//DRAG+TO_CREATE
		states.put(DRAG+TO_CREATE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.MOVE+CursorST.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+DRAG+TO_CREATE,getModulo(),getNodulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
						}else if(getNodulo()!=null){
							tree.getActions().addAndSelect(getNodulo());
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
							tree.getActions().addAndSelect(getNodulo());
							setState(NORMAL);
						}
					
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
						}else if(getNodulo()!=null){
							tree.getActions().addAndSelect(getNodulo());
						}
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(getModulo()!=null){
							setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
						}else{
							tree.getActions().addAndSelect(getNodulo());
							setState(DRAG_ALL+NORMAL);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(getModulo()!=null){
							tree.getActions().addAndSelect(getModulo());
						}else if(getNodulo()!=null){
							tree.getActions().addAndSelect(getNodulo());
						}
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG+TO_CREATE,getModulo(),getNodulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
					tree.getUI().getCursor().set(CursorST.MOVE+CursorST.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modHover=tree.getActions().getModuloHover(mouse,getModulo());
						if(modHover!=null){
							tree.select(modHover);
							setState(WAITING2+TO_CREATE,getModulo());
						}else{
							setState(WAITING1+TO_CREATE,getModulo());
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+MOVE+TO_CREATE,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().addAndSelect(getModulo());
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						//NADA
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().addAndSelect(getModulo());
						setState(NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseMovedAtual=tree.getActions().getGridPosition(m.getPoint());
					final Point mouse=tree.getActions().getPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL)){
						getModulo().setLocationIndex(mouseMovedAtual.x-Tree.getLocalX(),mouseMovedAtual.y-Tree.getLocalY());
						tree.unSelectAll();
						final Modulo modHover=tree.getActions().getModuloHover(mouse,getModulo());
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().addAndSelect(getModulo());
						tree.getActions().setArea(SelecaoST.State.TO_SELECT,false,
								tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
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
							final Modulo modHover=tree.getActions().getModuloHover(tree.getActions().getNonGridPosition(tree.getActions().mouseMoved),getModulo());
							if(modHover!=null){
								tree.selectToBePai(modHover);
								tree.draw();
							}
							tree.getUI().getCursor().set(CursorST.PAI+CursorST.NORMAL);
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
							tree.getUI().getCursor().set(CursorST.MOVE+CursorST.NORMAL);
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						//NADA
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().addAndSelect(getModulo());
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(MOVE+TO_CREATE,getModulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
							final Modulo modHover=tree.getActions().getModuloHover(tree.getActions().getNonGridPosition(tree.getActions().mouseMoved),getModulo());
							if(modHover!=null){
								tree.selectToBePai(modHover);
								tree.draw();
							}
							tree.getUI().getCursor().set(CursorST.DRAG+CursorST.PAI+CursorST.NORMAL);
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
							tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.NORMAL);
						break;
					}
				}
			});
		}});
//TO_CONNECT
		states.put(TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.SON+CursorST.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						Modulo modSelec=tree.getActions().getModuloHover(mouse);
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
					final Point mouse=tree.getActions().getPosition(m.getPoint());
					final Modulo modHover=tree.getActions().getModuloHover(mouse);
					tree.selectToBeSon(modHover);
					tree.draw();
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_UNSELECT);
					setState(NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_DELETE);
					setState(TO_DELETE);
				}
			});
		}});
//DRAG_ALL+TO_CONNECT
		states.put(DRAG_ALL+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.SON+CursorST.SELECT);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE);
					setState(DRAG_ALL+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_UNSELECT);
					setState(DRAG_ALL+NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_DELETE);
					setState(DRAG_ALL+TO_DELETE);
				}
			});
		}});
//WAITING1+TO_CONNECT
		states.put(WAITING1+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getUI().getCursor().set(CursorST.SON+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.PAI+CursorST.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING1+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().deleteGhostCoxs();
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
							tree.getActions().createNodOnGhostCoxs(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							setState(MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().deleteGhostCoxs();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						if(Tree.getGhost().getConexoes().isEmpty()){
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,true,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,true,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
						tree.draw();
						setState(SELECT_AREA+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						if(Tree.getGhost().getConexoes().isEmpty()){
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,false,
									tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
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
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.SON+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.PAI+CursorST.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							setState(DRAG_ALL+TO_CONNECT);
						}else{
							tree.getActions().createNodOnGhostCoxs(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							setState(DRAG_ALL+MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().deleteGhostCoxs();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						setState(WAITING1+TO_CONNECT);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
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
						tree.getUI().getCursor().set(CursorST.AREA_SON+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.AREA_PAI+CursorST.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().deleteGhostCoxs();
						tree.getUI().getSelecao().setEmpty();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							tree.draw();
							if(Tree.getGhost().getConexoes().isEmpty()){
								setState(TO_CONNECT);
							}else{
								setState(MOVE+TO_CONNECT);
							}
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
								tree.getActions().relateToGhost(tree.getSelectedObjetos().getModulos());
							}else{
								tree.getActions().relateToMods(tree.getSelectedObjetos().getModulos());
							}
							setState(MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							tree.getActions().deleteGhostCoxs();
							setState(NORMAL);
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
								tree.getActions().relateToGhost(tree.getSelectedObjetos().getModulos());
								setState(MOVE+TO_CONNECT);
							}else{
								tree.getActions().relateToMods(tree.getSelectedObjetos().getModulos());
								tree.getActions().deleteGhostCoxs();
								setState(NORMAL);
							}
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,true,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false,
									mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						}else{
							Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
							tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,false,
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
								tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
							}else tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().deleteGhostCoxs();
					tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
					setState(SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().deleteGhostCoxs();
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
					setState(SELECT_AREA+NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().deleteGhostCoxs();
					tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
					setState(SELECT_AREA+TO_DELETE);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+TO_CONNECT
		states.put(DRAG_ALL+SELECT_AREA+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.AREA_SON+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.AREA_PAI+CursorST.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							tree.draw();
							if(Tree.getGhost().getConexoes().isEmpty()){
								setState(DRAG_ALL+TO_CONNECT);
							}else{
								setState(DRAG_ALL+MOVE+TO_CONNECT);
							}
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
								tree.getActions().relateToGhost(tree.getSelectedObjetos().getModulos());
							}else{
								tree.getActions().relateToMods(tree.getSelectedObjetos().getModulos());
							}
							setState(DRAG_ALL+MOVE+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getUI().getSelecao().setEmpty();
						if(tree.getSelectedObjetos().getModulos().isEmpty()){
							tree.getActions().deleteGhostCoxs();
							setState(DRAG_ALL+NORMAL);
						}else{
							if(Tree.getGhost().getConexoes().isEmpty()){
								Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
								tree.getActions().relateToGhost(tree.getSelectedObjetos().getModulos());
								setState(DRAG_ALL+MOVE+TO_CONNECT);
							}else{
								tree.getActions().relateToMods(tree.getSelectedObjetos().getModulos());
								tree.getActions().deleteGhostCoxs();
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							if(Tree.getGhost().getConexoes().isEmpty()){
								tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
							}else tree.getActions().setArea(SelecaoST.State.TO_CREATE_PAI,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().deleteGhostCoxs();
					tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().deleteGhostCoxs();
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
					setState(DRAG_ALL+SELECT_AREA+NORMAL);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().deleteGhostCoxs();
					tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
				}
			});
		}});
//WAITING2+TO_CONNECT
		states.put(WAITING2+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					if(Tree.getGhost().getConexoes().isEmpty()){
						tree.getUI().getCursor().set(CursorST.SON+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.PAI+CursorST.SELECT);
					}
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+WAITING2+TO_CONNECT,getModulo());
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().deleteGhostCoxs();
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
							Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							tree.getActions().relateToGhost(getModulo());
						}else{
							tree.getActions().relateToMods(getModulo());
						}
						setState(MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							tree.getActions().relateToGhost(getModulo());
							setState(MOVE+TO_CONNECT);
						}else{
							tree.getActions().relateToMods(getModulo());
							tree.getActions().deleteGhostCoxs();
							setState(NORMAL);
						}
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						tree.getActions().relateToGhost(getModulo());
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
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.SON+CursorST.SELECT);
					}else{
						tree.getUI().getCursor().set(CursorST.DRAG+CursorST.PAI+CursorST.SELECT);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							tree.getActions().relateToGhost(getModulo());
						}else{
							tree.getActions().relateToMods(getModulo());
						}
						setState(DRAG_ALL+MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						if(Tree.getGhost().getConexoes().isEmpty()){
							Tree.getGhost().setLocationIndex(tree.getActions().mouseReleased.x-Tree.getLocalX(),tree.getActions().mouseReleased.y-Tree.getLocalY());
							tree.getActions().relateToGhost(getModulo());
							setState(DRAG_ALL+MOVE+TO_CONNECT);
						}else{
							tree.getActions().relateToMods(getModulo());
							tree.getActions().deleteGhostCoxs();
							setState(DRAG_ALL+NORMAL);
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(WAITING2+TO_CONNECT,getModulo());
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//DRAG+TO_CONNECT
		states.put(DRAG+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.MOVE+CursorST.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+DRAG+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().deleteGhostCoxs();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){
							tree.getActions().relateToMods(modSelec);
						}
						setState(MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){
							tree.getActions().relateToMods(modSelec);
						}
						tree.getActions().deleteGhostCoxs();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT)){
						Tree.getGhost().setLocationIndex(mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						tree.unSelectAll();
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modHover=tree.getActions().getModuloHover(mouse);
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.MOVE+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){
							tree.getActions().relateToMods(modSelec);
						}
						setState(DRAG_ALL+MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){
							tree.getActions().relateToMods(modSelec);
						}
						tree.getActions().deleteGhostCoxs();
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG+TO_CONNECT);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//MOVE+TO_CONNECT
		states.put(MOVE+TO_CONNECT,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.PAI+CursorST.NORMAL);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Modulo modSelec=tree.getActions().getModuloHover(mouse);
						if(modSelec!=null){
							setState(WAITING2+TO_CONNECT,modSelec);
						}else{
							setState(WAITING1+TO_CONNECT);
						}
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+MOVE+TO_CONNECT);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getActions().deleteGhostCoxs();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseMovedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseMovedAtual=tree.getActions().getGridPosition(m.getPoint());
					Tree.getGhost().setLocationIndex(mouseMovedAtual.x-Tree.getLocalX(),mouseMovedAtual.y-Tree.getLocalY());
					tree.unSelectAllMods();
					final Point mouse=tree.getActions().getPosition(m.getPoint());
					final Modulo modHover=tree.getActions().getModuloHover(mouse);
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.PAI+CursorST.SELECT);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//TO_DELETE
		states.put(TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DELETE+CursorST.NORMAL);
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
					final Point mouse=tree.getActions().getPosition(m.getPoint());
					tree.unSelectAll();
					final Modulo modDel=tree.getActions().getModuloHover(mouse);
					if(modDel!=null){
						tree.selectToBeDeleted(modDel);
					}else{
						final Nodulo nodDel=tree.getActions().getNoduloHover(mouse);
						if(nodDel!=null){
							tree.selectToBeDeleted(nodDel);
						}else{
							final Conexao coxDel=tree.getActions().getConexaoHover(mouse);
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
					tree.getActions().setHover(SelecaoST.State.TO_CREATE);
					setState(TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE_SON);
					setState(TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_UNSELECT);
					setState(NORMAL);
				}
			});
		}});
//DRAG_ALL+TO_DELETE
		states.put(DRAG_ALL+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.DELETE+CursorST.SELECT);
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE);
					setState(DRAG_ALL+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_CREATE_SON);
					setState(DRAG_ALL+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setHover(SelecaoST.State.TO_UNSELECT);
					setState(DRAG_ALL+NORMAL);
				}
			});
		}});
//WAITING1+TO_DELETE
		states.put(WAITING1+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DELETE+CursorST.SELECT);
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
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Objeto objSelec=tree.getActions().getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
							}
						}
						tree.draw();
						setState(TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Objeto objSelec=tree.getActions().getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
							}
						}
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_DELETE,true,
								tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						setState(SELECT_AREA+TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_DELETE,false,
								tree.getActions().mouseDragged.x-Tree.getLocalX(),tree.getActions().mouseDragged.y-Tree.getLocalY(),
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
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.DELETE+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Objeto objSelec=tree.getActions().getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
							}
						}
						tree.draw();
						setState(DRAG_ALL+TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						final Point mouse=tree.getActions().getPosition(m.getPoint());
						final Objeto objSelec=tree.getActions().getObjetoHover(mouse);
						if(objSelec!=null){
							if(tree.getSelectedObjetos().getAll().containsValue(objSelec)){
								tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
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
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
		}});
//SELECT_AREA+TO_DELETE
		states.put(SELECT_AREA+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.AREA_DELETE+CursorST.SELECT);
				}
			});
			setMousePressedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.MIDDLE)){
						setState(DRAG_ALL+SELECT_AREA+TO_DELETE);
					}else if(Cursor.match(m,Cursor.RIGHT)){
						tree.getUI().getSelecao().setEmpty();
						tree.unSelectAll();
						tree.draw();
						setState(RIGHT+WAITING1+NORMAL);
					}
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getUI().getSelecao().setEmpty();
						tree.draw();
						setState(TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getUI().getSelecao().setEmpty();
						tree.draw();
						setState(NORMAL);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_DELETE,true,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
						tree.draw();
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().setArea(SelecaoST.State.TO_DELETE,false,
								mouseDraggedAtual.x-Tree.getLocalX(),mouseDraggedAtual.y-Tree.getLocalY());
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
					setState(SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
					setState(SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
					setState(SELECT_AREA+NORMAL);
				}
			});
		}});
//DRAG_ALL+SELECT_AREA+TO_DELETE
		states.put(DRAG_ALL+SELECT_AREA+TO_DELETE,new State(){{
			setAction(new Action(){
				public void run(){
					tree.getUI().getCursor().set(CursorST.DRAG+CursorST.AREA_DELETE+CursorST.SELECT);
				}
			});
			setMouseReleasedAction(new Action(){
				public void run(MouseEvent m){
					if(Cursor.match(m,Cursor.CTRL,Cursor.LEFT)){
						tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getUI().getSelecao().setEmpty();
						tree.draw();
						setState(DRAG_ALL+TO_DELETE);
					}else if(Cursor.match(m,Cursor.LEFT)){
						tree.getActions().delUndoable(new ArrayList<>(tree.getSelectedObjetos().getAll().values()));
						tree.getUI().getSelecao().setEmpty();
						tree.draw();
						setState(DRAG_ALL+NORMAL);
					}else if(Cursor.match(m,Cursor.MIDDLE)){
						setState(SELECT_AREA+TO_DELETE);
					}
				}
			});
			setMouseDraggedAction(new Action(){
				public void run(MouseEvent m){
					final Point mouseDraggedAtual=tree.getActions().getGridPosition(m.getPoint());
					if(Cursor.match(m,Cursor.LEFT,Cursor.MIDDLE)){
						Tree.setLocal(Tree.getLocalX()-(tree.getActions().mouseDragged.x-mouseDraggedAtual.x),Tree.getLocalY()-(tree.getActions().mouseDragged.y-mouseDraggedAtual.y));
						tree.draw();
					}
				}
			});
			setKeyReleasedAction(new Action(){
				public void run(KeyEvent k){
					switch(k.getKeyCode()){
						case KeyEvent.VK_CONTROL:
							tree.getActions().setArea(SelecaoST.State.TO_DELETE,false);
						break;
					}
				}
			});
			setCreateAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CREATE);
				}
			});
			setConnectAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_CREATE_SON,false);
					setState(DRAG_ALL+SELECT_AREA+TO_CONNECT);
				}
			});
			setDeleteAction(new Action(){
				public void run(){
					tree.getActions().setArea(SelecaoST.State.TO_SELECT,false);
					setState(DRAG_ALL+SELECT_AREA+NORMAL);
				}
			});
		}});
		setState(NORMAL);
	}
}