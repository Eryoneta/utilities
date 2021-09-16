package element.tree.objeto.conexao;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import element.tree.Tree;
import element.tree.TreeUI;
import element.tree.objeto.nodulo.Nodulo;
import element.tree.objeto.conexao.segmento.Segmento;
import element.tree.objeto.conexao.segmento.SegmentoST;
import element.tree.objeto.modulo.ModuloST;
import element.tree.propriedades.Cor;
import element.tree.propriedades.Grossura;
public class ConexaoUI{
//CONEXÃO
	private Conexao cox;
//MAIN
	public ConexaoUI(Conexao cox){this.cox=cox;}
//DRAW
	public void draw(Graphics2D imagemEdit,int unit){
		final float borda=TreeUI.getBordaValue(unit);
		drawBrilho(imagemEdit,unit,borda);
		drawLinha(imagemEdit,unit,borda);
		drawPonta(imagemEdit,unit);
	}
		private void drawBrilho(Graphics2D imagemEdit,int unit,float borda){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			if(cox.getState().is(ConexaoST.State.UNSELECTED,ConexaoST.State.HIGHLIGHTED))return;
			switch(cox.getGrossura().getIndex()){
				case Grossura.THIN:
					imagemEdit.setStroke(new BasicStroke(cox.getGrossura().getValor(borda)*4*4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
				case Grossura.MEDIUM:default:
					imagemEdit.setStroke(new BasicStroke(cox.getGrossura().getValor(borda)*3*3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
				case Grossura.WIDE:
					imagemEdit.setStroke(new BasicStroke(cox.getGrossura().getValor(borda)*2*2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
				case Grossura.ULTRA_WIDE:
					imagemEdit.setStroke(new BasicStroke(cox.getGrossura().getValor(borda)*1.5f*1.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
				break;
			}
			imagemEdit.setColor(getBrilhoCor(cox.getState()));
			if(cox.getState().is(ConexaoST.State.TO_BE_CREATOR)){		//BRILHO DO SEG TO_BE_CREATOR
				for(Segmento seg:cox.getSegmentos()){
					if(seg.getState().is(SegmentoST.State.UNSELECTED))continue;
					final Path2D.Float forma=new Path2D.Float();
					forma.moveTo(seg.getX1(unit),seg.getY1(unit));
					forma.lineTo(seg.getX2(unit),seg.getY2(unit));
					imagemEdit.draw(forma);
				}
				return;
			}
			final Path2D.Float forma=new Path2D.Float();
			forma.moveTo(cox.getX1(unit),cox.getY1(unit));
			for(Segmento seg:cox.getSegmentos()){
				if(seg.getPonta2()==Tree.getGhost()){
					forma.lineTo(Tree.getGhost().getX(unit),Tree.getGhost().getY(unit));
				}else forma.lineTo(seg.getX2(unit),seg.getY2(unit));
			}
			imagemEdit.draw(forma);								//BRILHO DO SELECTED OU TO_BE_DELETED
		}
		private void drawLinha(Graphics2D imagemEdit,int unit,float borda){
			if(unit<=2&&cox.getGrossura().getIndex()==Grossura.THIN)return;		//EM ZOOM<=2 COM GROSSURA FINA, É IRRELEVANTE
			if(unit<=1&&cox.getGrossura().getIndex()==Grossura.MEDIUM)return;	//EM ZOOM<=1 COM GROSSURA MEDIUM, É IRRELEVANTE
			imagemEdit.setStroke(cox.getBorda().getVisual(cox.getGrossura().getValor(borda)));
			imagemEdit.setColor(getLinhaCor(cox.getState()));
			final Path2D.Float forma=new Path2D.Float();
			forma.moveTo(cox.getX1(unit),cox.getY1(unit));
			for(Segmento seg:cox.getSegmentos()){
				if(seg.getPonta2()==Tree.getGhost()){
					forma.lineTo(Tree.getGhost().getX(unit),Tree.getGhost().getY(unit));
				}else forma.lineTo(seg.getX2(unit),seg.getY2(unit));
			}
			imagemEdit.draw(forma);
		}
		private void drawPonta(Graphics2D imagemEdit,int unit){
			if(unit<=2)return;		//EM ZOOM<=2, É IRRELEVANTE
			float x1=cox.getX1(unit);
			float y1=cox.getY1(unit);
			float x2=cox.getX2(unit);
			float y2=cox.getY2(unit);
			if(cox.getPai()==Tree.getGhost()){
				x2=Tree.getGhost().getX(unit);
				y2=Tree.getGhost().getY(unit);
			}
			if(!cox.getNodulos().isEmpty()){
				final Nodulo nod=cox.getNodulos().get(cox.getNodulos().size()-1);
				x1=nod.getX(unit);
				y1=nod.getY(unit);
			}
			final Point2D.Float ponta=getPonta(unit,x1,y1,x2,y2);
			float size=unit;
			switch(cox.getGrossura().getIndex()){
				case Grossura.THIN:				size*=0.5f;	break;
				case Grossura.MEDIUM:default:	size*=1.0f;	break;
				case Grossura.WIDE:				size*=2.0f;	break;
				case Grossura.ULTRA_WIDE:		size*=3.0f;	break;
			}
			final float x=(float)(ponta.getX())-(size/2);
			final float y=(float)(ponta.getY())-(size/2);
			imagemEdit.fill(new Ellipse2D.Float(x,y,size,size));
		}
			private Point2D.Float getPonta(int unit,float x1,float y1,float x2,float y2){
				float x=0;
				float y=0;
				if(cox.getPai()==Tree.getGhost()){
					x=x2;
					y=y2;
				}else{
					if(y1>y2){
						if(x1>x2){	//DIR-INF
							x=cox.getPai().getX(unit)+cox.getPai().getWidth(unit);
							y=y2+(((y1-y2)*(cox.getPai().getX(unit)+cox.getPai().getWidth(unit)-x2))/Math.max(1,x1-x2));
							if(y>cox.getPai().getY(unit)+cox.getPai().getHeight(unit)){	//INF-DIR
								x=x2+(((x1-x2)*(cox.getPai().getY(unit)+cox.getPai().getHeight(unit)-y2))/Math.max(1,y1-y2));
								y=cox.getPai().getY(unit)+cox.getPai().getHeight(unit);
							}
						}else{		//INF-ESQ
							x=x2-(((x2-x1)*(cox.getPai().getY(unit)+cox.getPai().getHeight(unit)-y2))/Math.max(1,y1-y2));
							y=cox.getPai().getY(unit)+cox.getPai().getHeight(unit);
							if(x<cox.getPai().getX(unit)){	//ESQ-INF
								x=cox.getPai().getX(unit);
								y=y2+(((y1-y2)*(x2-cox.getPai().getX(unit)))/Math.max(1,x2-x1));
							}
						}
					}else{
						if(x1<x2){	//ESQ-SUP
							x=cox.getPai().getX(unit);
							y=y2-(((y2-y1)*(x2-cox.getPai().getX(unit)))/Math.max(1,x2-x1));
							if(y<cox.getPai().getY(unit)){	//SUP-ESQ
								x=x2-(((x2-x1)*(y2-cox.getPai().getY(unit)))/Math.max(1,y2-y1));
								y=cox.getPai().getY(unit);
							}
						}else{
							if(y2==y1)x=cox.getPai().getX(unit)+cox.getPai().getWidth(unit)+1;else{	//SUP-DIR
								x=x2+(((x1-x2)*(y2-cox.getPai().getY(unit)))/Math.max(1,y2-y1));
								y=cox.getPai().getY(unit);
							}
							if(x>cox.getPai().getX(unit)+cox.getPai().getWidth(unit)){	//DIR-SUP
								x=cox.getPai().getX(unit)+cox.getPai().getWidth(unit);
								y=y2-(((y2-y1)*(cox.getPai().getX(unit)+cox.getPai().getWidth(unit)-x2))/Math.max(1,x1-x2));
							}
						}
					}
				}
				return new Point2D.Float(x,y);
			}
	public Cor getBrilhoCor(ConexaoST.State state){
		switch(state){
			case SELECTED:default:	return cox.getSon().getUI().getBrilhoCor(ModuloST.State.SELECTED);
			case TO_BE_CREATOR:		return cox.getSon().getUI().getBrilhoCor(ModuloST.State.TO_BE_CREATOR);
			case TO_BE_DELETED:		return cox.getSon().getUI().getBrilhoCor(ModuloST.State.TO_BE_DELETED);
		}
	}
	public Cor getLinhaCor(ConexaoST.State state){
		switch(state){
			case UNSELECTED:default:	return cox.getSon().getUI().getBordaCor(ModuloST.State.UNSELECTED);
			case SELECTED:				return cox.getSon().getUI().getBordaCor(ModuloST.State.SELECTED);
			case HIGHLIGHTED:
				if(cox.getSon().getState().is(ModuloST.State.SELECTED)){
					return cox.getSon().getUI().getBordaCor(ModuloST.State.SELECTED);
				}else return cox.getSon().getUI().getBordaCor(ModuloST.State.HIGHLIGHTED);
			case TO_BE_CREATOR:			return cox.getSon().getUI().getBordaCor(ModuloST.State.TO_BE_CREATOR);
			case TO_BE_DELETED:			return cox.getSon().getUI().getBordaCor(ModuloST.State.TO_BE_DELETED);
		}
	}
}