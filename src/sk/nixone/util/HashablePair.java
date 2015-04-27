package sk.nixone.util;

public class HashablePair<T1, T2> {

	private T1 first;
	private T2 second;
	
	public HashablePair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof HashablePair)) {
			return false;
		}
		HashablePair<Object, Object> t = (HashablePair<Object, Object>)o;
		return t.first == first && t.second == second;
	}
	
	public T1 getFirst() {
		return first;
	}
	
	public T2 getSecond() {
		return second;
	}
}
