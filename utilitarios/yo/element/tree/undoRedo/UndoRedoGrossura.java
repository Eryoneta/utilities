package element.tree.undoRedo;
import element.tree.Grossura;
import element.tree.objeto.conexao.Conexao;
public class UndoRedoGrossura extends UndoRedoNodo{
//GROSSURA
	private Grossura[]grossuras;
		public Grossura getGrossura(int index){return grossuras[index];}
//CONEXÕES
	private Conexao[]coxs;
		public Conexao[]getConexoes(){return coxs;}
		public Conexao getConexao(int index){return coxs[index];}
		public int getConexoesCount(){return coxs.length;}
//MAIN
	public UndoRedoGrossura(Conexao[]coxs,Grossura[]grossuras){
		super(UndoRedoNodo.Tipo.GROSSURA);
		this.grossuras=grossuras;
		this.coxs=coxs;
	}
//INVERT
	public UndoRedoGrossura invert(Grossura[]grossuras){
		this.grossuras=grossuras;
		return this;
	}
}