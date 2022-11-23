package element.tree.popup.grossura;
import element.tree.propriedades.Grossura;
import element.tree.objeto.conexao.Conexao;
public interface GrossuraPickListener{
	public void grossuraModified(Conexao[]coxs,Grossura[]oldGrossuras,Grossura newGrossura);
}