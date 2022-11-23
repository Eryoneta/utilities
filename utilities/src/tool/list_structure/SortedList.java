package tool.list_structure;
import java.util.ArrayList;
@SuppressWarnings("unchecked")
public class SortedList<T> extends ArrayList<T>{
//SERIAL
	/***/
	private static final long serialVersionUID = -1292279763987788417L;
//FUNCS
	public boolean add(T value){
		super.add(getIndexToInsert(value),value);
		return true;
	}
	private int getIndexToInsert(T value){
		int minIndex=0;
		int maxIndex=size()-1;
		while(minIndex<=maxIndex){
			final int midIndex=(minIndex+maxIndex)>>>1;
			final Comparable<? super T>midValor=(Comparable<T>)super.get(midIndex);
			final int compara=midValor.compareTo(value);
			if(compara<0){			//VALOR MENOR
				minIndex=midIndex+1;
			}else if(compara>0){	//VALOR MAIOR
				maxIndex=midIndex-1;
			}else{					//VALOR IGUAL
				for(int index=midIndex+1;true;index++){
					if(++index>=super.size())return index-1;	//VALOR > LISTA = FIM DA LISTA
					final Comparable<? super T>indexValor=(Comparable<T>)super.get(index);
					final int subCompara=indexValor.compareTo(value);
					if(subCompara!=0)return index;				//VALOR_INDEX > VALOR = FIM DA SUBLISTA
				}
			}
		}
		return minIndex;	//INDEX MANTENDO A ORDEM
	}
}