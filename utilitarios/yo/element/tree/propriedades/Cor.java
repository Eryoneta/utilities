package element.tree.propriedades;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("serial")
public class Cor extends Color{
//CORES
	public static Cor TRANSPARENTE=new Cor(0,0,0,0);
//BRILHO
	private int brilho;
		public int getBrilho(){return brilho;}
		private void setBrilho(Cor cor){this.brilho=getBrightness(cor);}
		public boolean isDark(){return(brilho<130);}
//MAIN
	public Cor(int r,int g,int b){super(r,g,b);setBrilho(this);}
	public Cor(int r,int g,int b,int a){super(r,g,b,a);setBrilho(this);}
	public Cor(float r,float g,float b,float a){super(r,g,b,a);setBrilho(this);}
	public Cor(String hex){super(Integer.valueOf(hex.substring(1,3),16),Integer.valueOf(hex.substring(3,5),16),Integer.valueOf(hex.substring(5,7),16));}
	public Cor(Color cor){super(cor.getRGB());setBrilho(this);}
//FUNCS
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+brilho;
		return result;
	}
@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj==null)return false;
		if(!super.equals(obj))return false;
		if(getClass()!=obj.getClass())return false;
		final Cor cor=(Cor)obj;
		if(brilho!=cor.brilho)return false;
		return true;
	}
//CONVERSÕES
	public String toHexOpaque(){
		return String.format("%02X%02X%02X",getRed(),getGreen(),getBlue());
	}
	public String toHexAlpha(){
		return String.format("%02X%02X%02X%02X",getRed(),getGreen(),getBlue(),getAlpha());
	}
	public static Cor RGBToHex(String rgb){		//USADO NO UPDATE DE .MINDS ANTIGOS
		final Matcher rgbs=Pattern.compile("([0-9]+),([0-9]+),([0-9]+)").matcher(rgb);
		if(rgbs.find())return new Cor(Integer.valueOf(rgbs.group(1)),Integer.valueOf(rgbs.group(2)),Integer.valueOf(rgbs.group(3)),255);
		return null;
	}
	public static Cor HexToRGBA(String hex)throws Exception{
		final Matcher hexes=Pattern.compile("([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{0,2})").matcher(hex);
		if(hexes.find())return new Cor(
				Integer.valueOf(hexes.group(1),16),
				Integer.valueOf(hexes.group(2),16),
				Integer.valueOf(hexes.group(3),16),
				Integer.valueOf(hexes.group(4).isEmpty()?"FF":hexes.group(4),16)
		);
		throw new Exception("Hex color is irregular!");
	}
	public static int getBrightness(Color cor){
	    return (int)Math.sqrt((cor.getRed()*cor.getRed()*0.241)+(cor.getGreen()*cor.getGreen()*0.691)+(cor.getBlue()*cor.getBlue()*0.068));
	}
//TRANSFORMAÇÕES
	public static Cor getChanged(Color cor,float brilho){
		return new Cor(Math.min(255,(int)(cor.getRed()*brilho)),Math.min(255,(int)(cor.getGreen()*brilho)),Math.min(255,(int)(cor.getBlue()*brilho)),cor.getAlpha());
	}
	public static Cor getChanged(Color cor,int valor){
		return new Cor(Math.max(0,Math.min(255,cor.getRed()+valor)),Math.max(0,Math.min(255,cor.getGreen()+valor)),Math.max(0,Math.min(255,cor.getBlue()+valor)));
	}
	public static Cor getTransparent(Color cor,float opacity){
		return new Cor((float)cor.getRed()/255,(float)cor.getGreen()/255,(float)cor.getBlue()/255,opacity);
	}
	public static Cor getInverted(Color cor){
		return new Cor(255-cor.getRed(),255-cor.getGreen(),255-cor.getBlue(),cor.getAlpha());
	}
	public static Cor getContraste(Cor corFundo,Cor cor,boolean esclarecer){
		final int diff=30;
		if(cor.getRed()<corFundo.getRed()+diff&&cor.getRed()>corFundo.getRed()-diff){
			if(cor.getGreen()<corFundo.getGreen()+diff&&cor.getGreen()>corFundo.getGreen()-diff){
				if(cor.getBlue()<corFundo.getBlue()+diff&&cor.getBlue()>corFundo.getBlue()-diff){
					return Cor.getChanged(cor,esclarecer?30:-30);
				}else return cor;
			}else return cor;
		}else return cor;
	}
}