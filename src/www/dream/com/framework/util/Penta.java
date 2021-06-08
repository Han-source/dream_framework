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
public class Penta<A extends Comparable, B, C, D, E> implements Comparable<Penta<A , B, C , D, E>> {
	private A a;
	private B b;
	private C c;
	private D d;
	private E e;
	
	
	public Penta(A a, B b, C c, D d, E e) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
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


	public E getE() {
		return e;
	}



	@Override
	public String toString() {
		return "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e ;
	}

	@Override
	public int compareTo(Penta<A, B, C, D, E> o) {
		return this.a.compareTo(o.a);
	}
	

}
