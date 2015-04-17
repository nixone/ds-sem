package sk.nixone.ds.agent.annotation;

import java.lang.reflect.Method;

public class AnnotatedMethod<A> {
	final public Method METHOD;
	final public A ANNOTATION;
	
	protected AnnotatedMethod(Method method, A annotation) {
		this.METHOD = method;
		this.ANNOTATION = annotation;
	}
}