package tool.language_manager;
import java.util.HashMap;
public class Language{
//NOME
	private String nome="";
		public String getNome(){return nome;}
//SIGLA
	private String sigla="";
		public String getSigla(){return sigla;}
//TEXTO
	private HashMap<String,String>texto=new HashMap<>();
		public String get(String index,String padrao){
			final String linha=texto.get(index);
			if(linha==null||linha.isEmpty())return padrao;
			return linha;
		}
//MAIN
	public Language(String nome,String sigla,HashMap<String,String>texto){
		this.nome=nome;
		this.sigla=sigla;
		this.texto=texto;
	}
}