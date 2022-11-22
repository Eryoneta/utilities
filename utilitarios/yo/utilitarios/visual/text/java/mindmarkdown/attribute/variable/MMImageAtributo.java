package utilitarios.visual.text.java.mindmarkdown.attribute.variable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.StyleConstants;
import utilitarios.visual.text.java.mindmarkdown.MindMarkDocumento;
import utilitarios.visual.text.java.mindmarkdown.attribute.MindMarkAtributo;
@SuppressWarnings("serial")
public class MMImageAtributo extends MindMarkVariableAtributo{
//VAR GLOBAIS
	public final static String IMAGE="image";
//SYMBOLS
	private final static String WIDTH_VAR="w";
	private final static String HEIGHT_VAR="h";
//TAGS
	public final static String TAG=Pattern.quote("!");
	private final static String WIDTH_VAR_TAG=Pattern.quote(WIDTH_VAR);
	private final static String HEIGHT_VAR_TAG=Pattern.quote(HEIGHT_VAR);
//DEFINITION
	private final String variablesDefinition=(
		//(?:(?<sizeTag>w|h)(?<sizeValue>[0-9]{1,3}))?
			pseudoGroup(
					namedGroup("sizeTag",oneOrOther(WIDTH_VAR_TAG,HEIGHT_VAR_TAG))+
					namedGroup("sizeValue",
							characters(range(0,9))+occursBetween(1,3)
					)
			)+zeroOrOne()
	);
//IMAGE
	public static class Image{
	//WIDTH
		private int width=0;
			public int getWidth(){return width;}
	//HEIGHT
		private int height=0;
			public int getHeight(){return height;}
	//LEGENDA
		private String legenda="";
			public String getLegenda(){return legenda;}
	//LINK
		private String link="";
			public String getLink(){return link;}
	//MAIN
		public Image(String legenda,String link){
			this.legenda=legenda;
			this.link=link;
		}
	}
//MAIN
	public MMImageAtributo(){
		//(?<=^|\n)...(?=\n|$)
		buildDefinitionWithURLAndVars(precededBy(oneOrOther(startOfText(),lineBreak())),TAG,true,true,true,followedBy(oneOrOther(lineBreak(),endOfText())));
	}
//FUNCS
	private MindMarkDocumento doc;
	private String url;
@Override
	public void applyStyle(MindMarkDocumento doc){
		this.doc=doc;
		super.applyStyle(doc);
	}
@Override
	protected void setURLAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String url){
		this.url=url;
		final  MindMarkAtributo imagemAtributo=new MindMarkAtributo();
		StyleConstants.setAlignment(imagemAtributo,StyleConstants.ALIGN_CENTER);
		final Image imagem=new Image(texto,url);
		imagemAtributo.addAttribute(IMAGE,imagem);
		StyleConstants.setSpaceAbove(imagemAtributo,Math.max(imagem.height,10));
		doc.setParagraphAttributes(indexTexto,0,imagemAtributo,false);
	}
@Override
	protected void setVarsAtributos(MindMarkAtributo atributo,int indexTexto,String texto,String vars){
		final  MindMarkAtributo imagemAtributo=new MindMarkAtributo();
		StyleConstants.setAlignment(imagemAtributo,StyleConstants.ALIGN_CENTER);
		final Image imagem=new Image(texto,url);
		if(vars!=null){
			final Matcher match=Pattern.compile(variablesDefinition).matcher(vars);
			while(match.find()){
				setImagemAtributo(imagem,match.group("sizeTag"),match.group("sizeValue"));
			}
		}
		imagemAtributo.addAttribute(IMAGE,imagem);
		StyleConstants.setSpaceAbove(imagemAtributo,Math.max(imagem.height,10));
		doc.setParagraphAttributes(indexTexto,0,imagemAtributo,false);
	}
		private void setImagemAtributo(Image imagem,String var,String valor){
			if(var==null||valor==null)return;
			switch(var){
				case WIDTH_VAR:
					imagem.width=Integer.parseInt(valor);
					return;
				case HEIGHT_VAR:
					imagem.height=Integer.parseInt(valor);
					return;
			}
		}
}