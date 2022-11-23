package tool.language_manager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LanguagePackage{
//LANG
	private List<Language>langs=new ArrayList<>();
		public String get(String index,String padrao){
			if(langs.isEmpty())return padrao;
			final Language texto=langs.get(langIndex);
			if(texto==null)return padrao;
			return texto.get(index,padrao);
		}
		public Language getLanguage(){
			if(langs.isEmpty())return null;
			return langs.get(langIndex);
		}
//INDEX
	private int langIndex=0;
		public void setLanguage(String lang){
			lang=lang.toUpperCase();
			for(int i=0,size=langs.size();i<size;i++){
				final Language language=langs.get(i);
				final String nome=language.getNome().toUpperCase();
				final String sigla=language.getSigla().toUpperCase();
				if(lang.equals(nome)||lang.equals(sigla)){
					langIndex=i;
					return;
				}
			}
		}
//MAIN
	public LanguagePackage(){}
//FUNCS
	public boolean add(File link,String idiomaFiltro,String prefixFiltro){	//ADICIONA APENAS O IDIOMA-FILTRO
		idiomaFiltro=idiomaFiltro.toUpperCase()+".txt";
		if(!link.exists())return false;
		if(link.isFile()){				//ARQUIVO .TXT
			if(link.getName().toUpperCase().equals(idiomaFiltro))return false;
			final HashMap<String,String>texto=getLang(link,prefixFiltro);
			if(texto==null)return false;
			final String nome=texto.get("0");
			final String sigla=texto.get("1");
			langs.add(new Language(nome,sigla,texto));
			return true;
		}else if(link.isDirectory()){	//PASTA COM .TXTs
			boolean hasLang=false;
			for(File langLink:link.listFiles()){
				final boolean addedLang=add(langLink,idiomaFiltro,prefixFiltro);
				if(addedLang)hasLang=true;
			}
			return hasLang;
		}
		return false;
	}
	public String[]add(File link,String prefixFiltro){	//ADICIONA TODOS OS IDIOMAS ENCONTRADOS
		if(!link.exists())return null;
		if(link.isFile()){				//ARQUIVO .TXT
			final HashMap<String,String>texto=getLang(link,prefixFiltro);
			if(texto==null)return null;
			langs.add(new Language(texto.get("0"),texto.get("1"),texto));
			return new String[]{texto.get("0")};
		}else if(link.isDirectory()){	//PASTA COM .TXTs
			final List<String>listaLangs=new ArrayList<>();
			for(File langLink:link.listFiles()){
				final String lang=add(langLink,prefixFiltro)[0];
				listaLangs.add(lang);
			}
			if(listaLangs.isEmpty())return null;
			return listaLangs.toArray(new String[0]);
		}
		return null;
	}
		private HashMap<String,String>getLang(File link,String prefixFiltro){
			if(!link.getName().endsWith(".txt"))return null;
			try{
				final HashMap<String,String>lista=new HashMap<>();
				for(String linha:Files.readAllLines(link.toPath(),StandardCharsets.UTF_8)){
					final Matcher match=Pattern.compile("^[\t ]{0,}([a-zA-Z0-9_]+)[\t ]{0,}\"([^\"]*)\"").matcher(linha);
							//[TAB|SPACE] [ID_DO_TEXTO_17] [TAB|SPACE] "TEXTO"
					if(match.find()){
						final String nome=match.group(1);
						final String sigla=match.group(2);
						if(nome.startsWith(prefixFiltro)||nome.startsWith("0")||nome.startsWith("1")){
							lista.put(nome,sigla);		//ACEITA APENAS OS COM PREFIXO, OU O INDEX 0 OU 1
						}
					}
				}
				if(lista.isEmpty())return null;
				return lista;
			}catch(IOException erro){
				return null;
			}
		}
}