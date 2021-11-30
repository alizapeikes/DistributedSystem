/*
 	Array list of jobs to send and remove them in sc thread when they are actually sent. we are going to use a while
 	loop to control this. Then when the slave finishes a job the master adds it to a different arraylist, and the scthread
 	reads from this arraylist and sends the jobs back to the client.
 	find out about synchronized block regarding adding info to an arrayList.
 */
import java.net.*;
import java.io.*;
import java.util.*;
public class Master {
	public static void main(String[] args) {
		
		//Hardcode port number
		args = new String[] {"30121"};
		if(args.length != 1) {
			System.err.println("Usage: Java master <port number>");
			System.exit(1);
		}
		
		int portNumber = Integer.parseInt(args[0]);
		
		//Hard coded for 2 clients
		int clients = 2;
		try(
			//Resources for clients
			ServerSocket clientCommunicate = new ServerSocket(portNumber);
			Socket client1Socket = clientCommunicate.accept();
			Socket client2Socket = clientCommunicate.accept();
			PrintWriter cResponseWriter1 = new PrintWriter(client1Socket.getOutputStream(), true);
			BufferedReader cRequestReader1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));
			PrintWriter cResponseWriter2 = new PrintWriter(client2Socket.getOutputStream(), true);
			BufferedReader cRequestReader2 = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));
				
			//Resources for slaves
			ServerSocket slaveCommunicate = new ServerSocket(portNumber);
			Socket slave1Socket = clientCommunicate.accept();
			Socket slave2Socket = clientCommunicate.accept();
			PrintWriter sResponseWriter1 = new PrintWriter(client1Socket.getOutputStream(), true);
			BufferedReader sRequestReader1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));
			PrintWriter sResponseWriter2 = new PrintWriter(client2Socket.getOutputStream(), true);
			BufferedReader sRequestReader2 = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));
		 	
		){	
			ArrayList<String> jobs4Slave1 = new ArrayList<>();
			ArrayList<String> jobs4Slave2 = new ArrayList<>();
			Object jobs4Slave1_lock = new Object();
			Object jobs4Slave2_lock = new Object();
			LoadTracker tracker = new LoadTracker();
			Object tracker1_lock = new Object();
			Object tracker2_lock = new Object();
			
			
			ArrayList<Thread> threads = new ArrayList<>();
			//Client threads
			threads.add(new Thread(new CCThread(cResponseWriter1, 1, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock, tracker, tracker1_lock, tracker2_lock)));
			threads.add(new Thread(new CCThread(cRequestReader1, 1, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock, tracker, tracker1_lock, tracker2_lock)));
			threads.add(new Thread(new CCThread(cResponseWriter2, 2, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock, tracker, tracker1_lock, tracker2_lock)));
			threads.add(new Thread(new CCThread(cRequestReader2, 2, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock, tracker, tracker1_lock, tracker2_lock)));
			
			//Slave threads
			threads.add(new Thread(new SCThread(sResponseWriter1, 1, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock)));
			threads.add(new Thread(new SCThread(sRequestReader1, 1, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock)));
			threads.add(new Thread(new SCThread(sResponseWriter2, 2, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock)));
			threads.add(new Thread(new SCThread(sRequestReader2, 2, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock)));
			
			for(Thread t: threads) {
				t.start();
			}
			
			try{
				for(Thread t: threads) {
					t.join();
				}
			}
			catch(Exception e) {};
			
		
		}catch(IOException e) {
			System.out.println("Exception caught when trying to listen on port");
			System.out.println(e.getMessage());
		}
		
}
}	
