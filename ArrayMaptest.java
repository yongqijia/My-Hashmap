import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class ArrayMaptest {
	
	@Test
	public void put() {
		ArrayMap<String, Integer> temp = new ArrayMap<String, Integer>();
		temp.put("1", 10);
		temp.put("2", 20);
		temp.put("1", 30);
	}
	
	@Test
	public void size() {
		ArrayMap<String, Integer> temp = new ArrayMap<String, Integer>();
		temp.put("1", 10);
		temp.put("2", 20);
		assertTrue(temp.size() == 2);
	}
	
	@Test
	public void entrySet() {
		ArrayMap<String, Integer> temp = new ArrayMap<String, Integer>();
		temp.put("1", 10);
		temp.put("2", 20);
		assertTrue(2 == temp.entrySet().size());
	}
	
	@Test
	public void contains() {
		ArrayMap<String, Integer> temp = new ArrayMap<String, Integer>();
		temp.put("1", 10);
		temp.put("2", 20);
		Set<Entry<String, Integer>> set = temp.entrySet();
		Iterator<Entry<String, Integer>> it = set.iterator();
		Entry<String, Integer> cur = it.next();
		set.contains(cur);
//		System.out.println(set);
		it.remove();
		
		set.contains(cur);
	}
	
	@Test
	public void iterator() {
		ArrayMap<String, Integer> temp = new ArrayMap<String, Integer>();
		temp.put("1", 10);
		temp.put("2", 20);
		Iterator<Entry<String, Integer>> it = temp.entrySet().iterator();
		while(it.hasNext()) {
			it.next();
		}
		it.remove();
		it.remove();
	}	
}
