import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Vector;

public class Inspector {

		public Inspector() { }

	    //-----------------------------------------------------------
	    public void inspect(Object obj, boolean recursive)
	    {
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass();

		System.out.println("	Inside inspector: " + obj + " (recursive = "+recursive+")");
		
		
		//get class name
		String className = ObjClass.getName();
		System.out.println("	Current Class: " + className);
		
		//get declaring class 
		Class declaringClass = ObjClass.getDeclaringClass();
		System.out.println("	Declaring Class: " + declaringClass);
		
		
		//get first super class
		Class superClass = ObjClass.getSuperclass();
		System.out.println("	Super Class: " + superClass);
		
		//get interfaces
		Class[] interfaces = ObjClass.getInterfaces();
		if(interfaces.length > 0)
		{
					
			for(int i = 0; i < interfaces.length; i++)
			{
				System.out.println("	Interface [" + i + "]: " + interfaces[i]);
			}
		}
		else
		{
			System.out.println("	Interface: This class has no interfaces");
		}
		
		//for (Class inter : ObjClass.getInterfaces())
		//{
		//	System.out.println("Implements Class: " + inter);
		//}
		
		//get methods
		Method[] methods = ObjClass.getDeclaredMethods();
		for(int i = 0; i < methods.length; i++)
		{
			//System.out.println("Method: " + methods[i]);
			//method name
			System.out.println("	Method name: " + methods[i].getName());
			
			//exceptions thrown
			Class[] exception = methods[i].getExceptionTypes();
			for(int j = 0; j < exception.length; j++)
			{
				System.out.println("		Exception thrown: " + exception[j].getName());
			}
			
			//parameter types
			Class[] parameters = methods[i].getParameterTypes();
			for(int k = 0; k < parameters.length; k++)
			{
				System.out.println("		Parameters: " + parameters[k].getName());
			}
			
			//return type
			Class returnName = methods[i].getReturnType();
			System.out.println("		Return type: " + returnName);
			
			//Modifier
			int modifier = methods[i].getModifiers();
			System.out.println("		Modifiers: " + Modifier.toString(modifier));
			
			
			
			
		}
		
		//for (Method method: ObjClass.getMethods())
		//{
		//	System.out.println("Method: " + method);
		//}
		
		
		
		//inspect the current class
		//inspectInterfaces(obj, ObjClass, objectsToInspect);
		inspectFields(obj, ObjClass,objectsToInspect);
		
		if(recursive)
		    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
		   
	    }
	    
	    private void inspectInterfaces(Object obj, Class objClass, Vector objectsToInspect)
	    {
	    	//TODO
	    }
	    //-----------------------------------------------------------
	    private void inspectFieldClasses(Object obj,Class ObjClass,
					     Vector objectsToInspect,boolean recursive)
	    {
		
		if(objectsToInspect.size() > 0 )
		    System.out.println("---- Inspecting Field Classes ----");
		
		Enumeration e = objectsToInspect.elements();
		while(e.hasMoreElements())
		    {
			Field f = (Field) e.nextElement();
			System.out.println("Inspecting Field: " + f.getName() );
			
			try
			    {
				System.out.println("******************");
				inspect( f.get(obj) , recursive);
				System.out.println("******************");
			    }
			catch(Exception exp) { exp.printStackTrace(); }
		    }
	    }
	    //-----------------------------------------------------------
	    private void inspectFields(Object obj,Class ObjClass,Vector objectsToInspect)
	  
	    {
		
		if(ObjClass.getDeclaredFields().length >= 1)
		    {
			Field f = ObjClass.getDeclaredFields()[0];
			
			f.setAccessible(true);
			
			if(! f.getType().isPrimitive() ) 
			    objectsToInspect.addElement( f );
			
			try
			    {
				
				System.out.println("Field: " + f.getName() + " = " + f.get(obj));
			    }
			catch(Exception e) {}    
		    }

		if(ObjClass.getSuperclass() != null)
		    inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
	    }

}
