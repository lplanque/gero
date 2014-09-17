package com.lplanque.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public abstract class BijectionTest<E, F> {

	protected final E x;
	protected final F y;
	protected final Bijection<E, F> f;
	
	protected BijectionTest(E x, F y, Bijection<E, F> f) {
		this.x = x;
		this.y = y;
		this.f = f;
	}
	
	@Test public void fof_1() {
		// f-1(f(x)) = x
		assertEquals(x, f.unply(f.apply(x)));
	}
	
	@Test public void f_1of() { 
		// f(f-1(y)) = y
		assertEquals(y, f.apply(f.unply(y)));
	}
}
