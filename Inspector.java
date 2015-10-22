/*==========================================================================
CPSC 501
File: Inspector.java
Purpose: Inspects objects, classes, and interfaces.

Location: University of Calgary, Alberta, Canada
Created By: Gorman Law
ID: 10053193
Date: October 21, 2015
========================================================================*/

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.lang.reflect.Array;
import java.util.Vector;
import java.util.*;

public class Inspector {
	
	private PrintWriter printer;
	
	public Inspector() 
	{ 
		try {
			printer = new PrintWriter("output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	    //-----------------------------------------------------------
	public void inspect(Object obj, boolean recursive)
	{
		
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass();
		inspect(obj, ObjClass, recursive);
		printer.close();
	}
	
	public void inspect(Object obj, Class ObjClass, boolean recursive)
	{

		if(ObjClass.isArray())
		{
			inspectArray(obj, ObjClass, recursive);
		}
		else
		{
			inspectClass(obj, ObjClass, recursive);
		}
	}
	
	private void inspectClass(Object obj, Class ObjClass, boolean recursive)
	{
		Vector objectsToInspect = new Vector();
		
		Class superClass = ObjClass.getSuperclass();
		Class[] superInterface = ObjClass.getInterfaces();
		
		printer.println("");
		printer.println("	Inside inspector: " + ObjClass + " (recursive = "+recursive+")");
	
		String className = ObjClass.getName();
		printer.println("");
		printer.println("@   Current Class: " + className);
		
		//get first super class
		//Class superClass = ObjClass.getSuperclass();
		if(superClass != null)
			printer.println("	Immediate Super Class: " + superClass.getName());
		
		inspectConstructors(ObjClass);
	
		inspectInterfaces(ObjClass);
		
		printer.println("");
		printer.println("\tFields: ");
		inspectFields(obj, ObjClass, objectsToInspect);
	
		inspectMethods(ObjClass);
		
		if(recursive)
		{
			inspectFieldClasses(obj, ObjClass, objectsToInspect, recursive);
			
			if(superClass != null)
			{
				printer.println("");
				printer.println("~~~~~~~~~~~~~~~~~~~~");
				printer.println("SuperClass of: " + className);
				printer.println("~~~~~~~~~~~~~~~~~~~~");
				inspect(obj, superClass, recursive);
			}
			
			if(superInterface.length>0)
			{
				for(int i = 0; i < superInterface.length;i++)
				{
					printer.println("");
					printer.println("~~~~~~~~~~~~~~~~~~~~");
					printer.println("SuperInterface of: " + className);
					printer.println("~~~~~~~~~~~~~~~~~~~~");
					inspect(obj, superInterface[i], recursive);
				}
			}
			
		}
	}
	
	private void inspectArray(Object obj, Class ObjClass, boolean recursive)
	{
		printer.println("\tARRAY");
		printer.println("\tArray Length: " + Array.getLength(obj));
		if(Array.getLength(obj)>0)
		{
			
			for(int i = 0; i < Array.getLength(obj); i++)
			{
				Object arrayObj = Array.get(obj, i);
				printer.println("");
				printer.println("\t\tArray Element " + i + ": " + arrayObj);
				if(arrayObj != null)
				{
					
					inspect(arrayObj, arrayObj.getClass(), recursive);
					
				}
				else
				{
					printer.println("\t\tArray Element is null");
				}
				
			}
		}
	}

	/*--------------------------------------------------------------------*/
	private void inspectInterfaces(Class ObjClass)
	{
		printer.println("");
		printer.println("\tInterface(s): ");
		
	    Class[] interfaces = ObjClass.getInterfaces();
		if(interfaces.length > 0)
		{
						
			for(int i = 0; i < interfaces.length; i++)
			{
				printer.println("\t\t" + interfaces[i]);
			}
		}
		else
		{
			printer.println("\t\tThis class has no interfaces");
		}
		
	}
	
	/*-----------------------------------------------------------------------------*/
	private void inspectMethods(Class ObjClass)
	{
		printer.println("");
		printer.println("\tMethod(s): ");
		
		Method[] methods = ObjClass.getDeclaredMethods();
		if(methods.length>0)
		{
			for(int i = 0; i < methods.length; i++)
			{
				methods[i].setAccessible(true);
			
				//method name
				printer.println("\t\tName: " + methods[i].getName());
			
				//exceptions thrown
				Class[] exception = methods[i].getExceptionTypes();
				for(int j = 0; j < exception.length; j++)
				{
					printer.println("\t\t\tException thrown: " + exception[j].getName());
				}
			
				//parameter types
				Class[] parameters = methods[i].getParameterTypes();
				for(int k = 0; k < parameters.length; k++)
				{
					printer.println("\t\t\tParameters: " + parameters[k].getName());
				}
			
				//return type
				Class returnName = methods[i].getReturnType();
				printer.println("\t\t\tReturn type: " + returnName.getName());
			
				//Modifier
				int modifier = methods[i].getModifiers();
				printer.println("\t\t\tModifiers: " + Modifier.toString(modifier));
			
			}
		}
		else
		{
			printer.println("\t\tThis class has no methods");
		}
	}
	
	//--------------------------------------------------------------------------
	private void inspectConstructors(Class ObjClass)
	{
		printer.println("");
		printer.println("\tConstructor(s): ");
		
		Constructor[] constructors = ObjClass.getDeclaredConstructors();
		
		if(constructors.length>0)
		{
			for(int i = 0; i < constructors.length; i++)
			{
				constructors[i].setAccessible(true);
			
				//name
				String cName = constructors[i].getName();
				printer.println("\t\tName: " + cName);
			
				//parameters
				Class[] parameters = constructors[i].getParameterTypes();
				for(int j = 0; j < parameters.length; j++)
				{
					printer.println("\t\t\tParameters: " + parameters[j].getName());
				}
				//modifiers
				int modifier = constructors[i].getModifiers();
				printer.println("\t\t\tModifiers: " + Modifier.toString(modifier));
			}
		}
		else
		{
			printer.println("\t\tThis class has no Constructors");
		}
	}
	
	    //-----------------------------------------------------------
	private void inspectFieldClasses(Object obj, Class ObjClass, Vector objectsToInspect, boolean recursive)
	{		
	    if(objectsToInspect.size() > 0 )
	    {
	    	printer.println("");
	    	printer.println("---- Inspecting Field Classes ----");
	    }
		
	    Enumeration e = objectsToInspect.elements();
	    while(e.hasMoreElements())
	    {
	    	Field f = (Field) e.nextElement();
	    	printer.println("");
	    	printer.println("******************");
	    	printer.println("Inspecting Field: " + f.getName() );
			
	    	try
			{
	    		printer.println("******************");
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
	    	
				printer.println("\t\tName: " + fields[i].getName());
	    	
				printer.println("\t\t\tDeclaring Class: " + fields[i].getDeclaringClass().getName());
	    	
				Class fType = fields[i].getType();
				printer.println("\t\t\tType: " + fType.getName());
			
				int fMod = fields[i].getModifiers();
				printer.println("\t\t\tModifiers: " + Modifier.toString(fMod));
			
				Object value = null;
				try
				{
					value = fields[i].get(obj);
				}
				catch(Exception e) {}    
//	    	
				printer.println("\t\t\tValue: " + value);
			}
		}
		else
		{
			printer.println("\t\tThis class has no Fields");
		}
			//if(ObjClass.getSuperclass() != null)
			//	inspectFields(obj, ObjClass.getSuperclass() , objectsToInspect);
	}

}
