package tool.list_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** 
 * Basically, a {@link HashMap} that allows for multiple values, stored inside a {@link List}<br>
 * The entire set is considered to be a single long list
 */
public class HashList<K,V> extends HashMap<K,List<V>>{
	private static final long serialVersionUID = 6830636340927250796L;
//MAIN
	public HashList(){}
//FUNCS
	
	/** 
	 * Adds a value to a sublist under a key
	 * @param key - The object used to store a value
	 * @param value - The value to be stored
	 */
	public void add(K key,V value){
		if(!containsKey(key))put(key,new ArrayList<V>());
		get(key).add(value);
	}
	
	/** 
	 * Removes a value given an index
	 * @param index - The index of the value to remove, as counted from the first sublist
	 * @return The removed value, or null id not found
	 */
	public V remove(int index){
		if(index<0||index>=totalSize())return null;
		int countedIndex=-1;
		for(List<V>list:values()){
			if(countedIndex+list.size()<index) {
				countedIndex+=list.size();
				continue;
			}
			index-=countedIndex+1;
			final V value=list.remove(index);
//			if(list.isEmpty())remove(key);	//É SUBSTITUIDO NO ADD
			return value;
		}
		return null;
	}
	
	/** 
	 * The index of a given key
	 * @param key - The key to find the index of
	 * @return The index of the key, or -1 if not found
	 */
	public int indexOfKey(K key){
		int index=-1;
		for(K k:keySet()){
			index++;
			if(k==key)break;
		}
		return index;
	}
	
	/** 
	 * The index of a given value
	 * @param value - The value to find the index of
	 * @return The index, as counted from the first sublist, or -1 if not found
	 */
	public int indexOf(V value){
		int countedIndex=0;
		for(List<V>list:values()){
			final int indexOf=list.indexOf(value);
			if(indexOf!=-1){
				return countedIndex+indexOf;
			}else countedIndex+=list.size();
		}
		return -1;
	}

	/** 
	 * If the value is present in the set
	 * @param value - The value to check 
	 * @return True if the value is present, false if not
	 */
	@Override public boolean containsValue(Object value){
		for(List<V>list:values()){
			if(list.contains(value))return true;
		}
		return false;
	}

	/** 
	 * Returns a value, given an index
	 * @param index - The index, as counted from the first sublist
	 * @return The value if found, null if not
	 */
	public V getValue(int index){
		if(index<0||index>=totalSize())return null;
		int countedIndex=-1;
		for(List<V>list:values()){
			if(countedIndex+list.size()<index) {
				countedIndex+=list.size();
				continue;
			}
			index-=countedIndex+1;
			return list.get(index);
		}
		return null;
	}
	
	/** 
	 * The total size of the entire set
	 * @return The size of all sublists combined
	 */
	public int totalSize(){
		int size=0;
		for(List<V>list:values())size+=list.size();
		return size;
	}
}