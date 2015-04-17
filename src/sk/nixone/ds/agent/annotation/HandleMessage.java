package sk.nixone.ds.agent.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import OSPABA.MessageForm;

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