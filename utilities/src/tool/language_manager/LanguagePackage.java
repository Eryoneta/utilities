package tool.language_manager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LanguagePackage{
//LANGS
	private HashMap<String,Language>langs=new HashMap<>();
	
		/** 
		 * Gets a text as defined by the locale
		 * @param key - The key to retrieve the text
		 * @param padrao - The default text to display in case of text-not-found
		 * @return The text according the the defined locale
		 */
		public String get(String key,String padrao){
			if(langs.isEmpty())return padrao;
			final Language lang=langs.get(currentLang);
			if(lang==null)return padrao;
			return lang.get(key,padrao);
		}
	
		/** 
		 * Returns an array listing all the locals
		 * @return An array with the name of all the locals
		 */
		public String[]getLanguages(){
			final List<String>allLangs=new ArrayList<>();
			for(Language lang:langs.values())allLangs.add(lang.getNome());
			return allLangs.toArray(new String[0]);
		}
//INDEX
	private String currentLang="";

		/** 
		 * Returns the current {@link Language} being used to define the locale text
		 * @return A {@link Language} reference
		 */
		public Language getLanguage(){
			if(langs.isEmpty())return null;
			return langs.get(currentLang);
		}

		/** 
		 * Sets a locale
		 * @param language - A symbol(Example: "EN-US") or a name of the local. 
		 */
		public void setLanguage(String language){
			language=language.toUpperCase();
			for(Language lang:langs.values()) {
				final String nome=lang.getNome().toUpperCase();
				final String sigla=lang.getSigla().toUpperCase();
				if(nome.equals(language)||sigla.equals(language)){
					currentLang=sigla;
					return;
				}
			}
		}
//MAIN
	public LanguagePackage(){}
//FUNCS
	
	/** 
	 * Loads the package with a language contained in a folder
	 * @param folder - A path to a folder
	 * @param languageSymbol - A symbol of the local. Example: "EN-US"
	 * @return If the operation was a success or not
	 */
	public boolean load(File folder, String languageSymbol){	//ADICIONA APENAS O IDIOMA-FILTRO
		if(folder==null||!folder.exists())return false;
		if(languageSymbol==null)languageSymbol="";
		languageSymbol=languageSymbol.toUpperCase();
		final String languageFile=languageSymbol+".lang";
	//SE É ARQUIVO
		if(folder.isFile()) {
			final File file=folder;
			if(!languageSymbol.isEmpty()) {
				if(!file.getName().toUpperCase().equals(languageFile))return false;
			}
			final HashMap<String,String>texto=getContent(file);
			if(texto==null)return false;
			Locale locale=new Locale(languageSymbol);
			langs.put(languageSymbol,new Language(locale.getDisplayName(),languageSymbol,texto));
			return true;
		}
	//SE É PASTA
		if(folder.isDirectory()) {
			boolean hadFound=false;
			for(File file:folder.listFiles()){
				final boolean found=load(file,languageSymbol);
				if(found) {
					hadFound=true;
					if(!languageSymbol.isEmpty()) return true;
				}
			}
			return hadFound;
		}
		return false;
	}
	
	/** 
	 * Loads the package with all languages contained in a folder
	 * @param folder - A path to a folder
	 * @return If the operation was a success or not
	 */
	public boolean load(File folder){	//ADICIONA TODOS OS IDIOMAS ENCONTRADOS
		return load(folder,"");
	}
	
		private HashMap<String,String>getContent(File file){
			if(!file.getName().endsWith(".lang"))return null;
			try{
				final HashMap<String,String>lista=new HashMap<>();
				for(String linha:Files.readAllLines(file.toPath(),StandardCharsets.UTF_8)){
					final String lineDefinition="^[\t ]*([a-zA-Z0-9_]+)[\t ]*\"([^\"]*)\"";	//[TAB|SPACE] [ID_DO_TEXTO] [TAB|SPACE] "TEXTO"
					final Matcher match=Pattern.compile(lineDefinition).matcher(linha);
					if(match.find()){
						final String id=match.group(1);
						final String texto=match.group(2);
						lista.put(id,texto);
					}
				}
				if(lista.isEmpty())return null;
				return lista;
			}catch(IOException error){
				return null;
			}
		}
}