package com.lplanque.gero.math;

/**
 * This interface denotes a <I>bijection</I> in the mathematical sense :<br/>
 * <ul>
 *    <li> {@link #apply(Object)} is a function from E to F,</li>
 *    <li> {@link #unply(Object)} is a function from F to E,</li>
 *    <li> {@link #unply(Object)} is the <I>invert function</I> of {@link #apply(Object)}.</li>
 * </ul><br/>
 * So the following axiom has to be verified :
 * <ul>
 *    <li>Let <I>f</I> be an instance of <code>Bijection<E, F></code>,</li>
 *    <li>Let <I>x</I> be of type <code>E</code> and <I>y</I> be of type <code>F</code>,</li>
 *    <li>Then <I>f</I><code>.unply(</code><I>f</I><code>.apply(</code><I>x</I><code>)).equals(</code><I>x</I><code>)</code>.</li>
 * </ul>
 * @author <a href="https://github.com/lplanque" target="_blank">Laurent Planque</a>
 */
public interface Bijection<E, F> {
	
	/**
	 * A function from <code>E</code> to <code>F</code>.
	 * @param x An instance of <code>E</code>.
	 * @return An instance of <code>F</code> 
	 * such as <I>f</I><code>.unply(</code><I>f</I><code>.apply(</code><I>x</I><code>)).equals(</code><I>x</I><code>)</code>.
	 */
	F apply(E x); // f(x) such as f-1(f(x)) = x
	
	/**
	 * A function from <code>F</code> to <code>E</code> that is the <I>invert function</I>
	 * of {@link #apply(Object)}.
	 * @param y An instance of <code>F</code>.
	 * @return An instance of <code>E</code> 
	 * such as <I>f</I><code>.apply(</code><I>f</I><code>.unply(</code><I>y</I><code>)).equals(</code><I>y</I><code>)</code>.
	 */
	E unply(F y); // f-1(y) such as f(f-1(y)) = y
}
