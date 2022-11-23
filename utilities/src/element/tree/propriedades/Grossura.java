package element.tree.propriedades;
import element.tree.main.TreeUI;
public class Grossura{
//GROSSURAS
	public static final int MEDIUM=0;
	public static final int THIN=1;
	public static final int WIDE=2;
	public static final int ULTRA_WIDE=3;
	private int index=MEDIUM;
		public int getIndex(){return index;}
		public void setIndex(int index){this.index=index;}
//VALOR
	public float getStaticValor(){return getValor(6*0.3f);}			//PARA POPUP
	public float getValor(float borda){
		switch(getIndex()){
			case MEDIUM:default:	return borda;
			case THIN:				return borda/3;
			case WIDE:				return borda*3;
			case ULTRA_WIDE:		return borda*3*2;
		}
	}
//NOME
	public String getNome(){
		switch(index){
			case MEDIUM:default:	return TreeUI.getLang().get("T_Pop_G_M","Medium");
			case THIN:				return TreeUI.getLang().get("T_Pop_G_F","Thin");
			case WIDE:				return TreeUI.getLang().get("T_Pop_G_G","Thin");
			case ULTRA_WIDE:		return TreeUI.getLang().get("T_Pop_G_EG","Extra Wide");
		}
	}
//MAIN
	public Grossura(int index){
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
		final Grossura grossura=(Grossura)obj;
		if(index!=grossura.index)return false;
		return true;
	}
//TODAS AS GROSSURAS
	public static Grossura[]getAllGrossuras(){
		return new Grossura[]{
				new Grossura(THIN),
				new Grossura(MEDIUM),
				new Grossura(WIDE),
				new Grossura(ULTRA_WIDE)
		};
	}
}