package utilitarios;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class HashList<K,V> extends HashMap<K,List<V>>{
//SERIAL
	/***/
	private static final long serialVersionUID = 6830636340927250796L;
//MAIN
	public HashList(){}
//FUNCS
	public void add(K key,V value){
		if(!containsKey(key))put(key,new ArrayList<V>());
		get(key).add(value);
	}
//FUNCS: HASH
	public int indexOfKey(K key){
		int index=-1;
		for(K k:keySet()){
			index++;
			if(k==key)break;
		}
		return index;
	}
//FUNCS: LIST
@Override
	public boolean containsValue(Object value){
		boolean containsValue=false;
		for(List<V>list:values()){
			containsValue=list.contains(value);
			if(containsValue)break;
		}
		return containsValue;
	}
	public V getValue(int index){
		if(index<0||index>=totalSize())return null;
		int countedIndex=0;
		for(List<V>list:values()){
			if(countedIndex+list.size()>index){
				index-=countedIndex;
				return list.get(index);
			}else countedIndex=list.size();
		}
		return null;
	}
	public V remove(int index){
		if(index<0||index>=totalSize())return null;
		int countedIndex=0;
		for(List<V>list:values()){
			if(countedIndex+list.size()>index){
				index-=countedIndex;
				final V value=list.remove(index);
				if(list.isEmpty())remove(list);
				return value;
			}else countedIndex=list.size();
		}
		return null;
	}
	public int totalSize(){
		int size=0;
		for(List<V>list:values())size+=list.size();
		return size;
	}
	public int indexOf(V value){
		int index=-1;
		int countedIndex=0;
		for(List<V>list:values()){
			final int indexOf=list.indexOf(value);
			if(indexOf!=-1){
				return countedIndex+index;
			}else countedIndex=list.size();
		}
		return index;
	}
}