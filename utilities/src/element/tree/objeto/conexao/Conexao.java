package element.tree.objeto.conexao;

import java.awt.Point;

import architecture.rrf_vp.root.Root;
import architecture.rrf_vp.root.RootJoint;
import element.tree.objeto.Objeto;

public class Conexao extends Objeto implements RootJoint<Conexao,ConexaoRule,ConexaoFlow,ConexaoView,ConexaoPlan> {
//RULE
	private ConexaoRule rule=new ConexaoRule(this);
		@Override public ConexaoRule getRule() {return rule;}
//FLOW
	private ConexaoFlow flow=new ConexaoFlow(this);
		@Override public ConexaoFlow getFlow() {return flow;}
//VIEW
	private ConexaoView view=new ConexaoView(this);
		@Override public ConexaoView getView() {return view;}
//PLAN
	private ConexaoPlan plan=new ConexaoPlan(this);
		@Override public ConexaoPlan getPlan() {return plan;}
//MAIN
	public Conexao(Tipo tipo) {
		super(tipo);
		Root.init(this);
	}
//FUNCS
	@Override public boolean contains(Point mouse) {
		return false;
	}
}