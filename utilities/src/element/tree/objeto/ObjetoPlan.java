package element.tree.objeto;

import architecture.rrf_vp.Plan;

public abstract class ObjetoPlan<R> extends Plan<R> {
//TIPO
	protected Objeto.Tipo tipo;
		public Objeto.Tipo getTipo(){return tipo;}
//MAIN
	public ObjetoPlan(R root) {
		super(root);
	}
//FUNCS
	@Override
	protected abstract void init();
	@Override
	public int hashCode(){
		final Objeto<?,?,?,?>root=(Objeto<?,?,?,?>)this.root;
		final ObjetoView<?>view=(ObjetoView<?>)root.view;
		final int prime=31;
		int result=1;
		result=prime*result+((tipo==null)?0:tipo.hashCode());
		result=prime*result+view.index;
		return result;
	}
	@Override
	public boolean equals(Object objeto){
		if(this==objeto)return true;
		if(objeto==null)return false;
		if(getClass()!=objeto.getClass())return false;
		final Objeto<?,?,?,?>root=(Objeto<?,?,?,?>)objeto;
		final ObjetoPlan<?>plan=(ObjetoPlan<?>)root.plan;
		final ObjetoView<?>view=(ObjetoView<?>)root.view;
		if(tipo!=plan.tipo)return false;
		final Objeto<?,?,?,?>thisRoot=(Objeto<?,?,?,?>)this.root;
		final ObjetoView<?>thisView=(ObjetoView<?>)thisRoot.view;
		if(thisView.index!=view.index)return false;
		return true;
	}
}