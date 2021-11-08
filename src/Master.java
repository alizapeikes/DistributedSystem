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
		try(ServerSocket clientCommunicate = new ServerSocket(portNumber);
			 Socket client1Socket = clientCommunicate.accept();
			 Socket client2Socket = clientCommunicate.accept();
			 PrintWriter responseWriter1 = new PrintWriter(client1Socket.getOutputStream(), true);
			 BufferedReader requestReader1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));
			 PrintWriter responseWriter2 = new PrintWriter(client2Socket.getOutputStream(), true);
			 BufferedReader requestReader2 = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));
			 	
		){	
			ArrayList<Integer> jobs4Slave1 = new ArrayList<>();
			ArrayList<Integer> jobs4Slave2 = new ArrayList<>();
			Object jobs4Slave1_lock = new Object();
			Object jobs4Slave2_lock = new Object();
			
			
			ArrayList<Thread> threads = new ArrayList<>();
			threads.add(new Thread(new CCThread(responseWriter1, 1)));
			threads.add(new Thread(new CCThread(requestReader1, 1, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock)));
			threads.add(new Thread(new CCThread(responseWriter2, 2)));
			threads.add(new Thread(new CCThread(requestReader2, 2, jobs4Slave1, jobs4Slave2, jobs4Slave1_lock, jobs4Slave2_lock)));
			
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
