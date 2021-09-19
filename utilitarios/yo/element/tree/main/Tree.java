package element.tree.main;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.JFrame;
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
import element.tree.propriedades.Cor;
import element.tree.propriedades.Icone;
import element.Elemento;
import element.Painel;
import element.tree.Chunk;
import element.tree.ObjetoFocusListener;
import element.tree.objeto.ListaObjeto;
import element.tree.objeto.Objeto;
import element.tree.objeto.ObjetoBoundsListener;
import element.tree.objeto.conexao.Conexao;
import element.tree.objeto.conexao.ConexaoST;
import element.tree.objeto.nodulo.Nodulo;
import element.tree.objeto.nodulo.NoduloST;
import element.tree.objeto.conexao.segmento.Segmento;
import element.tree.objeto.conexao.segmento.SegmentoST;
import element.tree.objeto.modulo.Modulo;
import element.tree.objeto.modulo.ModuloST;
import element.tree.undoRedo.UndoRedo;
public class Tree extends Elemento{
//ST
	private final TreeST ST=new TreeST(this);
		public TreeST getST(){return ST;}
		public int getState(){return getST().getState();}
		public TreeST.State getStateContent(int state){return getST().getStateContent(state);}
		public void setState(int stateIndex,Objeto...objs){getST().setState(stateIndex,objs);}
//UI
	private final TreeUI UI=new TreeUI(this);
		public TreeUI getUI(){return UI;}
//LOCAL
	private static int X=0;
		public static int getLocalX(){return X;}
	private static int Y=0;
		public static int getLocalY(){return Y;}
	public static Point getLocal(){return new Point(X,Y);}
	public static void setLocal(int x,int y){X=x;Y=y;}
//VAR GLOBAIS
	public static int UNIT=8;
//MÓDULOS ESPECIAIS
	private static Modulo MESTRE;
		public static Modulo getMestre(){return MESTRE;}
	private static Modulo GHOST;
		public static Modulo getGhost(){return GHOST;}
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
//UNDO-REDO
	private UndoRedo undoRedo;
		public UndoRedo getUndoRedoManager(){return undoRedo;}
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
					mod.setState(ModuloST.State.SELECTED);
					if(selectedObjetos.contains(mod))return true;
					getUI().setTexto(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==0?mod:getGhost());
					selectedObjetos.add(mod);
					triggerObjetoListener(mod,true);
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					cox.setState(ConexaoST.State.SELECTED);
					if(selectedObjetos.contains(cox))return true;
					getUI().setTexto(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==0?cox:getGhost());
					selectedObjetos.add(cox);
					triggerObjetoListener(cox,true);
					return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					nod.setState(NoduloST.State.SELECTED);
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
							if(cox.getState().is(ConexaoST.State.SELECTED))continue;
							if(cox.getSon().getState().is(ModuloST.State.SELECTED))continue;
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
					mod.setState(ModuloST.State.TO_BE_CREATOR);
					if(selectedObjetos.contains(mod))return true;
					selectedObjetos.add(mod);
					return true;
				case CONEXAO:	return false;	//COX PODE APENAS SER CHAMADO CREATOR PELO SEG
				case NODULO:	return false;	//NOD PODE APENAS SER CHAMADO CREATOR PELO COX
				case SEGMENTO:
					final Segmento seg=(Segmento)obj;
					seg.setState(SegmentoST.State.TO_BE_CREATOR);
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
			mod.setState(ModuloST.State.TO_BE_SON);
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
			mod.setState(ModuloST.State.TO_BE_PAI);
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
					mod.setState(ModuloST.State.TO_BE_DELETED);
					for(Conexao cox:mod.getConexoes()){
						selectToBeDeleted(cox);
					}
					if(selectedObjetos.contains(mod))return true;
					selectedObjetos.add(mod);
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					cox.setState(ConexaoST.State.TO_BE_DELETED);
					for(Nodulo nod:cox.getNodulos()){
						selectToBeDeleted(nod);
					}
					if(selectedObjetos.contains(cox))return true;
					selectedObjetos.add(cox);
					return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					nod.setState(NoduloST.State.TO_BE_DELETED);
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
					mod.setState(ModuloST.State.UNSELECTED);
					if(!selectedObjetos.contains(mod))return true;
					selectedObjetos.del(mod);
					if(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==1){
						if(selectedObjetos.getModulos().size()==1){
							getUI().setTexto(selectedObjetos.getModulos().get(0));
						}else if(selectedObjetos.getConexoes().size()==1){
							getUI().setTexto(selectedObjetos.getConexoes().get(0));
						}
					}else if(getUI().getTexto().getObjeto()!=getGhost()){
						getUI().setTexto(getGhost());
					}
					triggerObjetoListener(mod,false);
					return true;
				case CONEXAO:
					final Conexao cox=(Conexao)obj;
					cox.setState(ConexaoST.State.UNSELECTED);
					if(!selectedObjetos.contains(cox))return true;
					selectedObjetos.del(cox);
					if(selectedObjetos.getModulos().size()+selectedObjetos.getConexoes().size()==1){
						if(selectedObjetos.getModulos().size()==1){
							getUI().setTexto(selectedObjetos.getModulos().get(0));
						}else if(selectedObjetos.getConexoes().size()==1){
							getUI().setTexto(selectedObjetos.getConexoes().get(0));
						}
					}else if(getUI().getTexto().getObjeto()!=getGhost()){
						getUI().setTexto(getGhost());
					}
					triggerObjetoListener(cox,false);
					return true;
				case NODULO:
					final Nodulo nod=(Nodulo)obj;
					nod.setState(NoduloST.State.UNSELECTED);
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
							if(!cox.getState().is(ConexaoST.State.SELECTED))continue;
							if(!cox.getSon().getState().is(ModuloST.State.SELECTED))continue;
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
					case MODULO:	areaSimples=((Modulo)obj).getForm(Tree.UNIT);	break;
					case CONEXAO:	areaComplex=((Conexao)obj).getForm(Tree.UNIT);	break;
					case NODULO:	areaSimples=((Nodulo)obj).getForm(Tree.UNIT);	break;
					case SEGMENTO:													break;
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
		undoRedo=new UndoRedo(this);
		getUI().build();
		painel.add(getUI().getTitulo());
		actions=new Actions(this);
		getST().loadStates();
		clear();
		actions.putIntputs();
	}
//FUNCS
	public void clear(){
		Tree.UNIT=8;
		setLocal(0,0);
		TreeUI.Fonte.FONTE=new Font("Courier New",Font.PLAIN,15);
		getUI().getSelecao().setEmpty();
		undoRedo.clear();
		getObjetos().clear();
		getSelectedObjetos().clear();
		getVisibleMods().clear();
		getVisibleCoxs().clear();
		getVisibleNods().clear();
		getChunks().clear();
		objsListaMaxSize=0;
		Tree.MESTRE=new Modulo(0,0,TreeUI.getLang().get("T_M","New Mind Map"));
		getMestre().setCor(new Cor(0,255,255));
		Tree.GHOST=new Modulo(0,0,TreeUI.getLang().get("T_G","Text"));
		add(getMestre());
		getUI().setTitulo(getGhost());
		getUI().setTexto(getGhost());
		setAntialias(true);
		setEnabled(true);
		setState(TreeST.NORMAL);
	}
//DRAW
@Override
	public synchronized void draw(Graphics2D imagemEdit){
		getUI().draw(imagemEdit);
	}
//TAG -> TREE
	public List<Objeto>addTree(Element mindTag,boolean replaceMestre)throws Exception{
		final List<Objeto>lista=new ArrayList<Objeto>();
		final HashMap<Integer,Modulo>mods=new HashMap<Integer,Modulo>();
		if(replaceMestre){
			final String fontNome=mindTag.getAttribute("fontName");			//SET FONT
			final int fontStyle=Integer.parseInt(mindTag.getAttribute("fontStyle"));
			final int fontSize=Integer.parseInt(mindTag.getAttribute("fontSize"));
			getUI().setFonte(new Font(fontNome,fontStyle,fontSize));
		}
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
			mindTag.setAttribute("fontName",TreeUI.Fonte.FONTE.getName());					//FONT_NAME
			mindTag.setAttribute("fontStyle",String.valueOf(TreeUI.Fonte.FONTE.getStyle()));	//FONT_STYLE
			mindTag.setAttribute("fontSize",String.valueOf(TreeUI.Fonte.FONTE.getSize()));	//FONT_SIZE
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
			TreeUI.mensagem(TreeUI.getLang().get("T_Err1","Error: Couldn't build .mind file!")+"\n"+erro,TreeUI.Options.ERRO);
		}
		return null;
	}
//TREE -> IMAGE
	public Image getImage(ListaObjeto lista){
		unSelectAll();
		if(lista.isEmpty())return null;
	//CALC AREA
		final int unit=8;
		final int imgBorda=unit;
		Rectangle area=null;
		for(Modulo mod:lista.getModulos())
			area=compararArea(area,unit,mod.getX(unit),mod.getY(unit),mod.getX(unit)+mod.getWidth(unit),mod.getY(unit)+mod.getHeight(unit));
		for(Nodulo nod:lista.getNodulos())
			area=compararArea(area,unit,nod.getX(unit),nod.getY(unit),nod.getX(unit),nod.getY(unit));
		for(Conexao cox:lista.getConexoes())
			area=compararArea(area,unit,cox.getX1(unit),cox.getY1(unit),cox.getX2(unit),cox.getY2(unit));
		final Rectangle tela=new Rectangle(area.x,area.y,area.width-area.x,area.height-area.y);
	//SET IMG
		final BufferedImage imagem=new BufferedImage(imgBorda+tela.width+imgBorda,imgBorda+tela.height+imgBorda,BufferedImage.TYPE_INT_ARGB);
		final Graphics2D imagemEdit=(Graphics2D)imagem.getGraphics();
		imagemEdit.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	//FUNDO
		if(!((JFrame)painel.getJanela()).isUndecorated()){
			imagemEdit.setColor(TreeUI.FUNDO);
			imagemEdit.fillRect(0,0,imagem.getWidth(),imagem.getHeight());	//FUNDO
			if(isShowingGrid()){											//GRADE
				imagemEdit.setColor(Cor.getChanged(TreeUI.FUNDO,0.95f));
				for(int x=0,size=imagem.getWidth()/unit;x<=size;x++)imagemEdit.drawLine(unit*x,0,unit*x,imagem.getHeight());
				for(int y=0,size=imagem.getHeight()/unit;y<=size;y++)imagemEdit.drawLine(0,unit*y,imagem.getWidth(),unit*y);
			}
		}
	//DRAW OBJS
		imagemEdit.translate(-tela.x+imgBorda,-tela.y+imgBorda);
		for(Conexao cox:lista.getConexoes())cox.draw(imagemEdit,unit);
		for(Nodulo nod:lista.getNodulos())nod.draw(imagemEdit,unit);
		for(Modulo mod:lista.getModulos())mod.draw(imagemEdit,unit);
		return imagem;
	}
		private Rectangle compararArea(Rectangle area,int unit,int minX,int minY,int maxX,int maxY){
			if(area==null){
				area=new Rectangle();
				area.x=minX;
				area.y=minY;
				area.width=maxX;
				area.height=maxY;
				return area;
			}
			area.x=Math.min(area.x,minX);
			area.y=Math.min(area.y,minY);
			area.width=Math.max(area.width,maxX);
			area.height=Math.max(area.height,maxY);
			return area;
		}
}