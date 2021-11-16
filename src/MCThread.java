import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class MCThread extends Thread {

	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private int clientID;
	
	public MCThread(PrintWriter responseWriter, int id) {
		this.responseWriter = responseWriter;
		this.clientID = id;	
	}
	
	public MCThread(BufferedReader requestReader, int id) {
		this.requestReader = requestReader;
		this.clientID = id;
	}
	
	@Override
	public void run() {
		if(responseWriter != null) {
			Scanner input = new Scanner(System.in);
			String choice;
			do {
				System.out.println("Enter -1 to quit");
				System.out.println("Please enter job type:a/b");
				choice = input.nextLine();
				while(!choice.equals("a") && !choice.equals("b") && !choice.equals("-1")) {
					System.out.println("Please enter a valid entry:a/b");
					choice = input.nextLine();
				}
				//TODO code for -1
				choice = choice + clientID;
				clientID+=2;
				responseWriter.println(choice);
				
			  }while(!choice.equals("-1"));
		}
		
		else {
			try{
				String requestString;
				System.out.println("Hi from else in MC thread");
				while((requestString = requestReader.readLine()) !=null) {
					System.out.println(requestString);
				}
			}
			catch(IOException e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
				
		}
	}
}

