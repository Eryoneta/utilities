package element.tree.propriedades;
import java.awt.BasicStroke;
import element.tree.TreeUI;
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
			case SOLID:default:				return TreeUI.getLang().get("T_Pop_B_S","Solid");
			case TRACED:					return TreeUI.getLang().get("T_Pop_B_T","Dash");
			case LONG_TRACED:				return TreeUI.getLang().get("T_Pop_B_Tl","Long Dash");
			case DOTTED:					return TreeUI.getLang().get("T_Pop_B_P","Dot");
			case DOTTED_1_TRACED_1:			return TreeUI.getLang().get("T_Pop_B_PT","1 dot e 1 dash");
			case DOTTED_2_TRACED_1:			return TreeUI.getLang().get("T_Pop_B_PPT","2 dots e 1 dash");
			case DOTTED_1_TRACED_2:			return TreeUI.getLang().get("T_Pop_B_PTT","1 dot e 2 dashes");
			case DOTTED_2_TRACED_2:			return TreeUI.getLang().get("T_Pop_B_PPTT","2 dots e 2 dashes");
			case DOTTED_1_LONG_TRACED_1:	return TreeUI.getLang().get("T_Pop_B_PTl","1 dot e 1 long dash");
			case DOTTED_2_LONG_TRACED_1:	return TreeUI.getLang().get("T_Pop_B_PPTl","2 dots e 1 long dash");
			case DOTTED_1_LONG_TRACED_2:	return TreeUI.getLang().get("T_Pop_B_PTlTl","1 dot e 2 long dashes");
			case DOTTED_2_LONG_TRACED_2:	return TreeUI.getLang().get("T_Pop_B_PPTlTl","2 dots e 2 long dashes");
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