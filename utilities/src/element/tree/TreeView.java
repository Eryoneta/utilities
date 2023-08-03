package element.tree;

import java.awt.Graphics2D;
import java.awt.Point;

import architecture.rrf_vp.view.ViewJoint;

public class TreeView implements ViewJoint<TreeView,Tree> {
//ROOT
	private Tree root;
		@Override public Tree getRoot() {return root;}
//VIEW
	private int viewX=0;
		public int getViewX(){return viewX;}
	private int viewY=0;
		public int getViewY(){return viewY;}
	public Point getViewLocation(){return new Point(viewX,viewY);}
	public void setViewLocation(int x,int y){viewX=x;viewY=y;}
//UNIT
	private byte ViewUnit=8;
	public byte getViewUnit() {return ViewUnit;}
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
//MAIN
	public TreeView(Tree root) {
		this.root=root;
	}
//FUNCS
	@Override public void init() {}
	protected void draw(Graphics2D imagemEdit) {
		
	}
	protected void draw() {
		
	}
}