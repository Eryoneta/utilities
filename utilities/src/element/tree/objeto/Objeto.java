package element.tree.objeto;

import architecture.rrf_vp.Root;

public abstract class Objeto<R,F,V,P> extends Root<R,F,V,P>{
//TIPO
	public enum Tipo{
		MODULO,
		CONEXAO,
		NODULO,
		SEGMENTO;
		public boolean is(Objeto.Tipo... tipos){
			for(Objeto.Tipo tipo:tipos)if(this.equals(tipo))return true;
			return false;
		}
	}
//MAIN
	public Objeto(Objeto.Tipo tipo){
		((ObjetoPlan<?>)plan).tipo=tipo;
	}
//FUNCS
	@Override
	protected abstract void init();
}