package element.tree.popup;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import element.tree.Actions;
import element.tree.propriedades.Borda;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Grossura;
import element.tree.propriedades.Icone;
import element.tree.Tree;
import element.tree.undoRedo.UndoRedoIcone;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.Segmento;
import element.tree.objeto.modulo.Modulo;
import element.tree.popup.borda.BordaPick;
import element.tree.popup.borda.BordaPickListener;
import element.tree.popup.color.CorPick;
import element.tree.popup.color.CorPickListener;
import element.tree.popup.grossura.GrossuraPick;
import element.tree.popup.grossura.GrossuraPickListener;
import element.tree.popup.icon.IconePick;
import element.tree.popup.icon.IconePickListener;
@SuppressWarnings("serial")
public class Popup{
//TREE(PAI)
	private Tree tree;
		public Tree getTree(){return tree;}
//LOCAL
	private Point local;
//OBJETO
	private Objeto obj;
//PASTA DE ICONES
	private File iconesPasta=new File(System.getProperty("user.dir")+"/Icons");
		public File getIconePasta(){return iconesPasta;}
		public void setIconePasta(File link){
			iconesPasta=link;
			update();
		}
//VAR GLOBAIS
	public static final int BLOCO_SIZE=16;
	public static final int GRID_BORDA=4;
	public static final int BLOCO_SPACE=4;
//ITENS
	private Menu mod_menuBordas;
	private Item mod_editTitle;
	private Item mod_newCox;
	private Item cox_newNod;
	private Item del;
//PICKS COM POPUP
	private List<IconePick>mod_iconPicks=new ArrayList<>();
	private CorPick mod_corPick;
	private BordaPick mod_bordaPick;
	private BordaPick cox_bordaPick;
	private GrossuraPick cox_grossuraPick;
//POPUPS
	private JPopupMenu treePopup;
	private JPopupMenu modPopup;
	private JPopupMenu coxPopup;
	private JPopupMenu nodPopup;
//FOCUS
	public static final int DELAY=300;
	private static Runnable focusRun;			//ITEM/MENU A FOCAR
		public static synchronized Runnable getFocus(){return focusRun;}
		public static synchronized void setFocus(Runnable focus){focusRun=focus;}
	private static boolean goingToFocus=false;	//SE IRÁ FOCAR EM UM MENU/ITEM
		public static synchronized boolean getGoingToFocus(){return goingToFocus;}
		public static synchronized void setGoingToFocus(boolean focus){goingToFocus=focus;}
//MAIN
	public Popup(Tree tree){
		super();
		this.tree=tree;
		UIManager.put("PopupMenu.consumeEventOnClose",Boolean.FALSE);		//IMPEDE QUE POPUP CONSUMA CLICK AO SUMIR
		ToolTipManager.sharedInstance().setInitialDelay(0);					//DELAY ANTES DE APARECER OS NOMES DOS ITENS
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);	//TEMPO EM QUE MOSTRA OS NOMES DOS ITENS
		UIManager.put("ToolTip.background",Cor.getChanged(Modulo.Cores.FUNDO,1.1f));
		UIManager.put("ToolTip.foreground",Tree.Fonte.DARK);
		UIManager.put("ToolTip.border",BorderFactory.createLineBorder(Cor.getChanged(Modulo.Cores.FUNDO,0.9f)));
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException erro){
			Tree.mensagem(
					Tree.getLang().get("T_Err3","Error: Couldn't configure toolbox!")+"\n"+erro,
					Tree.Options.ERRO);
		}
		update();
	}
//SHOW
	public void show(Point local,Objeto obj){
		update();	//ATUALIZA ICONS E APAGA HOVER DE ITENS SELECIONADOS
		this.local=local;
		this.obj=obj;
		final Point mouseScreen=MouseInfo.getPointerInfo().getLocation();
		final int x=mouseScreen.x-tree.getPainel().getJanela().getX();
		final int y=mouseScreen.y-tree.getPainel().getJanela().getY();
		if(obj==null){
			if(tree.getPainel().getJanela().isVisible()){
				treePopup.show(tree.getPainel().getJanela(),x,y);
			}
		}else switch(obj.getTipo()){
			case MODULO:
				final boolean mestreAlone=(tree.getSelectedObjetos().getModulos().size()==1&&tree.getSelectedObjetos().getModulos().get(0)==Tree.getMestre());
				mod_menuBordas.setEnabled(!mestreAlone);									//APENAS SE NÃO FOR COM MESTRE OU APENAS COM VÁRIOS MODS
				mod_editTitle.setEnabled(tree.getSelectedObjetos().getModulos().size()==1);	//APENAS COM 1 MOD
				mod_newCox.setEnabled(!mestreAlone);										//APENAS SE NÃO FOR COM MESTRE OU APENAS COM VÁRIOS MODS
				del.setEnabled(!mestreAlone);												//APENAS SE NÃO FOR COM MESTRE OU APENAS COM VÁRIOS MODS
				modPopup.show(tree.getPainel().getJanela(),x,y);
			break;
			case CONEXAO:break;
			case SEGMENTO:
				cox_newNod.setEnabled(tree.getSelectedObjetos().getConexoes().size()==1);			//APENAS COM 1 COX
				coxPopup.show(tree.getPainel().getJanela(),x,y);
			break;
			case NODULO:
				nodPopup.show(tree.getPainel().getJanela(),x,y);
			break;
		}
		showTooltip(true);
	}
	public void close(){
		showTooltip(false);
		treePopup.setVisible(false);
		modPopup.setVisible(false);
		coxPopup.setVisible(false);
		nodPopup.setVisible(false);
	}
	public static void showTooltip(boolean show){
		ToolTipManager.sharedInstance().setEnabled(false);
		if(show)ToolTipManager.sharedInstance().setEnabled(true);
	}
//UPDATE
	public void update(){
	//PICKS
		mod_corPick=new CorPick(tree,tree.getSelectedObjetos().getModulos()){{
			addCorPickListener(new CorPickListener(){
				public void colorModified(Modulo[]mods,Cor[]oldCores,Cor newCor){
					tree.getUndoRedoManager().addUndoCor(mods,oldCores);
				}
			});
		}};
		mod_bordaPick=new BordaPick(tree,tree.getSelectedObjetos().getModulos(),tree.getSelectedObjetos().getConexoes()){{
			addBordaPickListener(new BordaPickListener(){
				public void bordaModified(Objeto[]objs,Borda[]oldBordas,Borda newBorda){
					tree.getUndoRedoManager().addUndoBorda(objs,oldBordas);
				}
			});
		}};
		cox_bordaPick=new BordaPick(tree,tree.getSelectedObjetos().getModulos(),tree.getSelectedObjetos().getConexoes()){{
			addBordaPickListener(new BordaPickListener(){
				public void bordaModified(Objeto[]objs,Borda[]oldBordas,Borda newBorda){
					tree.getUndoRedoManager().addUndoBorda(objs,oldBordas);
				}
			});
		}};
		cox_grossuraPick=new GrossuraPick(tree,tree.getSelectedObjetos().getConexoes()){{
			addGrossuraPickListener(new GrossuraPickListener(){
				public void grossuraModified(Conexao[]coxs,Grossura[]oldGrossuras,Grossura newGrossuras){
					tree.getUndoRedoManager().addUndoGrossura(coxs,oldGrossuras);
				}
			});
		}};
	//TREE
		treePopup=new JPopupMenu(){{
			add(new Item(Tree.getLang().get("T_Pop_Tree_NM","New Module"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						final Modulo newMod=new Modulo(local.x-Tree.getLocalX(),local.y-Tree.getLocalY(),"");
						tree.add(newMod);
						tree.getActions().addUndoable(Arrays.asList(newMod),true);
						tree.unSelectAll();
						tree.select(newMod);
						tree.draw();
					}
				});
			}});
			addPopupMenuListener(new PopupMenuListener() {
				public void popupMenuWillBecomeVisible(PopupMenuEvent p){}
				public void popupMenuWillBecomeInvisible(PopupMenuEvent p){showTooltip(false);}
				public void popupMenuCanceled(PopupMenuEvent p){showTooltip(false);}
			});
		}};
	//MODS
		modPopup=new JPopupMenu(){{
			add(getMenuIcon(iconesPasta));
			add(new Menu(Tree.getLang().get("T_Pop_C","Colors")){{
				add(mod_corPick);
			}});
			add(mod_menuBordas=new Menu(Tree.getLang().get("T_Pop_B","Borders")){{
				add(mod_bordaPick);
			}});
			add(mod_editTitle=new Item(Tree.getLang().get("T_Pop_Mod_ET","Edit Title"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						tree.getActions().editTitulo();
					}
				});
			}});
			add(new Item(Tree.getLang().get("T_Pop_Mod_NMR","New Related"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						final Modulo newMod=tree.getActions().createModRelacionado(
								local.x-Tree.getLocalX(),local.y-Tree.getLocalY(),
								tree.getSelectedObjetos().getModulos());
						tree.getActions().setState(Actions.MOVE+Actions.TO_CREATE,newMod);
					}
				});
			}});
			add(mod_newCox=new Item(Tree.getLang().get("T_Pop_Mod_NR","New Relation"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						Tree.getGhost().setLocationIndex(local.x-Tree.getLocalX()-2,local.y-Tree.getLocalY()-1);
						tree.getActions().relateToGhost(tree.getSelectedObjetos().getModulos());
						tree.getActions().setState(Actions.MOVE+Actions.TO_CONNECT);
					}
				});
			}});
			add(del=new Item(Tree.getLang().get("T_Pop_Mod_D","Delete"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						tree.getActions().delete();
					}
				});
			}});
			addPopupMenuListener(new PopupMenuListener() {
				public void popupMenuWillBecomeVisible(PopupMenuEvent p){}
				public void popupMenuWillBecomeInvisible(PopupMenuEvent p){showTooltip(false);}
				public void popupMenuCanceled(PopupMenuEvent p){showTooltip(false);}
			});
		}};
	//COXS
		coxPopup=new JPopupMenu(){{
			add(new Menu(Tree.getLang().get("T_Pop_B","Borders")){{
				add(cox_bordaPick);
			}});
			add(new Menu(Tree.getLang().get("T_Pop_G","Thickness")){{
				add(cox_grossuraPick);
			}});
			add(new Item(Tree.getLang().get("T_Pop_Cox_IR","Invert Relation"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						tree.getActions().invertCox();
					}
				});
			}});
			add(cox_newNod=new Item(Tree.getLang().get("T_Pop_Cox_NN","New Nodule"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						if(!obj.getTipo().is(Objeto.Tipo.SEGMENTO))return;
						tree.getActions().createNod((Segmento)obj,local.x-Tree.getLocalX(),local.y-Tree.getLocalY(),true);
						tree.draw();
					}
				});
			}});
			add(new Item(Tree.getLang().get("T_Pop_Cox_D","Delete"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						tree.getActions().delete();
					}
				});
			}});
			addPopupMenuListener(new PopupMenuListener() {
				public void popupMenuWillBecomeVisible(PopupMenuEvent p){}
				public void popupMenuWillBecomeInvisible(PopupMenuEvent p){showTooltip(false);}
				public void popupMenuCanceled(PopupMenuEvent p){showTooltip(false);}
			});
		}};
	//NODS
		nodPopup=new JPopupMenu(){{
			add(new Item(Tree.getLang().get("T_Pop_Nod_D","Delete"),tree){{
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a){
						tree.getActions().delete();
					}
				});
			}});
			addPopupMenuListener(new PopupMenuListener() {
				public void popupMenuWillBecomeVisible(PopupMenuEvent p){}
				public void popupMenuWillBecomeInvisible(PopupMenuEvent p){showTooltip(false);}
				public void popupMenuCanceled(PopupMenuEvent p){showTooltip(false);}
			});
		}};
	}
	//UPDATE ICONES
		private Menu getMenuIcon(File pastaIcon){
			final Menu menu=new Menu(Tree.getLang().get("T_Pop_I","Icons"));
			setMenuIcon(menu,pastaIcon,"");
			return menu;
		}
			private void setMenuIcon(Menu menu,File pasta,String subPasta){
				final File pastaAbsLink=new File(pasta.toString()+"/"+subPasta);	//LINK ABS+REL
				if(!pastaAbsLink.exists()||pastaAbsLink.listFiles().length==0){		//NÃO EXISTE OU ESTÁ VAZIO
					menu.add(new JMenuItem(Tree.getLang().get("T_Pop_I_V","Empty")){{
						setEnabled(false);
					}});
					return;
				}
				boolean temSubMenu=false;		//LISTA DE MENUS COM SUB-MENUS
				for(File file:pastaAbsLink.listFiles()){
					if(file.isDirectory()){		//É UMA SUBPASTA
						final Menu subMenu=new Menu(file.getName());
						menu.add(subMenu);
						setMenuIcon(subMenu,pasta,subPasta+"/"+file.getName());
						temSubMenu=true;
					}
				}
				for(File fileIcon:pastaAbsLink.listFiles()){
					if(fileIcon.getName().endsWith(".png")){	//É UM ICONE
						final double minWidth=(temSubMenu?menu.getPreferredSize().getWidth():0);	//SE O MENU TEM SUB-MENUS, EXISTE UM TAMANHO-MIN
						final IconePick iconPick=new IconePick(tree,pasta,subPasta,tree.getSelectedObjetos().getModulos(),minWidth){{
							addIconePickListener(new IconePickListener(){
								public void iconAdded(Modulo[]mods,Icone icone){
									tree.getUndoRedoManager().addUndoIcone(mods,icone,UndoRedoIcone.Modo.DEL);
								}
								public void iconRemoved(Modulo[]mods,Icone icone){
									tree.getUndoRedoManager().addUndoIcone(mods,icone,UndoRedoIcone.Modo.ADD);
								}
							});
						}};
						mod_iconPicks.add(iconPick);
						menu.add(iconPick);
						break;
					}
				}
			}
	//UPDATE CORES
		public void setCoresPaleta(Cor[][]coresPaleta){
			CorPick.PALETA_DEFAULT=coresPaleta;
			update();
		}
	public boolean isShowing(){
		return (treePopup.isShowing()||
				modPopup.isShowing()||
				coxPopup.isShowing()||
				nodPopup.isShowing());
	}
}