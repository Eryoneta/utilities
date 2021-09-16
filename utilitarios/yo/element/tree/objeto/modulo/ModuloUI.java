package element.tree.objeto.modulo;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import element.tree.Tree;
import element.tree.TreeUI;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Icone;
public class ModuloUI{
//VAR GLOBAIS
	public static int getRoundValue(int unit){return unit;}
	public static class Cores{
		public static Cor FUNDO=new Cor(215,215,215);
		public static Cor SELECT=new Cor(120,120,120);
		public static Cor CREATE=new Cor(65,231,82);
		public static Cor PAI=new Cor(117,132,236);
		public static Cor SON=new Cor(236,220,117);
		public static Cor DELETE=new Cor(170,45,45);
	}
//MÓDULO
	private Modulo mod;
//MAIN
	public ModuloUI(Modulo mod){this.mod=mod;}
//DRAW
	public void draw(Graphics2D imagemEdit,int unit){
		if(mod==Tree.getGhost())return;
		final int round=ModuloUI.getRoundValue(unit);
		final float borda=TreeUI.getBordaValue(unit);
		drawBrilho(imagemEdit,unit,round,borda);
		drawFundo(imagemEdit,unit,round);
		drawBorda(imagemEdit,unit,round,borda);
		if(!mod.getTitle().isEmpty())drawTitulo(imagemEdit,unit);
		drawIcone(imagemEdit,unit);
	}
		private void drawBrilho(Graphics2D imagemEdit,int unit,int round,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(mod.getState().equals(ModuloST.State.UNSELECTED)||mod.getState().equals(ModuloST.State.HIGHLIGHTED))return;
			imagemEdit.setColor(getBrilhoCor(mod.getState()));
			final float x=mod.getX(unit)-borda*2.6f;
			final float y=mod.getY(unit)-borda*2.6f;
			final float width=mod.getWidth(unit)+((borda*2.6f)*2);
			final float height=mod.getHeight(unit)+((borda*2.6f)*2);
			round*=1.6;
			if(mod==Tree.getMestre()){
				imagemEdit.fill(new Rectangle2D.Float(x,y,width,height));
			}else imagemEdit.fill(new RoundRectangle2D.Float(x,y,width,height,round,round));
		}
		private void drawFundo(Graphics2D imagemEdit,int unit,int round){
			imagemEdit.setColor(getFundoCor(mod.getState()));
			if(mod==Tree.getMestre()){
				imagemEdit.fill(new Rectangle2D.Float(mod.getX(unit),mod.getY(unit),mod.getWidth(unit),mod.getHeight(unit)));
			}else imagemEdit.fill(new RoundRectangle2D.Float(mod.getX(unit),mod.getY(unit),mod.getWidth(unit),mod.getHeight(unit),round,round));
		}
		private void drawBorda(Graphics2D imagemEdit,int unit,int round,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			imagemEdit.setStroke(mod.getBorda().getVisual(borda));
			imagemEdit.setColor(getBordaCor(mod.getState()));
			if(mod==Tree.getMestre()){
				imagemEdit.setStroke(new BasicStroke(TreeUI.getBordaValue(unit),BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));	//BORDAS RETAS
				imagemEdit.draw(new Rectangle2D.Float(mod.getX(unit),mod.getY(unit),mod.getWidth(unit),mod.getHeight(unit)));
			}else imagemEdit.draw(new RoundRectangle2D.Float(mod.getX(unit),mod.getY(unit),mod.getWidth(unit),mod.getHeight(unit),round,round));
		}
		private void drawTitulo(Graphics2D imagemEdit,int unit){
			switch(mod.getState()){
				case UNSELECTED:default:
				case SELECTED:
				case HIGHLIGHTED:			imagemEdit.setColor(mod.getCor().isDark()?TreeUI.Fonte.LIGHT:TreeUI.Fonte.DARK);break;
				case TO_BE_CREATOR:		
				case TO_BE_PAI:
				case TO_BE_SON:
				case TO_BE_DELETED:			imagemEdit.setColor(TreeUI.Fonte.DARK);break;
			}
			if(unit<=3){		//EM ZOOM<=3, É ILEGÍVEL, TROCA POR LINHA
				imagemEdit.setStroke(new BasicStroke(1));
				final int borda=1*unit;
				final int linhasQtd=(unit<=2?Math.max(1,mod.titulo.length/2):mod.titulo.length);	//LINHAS MUITO JUNTAS FORMAM BLOCOS
				final float linhaHeight=(float)(mod.getHeight(unit))/linhasQtd;
				final int width=Math.max(1,mod.getWidth(unit)-borda-borda);	//DEVE APARECER PELO MENOS 1 PÍXEL
				final int x=mod.getX(unit)+borda;
				float y=mod.getY(unit)+linhaHeight/2;
				for(int i=0;i<linhasQtd;i++){
					imagemEdit.drawLine(x,(int)Math.round(y),x+width,(int)Math.round(y));
					y+=linhaHeight;
				}
			}else{
				imagemEdit.setFont(mod.getRelativeFont(unit));
				final int height=imagemEdit.getFontMetrics().getHeight();
//				final double heightUnit=((double)getHeight()/titulo.length);	//DESCENTRALIZA COM O TITULO 
				double y=mod.getY(unit)+(mod.isIconified()?unit*2:0)+height;
				for(String linha:mod.titulo){
					imagemEdit.drawString(linha,mod.getX(unit)+(mod.getWidth(unit)-imagemEdit.getFontMetrics().stringWidth(linha))/2,(int)y);
					y+=height;
				}
			}
		}
		private void drawIcone(Graphics2D imagemEdit,int unit){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(!mod.isIconified())return;
			final int iconsWidth=Icone.getSize(unit)*mod.getIcones().size();
			for(int i=0,size=mod.getIcones().size(),x=mod.getX(unit)+((mod.getWidth(unit)-iconsWidth)/2),y=mod.getY(unit);i<size;i++,x+=Icone.getSize(unit)){
				mod.getIcones().get(i).draw(imagemEdit,unit,x,y);
			}
		}
	public Cor getBrilhoCor(ModuloST.State state){
		final float transparency=0.4f;
		switch(state){
			case SELECTED:default:		return Cor.getTransparent(Cor.getChanged(ModuloUI.Cores.SELECT,0.8f),transparency);
			case TO_BE_CREATOR:			return Cor.getTransparent(Cor.getChanged(ModuloUI.Cores.CREATE,0.8f),transparency);
			case TO_BE_PAI:				return Cor.getTransparent(Cor.getChanged(ModuloUI.Cores.PAI,0.8f),transparency);
			case TO_BE_SON:				return Cor.getTransparent(Cor.getChanged(ModuloUI.Cores.SON,0.8f),transparency);
			case TO_BE_DELETED:			return Cor.getTransparent(Cor.getChanged(ModuloUI.Cores.DELETE,0.8f),transparency);
		}
	}
	public Cor getFundoCor(ModuloST.State state){
		switch(state){
			case UNSELECTED:default:	return mod.getCor();
			case SELECTED:
			case HIGHLIGHTED:
				Cor corHigh=Cor.getChanged(mod.getCor(),60);										//FUNDO MAIS CLARO
				if(corHigh.getRed()==255&&corHigh.getGreen()==255&&corHigh.getBlue()==255){		//SE CLARO DEMAIS
					corHigh=Cor.getChanged(mod.getCor(),-30);										//FUNDO MAIS ESCURO
				}
				return corHigh;
			case TO_BE_CREATOR:			return ModuloUI.Cores.CREATE;
			case TO_BE_PAI:				return ModuloUI.Cores.PAI;
			case TO_BE_SON:				return ModuloUI.Cores.SON;
			case TO_BE_DELETED:			return ModuloUI.Cores.DELETE;
		}
	}
	public Cor getBordaCor(ModuloST.State state){
		switch(state){
			case UNSELECTED:default:
				Cor corUnSelec=Cor.getChanged(mod.getCor(),-30);											//BORDA MAIS ESCURA QUE O FUNDO
				if(corUnSelec.getRed()==0&&corUnSelec.getGreen()==0&&corUnSelec.getBlue()==0){			//SE ESCURO DEMAIS
					corUnSelec=Cor.getChanged(mod.getCor(),(int)((255-mod.getCor().getBrilho())/2));		//ESCLARECE
				}
				return Cor.getContraste(TreeUI.FUNDO,corUnSelec,false);
			case SELECTED:				return Cor.getContraste(TreeUI.FUNDO,Cor.getInverted(mod.getCor()),false);
			case HIGHLIGHTED:			return Cor.getContraste(TreeUI.FUNDO,mod.getCor(),false);
			case TO_BE_CREATOR:			return Cor.getContraste(TreeUI.FUNDO,Cor.getChanged(ModuloUI.Cores.CREATE,0.8f),false);
			case TO_BE_PAI:				return Cor.getContraste(TreeUI.FUNDO,Cor.getChanged(ModuloUI.Cores.PAI,0.8f),false);
			case TO_BE_SON:				return Cor.getContraste(TreeUI.FUNDO,Cor.getChanged(ModuloUI.Cores.SON,0.8f),false);
			case TO_BE_DELETED:			return Cor.getContraste(TreeUI.FUNDO,Cor.getChanged(ModuloUI.Cores.DELETE,0.8f),false);
		}
	}
}