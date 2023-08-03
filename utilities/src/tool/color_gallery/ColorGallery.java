package tool.color_gallery;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tool.color_manager.Cor;
public class ColorGallery{
//COR
	public static class NamedColor{
	//NOME
		private String nome="";
			public String getNome(){return nome;}
	//COR
		private Color cor=new Color(0,0,0);
			public Color getCor(){return cor;}
	//MAIN
		public NamedColor(String nome,Color cor){
			this.nome=nome;
			this.cor=cor;
		}
	}
//LISTA DE CORES
	private static NamedColor[]cores=new NamedColor[]{
		new NamedColor("alicebu",hexToColor("#f0f8ff")),
		new NamedColor("antiquewhite",hexToColor("#faebd7")),
		new NamedColor("aqua",hexToColor("#00ffff")),
		new NamedColor("aquamarine",hexToColor("#7fffd4")),
		new NamedColor("azure",hexToColor("#f0ffff")),
		new NamedColor("beige",hexToColor("#f5f5dc")),
		new NamedColor("bisque",hexToColor("#ffe4c4")),
		new NamedColor("black",hexToColor("#000000")),
		new NamedColor("blanchedalmond",hexToColor("#ffebcd")),
		new NamedColor("blue",hexToColor("#0000ff")),
		new NamedColor("blueviolet",hexToColor("#8a2be2")),
		new NamedColor("brown",hexToColor("#a52a2a")),
		new NamedColor("burlywood",hexToColor("#deb887")),
		new NamedColor("cadetblue",hexToColor("#5f9ea0")),
		new NamedColor("chartreuse",hexToColor("#7fff00")),
		new NamedColor("chocolate",hexToColor("#d2691e")),
		new NamedColor("coral",hexToColor("#ff7f50")),
		new NamedColor("cornflowerblue",hexToColor("#6495ed")),
		new NamedColor("cornsilk",hexToColor("#fff8dc")),
		new NamedColor("crimson",hexToColor("#dc143c")),
		new NamedColor("cyan",hexToColor("#00ffff")),
		new NamedColor("darkblue",hexToColor("#00008b")),
		new NamedColor("darkcyan",hexToColor("#008b8b")),
		new NamedColor("darkgoldenrod",hexToColor("#b8860b")),
		new NamedColor("darkgray",hexToColor("#a9a9a9")),
		new NamedColor("darkgreen",hexToColor("#006400")),
		new NamedColor("darkkhaki",hexToColor("#bdb76b")),
		new NamedColor("darkmagenta",hexToColor("#8b008b")),
		new NamedColor("darkolivegreen",hexToColor("#556b2f")),
		new NamedColor("darkorange",hexToColor("#ff8c00")),
		new NamedColor("darkorchid",hexToColor("#9932cc")),
		new NamedColor("darkred",hexToColor("#8b0000")),
		new NamedColor("darksalmon",hexToColor("#e9967a")),
		new NamedColor("darkseagreen",hexToColor("#8fbc8f")),
		new NamedColor("darkslateblue",hexToColor("#483d8b")),
		new NamedColor("darkslategray",hexToColor("#2f4f4f")),
		new NamedColor("darkturquoise",hexToColor("#00ced1")),
		new NamedColor("darkviolet",hexToColor("#9400d3")),
		new NamedColor("deeppink",hexToColor("#ff1493")),
		new NamedColor("deepskyblue",hexToColor("#00bfff")),
		new NamedColor("dimgray",hexToColor("#696969")),
		new NamedColor("dodgerblue",hexToColor("#1e90ff")),
		new NamedColor("feldspar",hexToColor("#d19275")),
		new NamedColor("firebrick",hexToColor("#b22222")),
		new NamedColor("floralwhite",hexToColor("#fffaf0")),
		new NamedColor("forestgreen",hexToColor("#228b22")),
		new NamedColor("fuchsia",hexToColor("#ff00ff")),
		new NamedColor("gainsboro",hexToColor("#dcdcdc")),
		new NamedColor("ghostwhite",hexToColor("#f8f8ff")),
		new NamedColor("gold",hexToColor("#ffd700")),
		new NamedColor("goldenrod",hexToColor("#daa520")),
		new NamedColor("gray",hexToColor("#808080")),
		new NamedColor("green",hexToColor("#008000")),
		new NamedColor("greenyellow",hexToColor("#adff2f")),
		new NamedColor("honeydew",hexToColor("#f0fff0")),
		new NamedColor("hotpink",hexToColor("#ff69b4")),
		new NamedColor("indianred ",hexToColor("#cd5c5c")),
		new NamedColor("indigo ",hexToColor("#4b0082")),
		new NamedColor("ivory",hexToColor("#fffff0")),
		new NamedColor("khaki",hexToColor("#f0e68c")),
		new NamedColor("lavender",hexToColor("#e6e6fa")),
		new NamedColor("lavenderblush",hexToColor("#fff0f5")),
		new NamedColor("lawngreen",hexToColor("#7cfc00")),
		new NamedColor("lemonchiffon",hexToColor("#fffacd")),
		new NamedColor("lightblue",hexToColor("#add8e6")),
		new NamedColor("lightcoral",hexToColor("#f08080")),
		new NamedColor("lightcyan",hexToColor("#e0ffff")),
		new NamedColor("lightgoldenrodyellow",hexToColor("#fafad2")),
		new NamedColor("lightgrey",hexToColor("#d3d3d3")),
		new NamedColor("lightgreen",hexToColor("#90ee90")),
		new NamedColor("lightpink",hexToColor("#ffb6c1")),
		new NamedColor("lightsalmon",hexToColor("#ffa07a")),
		new NamedColor("lightseagreen",hexToColor("#20b2aa")),
		new NamedColor("lightskyblue",hexToColor("#87cefa")),
		new NamedColor("lightslateblue",hexToColor("#8470ff")),
		new NamedColor("lightslategray",hexToColor("#778899")),
		new NamedColor("lightsteelblue",hexToColor("#b0c4de")),
		new NamedColor("lightyellow",hexToColor("#ffffe0")),
		new NamedColor("lime",hexToColor("#00ff00")),
		new NamedColor("limegreen",hexToColor("#32cd32")),
		new NamedColor("linen",hexToColor("#faf0e6")),
		new NamedColor("magenta",hexToColor("#ff00ff")),
		new NamedColor("maroon",hexToColor("#800000")),
		new NamedColor("mediumaquamarine",hexToColor("#66cdaa")),
		new NamedColor("mediumblue",hexToColor("#0000cd")),
		new NamedColor("mediumorchid",hexToColor("#ba55d3")),
		new NamedColor("mediumpurple",hexToColor("#9370d8")),
		new NamedColor("mediumseagreen",hexToColor("#3cb371")),
		new NamedColor("mediumslateblue",hexToColor("#7b68ee")),
		new NamedColor("mediumspringgreen",hexToColor("#00fa9a")),
		new NamedColor("mediumturquoise",hexToColor("#48d1cc")),
		new NamedColor("mediumvioletred",hexToColor("#c71585")),
		new NamedColor("midnightblue",hexToColor("#191970")),
		new NamedColor("mintcream",hexToColor("#f5fffa")),
		new NamedColor("mistyrose",hexToColor("#ffe4e1")),
		new NamedColor("moccasin",hexToColor("#ffe4b5")),
		new NamedColor("navajowhite",hexToColor("#ffdead")),
		new NamedColor("navy",hexToColor("#000080")),
		new NamedColor("oldlace",hexToColor("#fdf5e6")),
		new NamedColor("olive",hexToColor("#808000")),
		new NamedColor("olivedrab",hexToColor("#6b8e23")),
		new NamedColor("orange",hexToColor("#ffa500")),
		new NamedColor("orangered",hexToColor("#ff4500")),
		new NamedColor("orchid",hexToColor("#da70d6")),
		new NamedColor("palegoldenrod",hexToColor("#eee8aa")),
		new NamedColor("palegreen",hexToColor("#98fb98")),
		new NamedColor("paleturquoise",hexToColor("#afeeee")),
		new NamedColor("palevioletred",hexToColor("#d87093")),
		new NamedColor("papayawhip",hexToColor("#ffefd5")),
		new NamedColor("peachpuff",hexToColor("#ffdab9")),
		new NamedColor("peru",hexToColor("#cd853f")),
		new NamedColor("pink",hexToColor("#ffc0cb")),
		new NamedColor("plum",hexToColor("#dda0dd")),
		new NamedColor("powderblue",hexToColor("#b0e0e6")),
		new NamedColor("purple",hexToColor("#800080")),
		new NamedColor("red",hexToColor("#ff0000")),
		new NamedColor("rosybrown",hexToColor("#bc8f8f")),
		new NamedColor("royalblue",hexToColor("#4169e1")),
		new NamedColor("saddlebrown",hexToColor("#8b4513")),
		new NamedColor("salmon",hexToColor("#fa8072")),
		new NamedColor("sandybrown",hexToColor("#f4a460")),
		new NamedColor("seagreen",hexToColor("#2e8b57")),
		new NamedColor("seashell",hexToColor("#fff5ee")),
		new NamedColor("sienna",hexToColor("#a0522d")),
		new NamedColor("silver",hexToColor("#c0c0c0")),
		new NamedColor("skyblue",hexToColor("#87ceeb")),
		new NamedColor("slateblue",hexToColor("#6a5acd")),
		new NamedColor("slategray",hexToColor("#708090")),
		new NamedColor("snow",hexToColor("#fffafa")),
		new NamedColor("springgreen",hexToColor("#00ff7f")),
		new NamedColor("steelblue",hexToColor("#4682b4")),
		new NamedColor("tan",hexToColor("#d2b48c")),
		new NamedColor("teal",hexToColor("#008080")),
		new NamedColor("thistle",hexToColor("#d8bfd8")),
		new NamedColor("tomato",hexToColor("#ff6347")),
		new NamedColor("turquoise",hexToColor("#40e0d0")),
		new NamedColor("violet",hexToColor("#ee82ee")),
		new NamedColor("violetred",hexToColor("#d02090")),
		new NamedColor("wheat",hexToColor("#f5deb3")),
		new NamedColor("white",hexToColor("#ffffff")),
		new NamedColor("whitesmoke",hexToColor("#f5f5f5")),
		new NamedColor("yellow",hexToColor("#ffff00")),
		new NamedColor("yellowgreen",hexToColor("#9acd32"))
	};
		private static Color hexToColor(String hex) {
			try {
				return Cor.hexToRGB(hex);
			} catch (Exception error) {
				//VALORES ESTÁTICOS, NÃO DEVE OCORRER ERROS
			}
			return Color.BLACK;
		}
//MAIN
	public ColorGallery(){}
//FUNCS
	
	/** 
	 * Returns a list of colors corresponding to a given keyword
	 * @param keyWord - Part of a color name
	 * @return A list of colors relevant for the keyword, ordered by name length
	 */
	public static NamedColor[] searchForAll(String keyWord){
		final List<NamedColor>result=new ArrayList<>();
		for(NamedColor cor:cores)if(cor.getNome().contains(keyWord))result.add(cor);
		Collections.sort(result, (s1,s2)-> s1.getNome().length() - s2.getNome().length());	//ORDENA POR TAMANHO DE NOME
		return result.toArray(new NamedColor[0]);
	}

	/** 
	 * Returns a single color corresponding to a given keyword
	 * @param keyWord - Part of a color name
	 * @return A color relevant for the keyword, with the smallest name
	 */
	public static NamedColor searchForOne(String keyWord){
		NamedColor result=null;
		for(NamedColor cor:cores) {
			if(cor.getNome().contains(keyWord)) {
				if(result!=null) {
					result=(result.getNome().length()<=cor.getNome().length()?result:cor);
				} else result=cor;
			}
		}
		return result;
	}
}