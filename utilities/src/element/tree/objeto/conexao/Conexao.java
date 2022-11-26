package element.tree.objeto.conexao;

import element.tree.objeto.Objeto;

public class Conexao extends Objeto<ConexaoRule,ConexaoFlow,ConexaoView,ConexaoPlan> {
//RRF-VP
	@Override
	protected ConexaoRule getRule() {return new ConexaoRule(this);}
	@Override
	protected ConexaoFlow getFlow() {return new ConexaoFlow(this);}
	@Override
	protected ConexaoView getView() {return new ConexaoView(this);}
	@Override
	protected ConexaoPlan getPlan() {return new ConexaoPlan(this);}
//MAIN
	public Conexao(Tipo tipo) {
		super(tipo);
	}
//FUNCS
	@Override
	protected void init() {}
}