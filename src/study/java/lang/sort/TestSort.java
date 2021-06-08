package study.java.lang.sort;

import java.util.TreeSet;

import www.dream.com.framework.util.Quad;

public class TestSort {
	public static void main(String[] args) {

		TreeSet<Quad<MyObj, Integer, String, Long>> sorted = new TreeSet<>();
		for (int i = 0; i < 10; i++) {
			int r = (int) (Math.random() * 10);
			sorted.add(new Quad<>(new MyObj(r, "홍" + r),2, "sdd", 4L));
		}
		for (Quad o : sorted) {
			System.out.println(o);
		}
	}
}