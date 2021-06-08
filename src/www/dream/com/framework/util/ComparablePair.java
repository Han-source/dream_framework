package www.dream.com.framework.util;

// 맨앞은 비교가능한거고 전체적으로 비교기능을 implements해준 것.
public class ComparablePair<F extends Comparable, S> implements Comparable<ComparablePair<F, S>>{
	private F first;
	private S second;
	

	public ComparablePair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public F getFirst() {
		return first;
	}
	
	public S getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComparablePair other = (ComparablePair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}

	//비교 가능한 녀석들만을 담는다.
	@Override
	public int compareTo(ComparablePair<F, S> o) {
		return this.first.compareTo(o.first);
		
	}
	
}
