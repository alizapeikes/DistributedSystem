import java.net.*;
import java.io.*;
import java.util.*;

public class CCThread implements Runnable {
	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private ArrayList<String> jobs4Slave1;
	private ArrayList<String> jobs4Slave2;
	private Object jobs4Slave1_Lock;
	private Object jobs4Slave2_Lock;
	private int id;
	private LoadTracker tracker;
	private Object tracker1_lock;
	private Object tracker2_lock;
	
	public CCThread(PrintWriter responseWriter, int id, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock,  LoadTracker tracker, Object tracker1_lock, Object tracker2_lock) {
		this.responseWriter = responseWriter;
		this.id = id;
		this.jobs4Slave1 = jobs4Slave1;
		this.jobs4Slave2 = jobs4Slave2;
		this.jobs4Slave1_Lock = jobs4Slave1_Lock;
		this.jobs4Slave2_Lock = jobs4Slave2_Lock;
		this.tracker = tracker;
		this.tracker1_lock = tracker1_lock;
		this.tracker2_lock = tracker2_lock;
	}
	
	public CCThread(BufferedReader requestReader, int id, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock, LoadTracker tracker, Object tracker1_lock, Object tracker2_lock) {
		this.requestReader = requestReader;
		this.id = id;
		this.jobs4Slave1 = jobs4Slave1;
		this.jobs4Slave2 = jobs4Slave2;
		this.jobs4Slave1_Lock = jobs4Slave1_Lock;
		this.jobs4Slave2_Lock = jobs4Slave2_Lock;
		this.tracker = tracker;
		this.tracker1_lock = tracker1_lock;
		this.tracker2_lock = tracker2_lock;
	}
	
	@Override
	public void run() {
		if(requestReader != null) {
			try {
				String requestString;
				while((requestString = requestReader.readLine()) !=null) {
					char jobType = requestString.charAt(0);
					String jobName = requestString;
					synchronized(tracker1_lock) {
						if((jobType == 'a' && tracker.getSlaveALoad() - tracker.getSlaveBLoad() <= 8) || (jobType == 'b' && tracker.getSlaveBLoad() - tracker.getSlaveALoad() > 8)){
							synchronized(jobs4Slave1_Lock) {
								jobs4Slave1.add(jobName);
							} 
							int amount = jobType == 'a' ? 2: 10;
							tracker.addWorkA(amount);
							System.out.println("Adding job a to the job a list");
						}else {
							synchronized(jobs4Slave2_Lock) {
								jobs4Slave2.add(jobName);
							}
							int amount = jobType == 'b' ? 2: 10;
							tracker.addWorkB(amount);
							System.out.println("Adding job b to the job b list");
						}
					}	
				}
			}
			catch(IOException e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
		}
		else {
			System.out.println("Hi from else in CC Thread");
			
			int infinite = 0;
			while(infinite == 0) {
				responseWriter.println("hello");
				break;
			}
			
		}
		
			
		}
	
}
