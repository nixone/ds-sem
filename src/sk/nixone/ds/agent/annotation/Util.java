package sk.nixone.ds.agent.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
	
	public static <C, A extends Annotation> List<AnnotatedMethod<A>> getMethodsAnnotatedWith(final Class<C> clasz, final Class<A> annotationClass) {
	    final List<AnnotatedMethod<A>> methods = new ArrayList<AnnotatedMethod<A>>();
	    Class<?> klass = clasz;
	    while (klass != Object.class) {
	    	final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));       
	        for (final Method method : allMethods) {
	            if (annotationClass == null || method.isAnnotationPresent(annotationClass)) {
	                methods.add(new AnnotatedMethod<A>(method, method.getAnnotation(annotationClass)));
	            }
	        }
	        klass = klass.getSuperclass();
	    }
	    return methods;
	}
}
