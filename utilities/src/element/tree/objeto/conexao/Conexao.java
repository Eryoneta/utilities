package element.tree.objeto.conexao;

import architecture.rrf_vp.Flow;
import architecture.rrf_vp.Plan;
import architecture.rrf_vp.Rule;
import architecture.rrf_vp.View;
import element.tree.objeto.Objeto;

public class Conexao extends Objeto {
//RRF-VP
	@Override
	protected Rule getRule() {return new ConexaoRule(this);}
	@Override
	protected Flow getFlow() {return new ConexaoFlow(this);}
	@Override
	protected View getView() {return new ConexaoView(this);}
	@Override
	protected Plan getPlan() {return new ConexaoPlan(this);}
//MAIN
	public Conexao(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}