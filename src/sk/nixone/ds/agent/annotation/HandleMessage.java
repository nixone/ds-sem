package sk.nixone.ds.agent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import OSPABA.MessageForm;

@Retention(value = RetentionPolicy.RUNTIME)     
@Target(value = ElementType.METHOD)
public @interface HandleMessage {
	
	public static class Processor {
		private Object object;
		private HashMap<Integer, Method> methodMap = new HashMap<Integer, Method>();
		private HashMap<Integer, Class<? extends MessageForm>> classMap = new HashMap<Integer, Class<? extends MessageForm>>();
		
		public Processor(Object object) {
			this.object = object;
			List<AnnotatedMethod<HandleMessage>> all = Util.getMethodsAnnotatedWith(object.getClass(), HandleMessage.class);

			for (AnnotatedMethod<HandleMessage> one : all) {
				methodMap.put(one.ANNOTATION.code(), one.METHOD);
				classMap.put(one.ANNOTATION.code(), one.ANNOTATION.accept());
			}
		}
		
		public Set<Integer> getCodes() {
			return methodMap.keySet();
		}
		
		public void process(MessageForm message) {
			Method method = methodMap.get(message.code());
			Class<? extends MessageForm> newClass = classMap.get(message.code());
			if(method == null || newClass == null) {
				System.err.println("@HandleMessage: method == null || newClass == null");
			}
			if (!newClass.isInstance(message)) {
				throw new IllegalArgumentException("@HandleMessage: "+method+" can't accept "+message.getClass()+" messages.");
			}
			try {
				method.invoke(object, message);
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	int code();
	Class<? extends MessageForm> accept() default MessageForm.class;
}