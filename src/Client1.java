import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
public class Client1 {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String choice;
		int id = 0;
		
	  String hostName = "127.0.0.1";
	  int portNumber = 30121;

	  try (
	       Socket clientSocket = new Socket(hostName, portNumber);
	        		
	       BufferedReader responseReader= // stream to read text response from server
	          new BufferedReader(
	            new InputStreamReader(clientSocket.getInputStream())); 	
	        		
	        PrintWriter requestWriter = // stream to write text requests to server
	            new PrintWriter(clientSocket.getOutputStream(), true);
	   ){
		  
		  do {
			System.out.println("Enter -1 to quit");
			System.out.println("Please enter job type:a/b");
			choice = input.nextLine();
			while(!choice.equals("a") && !choice.equals("b") && !choice.equals("-1")) {
				System.out.println("Please enter a valid entry:a/b");
				choice = input.nextLine();
			}
			choice = choice + id;
			id+=2;
			requestWriter.println(choice);
				
			
		  }while(!choice.equals("-1"));
		  
	  } catch (UnknownHostException e) {
        System.err.println("Don't know about host " + hostName);
        System.exit(1);
	  } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to " +
            hostName);
        System.exit(1);
    } 
		
		
	}
}
