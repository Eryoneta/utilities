package element.tree;
import java.awt.BasicStroke;
public class Borda{
//BORDAS
	public static final int SOLID=0;
	public static final int TRACED=1;
	public static final int LONG_TRACED=2;
	public static final int DOTTED=3;
	public static final int DOTTED_1_TRACED_1=4;
	public static final int DOTTED_2_TRACED_1=5;
	public static final int DOTTED_1_TRACED_2=6;
	public static final int DOTTED_2_TRACED_2=7;
	public static final int DOTTED_1_LONG_TRACED_1=8;
	public static final int DOTTED_2_LONG_TRACED_1=9;
	public static final int DOTTED_1_LONG_TRACED_2=10;
	public static final int DOTTED_2_LONG_TRACED_2=11;
	private int index=SOLID;
		public int getIndex(){return index;}
		public void setIndex(int index){this.index=index;}
//BORDA VISUAL
	public BasicStroke getVisual(){return getVisual(Tree.getBordaValue());}					//PARA MODS
	public BasicStroke getVisual(Grossura grossura){return getVisual(grossura.getValor());}	//PARA COXS
	public BasicStroke getStaticVisual(){return getVisual(6*0.3f);}							//PARA POPUP
	public BasicStroke getVisual(float width){
		float[]pattern=null;
		final float ponto=width;
		final float space=width;
		final float spaceLongo=space*3;
		final float traco=ponto*3+space;
		final float tracoLongo=traco*3;
		switch(getIndex()){
			case SOLID:default:				return new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);
			case TRACED:					pattern=new float[]{traco,spaceLongo};											break;
			case LONG_TRACED:				pattern=new float[]{tracoLongo,spaceLongo};										break;
			case DOTTED:					pattern=new float[]{ponto,space};												break;
			case DOTTED_1_TRACED_1:			pattern=new float[]{ponto,space,traco,space};									break;
			case DOTTED_2_TRACED_1:			pattern=new float[]{ponto,space,ponto,space,traco,space};						break;
			case DOTTED_1_TRACED_2:			pattern=new float[]{ponto,space,traco,space,traco,space};						break;
			case DOTTED_2_TRACED_2:			pattern=new float[]{ponto,space,ponto,space,traco,space,traco,space};			break;
			case DOTTED_1_LONG_TRACED_1:	pattern=new float[]{ponto,space,tracoLongo,space};								break;
			case DOTTED_2_LONG_TRACED_1:	pattern=new float[]{ponto,space,ponto,space,tracoLongo,space};					break;
			case DOTTED_1_LONG_TRACED_2:	pattern=new float[]{ponto,space,tracoLongo,space,tracoLongo,space};				break;
			case DOTTED_2_LONG_TRACED_2:	pattern=new float[]{ponto,space,ponto,space,tracoLongo,space,tracoLongo,space};	break;
		}
		return new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,pattern,1.0f);
	}
//NOME
	public String getNome(){
		switch(index){
			case SOLID:default:				return "Sólido";
			case TRACED:					return "Tracejado";
			case LONG_TRACED:				return "Tracejado longo";
			case DOTTED:					return "Pontilhado";
			case DOTTED_1_TRACED_1:			return "1 ponto e 1 traço";
			case DOTTED_2_TRACED_1:			return "2 pontos e 1 traço";
			case DOTTED_1_TRACED_2:			return "1 ponto e 2 traços";
			case DOTTED_2_TRACED_2:			return "2 pontos e 2 traços";
			case DOTTED_1_LONG_TRACED_1:	return "1 ponto e 1 traço longo";
			case DOTTED_2_LONG_TRACED_1:	return "2 pontos e 1 traço longo";
			case DOTTED_1_LONG_TRACED_2:	return "1 ponto e 2 traços longos";
			case DOTTED_2_LONG_TRACED_2:	return "2 pontos e 2 traços longos";
		}
	}
//MAIN
	public Borda(int index){
		setIndex(index);
	}
//FUNCS
@Override
	public int hashCode(){
		final int prime=31;
		int result=super.hashCode();
		result=prime*result+index;
		return result;
	}
@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj==null)return false;
		if(!super.equals(obj))return false;
		if(getClass()!=obj.getClass())return false;
		final Borda borda=(Borda)obj;
		if(index!=borda.index)return false;
		return true;
	}
//TODAS AS BORDAS
	public static Borda[]getAllBordas(){
		return new Borda[]{
				new Borda(SOLID),
				new Borda(TRACED),
				new Borda(LONG_TRACED),
				new Borda(DOTTED),
				new Borda(DOTTED_1_TRACED_1),
				new Borda(DOTTED_2_TRACED_1),
				new Borda(DOTTED_1_TRACED_2),
				new Borda(DOTTED_2_TRACED_2),
				new Borda(DOTTED_1_LONG_TRACED_1),
				new Borda(DOTTED_2_LONG_TRACED_1),
				new Borda(DOTTED_1_LONG_TRACED_2),
				new Borda(DOTTED_2_LONG_TRACED_2)
		};
	}
}