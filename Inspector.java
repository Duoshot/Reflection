import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.lang.reflect.Array;
import java.util.Vector;
import java.util.*;

public class Inspector {

	
	public Inspector() 
	{ 
	}

	    //-----------------------------------------------------------
	public void inspect(Object obj, boolean recursive)
	{
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass();
		inspect(obj, ObjClass, recursive);
		   
	}
	
	public void inspect(Object obj, Class ObjClass, boolean recursive)
	{

		if(ObjClass.isArray())
		{
			System.out.println("\tARRAY");
			System.out.println("\tArray Length: " + Array.getLength(obj));
			if(Array.getLength(obj)>0)
			{
				
				for(int i = 0; i < Array.getLength(obj); i++)
				{
					Object arrayObj = Array.get(obj, i);
					if(arrayObj != null)
					{
						inspect(arrayObj, arrayObj.getClass(), recursive);
					}
					else
					{
						System.out.println("\t\tArray Element " + i + ": " + arrayObj);
					}
					
				}
			}
		}
		else
		{
			inspectObject(obj, ObjClass, recursive);
		}
	}
	
	private void inspectObject(Object obj, Class ObjClass, boolean recursive)
	{
		Vector objectsToInspect = new Vector();
		
		Class superClass = ObjClass.getSuperclass();
		
		System.out.println("");
		System.out.println("	Inside inspector: " + ObjClass + " (recursive = "+recursive+")");
	
		String className = ObjClass.getName();
		System.out.println("	Current Class: " + className);
		
		//get first super class
		//Class superClass = ObjClass.getSuperclass();
		if(superClass != null)
			System.out.println("	Immediate Super Class: " + superClass.getName());
		
		inspectConstructors(ObjClass);
	
		inspectInterfaces(ObjClass);
		
		System.out.println("");
		System.out.println("\tFields: ");
		inspectFields(obj, ObjClass, objectsToInspect);
	
		inspectMethods(ObjClass);
		
		if(recursive)
		{
			inspectFieldClasses(obj, ObjClass, objectsToInspect, recursive);
			
			if(superClass != null)
			{
				System.out.println("");
				System.out.println("~~~~~~~~~~~~~~~~~~~~");
				System.out.println("SuperClass of: " + className);
				System.out.println("~~~~~~~~~~~~~~~~~~~~");
				inspect(obj, superClass, recursive);
			}
		}
	}
	

	/*--------------------------------------------------------------------*/
	private void inspectInterfaces(Class ObjClass)
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
			System.out.println("\t\tThis class has no interfaces");
		}
		
	}
	
	/*-----------------------------------------------------------------------------*/
	private void inspectMethods(Class ObjClass)
	{
		System.out.println("");
		System.out.println("\tMethod(s): ");
		
		Method[] methods = ObjClass.getDeclaredMethods();
		if(methods.length>0)
		{
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
		else
		{
			System.out.println("\t\tThis class has no methods");
		}
	}
	
	//--------------------------------------------------------------------------
	private void inspectConstructors(Class ObjClass)
	{
		System.out.println("");
		System.out.println("\tConstructor(s): ");
		
		Constructor[] constructors = ObjClass.getDeclaredConstructors();
		
		if(constructors.length>0)
		{
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
		else
		{
			System.out.println("\t\tThis class has no Constructors");
		}
	}
	
	    //-----------------------------------------------------------
	private void inspectFieldClasses(Object obj, Class ObjClass, Vector objectsToInspect, boolean recursive)
	{		
	    if(objectsToInspect.size() > 0 )
	    {
	    	System.out.println("");
	    	System.out.println("---- Inspecting Field Classes ----");
	    }
		
	    Enumeration e = objectsToInspect.elements();
	    while(e.hasMoreElements())
	    {
	    	Field f = (Field) e.nextElement();
	    	System.out.println("");
	    	System.out.println("******************");
	    	System.out.println("Inspecting Field: " + f.getName() );
			
	    	try
			{
	    		System.out.println("******************");
	    		inspect( f.get(obj) , f.get(obj).getClass(), recursive);
	    		
			}
	    	catch(Exception exp) { exp.printStackTrace(); }
		}
	}
	    //-----------------------------------------------------------
	private void inspectFields(Object obj, Class ObjClass, Vector objectsToInspect)
	{
		Field[] fields = ObjClass.getDeclaredFields();
		
		if(fields.length > 0)
		{
		
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
//	    	
				System.out.println("\t\t\tValue: " + value);
			}
		}
		else
		{
			System.out.println("\t\tThis class has no Fields");
		}
			//if(ObjClass.getSuperclass() != null)
			//	inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
	}

}
