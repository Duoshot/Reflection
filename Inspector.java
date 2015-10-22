/*==========================================================================
CPSC 501
File: Inspector.java
Purpose: Inspects objects, classes, and interfaces.

Location: University of Calgary, Alberta, Canada
Created By: Gorman Law
ID: 10053193
Date: October 21, 2015
========================================================================*/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.lang.reflect.Array;
import java.util.Vector;
import java.util.*;

public class Inspector {
	
	//print to file
	private FileWriter fw;
	private BufferedWriter bw;
	File file = new File("test.txt");
	
	public Inspector()
	{ 
	}

//-----------------------------------------------------------------
	public void inspect(Object obj, boolean recursive) throws IOException
	{
		Vector objectsToInspect = new Vector();
		Class ObjClass = obj.getClass();
		fw = new FileWriter(file.getAbsoluteFile(), true);
		bw = new BufferedWriter(fw);
		inspect(obj, ObjClass, recursive);
		bw.close();
		
	}

//------------------------------------------------------------------------
	public void inspect(Object obj, Class ObjClass, boolean recursive) throws IOException
	{
		//if class is an array, inspect array
		if(ObjClass.isArray())
		{
			inspectArray(obj, ObjClass, recursive);
		}
		else	//else inspect the class normally
		{
			inspectClass(obj, ObjClass, recursive);
		}
	}
	
//-------------------------------------------------------------------------
	private void inspectClass(Object obj, Class ObjClass, boolean recursive) throws IOException
	{
		Vector objectsToInspect = new Vector();
		
		Class superClass = ObjClass.getSuperclass();
		Class[] superInterface = ObjClass.getInterfaces();
		
		bw.write("" + "\n");
		bw.write("	Inside inspector: " + ObjClass + " (recursive = "+recursive+")" + "\n");
		//name
		String className = ObjClass.getName();
		bw.write("");
		bw.write("@   Current Class: " + className);
		
		//get first super class
		if(superClass != null)
			bw.write("	Immediate Super Class: " + superClass.getName() + "\n");
		
		//inspect constructors
		inspectConstructors(ObjClass);
	
		//inspect interfaces
		inspectInterfaces(ObjClass);
		
		//inspect fields
		bw.write("" + "\n");
		bw.write("\tFields: " + "\n");
		inspectFields(obj, ObjClass, objectsToInspect);
	
		//inspect methods
		inspectMethods(ObjClass);
		
		if(recursive)
		{
			inspectFieldClasses(obj, ObjClass, objectsToInspect, recursive);
			
			if(superClass != null)
			{
				bw.write("" + "\n");
				bw.write("~~~~~~~~~~~~~~~~~~~~" + "\n");
				bw.write("SuperClass of: " + className + "\n");
				bw.write("~~~~~~~~~~~~~~~~~~~~" + "\n");
				inspect(obj, superClass, recursive);	//recursively run on superclasses
			}
			
			if(superInterface.length>0)
			{
				for(int i = 0; i < superInterface.length;i++)
				{
					bw.write("" + "\n");
					bw.write("~~~~~~~~~~~~~~~~~~~~" + "\n");
					bw.write("SuperInterface of: " + className + "\n");
					bw.write("~~~~~~~~~~~~~~~~~~~~" + "\n");
					inspect(obj, superInterface[i], recursive);	//recursively run on superinterfaces
				}
			}
			
		}
	}
	
//----------------------------------------------------------------------
	private void inspectArray(Object obj, Class ObjClass, boolean recursive) throws IOException
	{
		bw.write("\tARRAY" + "\n");
		bw.write("\tArray Length: " + Array.getLength(obj) + "\n");
		if(Array.getLength(obj)>0)
		{
			
			for(int i = 0; i < Array.getLength(obj); i++)
			{
				Object arrayObj = Array.get(obj, i);
				bw.write("" + "\n");
				bw.write("\t\tArray Element " + i + ": " + arrayObj + "\n");
				if(arrayObj != null)
				{
					
					inspect(arrayObj, arrayObj.getClass(), recursive);	//recursively run on all of objects in array
					
				}
				else
				{
					bw.write("\t\tArray Element is null" + "\n");
				}
				
			}
		}
	}

/*--------------------------------------------------------------------*/
	private void inspectInterfaces(Class ObjClass) throws IOException
	{
		bw.write("" + "\n");
		bw.write("\tInterface(s): " + "\n");
		
	    Class[] interfaces = ObjClass.getInterfaces();	//get interfaces
		if(interfaces.length > 0)
		{
						
			for(int i = 0; i < interfaces.length; i++)
			{
				bw.write("\t\t" + interfaces[i] + "\n");
			}
		}
		else
		{
			bw.write("\t\tThis class has no interfaces" + "\n");
		}
		
	}
	
/*-----------------------------------------------------------------------------*/
	private void inspectMethods(Class ObjClass) throws IOException
	{
		bw.write("" + "\n");
		bw.write("\tMethod(s): " + "\n");
		
		Method[] methods = ObjClass.getDeclaredMethods();
		if(methods.length>0)
		{
			for(int i = 0; i < methods.length; i++)
			{
				methods[i].setAccessible(true);
			
				//method name
				bw.write("\t\tName: " + methods[i].getName() + "\n");
			
				//exceptions thrown
				Class[] exception = methods[i].getExceptionTypes();
				for(int j = 0; j < exception.length; j++)
				{
					bw.write("\t\t\tException thrown: " + exception[j].getName() + "\n");
				}
			
				//parameter types
				Class[] parameters = methods[i].getParameterTypes();
				for(int k = 0; k < parameters.length; k++)
				{
					bw.write("\t\t\tParameters: " + parameters[k].getName() + "\n");
				}
			
				//return type
				Class returnName = methods[i].getReturnType();
				bw.write("\t\t\tReturn type: " + returnName.getName() + "\n");
			
				//Modifier
				int modifier = methods[i].getModifiers();
				bw.write("\t\t\tModifiers: " + Modifier.toString(modifier) + "\n");
			
			}
		}
		else
		{
			bw.write("\t\tThis class has no methods" + "\n");
		}
	}
	
//--------------------------------------------------------------------------
	private void inspectConstructors(Class ObjClass) throws IOException
	{
		bw.write("" + "\n");
		bw.write("\tConstructor(s): " + "\n");
		
		Constructor[] constructors = ObjClass.getDeclaredConstructors();
		
		if(constructors.length>0)
		{
			for(int i = 0; i < constructors.length; i++)
			{
				constructors[i].setAccessible(true);
			
				//name
				String cName = constructors[i].getName();
				bw.write("\t\tName: " + cName + "\n");
			
				//parameters
				Class[] parameters = constructors[i].getParameterTypes();
				for(int j = 0; j < parameters.length; j++)
				{
					bw.write("\t\t\tParameters: " + parameters[j].getName() + "\n");
				}
				//modifiers
				int modifier = constructors[i].getModifiers();
				bw.write("\t\t\tModifiers: " + Modifier.toString(modifier) + "\n");
			}
		}
		else
		{
			bw.write("\t\tThis class has no Constructors" + "\n");
		}
	}
	
//-----------------------------------------------------------
	private void inspectFieldClasses(Object obj, Class ObjClass, Vector objectsToInspect, boolean recursive) throws IOException
	{		
	    if(objectsToInspect.size() > 0 )
	    {
	    	bw.write("" + "\n");
	    	bw.write("---- Inspecting Field Classes ----" + "\n");
	    }
		
	    Enumeration e = objectsToInspect.elements();
	    while(e.hasMoreElements())
	    {
	    	Field f = (Field) e.nextElement();
	    	bw.write("" + "\n");
	    	bw.write("******************" + "\n");
	    	bw.write("Inspecting Field: " + f.getName() + "\n");
			
	    	try
			{
	    		bw.write("******************" + "\n");
	    		inspect( f.get(obj) , f.get(obj).getClass(), recursive);
	    		
			}
	    	catch(Exception exp) { exp.printStackTrace(); }
		}
	}
//-----------------------------------------------------------
	private void inspectFields(Object obj, Class ObjClass, Vector objectsToInspect) throws IOException
	{
		Field[] fields = ObjClass.getDeclaredFields();
		
		if(fields.length > 0)
		{
			for(int i = 0; i < fields.length; i++)
			{
				fields[i].setAccessible(true);
			
				if(! fields[i].getType().isPrimitive() ) 
					objectsToInspect.addElement( fields[i] );
	    	
				//name
				bw.write("\t\tName: " + fields[i].getName() + "\n");
	    	
				//declaring class
				bw.write("\t\t\tDeclaring Class: " + fields[i].getDeclaringClass().getName() + "\n");
	    	
				//type
				Class fType = fields[i].getType();
				bw.write("\t\t\tType: " + fType.getName() + "\n");
			
				//modifiers
				int fMod = fields[i].getModifiers();
				bw.write("\t\t\tModifiers: " + Modifier.toString(fMod) + "\n");
			
				Object value = null;
				try
				{
					value = fields[i].get(obj);
				}
				catch(Exception e) {}    
				//value
				bw.write("\t\t\tValue: " + value + "\n");
			}
		}
		else
		{
			bw.write("\t\tThis class has no Fields" + "\n");
		}
	}

}
