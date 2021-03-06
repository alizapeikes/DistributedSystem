import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class ClientThread extends Thread {

	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private int clientID;
	
	public ClientThread(PrintWriter responseWriter, int id) {
		this.responseWriter = responseWriter;
		this.clientID = id;	
	}
	
	public ClientThread(BufferedReader requestReader, int id) {
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
				choice = input.nextLine().toLowerCase();
				
				while(!choice.equals("a") && !choice.equals("b") && !choice.equals("-1")) {
					System.out.println("Please enter a valid entry:a/b");
					choice = input.nextLine().toLowerCase();
				}
				
				if(!choice.equals("-1")) {
					choice = choice + clientID;
					clientID += 2;	//All jobs from each client have a client ID that corresponds to the original client's odd/even
					responseWriter.println(choice);
					System.out.println("Job " + choice + " sent to master");
				}
				
			  }while(!choice.equals("-1"));
		
		} 
		else {
			try{
				String requestString;
				while((requestString = requestReader.readLine()) !=null) {
					System.out.println("Job " + requestString + " Completed.");
				}
				sleep(250);
			}
			catch(IOException | InterruptedException g) {
				System.out.println("Error");
			}		
		}
	}
}
