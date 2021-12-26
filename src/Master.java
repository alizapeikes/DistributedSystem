/*
 * Looked over master and both client classes. In middle of looking over ReadFromClient
 * reminder to take away comments by infinite loops
 */
import java.net.*;
import java.io.*;
import java.util.*;
public class Master {
	
	public static void main(String[] args) {
		
		int portNumber1 = 30121; //Port to connect to clients
		int portNumber2 = 30122; //Port to connect to slaves
		System.out.println("Master running");
		try(
			//Resources for clients
			ServerSocket clientCommunicate = new ServerSocket(portNumber1);
			Socket client1Socket = clientCommunicate.accept();
			Socket client2Socket = clientCommunicate.accept();
			PrintWriter cResponseWriter1 = new PrintWriter(client1Socket.getOutputStream(), true);
			BufferedReader cRequestReader1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));
			PrintWriter cResponseWriter2 = new PrintWriter(client2Socket.getOutputStream(), true);
			BufferedReader cRequestReader2 = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));
				
			//Resources for slaves
			ServerSocket slaveCommunicate = new ServerSocket(portNumber2);
			Socket slave1Socket = slaveCommunicate.accept();
			Socket slave2Socket = slaveCommunicate.accept();
			PrintWriter sResponseWriter1 = new PrintWriter(slave1Socket.getOutputStream(), true);
			BufferedReader sRequestReader1 = new BufferedReader(new InputStreamReader(slave1Socket.getInputStream()));
			PrintWriter sResponseWriter2 = new PrintWriter(slave2Socket.getOutputStream(), true);
			BufferedReader sRequestReader2 = new BufferedReader(new InputStreamReader(slave2Socket.getInputStream()));
		 	
		){	
			System.out.println("Clients connected");
			ArrayList<String> jobs4SlaveA = new ArrayList<>();	//List of jobs to be sent to slave A
			ArrayList<String> jobs4SlaveB = new ArrayList<>();	//List of jobs to be sent to slave B
			ArrayList<String> jobs1Done = new ArrayList<>();   	//Client1's finished jobs
			ArrayList<String> jobs2Done = new ArrayList<>();   	//Client2's finished jobs
			LoadTracker tracker = new LoadTracker();			//To track each slave's load
			Object jobs4Slave1_lock = new Object();
			Object jobs4Slave2_lock = new Object();
			Object jobs1Done_lock = new Object();
			Object jobs2Done_lock = new Object();
			Object tracker_lock = new Object();		
			
			ArrayList<Thread> threads = new ArrayList<>();
			
			//Client threads
			threads.add(new WriteToClient(cResponseWriter1, 1, jobs1Done, jobs1Done_lock));
			threads.add(new ReadFromClient(cRequestReader1, jobs4SlaveA, jobs4SlaveB, jobs4Slave1_lock, jobs4Slave2_lock, tracker, tracker_lock));
			threads.add(new WriteToClient(cResponseWriter2, 2, jobs2Done, jobs2Done_lock));
			threads.add(new ReadFromClient(cRequestReader2, jobs4SlaveA, jobs4SlaveB, jobs4Slave1_lock, jobs4Slave2_lock, tracker, tracker_lock));
			
			//Slave threads
			threads.add(new WriteToSlave(sResponseWriter1, 1, jobs4SlaveA, jobs4SlaveB, jobs4Slave1_lock, jobs4Slave2_lock));
			threads.add(new ReadFromSlave(sRequestReader1, 1, jobs1Done, jobs2Done, jobs1Done_lock, jobs2Done_lock, tracker, tracker_lock));
			threads.add(new WriteToSlave(sResponseWriter2, 2, jobs4SlaveA, jobs4SlaveB, jobs4Slave1_lock, jobs4Slave2_lock));
			threads.add(new ReadFromSlave(sRequestReader2, 2, jobs1Done, jobs2Done, jobs1Done_lock, jobs2Done_lock, tracker, tracker_lock));
			
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
