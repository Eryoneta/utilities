package element.tree.objeto.modulo;

import architecture.rrf_vp.plan.PlanJoint;

public class ModuloPlan implements PlanJoint<ModuloPlan,Modulo> {
//ROOT
	private Modulo root;
		@Override public Modulo getRoot() {return root;}
//TITULO
	protected String[]titulo;
		public String getTitle(){return String.join("\n",titulo);}
		public void justSetTitle(String titulo){	//EVITA BOUNDS_LISTENER
			this.titulo=titulo.split("\n",-1);
			justSetSize();
		}
		public void setTitle(String titulo){
			this.titulo=titulo.split("\n",-1);
			setSize();
		}
//TEXTO
	private List<String>texto=new ArrayList<String>();
		public List<String>getTexto(){return texto;}
		public String getText(){return String.join("\n",texto);}
		public void setTexto(List<String>texto){this.texto=texto;}
		public void setTexto(String texto){setTexto(Arrays.asList(texto.split("\n",-1)));}
//POSIÇÃO DO CURSOR
	private int caret=0;
	public int getCaret(){return caret;}
		public void setCaret(int caret){this.caret=caret;}
//CONEXÕES
	private List<Conexao>conexoes=new ArrayList<Conexao>();
		public List<Conexao>getConexoes(){return conexoes;}
		public boolean addConexao(Conexao cox){
			conexoes.add(cox);
			return true;
		}
		public boolean delConexao(Conexao cox){
			return conexoes.remove(cox);
		}
//MAIN
	public ModuloPlan(Modulo root) {
		this.root=root;
	}
//FUNCS
	@Override public void init() {}
}