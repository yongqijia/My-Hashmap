import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Yongqi Jia
 *
 */
public class ArrayMap<K, V> extends AbstractMap<K, V> {
	Object[] keys = new Object[0];
	Object[] values = new Object[0];
	private int size;

	/**
	 * This method adds key and value to your map. If key already exists, 
	 * the new value replaces the old one, and the old one is returned.
	 * 
	 * @param K key
	 * @param V value
	 * 
	 * @return V value
	 */
	@Override
	public V put(K key, V value) {
		for(int i = 0; i < keys.length; i++) {
			if(keys[i] == key) {
				Object oldvalue = values[i];
				values[i] = value;
				return (V) oldvalue;
			} 
		}
		Object[] tempkeys = new Object[keys.length + 1];
		Object[] tempvalues = new Object[values.length + 1];
		System.arraycopy(keys, 0, tempkeys, 0, keys.length);
		System.arraycopy(values, 0, tempvalues, 0, values.length);
		keys = tempkeys;
		values = tempvalues;
		keys[keys.length-1] = key;
		values[values.length-1] = value;	
		size += 1;
		return value;	
	}
	
	/**
	 * This method returns the number of mappings that the object contains.
	 * 
	 * @return int size
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Returns a Set of key, value pairs contained in an Entry object. 
	 * The AbstractMap class provides a concrete SimpleEntry class that we 
	 * can use to hold them.
	 * 
	 * @return Set<Entry<K, V>>
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new ArrayMapEntrySet();
	}
	
	private class ArrayMapEntrySet extends AbstractSet<Entry<K,V>>{
		
		/**
		 * This method returns the size of the set (and of the Map).
		 * 
		 * @return int size
		 */
		@Override
		public int size() {
			return size;
		}
		
		/**
		 * This method should return true if the Set contains an Entry equal to 
		 * the the one represented by the parameter. If o is not an Entry, 
		 * this is trivially false.
		 * 
		 * @param Object o
		 * 
		 * @return boolean
		 */
		@Override
		public boolean contains(Object o) {
			Entry<K, V> e = (Entry<K, V>) o;
			for(int i = 0; i<size; i++) {
				if(keys[i].equals(e.getKey())) {
					if(values[i].equals(e.getValue())) {
						return true;
					}
				}
			}
			return false;	
		}
		
		/**
		 * This returns an iterator that walks over the Set of Entries in the Map.
		 * 
		 * @return Iterator<Entry<K, V>>
		 */
		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new ArrayMapEntrySetIterator<>();

		}	
		
		private class ArrayMapEntrySetIterator<T> implements Iterator<T>{
			private Object tempkey;
			private int index = 0;
			
			/**
			 * Returns true if there are more items in the Set of Entries being iterated over.
			 * 
			 * @return boolean
			 */
			@Override
			public boolean hasNext() {
				return index < size;
			}

			/**
			 * Returns an Entry (an AbstractMap.SimpleEntry<V,E> for us) that represents 
			 * the next mapping in our Map.
			 * 
			 * @return T
			 */
			@SuppressWarnings("unchecked")
			@Override
			public T next() {
				tempkey =  keys[index];
				Object key = keys[index];
				Object value = values[index];
				index += 1;
				return (T) new AbstractMap.SimpleEntry<K, V>((K) key, (V) value);
			}
			
			/**
			 * Removes from the underlying collection the last element returned by this 
			 * iterator (optional operation). This method can be called only once per call 
			 * to next(). The behavior of an iterator is unspecified if the underlying 
			 * collection is modified while the iteration is in progress in any way other
			 *  than by calling this method.
			 */
			@Override
			public void remove() {
				if(tempkey == null) {
					throw new IllegalStateException();
				}
				Object[] tempkeys = new Object[keys.length - 1];
				Object[] tempvalues = new Object[values.length - 1];
				System.arraycopy(keys, index, keys, index - 1, keys.length - index);
				System.arraycopy(values, index, values, index - 1, values.length - index);
				for(int i = 0; i < tempkeys.length; i++) {
					tempkeys[i] = keys[i];
					tempvalues[i] = values[i];
				}
				keys = tempkeys;
				values = tempvalues;
				tempkey = null;
				size -= 1;
			}
		}	
	}
}
