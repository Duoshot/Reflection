import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.lang.reflect.Array;
import java.util.Vector;
import java.util.*;

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
		
		//get first super class
		Class superClass = ObjClass.getSuperclass();
		System.out.println("	Super Class: " + superClass.getName());
		
		if(ObjClass.isArray())
		
		inspectInterfaces(obj, ObjClass);
		
		inspectMethods(obj, ObjClass);

		inspectConstructors(obj, ObjClass);
		
		//get fields
		//type and modifiers
		
		System.out.println("");
		System.out.println("\tFields: ");
	
		inspectFields(obj, ObjClass,objectsToInspect);
		
		//recursive = true;
		
		if(recursive)
		    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
		   
	}
	
	public void inspect(Object obj, Class ObjClass, boolean recursive)
	{
		
	}

	/*--------------------------------------------------------------------*/
	private void inspectInterfaces(Object obj, Class ObjClass)
	{
		System.out.println("");
		System.out.println("\tInterface(s): ");
		
	    Class[] interfaces = ObjClass.getInterfaces();
		if(interfaces.length > 0)
		{
						
			for(int i = 0; i < interfaces.length; i++)
			{
				System.out.println("\t\t" + interfaces[i]);
			}
		}
		else
		{
			System.out.println("This class has no interfaces");
		}
		
	}
	
	/*-----------------------------------------------------------------------------*/
	private void inspectMethods(Object obj, Class ObjClass)
	{
		System.out.println("");
		System.out.println("\tMethod(s): ");
		
		Method[] methods = ObjClass.getDeclaredMethods();
		for(int i = 0; i < methods.length; i++)
		{
			methods[i].setAccessible(true);
			
			//method name
			System.out.println("\t\tName: " + methods[i].getName());
			
			//exceptions thrown
			Class[] exception = methods[i].getExceptionTypes();
			for(int j = 0; j < exception.length; j++)
			{
				System.out.println("\t\t\tException thrown: " + exception[j].getName());
			}
			
			//parameter types
			Class[] parameters = methods[i].getParameterTypes();
			for(int k = 0; k < parameters.length; k++)
			{
				System.out.println("\t\t\tParameters: " + parameters[k].getName());
			}
			
			//return type
			Class returnName = methods[i].getReturnType();
			System.out.println("\t\t\tReturn type: " + returnName.getName());
			
			//Modifier
			int modifier = methods[i].getModifiers();
			System.out.println("\t\t\tModifiers: " + Modifier.toString(modifier));
			
		}
	}
	
	//--------------------------------------------------------------------------
	private void inspectConstructors(Object obj, Class ObjClass)
	{
		System.out.println("");
		System.out.println("\tConstructor(s): ");
		
		Constructor[] constructors = ObjClass.getDeclaredConstructors();
		
		for(int i = 0; i < constructors.length; i++)
		{
			constructors[i].setAccessible(true);
			
			String cName = constructors[i].getName();
			System.out.println("\t\tName: " + cName);
			
			//parameters
			Class[] parameters = constructors[i].getParameterTypes();
			for(int j = 0; j < parameters.length; j++)
			{
				System.out.println("\t\t\tParameters: " + parameters[j].getName());
			}
			//modifiers
			int modifier = constructors[i].getModifiers();
			System.out.println("\t\t\tModifiers: " + Modifier.toString(modifier));
			
		}
	}
	
	    //-----------------------------------------------------------
	private void inspectFieldClasses(Object obj, Class ObjClass, Vector objectsToInspect, boolean recursive)
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
	private void inspectFields(Object obj, Class ObjClass, Vector objectsToInspect)
	{
		Field[] fields = ObjClass.getDeclaredFields();
		for(int i = 0; i < fields.length; i++)
		{
			fields[i].setAccessible(true);
			
	    	if(! fields[i].getType().isPrimitive() ) 
	    		objectsToInspect.addElement( fields[i] );
	    	
	    	System.out.println("\t\tName: " + fields[i].getName());
	    	
	    	System.out.println("\t\t\tDeclaring Class: " + fields[i].getDeclaringClass().getName());
	    	
	    	Class fType = fields[i].getType();
			System.out.println("\t\t\tType: " + fType.getName());
			
			int fMod = fields[i].getModifiers();
			System.out.println("\t\t\tModifiers: " + Modifier.toString(fMod));
			
			Object value = null;
	    	try
			{
	    		value = fields[i].get(obj);
			}
	    	catch(Exception e) {}    
	    	
	    	if(fType.isArray())
			{
					System.out.println("\t\t\tArray Length: " + Array.getLength(value));

					for(int k = 0; k < Array.getLength(value); k++)
				{
						System.out.println("\t\t\t\tArray Element " + k + ": "+ Array.get(value, k));
					}
				}
				else
					System.out.println("\t\t\tValue: " + value);
				
			}


	    if(ObjClass.getSuperclass() != null)
	    		inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
	}

}
