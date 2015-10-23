import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InspectorTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams()
	{
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		
	}
	
	@After
	public void cleanUpStreams()
	{
		System.setOut(null);
		System.setErr(null);
	}
	
	@Test
	public void testMethods() throws IOException
	{
		Inspector inspect = new Inspector();
		
		ClassA classObject = new ClassA();
		Class testClass = classObject.getClass();
		boolean rec = false;
		
		inspect.inspectMethods(testClass);
		
		assertEquals("" + "\n" + "\tMethod(s): " + "\n" + "\t\tName: run\n" + "\t\t\tReturn type: " + "void" + "\n" + "\t\t\tModifiers: " + "public" + "\n" + "\t\tName: toString\n" + "\t\t\tReturn type: " + "java.lang.String" + "\n" + "\t\t\tModifiers: " + "public" + "\n" + "\t\tName: setVal\n" + "\t\t\tException thrown: " + "java.lang.Exception" + "\n" + "\t\t\tParameters: " + "int" + "\n" + "\t\t\tReturn type: " + "void" + "\n" + "\t\t\tModifiers: " + "public" + "\n" + "\t\tName: printSomething\n" + "\t\t\tReturn type: " + "void" + "\n" + "\t\t\tModifiers: " + "private" + "\n" + "\t\tName: getVal\n" + "\t\t\tReturn type: " + "int" + "\n" + "\t\t\tModifiers: " + "public" + "\n", outContent.toString());
		outContent.reset();
	}
	
	@Test
	public void testInterfaces() throws IOException
	{
		Inspector inspect = new Inspector();
		
		ClassA classObject = new ClassA();
		Class testClass = classObject.getClass();
		boolean rec = false;
		
		inspect.inspectInterfaces(testClass);
		
		assertEquals("" + "\n" + "\tInterface(s): " + "\n" + "\t\t" + "interface java.io.Serializable" + "\n" + "\t\t" + "interface java.lang.Runnable" + "\n", outContent.toString());
		outContent.reset();
	}
	
	@Test
	public void testConstructors() throws IOException
	{
		Inspector inspect = new Inspector();
		
		ClassA classObject = new ClassA();
		Class testClass = classObject.getClass();
		boolean rec = false;
		
		inspect.inspectConstructors(testClass);
		
		assertEquals("" + "\n" + "\tConstructor(s): " + "\n" + "\t\tName: " + "ClassA" + "\n" + "\t\t\tModifiers: " + "public" + "\n" + "\t\tName: " + "ClassA" + "\n" + "\t\t\tParameters: " + "int" + "\n" + "\t\t\tModifiers: " + "public" + "\n", outContent.toString());
		outContent.reset();
	}
	
	@Test
	public void testFields() throws IOException
	{
		Inspector inspect = new Inspector();
		
		ClassA classObject = new ClassA();
		Class testClass = classObject.getClass();
		boolean rec = false;
		Vector vec = new Vector();
		
		inspect.inspectFields(classObject, testClass, vec);
		
		assertEquals("\t\tName: val\n\t\t\tDeclaring Class: ClassA\n\t\t\tType: int\n\t\t\tModifiers: private\n\t\t\tValue: 3\n\t\tName: val2\n\t\t\tDeclaring Class: ClassA\n\t\t\tType: double\n\t\t\tModifiers: private\n\t\t\tValue: 0.2\n\t\tName: val3\n\t\t\tDeclaring Class: ClassA\n\t\t\tType: boolean\n\t\t\tModifiers: private\n\t\t\tValue: true\n", outContent.toString());
		outContent.reset();
	}
	
}
