package element.tree.objeto.conexao.segmento;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Plan;
import architecture.rrf_vp.Rule;
import architecture.rrf_vp.View;
import element.tree.objeto.Objeto;

public class Segmento extends Objeto {
//RRF-VP
	@Override
	protected Rule getRule() {return null;}
	@Override
	protected Flow getFlow() {return null;}
	@Override
	protected View getView() {return null;}
	@Override
	protected Plan getPlan() {return null;}
//MAIN
	public Segmento(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}