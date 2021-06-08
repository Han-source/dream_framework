package www.dream.com.framework.util;

/**
 * 
 * @author kosmo_20
 *
 * @param <A> 정렬을 위해 Comparable을 extends한다.
 * @param <B> 
 * @param <C>
 * @param <D>
 */
public class Quad<A extends Comparable, B, C, D> implements Comparable<Quad<A , B, C , D>> {
	private A a;
	private B b;
	private C c;
	private D d;
	
	
	public Quad(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public A getA() {
		return a;
	}


	public B getB() {
		return b;
	}


	public C getC() {
		return c;
	}


	public D getD() {
		return d;
	}




	@Override
	public String toString() {
		return "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d;
	}

	@Override
	public int compareTo(Quad<A, B, C, D> o) {
		return this.a.compareTo(o.a);
	}
	

}
