package element.tree.objeto.propriedade.grossura;

import element.tree.objeto.conexao.Conexao;

public interface GrossuraListener{
	public void grossuraModified(Conexao[]coxs,Grossura[]oldGrossuras,Grossura newGrossura);
}