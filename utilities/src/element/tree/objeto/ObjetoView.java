package element.tree.objeto;

import java.awt.Point;

import architecture.rrf_vp.View;

public abstract class ObjetoView<R> extends View<R> {
//INDEX
	protected int index=0;
		public int getIndex(){return index;}
		public void setIndex(int index){this.index=index;}
//MAIN
	public ObjetoView(R root) {
		super(root);
	}
//FUNCS
	public abstract boolean contains(Point mouse);
	@Override
	protected abstract void init();
}