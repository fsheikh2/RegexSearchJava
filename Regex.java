import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;
//import java.util.stream;

import java.io.File;
//import java.io.PrintFile;
import java.io.FileNotFoundException;

import java.nio.*;
//import java.nio.file.Path;
///import java.nio.file.Paths;
import java.nio.file.*;
// ch 11 pg 467, ch 19 pg 777, ch 21 pg 839

public class Regex{

	public static void main(String[] args) throws FileNotFoundException{
		
		Scanner userScan = new Scanner(System.in);
		String userInput ="";
		Path p;

		System.out.println("\nPlease enter the path containing the files you want to serach:\n");
		userInput = userScan.next();
		p = Paths.get(userInput);
		boolean done = false;

		while(!done){
			if(Files.isDirectory(p))
				done = true;
			else{
				System.out.println("\nSorry, that's not a proper path. Please enter a path to a directory.\n");
				userInput = userScan.next();
				p = Paths.get(userInput);
			}
		}

		/*
		 *	Below part creates a File obj for the directory given by user.
		 *	Then it stores all the files in that directory into a File array.
		 *	
		 *	Then make a regular expression to pick out any file name that ends 
		 *	in ".txt" by using the Pattern and Matcher classes. 
		 *
		 *	Traverse over the File array and apply the regex to each files name
		 * 	and add each match into an array list. 
		*/
		File searchDir = new File(userInput);
		File[] files = searchDir.listFiles();
		
		// Used for regular expression matching
		String pattern = ".*\\.txt";
		Pattern dotTxtRegex = Pattern.compile(pattern);
		
		ArrayList<File> txtFiles = new ArrayList<File>();

		// Traverse given directory and place each .txt file into the ArrayList txtFiles
		for(File f : files){
			Matcher m = dotTxtRegex.matcher(f.toString());
			if(m.find())
				txtFiles.add(f);
		}

		/* 
		 *	Below requests a reg ex from the user, validates it, and keeps
		 *  requesting till a valid reg ex is given. A valid regex is stored
		 *	in userRegx.
		 */
		System.out.println("\nPlease enter the regular expression you'd like to use to search:\n");
		pattern = userScan.next();
		Pattern userRegx =  null;
		done = false;
		while(!done){
			try{
				//pattern = makeJavaRegEx(pattern); // Adds extra \ to stop escape characters
				userRegx = Pattern.compile(pattern); // This will throw an exception if pattern is invalid
				done = true;
			}
			catch(PatternSyntaxException pse){
				System.out.println("That's an invalid regular expression. Please " +
									"enter a new one\n");
				pattern = userScan.next();
			}
		}

		/*
		 *	Below will traverse each matched file stored in the txtFiles ArrayList
		 *	and apply the regular expression taken from the user to the files 
		 *	contents.
		 *	Limitation: Any potential match must be on one line, i.e. a match cannot
		 *	partially be on two different, back to back lines.
		 */

		for(File txtFile : txtFiles){
			ArrayList<Matcher> matches = new ArrayList<Matcher>();
			
			try{
				Scanner txtFileScan = new Scanner(txtFile);
				
				while(txtFileScan.hasNextLine()){
					//matches.add(txtFileScan.next(userRegx));
					String line = txtFileScan.nextLine(); 
					Matcher tempM = userRegx.matcher(line);		// Creates a Matcher obj that can search the input using the RegEx given by the invoking Pattern obj
					if(tempM.find())						   //  This is where the actual traversal of the input and application of the reg ex happens. Calling it again after this line using the same Matcher results in false being returned in this case b/c this call forces it to move on. To call it again later, first reset the Matcher obj.
						matches.add(tempM);
				}
				
				if(matches.isEmpty())
					System.out.println("\n***\t***** No Matches Found In " + txtFile.toString()
										+ " *****\t***\n");
				else{
					System.out.println("\n***\t***** Matches Found In " + txtFile.toString()
										+ " *****\t***\n");
					for(Matcher m : matches)
						System.out.println(m.group());
					System.out.println("\n***\t***** End Matches Found In " + txtFile.toString()
										+ " *****\t***\n");
				}
			}
			catch(FileNotFoundException ex){
				System.out.println("Sorry, the file " + txtFile.toString() + " couldn't be read.\n");
			}
		}


	}

	public static String makeJavaRegEx(String userRegx){
		String javaRegx = userRegx;
		for(int i = 0; i < javaRegx.length(); i++){
			if(javaRegx.charAt(i) == '\\'){
				javaRegx = javaRegx.substring(0,i) + "\\" + javaRegx.substring(i);		
				i++;
			}
		}
		return javaRegx;
	}

}


/*
	Use the DirectoryStream class to iterate over a directory
	* DirectoryStream<Path> dirStream = Files.newDirectoryStream(path);

	The File class has a lot of methods related to files and some to directories
	Use the Path and Paths classes (from nio.files) to work with paths

	There's a File class from io, and Files from nio. 
*/
