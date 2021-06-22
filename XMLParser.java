import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
/*
HTML is a Markup Langauge 
Markup Language is the one with tags.
Browser is interpreter for HTML, it can understand and decode the HTML tags.
What are HTML tags?
They are predefined and have specific meaning.

XML is another language which stands for Xtensive Markup Language.
In this we can create our own tags (thats why Xtensive) which are used to store data, or transfer data from server to client.

Custom XML Parser
*/
/*
<applet code = "XMLParser" width = 500 height = 500>
</applet>
*/
class FileRead
{
	/*
	This method is used to read the content of the file and store it in the String passed as a parameter.
	Name- fileRead
	Return Type- String
	It is a static method hence can be accessed by using the class name which is FileRead.
	Here the content that has been read by the file is saved in the String info which is passed as the parameter.
	*/
	static String fileRead(String info){
		File file = new File("xmlParserFile.xml");
		try{
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while((line = br.readLine()) != null)
				info += line;
			br.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Exception: "+e);
		}
	
		catch(IOException e){
			System.out.println("Exception: "+e);
		}
		
		return info;
	}
}
public class XMLParser extends JApplet{
	//Declaring arrays for storing the particular values in XMLParserFile
	String students[] = new String[100];
	String rollNos[] = new String[100];
	String names[] = new String[100];
	String phNos[] = new String[100];
	String cities[] = new String[100];
	public void init(){
		//Declaring variable 'noOfRows' which will be later used to store the noOfRows in the table.
		int noOfRows = 0;
		//String 'xmlText' is storing the value of the data in the .xml file
		String xmlText = new String();
		xmlText = FileRead.fileRead(xmlText);
		Container contentPane = getContentPane();
		//Setting BorderLayout for the contentPane
    	contentPane.setLayout(new BorderLayout());
    	//Column Heading being fixed to Roll No., Name, Phone No. and City
    	final String[] colHeads = {"Roll No.", "Name", "Phone No.", "City"};
    	//Removing white spaces, this step is optional
    	xmlText = xmlText.replaceAll("\\s","");
    	//students array is calling patternAndMatcher method to store the data of different students
        students = patternAndMatcher("student", xmlText, 0, students);
        //Counting the no. of rows that are there in the table based on no. of students.
        for(int i=0; students[i] != null; i++)
        	noOfRows++;
        //Here we are finding the rollNos, name, phNos and cities of the repective students and storing the following information in the respective arrays.
        for(int i=0; i<noOfRows; i++){
        	rollNos = patternAndMatcher("roll-no", students[i], i, rollNos);
        	names = patternAndMatcher("name", students[i],i, names);
        	phNos = patternAndMatcher("ph-no", students[i],i, phNos);
        	cities = patternAndMatcher("city", students[i],i, cities);
        }
        final Object[][] data = new Object[noOfRows][colHeads.length];
        //Below we are inserting the values of the students by categorizing each data and placing them in the appropriate columns.
        for(int i=0; i<colHeads.length; i++){
        	for(int j=0; j<noOfRows; j++){
        		if(colHeads[i].equals("Roll No."))
        			data[j][i] = rollNos[j]; //Different rows, same column
        		else if(colHeads[i].equals("Name"))
        			data[j][i] = names[j];	//Different rows, same column
        		else if(colHeads[i].equals("Phone No."))
        			data[j][i] = phNos[j];	//Different rows, same column
        		else if(colHeads[i].equals("City"))
        			data[j][i] = cities[j];	//Different rows, same column
        		createJTable(data, colHeads, contentPane);
        	}
        }
	}
	/*
	This method is used to create a pattern for the specific tag and retrieve information from that file according to the repective parameters passed to it.
	Access Specifier- private
	Return Type- String[]
	Name- patternAndMatcher
	Parameters- 1)String tagName- This parameter is to mention what is the name of the tag from where you want to extract information from.
				2)String content- This parameter is the content (place) where the tag is present.
				3)int index- So this is the parameter which is used by the subtags, for example:
				<student>
					<name>ABC</name>
				</student>
				<student>
					<name>PQRS</name>
				</student>
				Here, student is the tag and name is the subtag, so index is just mentioning that ABC should be stored in 1st index and PQRS should be stored in 2nd index. For main tags like student you can pass 0 for this tag.
				4)String[] extractedInfo- Here you pass the string array where you want your data to be stored at.
	*/
	private String[] patternAndMatcher(String tagName, String content, int index, String[] extractedInfo){
		int count = index;
		String tagPattern = "(<"+tagName+">)(.*?)"+"(</"+tagName+">)";
		Pattern pattern = Pattern.compile(tagPattern);
		for(int i=index; i<index+1; i++){
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()){
				extractedInfo[count] = matcher.group(2);
				count++;
			}
		}
		return extractedInfo;
	}
	/*
	This method is used for creating JTable.
	Access Specfier- private
	Return Type- void (doesn't return anything)
	Name- createJTable
	Parameters- 1)Object[][] data - It is used to store the data of the table.
				2)String[] colHeads - It is used to store the column headings.
				3)Container contentPane - contentPane on which you want to add your JTable on. 
	*/
	private void createJTable(Object[][] data, String[] colHeads, Container contentPane){
		//Creating a JTable object with the given information and headings.
		JTable table = new JTable(data, colHeads);
		//Setting the foreground as black and background by default being white
		table.setForeground(Color.BLACK);
		//Setting the grid color of JTable to DARK GRAY
		table.setGridColor(Color.DARK_GRAY);
		//If the row in the table is selected then the background will change to blue, by default its white.
		table.setSelectionBackground(Color.BLUE);
		//If the row in the table is selected then the foreground will change to white, by default its black.
		table.setSelectionForeground(Color.WHITE);
		//Defining the constants of the scroll pane as we need to add the JTable on ScrollPane and then add the ScrollPane on the ContentPane as tables can increase of decrease in size.
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;    							  
		JScrollPane jsp = new JScrollPane(table, v, h);
		//Adding the scroll pane to content pane.
		contentPane.add(jsp);
	}
}
