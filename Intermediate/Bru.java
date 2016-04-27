import java.io.*;
import java.util.*;

public class Bru {
	//current symbol table
	public static Map<String, String> values = new HashMap<String, String>();
	//stack for symbol table
	public static Stack<Map<String, String>> symtab = new Stack<Map<String, String>>();
	//execution stack
	public static Stack<String> run = new Stack<String>();
	//stack data structure maintainance
	public static Map<String, Stack<Integer>> StackMap = new HashMap<String, Stack<Integer>>();
	public static String funcName = "";
	//instruction pointer maintainance
	public static Stack<BufferedReader> fileReader = new Stack<BufferedReader>();
	
	public static void main(String args[])throws IOException {
		String path = args[0];
		String line = null;
		Boolean condition = null;
		String whilelabel = "";
		BufferedReader br = new BufferedReader(new FileReader(path));
		while((line = br.readLine()).equals(".MainMethodStarts") == false);
		while((line = br.readLine()) != null){
			String[] command = line.split(" ");
			switch(command[0]){
				case "PUSH": 	
							try{
								run.push((command[1]));
							}
							catch(NumberFormatException e){
								String val = values.get(command[1]);
								run.push(val);
							}
								
					     	break;
				case "LOAD": 	
							if(values.containsKey(command[1])){
								run.push(values.get(command[1]));
							}
							else{
								System.out.println("Undeclared variable "+command[1]+", initialize it with some value before passing it to function");
							}
							break;
				case "STORE"://check for type mismatch if variable already exists
							if ((values.containsKey(command[1])) && (values.get(command[1]).getClass() != run.peek().getClass())){
								 System.out.println("Type Mismatch error"); 
								 System.exit(0); //runtime error
							}
							else values.put(command[1], (run.pop()));
				 	      	break;
				case "ADD": 	run.push(Integer.toString(Integer.parseInt(run.pop()) + Integer.parseInt(run.pop())));
					    	break;
				case "MUL": 	run.push(Integer.toString(Integer.parseInt(run.pop()) * Integer.parseInt(run.pop())));
					    	break;
				case "SUB": 	run.push(Integer.toString(Integer.parseInt(run.pop()) - Integer.parseInt(run.pop())));
				     	    	break;
				case "DIV": 	run.push(Integer.toString(Integer.parseInt(run.pop()) / Integer.parseInt(run.pop())));
					    	break;
				case "PRINT": if(command[1].charAt(0) == '\"')
								System.out.println(line.split(" ", 2)[1].split("\"")[1]);
							else 
								System.out.println(values.get(command[1]));
				  	      	break;
				case "ME" : int a = Integer.parseInt(run.pop());
					    	int b = Integer.parseInt(run.pop());
					    	if(a >= b) condition = true;
					    	else condition = false;
					    	break;
				case "LE" : a = Integer.parseInt(run.pop());
		    				b = Integer.parseInt(run.pop());
					    	if(a <= b) condition = true;
					    	else condition = false;
						break;
				case "EQ" : a = Integer.parseInt(run.pop());
							b = Integer.parseInt(run.pop());
						if(a == b) condition = true;
						else condition = false;
						break;
				case "NEQ" : 	a = Integer.parseInt(run.pop());
							b = Integer.parseInt(run.pop());
						if(a != b) condition = true;
						else condition = false;
						break;
				case "LESS" : 	a = Integer.parseInt(run.pop());
							b = Integer.parseInt(run.pop());
						if(a < b) condition = true;
						else condition = false;
						break;	
				case "GRT" : 	a = Integer.parseInt(run.pop());
							b = Integer.parseInt(run.pop());
						if(a > b) condition = true;
						else condition = false;
						break;
				case "AND" : 	Boolean abool = Boolean.valueOf(run.pop());
						Boolean bbool = Boolean.valueOf(run.pop());
						if (abool == null || bbool == null)System.exit(0); //error to be done
						if(abool && bbool) condition = true;
						else condition = false;
						break;
				case "OR" : abool = Boolean.valueOf(run.pop());
						bbool = Boolean.valueOf(run.pop());
						if (abool == null || bbool == null)System.exit(0); //error to be done
						if(abool && bbool) condition = true;
						else condition = false;
						break;
				case "IFtrue" : if(!condition){
							while((line = br.readLine()).equals("Go-Endifelse") == false);  //Semi-Colon used to skip and avoid labeling
						}	
						break;
				case "IFfalse" : if(condition){
							while((line = br.readLine()).equals("EndIfelse") == false);  //Semi-Colon used to skip labeling and avoid labeling
						}
						break;
				case "Go-Endifelse": break;
				case "EndIfelse" :break;
				case "WStart" : whilelabel = command[1];
						break;
				case "Whiletrue" : if(!condition){
							while((line = br.readLine()).equals("WEnd") == false);
						}
						break;
				case "WEnd" :break;	
				case "Go-WStart": br.close();
							br = new BufferedReader(new FileReader(path));
							while((line = br.readLine()).equals("WStart "+whilelabel) == false);
							break;
				case "STKDEC": try {StackMap.put(command[1], new Stack<Integer>());}
							   catch(Exception e) {System.out.println("error in stack declaration" + e);}
							   finally {break;}
				case "STORESTK":try {StackMap.get(command[1]).push(Integer.parseInt(run.pop()));}
								catch(Exception e) {System.out.println("error in stack store" + e);}
								finally {break;}
				case "STKPEEK": try {run.push(Integer.toString(StackMap.get(command[1]).peek()));}
								catch(Exception e) {System.out.println("error in stack peek" + e);}
								finally {break;}
				case "STKPOP":  try {int delete = StackMap.get(command[1]).pop();}
								catch(Exception e) {System.out.println("error in stack pop" + e);}
								finally {break;}	
				case "FuncCall":
								//symtab.push(values);
								//System.out.println("Calling functinon "+command[1]);
								funcName = command[1];
								//System.out.println("Calling functinon "+funcName);
								while((line = br.readLine()).equals("FuncCall Ends") == false){
									//System.out.println(line);
									command = line.split(" ");
									switch(command[0]){
										case "PUSH": 	run.push((command[1]));
											break;
										case "LOAD": 	
											if(values.containsKey(command[1])){
												run.push(values.get(command[1]));
											}
											else{
												System.out.println("Undeclared variable "+command[1]+", initialize it with some value before passing it to function");
											}
											break;
										default: 	;//System.out.println("Not able to identify the argument passed to function " + command[0]);
									  } // End of inner switch series inside the while loop
								} //End of while
								symtab.push(new HashMap<String,String>(values));//push current function symbol table to stack
								fileReader.push(new BufferedReader(br));
								//break;
								
				case "FuncDef":
							   //if(!command[1].equals("ends")){
								br = new BufferedReader(new FileReader(path));	
								while((line = br.readLine()).equals("FuncDef "+funcName) == false); //Semi-Colon used to skip labeling, shortcut method
								//System.out.println("Found "+line);
								while((line = br.readLine()).equals(".funcBodyStarts "+funcName) == false){
									command = line.split(" ");
									switch(command[0]){
									case "STORE"://check for type mismatch if variable already exists
											if ((values.containsKey(command[1])) && (values.get(command[1]).getClass() != run.peek().getClass())){
												System.out.println("Type Mismatch error"); 
												System.exit(0); //runtime error
											}
											else {
												//System.out.println(values.get(command[1])); 
												values.put(command[1], (run.pop()));
											}
											break;
									default: 	;//System.out.println("Not able to identify the argument passed to function " + command[0]);
								  } // End of inner switch series inside the while loop
								}//end of while
								break;
				case "RETURN" : ;
				case ".funcbodyends" : values = symtab.pop();
									   br = fileReader.pop();
									   //System.out.println(mp.get(run.pop()));break;
				case ".funcends"     : break;
				case ".MainMethodEnds":break;			
				case "" : 	break;
				default: 	System.out.println("Error: command not found " + command[0]); //System.exit(0);
			}	
		}
		br.close();		
}
}




