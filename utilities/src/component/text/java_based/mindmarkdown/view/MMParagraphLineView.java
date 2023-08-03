package component.text.java_based.mindmarkdown.view;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;

import component.text.java_based.mindmarkdown.attribute.MMLineAtributo;
public class MMParagraphLineView{
//VIEW_PAI
	private MMParagraphView paragraphView;
//LINE
	private class PencilScratchLine{
	//VAR GLOBAIS
		final static double Y_VARIATION=0.10;	//10%
	//BOUNDS
		private int x=0;
		private int y=0;
		private int width=0;
		private int height=0;
	//POINTS
		private int[]lineXs;
		private int[]lineYs;
	//MAIN
		public PencilScratchLine(int x,int y,int width,int height){
			this.x=x;
			this.y=y;
			this.width=width;
			this.height=height;
			generateLine(x,y,width,height);
		}
	//FUNCS
		public void generateLine(int x,int y,int width,int height){
			final int period=height;
			final int totalPoints=1+((width-period)/period)+1;	//START_POINT + TOTAL_POINTS + END_POINT
			final double xVariation=period;
			final double yVariation=height*Y_VARIATION;
			lineXs=new int[totalPoints];
			lineYs=new int[totalPoints];
			lineXs[0]=x;
			lineYs[0]=(int)(y+(Math.random()*yVariation));
			for(int i=1;i<totalPoints-1;i++){
				lineXs[i]=(int)(x+(period*i)+(xVariation*Math.random()));
				lineYs[i]=(int)(y+(yVariation*Math.random()));
			}
			lineXs[lineXs.length-1]=x+width;
			lineYs[lineYs.length-1]=(int)(y+(Math.random()*yVariation));
		}
		public boolean isSameArea(int x,int y,int width,int height){
			if(this.x!=x)return false;
			if(this.y!=y)return false;
			if(this.width!=width)return false;
			if(this.height!=height)return false;
			return true;
		}
	//DRAW
		public void draw(Graphics g){
			final int totalPoints=lineXs.length;
			g.drawPolyline(lineXs,lineYs,totalPoints);
		}
	}
	private PencilScratchLine pencilScratch=null;
//MAIN
	public MMParagraphLineView(MMParagraphView paragraphView){
		this.paragraphView=paragraphView;
	}
//DRAW
	public void draw(Graphics g,Shape s,MMLineAtributo.Line line){
		final Rectangle area=s.getBounds();
		//LINHA
		g.setColor(MMLineAtributo.COLOR);
		final Graphics2D g2D=(Graphics2D)g;
		final Stroke oldStroke=g2D.getStroke();
		g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		final int y=area.y;
		final int height=area.height;
		final int yMiddle=y+(height/2);
		int x=area.x;
		int width=area.width;
		double porcentagem=0;
		switch(line.getSize()){
			case SIZE_1:	porcentagem=0.10;	break;
			case SIZE_2:	porcentagem=0.20;	break;
			case SIZE_3:	porcentagem=0.30;	break;
			case SIZE_4:	porcentagem=0.40;	break;
			case SIZE_5:	porcentagem=0.50;	break;
			case SIZE_6:	porcentagem=0.60;	break;
			case SIZE_7:	porcentagem=0.70;	break;
			case SIZE_8:	porcentagem=0.80;	break;
			case SIZE_9:	porcentagem=0.90;	break;
			case SIZE_0:	porcentagem=1.00;	break;
		}
		width=(int)(area.width*porcentagem);
		x=(int)(area.x+((area.width-width)/2));
		final int halfWidth=width/2;
		switch(line.getType()){
			case SOLID:
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
				g.drawLine(x,yMiddle,x+width,yMiddle);
			break;
			case SOLID_THIN:
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH/2));
				g.drawLine(x,yMiddle,x+width,yMiddle);
			break;
			case TRACED:
				final float[]tracePattern=new float[]{MMLineAtributo.LINE_WIDTH*3,MMLineAtributo.LINE_WIDTH};
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,tracePattern,1.0f));
				g.drawLine(x,yMiddle,x+width,yMiddle);
			break;
			case DOTS:
				final float[]dotPattern=new float[]{MMLineAtributo.LINE_WIDTH,MMLineAtributo.LINE_WIDTH};
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,dotPattern,1.0f));
				g.drawLine(x,yMiddle,x+width,yMiddle);
			break;
			case PENCIL_SCRATCH:
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
				if(pencilScratch==null||!pencilScratch.isSameArea(x,yMiddle,width,height)){
					pencilScratch=new PencilScratchLine(x,yMiddle,width,height);
				}
				pencilScratch.draw(g);
			break;
			case ARROWS:
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
				final int arrowHeight=height;
				final int arrowWidth=arrowHeight*2;
				final int arrowHook=arrowHeight/2;
				if(arrowWidth>halfWidth){
					x-=arrowWidth-halfWidth;
					width=arrowWidth*2;
				}
				g.drawLine(x+arrowWidth,yMiddle,x+width-arrowWidth,yMiddle);
				g2D.setStroke(new BasicStroke(1));
				g.fillPolygon(new int[]{x,x+arrowWidth,x+arrowWidth-arrowHook,x+arrowWidth},
						new int[]{yMiddle,y,yMiddle,y+height},4);
				g.fillPolygon(new int[]{x+width,x+width-arrowWidth,x+width-arrowWidth+arrowHook,x+width-arrowWidth},
						new int[]{yMiddle,y,yMiddle,y+height},4);
			break;
			case MIDDLE_DOT:
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
				final int dotSize=height;
				if(dotSize<halfWidth)g.drawLine(x,yMiddle,x+halfWidth-dotSize,yMiddle);
				g.fillOval(x+halfWidth-(dotSize/2),y,dotSize,dotSize);
				if(dotSize<halfWidth)g.drawLine(x+halfWidth+dotSize,yMiddle,x+width,yMiddle);
			break;
			case THREE_DOTS:
				g2D.setStroke(new BasicStroke(1));
				final int smallDotSize=height/2;
				final int smallDotY=y+(smallDotSize/2);
				g.fillOval(x+halfWidth-(smallDotSize*2),smallDotY,smallDotSize,smallDotSize);
				g.fillOval(x+halfWidth-(smallDotSize/2),smallDotY,smallDotSize,smallDotSize);
				g.fillOval(x+halfWidth+smallDotSize,smallDotY,smallDotSize,smallDotSize);
			break;
			case DIAMOND:
				g2D.setStroke(new BasicStroke(1));
				g.fillPolygon(new int[]{x,x+halfWidth,x+width,x+halfWidth},
						new int[]{yMiddle,y,yMiddle,y+height},4);
			break;
			case GRADIENT:
				g2D.setStroke(new BasicStroke(MMLineAtributo.LINE_WIDTH));
				g2D.setPaint(new GradientPaint(
						x,yMiddle,Color.WHITE,
						x+halfWidth,yMiddle,
						MMLineAtributo.COLOR,true));
				g.drawLine(x,yMiddle,x+width,yMiddle);
			break;
		}
		g2D.setStroke(oldStroke);
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	//TEXTO
		paragraphView.paintContent(g,s);
	}
}