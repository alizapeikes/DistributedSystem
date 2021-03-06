
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client1 {
	public static void main(String[] args) {
		
	  int clientID = 1;
	  String hostName = "127.0.0.1";
	  int portNumber = 30121;

	  try (
		       Socket clientSocket = new Socket(hostName, portNumber);
		        		
		       BufferedReader requestReader= // stream to read text response from server
		       new BufferedReader(
		            new InputStreamReader(clientSocket.getInputStream())); 	
		        		
		        PrintWriter responeWriter = // stream to write text requests to server
		            new PrintWriter(clientSocket.getOutputStream(), true);
		 ){
		  	  System.out.println("Client 1 connected to server");
			  ClientThread reader = new ClientThread (requestReader, clientID);
			  ClientThread writer = new ClientThread (responeWriter, clientID);
			  
			  reader.start();
			  writer.start();
			  
			  try{
					reader.join();
					writer.join();
			  }
			  catch(Exception e) {};
			  
			  
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
