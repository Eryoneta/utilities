package element.tree.undoRedo;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import element.tree.objeto.ListaObjeto;
import element.tree.objeto.Objeto;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.Nodulo;
import element.tree.objeto.modulo.Modulo;
import element.tree.propriedades.Borda;
import element.tree.propriedades.Grossura;
import element.tree.Tree;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Icone;
public class UndoRedo{
//TREE
	private Tree tree;
		public Tree getTree(){return tree;}
		public void setTree(Tree tree){this.tree=tree;}
//LIMITE DE UNDO/REDO
	private int doLimite=100;
		public int getDoLimite(){return doLimite;}
		public void setDoLimite(int limite){this.doLimite=limite;}
//LISTENERS: DISPARAM QUANDO UM UNDO/REDO É FEITO
	private List<UndoRedoListener>undoRedoListeners=new ArrayList<UndoRedoListener>();
		public void addUndoRedoListener(UndoRedoListener undoRedoListener){undoRedoListeners.add(undoRedoListener);}
		public void triggerUndoRedoListener(boolean isSaved,boolean isUndo){
			if(isSaved){
				for(UndoRedoListener undoRedoListener:undoRedoListeners)undoRedoListener.actionSaved();
			}else if(isUndo){
				for(UndoRedoListener undoRedoListener:undoRedoListeners)undoRedoListener.actionUndone();
			}else{
				for(UndoRedoListener undoRedoListener:undoRedoListeners)undoRedoListener.actionRedone();
			}
		}
//SAVES
	public void addUndoLocal(Objeto[]objs,Point diff){									//LOCAIS(MODS/NODS)
		addUndo(new UndoRedoLocal(objs,diff));
	}
	public void addUndoTitulo(Modulo mod,String titulo){								//TITULO(MOD)
		addUndo(new UndoRedoTitulo(mod,titulo));
	}
	public void addUndoTexto(Objeto obj,List<String>texto){								//TEXTO(MOD/COX)
		addUndo(new UndoRedoTexto(obj,texto));
	}
	public void addUndoCor(Modulo[]mods,Cor[]cores){									//CORES(MODS)
		addUndo(new UndoRedoCor(mods,cores));
	}
	public void addUndoBorda(Objeto[]objs,Borda[]bordas){								//BORDAS(MODS/COXS)
		addUndo(new UndoRedoBorda(objs,bordas));
	}
	public void addUndoGrossura(Conexao[]coxs,Grossura[]grossuras){						//GROSSURAS(COXS)
		addUndo(new UndoRedoGrossura(coxs,grossuras));
	}
	public void addUndoIcone(Modulo[]mods,Icone icone,UndoRedoIcone.Modo modo){			//ICONES(MODS)
		addUndo(new UndoRedoIcone(mods,icone,modo));
	}
	public void addUndoObjeto(Objeto[]objs,UndoRedoObjeto.Modo modo){					//PRESENÇA(MODS/COXS/NODS)
		addUndo(new UndoRedoObjeto(objs,modo));		//OS OBJS DEVEM SEGUIR UMA ORDEM: NOD, COX, E MOD
	}
	public void addUndoObjeto(Objeto[]oldObjs,Objeto[]newObjs,UndoRedoObjeto.Modo modo){//TROCA(MODS/COXS/NODS)
		addUndo(new UndoRedoObjeto(oldObjs,newObjs,modo));		//OS OBJS DEVEM SEGUIR UMA ORDEM: NOD, COX, E MOD
	}
	private void addUndo(UndoRedoNodo acao){
		redo.clear();
		undo.add(acao);
		if(doLimite!=-1&&undo.size()>doLimite)undo.remove(0);			//REMOVE O UNDO MAIS ANTIGO
		triggerUndoRedoListener(true,false);
	}
//UNDO
	private List<UndoRedoNodo>undo=new ArrayList<UndoRedoNodo>();
		public void undo(){
			undoRedo(undo,redo);
			triggerUndoRedoListener(false,true);
		}
//REDO
	private List<UndoRedoNodo>redo=new ArrayList<UndoRedoNodo>();
		public void redo(){
			undoRedo(redo,undo);
			triggerUndoRedoListener(false,false);
		}
//UNDO/REDO
	private void add(List<UndoRedoNodo>undoRedo,UndoRedoNodo acao){
		undoRedo.add(acao);
		if(doLimite!=-1&&undoRedo.size()>doLimite)undoRedo.remove(0);	//REMOVE O UNDO/REDO MAIS ANTIGO
	}
	private void undoRedo(List<UndoRedoNodo>entrada,List<UndoRedoNodo>saida){
		if(entrada.isEmpty()){
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		final UndoRedoNodo acao=entrada.remove(entrada.size()-1);
		switch(acao.getTipo()){
			case LOCAL:
				final UndoRedoLocal acaoLocal=(UndoRedoLocal)acao;
				for(Objeto obj:acaoLocal.getObjetos()){
					if(obj.getTipo().is(Objeto.Tipo.MODULO)){
						final Modulo mod=(Modulo)obj;
						final int x=mod.getXIndex()+acaoLocal.getDiferenca().x;
						final int y=mod.getYIndex()+acaoLocal.getDiferenca().y;
						mod.setLocationIndex(x,y);
					}else if(obj.getTipo().is(Objeto.Tipo.NODULO)){
						final Nodulo nod=(Nodulo)obj;
						final int x=nod.getXIndex()+acaoLocal.getDiferenca().x;
						final int y=nod.getYIndex()+acaoLocal.getDiferenca().y;
						nod.setLocationIndex(x,y);
					}
				}
				focusAndSelect(acaoLocal.getObjetos());
				add(saida,acaoLocal.invert());
			break;
			case TITULO:
				final UndoRedoTitulo acaoTitulo=(UndoRedoTitulo)acao;
				final String oldTitulo=acaoTitulo.getModulo().getTitle();
				acaoTitulo.getModulo().setTitle(acaoTitulo.getTitulo());
				focusAndSelect(acaoTitulo.getModulo());
				add(saida,acaoTitulo.invert(oldTitulo));
			break;
			case TEXTO:
				final UndoRedoTexto acaoTexto=(UndoRedoTexto)acao;
				if(acaoTexto.getObjeto().getTipo().is(Objeto.Tipo.MODULO)){
					final Modulo mod=(Modulo)acaoTexto.getObjeto();
					final List<String>oldTexto=mod.getTexto();
					mod.setTexto(acaoTexto.getTexto());
					focusAndSelect(mod);
					add(saida,acaoTexto.invert(oldTexto));
				}else if(acaoTexto.getObjeto().getTipo().is(Objeto.Tipo.CONEXAO)){
					final Conexao cox=(Conexao)acaoTexto.getObjeto();
					final List<String>oldTexto=cox.getTexto();
					cox.setTexto(acaoTexto.getTexto());
					focusAndSelect(cox);
					add(saida,acaoTexto.invert(oldTexto));
				}
			break;
			case COR:
				final UndoRedoCor acaoCor=(UndoRedoCor)acao;
				final Cor[]oldCores=new Cor[acaoCor.getModulosCount()];
				for(int i=0;i<acaoCor.getModulosCount();i++){
					final Modulo mod=(Modulo)acaoCor.getModulo(i);
					oldCores[i]=mod.getCor();
					mod.setCor(acaoCor.getCor(i));
				}
				focusAndSelect(acaoCor.getModulos());
				add(saida,acaoCor.invert(oldCores));
			break;
			case GROSSURA:
				final UndoRedoGrossura acaoGrossura=(UndoRedoGrossura)acao;
				final Grossura[]oldGrossuras=new Grossura[acaoGrossura.getConexoesCount()];
				for(int i=0;i<acaoGrossura.getConexoesCount();i++){
					final Conexao cox=acaoGrossura.getConexao(i);
					oldGrossuras[i]=cox.getGrossura();
					cox.setGrossura(acaoGrossura.getGrossura(i));
				}
				focusAndSelect(acaoGrossura.getConexoes());
				add(saida,acaoGrossura.invert(oldGrossuras));
			break;
			case BORDA:
				final UndoRedoBorda acaoBorda=(UndoRedoBorda)acao;
				final Borda[]oldBordas=new Borda[acaoBorda.getObjetosCount()];
				for(int i=0;i<acaoBorda.getObjetosCount();i++){
					final Objeto obj=acaoBorda.getObjeto(i);
					if(obj.getTipo().is(Objeto.Tipo.MODULO)){
						final Modulo mod=(Modulo)obj;
						oldBordas[i]=mod.getBorda();
						mod.setBorda(acaoBorda.getBorda(i));
					}else if(obj.getTipo().is(Objeto.Tipo.CONEXAO)){
						final Conexao cox=(Conexao)obj;
						oldBordas[i]=cox.getBorda();
						cox.setBorda(acaoBorda.getBorda(i));
					}
				}
				focusAndSelect(acaoBorda.getObjetos());
				add(saida,acaoBorda.invert(oldBordas));
			break;
			case ICONE:
				final UndoRedoIcone acaoIcone=(UndoRedoIcone)acao;
				switch(acaoIcone.getModo()){
					case ADD:
						for(Modulo mod:acaoIcone.getModulos())mod.addIcone(acaoIcone.getIcone());
					break;
					case DEL:
						for(Modulo mod:acaoIcone.getModulos())mod.delIcone(acaoIcone.getIcone());
					break;
				}
				focusAndSelect(acaoIcone.getModulos());
				add(saida,acaoIcone.invert());
			break;
			case OBJETO:
				final UndoRedoObjeto acaoObjeto=(UndoRedoObjeto)acao;
				switch(acaoObjeto.getModo()){
					case ADD:
						final List<Objeto>listAdd=ListaObjeto.toList(acaoObjeto.getObjetos()).getAllOrdened();			//SEPARA EM MOD(1,2),COX(1,2,3),NOD(1,2,3,4)
						for(Objeto obj:listAdd)getTree().add(obj.getIndex(),obj);
						if(acaoObjeto.getObjetosCount()>0)focusAndSelect(acaoObjeto.getObjetos());
					break;
					case DEL:
						final List<Objeto>listDel=ListaObjeto.toList(acaoObjeto.getObjetos()).getAllOrdenedInverted();	//SEPARA EM NOD(1,2,3,4),COX(1,2,3),MOD(1,2)
						for(Objeto obj:listDel)getTree().del(obj);
					break;
//					case REPLACE:
//						final List<Objeto>listToRep=ListaObjeto.toList(acaoObjeto.getObjetos()).getAllOrdenedInverted();	//SEPARA EM NOD(1,2,3,4),COX(1,2,3),MOD(1,2)
//						final List<Objeto>listToAdd=ListaObjeto.toList(acaoObjeto.getToReplaceObjetos()).getAllOrdened();					//SEPARA EM MOD(1,2),COX(1,2,3),NOD(1,2,3,4)
//						for(Objeto obj:listToRep)getTree().del(obj);
//						for(Objeto obj:listToAdd)getTree().add(obj.getIndex(),obj);
//						if(acaoObjeto.getObjetosCount()>0)focusAndSelect(acaoObjeto.getObjetos());
//					break;
				}
				add(saida,acaoObjeto.invert());
			break;
		}
	}
//MAIN
	public UndoRedo(Tree tree){
		setTree(tree);
	}
//RESET
	public void clear(){
		undo.clear();
		redo.clear();
	}
//FOCUS
	private void focusAndSelect(Objeto[]objs){
		final Point local=Tree.getLocal();
		getTree().setFocusOn(objs);
		getTree().animate(local,Tree.getLocal());
		getTree().unSelectAll();
		for(Objeto obj:objs)getTree().select(obj);
	}
	private void focusAndSelect(Modulo mod){focusAndSelect(new Modulo[]{mod});}
	private void focusAndSelect(Conexao cox){focusAndSelect(new Conexao[]{cox});}
}