package utilitarios.visual.text.java.mindmarkdown.attribute;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
@SuppressWarnings("serial")
public class MindMarkSizeModifierAtributo extends SimpleAttributeSet{
//FONTE_SIZE
	public static int defaultFontSize=8;
		public static void setDefaultFontSize(int size){defaultFontSize=size;}
//INCREASE DEFINITIONS
	private List<String>increaseDefinitions=new ArrayList<>();
		public List<String>getIncreaseDefinitions(){return increaseDefinitions;}
		public void addIncreaseDefinition(String regex){increaseDefinitions.add(regex);}
		public void delIncreaseDefinition(String regex){increaseDefinitions.remove(regex);}
//DECREASE DEFINITIONS
	private List<String>decreaseDefinitions=new ArrayList<>();
		public List<String>getDecreaseDefinitions(){return decreaseDefinitions;}
		public void addDecreaseDefinition(String regex){decreaseDefinitions.add(regex);}
		public void delDecreaseDefinition(String regex){decreaseDefinitions.remove(regex);}
//SIZES
	private float[]biggerSizes;
		public void setBiggerSizeModifiers(float...sizeModifiers){biggerSizes=sizeModifiers;}
		public int getMaximumLevel(){return biggerSizes.length;}
	private float[]smallerSizes;
		public void setSmallerSizeModifiers(float...sizeModifiers){smallerSizes=sizeModifiers;}
		public int getMinimumLevel(){return smallerSizes.length*-1;}
	public float getFontSizeModifier(int level){	//..., -2, -1, 0, 1, 2, ...
		if(level<0){
			if(level<getMinimumLevel())return smallerSizes[smallerSizes.length-1];
			level*=-1;	//INVERTE DE - PARA +
			return smallerSizes[level-1];
		}
		if(level==0)return 1.0f;
		if(level>getMaximumLevel())return biggerSizes[biggerSizes.length-1];
		return biggerSizes[level-1];
	}
//MAIN
	public MindMarkSizeModifierAtributo(){}
//FUNCS
	public void updateFontSize(int level){
		final int newSize=(int)(defaultFontSize*getFontSizeModifier(level));
		StyleConstants.setFontSize(this,newSize);
	}
}