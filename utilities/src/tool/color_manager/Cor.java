package tool.color_manager;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * An extension of {@link Color}<br>
 * It can define itself as being a light or a dark color
 */
public class Cor extends Color{
	private static final long serialVersionUID = -3166229931183574019L;
//VAR GLOBAIS
	public static Cor TRANSPARENT=new Cor(0,0,0,0);
	private static int DARK_DEFINITION=130;
//BRILHO
	private int brilho;
		public int getBrightValue(){return brilho;}
		private void setBright(){this.brilho=Cor.getBrightness(this);}
		public boolean isDark(){return(brilho<Cor.DARK_DEFINITION);}
//MAIN
	public Cor(int r, int g, int b) {
		super(r, g, b);
		setBright();
	}
	public Cor(int r, int g, int b, int a) {
		super(r, g, b, a);
		setBright();
	}
	public Cor(float r, float g, float b) {
		super(r, g, b);
		setBright();
	}
	public Cor(float r, float g, float b, float a) {
		super(r, g, b, a);
		setBright();
	}
	public Cor(int rgb) {
		super(rgb);
		setBright();
	}
	public Cor(String hex){
		super(
				Integer.valueOf(hex.substring(1,3),16),
				Integer.valueOf(hex.substring(3,5),16),
				Integer.valueOf(hex.substring(5,7),16));
		setBright();
	}
	public Cor(Color cor){
		super(cor.getRGB());
		setBright();
	}
//FUNCS

	/** 
	 * Measure the brightness value for a given {@link Color}
	 * @param color - The {@link Color} to be measured
	 * @return A integer value representing the brightness: <i>0-255</i>
	 */
	public static int getBrightness(Color color){
	    return (int)Math.sqrt(
	    		(color.getRed()*color.getRed()*0.241)+
	    		(color.getGreen()*color.getGreen()*0.691)+
	    		(color.getBlue()*color.getBlue()*0.068));
	}

//FUNCS: CONVERSÕES
	
	/** 
	 * Converts a {@link Color} to a hex value
	 * @param color - The {@link Color} to be converted
	 * @return A equivalent hex value: <i>#FF00FF</i>
	 */
	public static String RGBToHex(Color color){
		return String.format("#%02X%02X%02X",color.getRed(),color.getGreen(),color.getBlue());
	}
	
	/** 
	 * Converts a {@link Color} to a hex value with an alpha channel
	 * @param color - The {@link Color} to be converted
	 * @return A equivalent hex value: <i>#FF00FFAA</i>
	 */
	public static String RGBAToHexAlpha(Color color){
		return String.format("#%02X%02X%02X%02X",color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
	}

	/** 
	 * Converts a given RBG to a {@link Cor}
	 * @param rgb - The RGB color: <i>R,G,B</i>
	 * @return A new {@link Cor}
	 */
	public static Cor RGBToHex(String rgb)throws Exception{
		final String rgbDefinition="([0-9]+),([0-9]+),([0-9]+)";
		final Matcher rgbs=Pattern.compile(rgbDefinition).matcher(rgb);
		if(rgbs.find())return new Cor(
				Integer.valueOf(rgbs.group(1)),
				Integer.valueOf(rgbs.group(2)),
				Integer.valueOf(rgbs.group(3)),
				255);
		throw new Exception("The RGB(\"R,G,B\") color \""+rgb+"\" is irregular!");
	}

	/** 
	 * Converts a given hex to a {@link Cor}
	 * @param hex - The hex color:  <i>#FF00FF</i>
	 * @return A new {@link Cor}
	 */
	public static Cor hexToRGB(String hex)throws Exception{
		final String rgbDefinition="#([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})";
		final Matcher hexes=Pattern.compile(rgbDefinition).matcher(hex);
		if(hexes.find())return new Cor(
				Integer.valueOf(hexes.group(1),16),
				Integer.valueOf(hexes.group(2),16),
				Integer.valueOf(hexes.group(3),16));
		throw new Exception("The Hex(\"FF00FF\") color \""+hex+"\" is irregular!");
	}

	/** 
	 * Converts a given hex to a {@link Cor}
	 * @param hex - The hex color:  <i>#FF00FFAA</i>
	 * @return A new {@link Cor}
	 */
	public static Cor hexToRGBA(String hex)throws Exception{
		final String rgbaDefinition="#([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})";
		final Matcher hexes=Pattern.compile(rgbaDefinition).matcher(hex);
		if(hexes.find())return new Cor(
				Integer.valueOf(hexes.group(1),16),
				Integer.valueOf(hexes.group(2),16),
				Integer.valueOf(hexes.group(3),16),
				Integer.valueOf(hexes.group(4),16));
		throw new Exception("The Hex(\"FF00FFAA\") color \""+hex+"\" is irregular!");
	}
//FUNCS: TRANSFORMAÇÕES

	/** 
	 * Changes the bright of a given {@link Color} with a percentage<br>
	 * <i>r = r * bright</i><br>
	 * <i>g = g * bright</i><br>
	 * <i>b = b * bright</i>
	 * @param color - The {@link Color} to be changed
	 * @param bright - The value to change the color with: <i>0%-100% = 0.0-1.0</i>
	 * @return A new {@link Cor} with the modified brightness
	 */
	public static Cor changeBright(Color color,float bright){
		return new Cor(
				Math.max(0,Math.min(255,(int)(color.getRed()*bright))),
				Math.max(0,Math.min(255,(int)(color.getGreen()*bright))),
				Math.max(0,Math.min(255,(int)(color.getBlue()*bright))),
				color.getAlpha());
	}

	/** 
	 * Changes the bright of a given {@link Color} with a value<br>
	 * <i>r = r + value</i><br>
	 * <i>g = g + value</i><br>
	 * <i>b = b + value</i>
	 * @param color - The {@link Color} to be changed
	 * @param value - The value to change the color with: <i>0-255</i>
	 * @return A new {@link Cor} with the modified brightness
	 */
	public static Cor changeBright(Color color,int value){
		return new Cor(
				Math.max(0,Math.min(255,color.getRed()+value)),
				Math.max(0,Math.min(255,color.getGreen()+value)),
				Math.max(0,Math.min(255,color.getBlue()+value)));
	}

	/** 
	 * Changes the opacity of a given {@link Color} with a percentage<br>
	 * <i>r = r + value</i><br>
	 * <i>g = g + value</i><br>
	 * <i>b = b + value</i>
	 * @param color - The {@link Color} to be changed
	 * @param opacity - The value to change the color with: <i>0%-100% = 0.0-1.0</i>
	 * @return A new {@link Cor} with the modified opacity
	 */
	public static Cor changeOpacity(Color color,float opacity){
		return new Cor(
				color.getRed(),
				color.getGreen(),
				color.getBlue(),
				(int)opacity*255);
	}

	/** 
	 * Inverts a {@link Color}
	 * @param color - The {@link Color} to be inverted
	 * @return A new {@link Cor} with the inverted values of <b>color</b>
	 */
	public static Cor invert(Color color){
		return new Cor(
				255-color.getRed(),
				255-color.getGreen(),
				255-color.getBlue(),
				color.getAlpha());
	}
}