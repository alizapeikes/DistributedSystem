import java.net.*;
import java.io.*;
import java.util.*;

public class CCThread extends Thread {
	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private ArrayList<String> jobs4Slave1;
	private ArrayList<String> jobs4Slave2;
	private Object jobs4Slave1_Lock;
	private Object jobs4Slave2_Lock;
	private int id;
	private LoadTracker tracker;
	private Object tracker_lock;
	private ArrayList<String> finishedJobs;
	private Object finishedJobs_Lock;
	
	public CCThread(PrintWriter responseWriter, int id, ArrayList<String> jobs, Object jobs_lock) {
		this.responseWriter = responseWriter;
		this.id = id;
		finishedJobs = jobs;
		finishedJobs_Lock = jobs_lock;
		
	}
	
	public CCThread(BufferedReader requestReader, int id, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock, LoadTracker tracker, Object tracker_lock) {
		this.requestReader = requestReader;
		this.id = id;
		this.jobs4Slave1 = jobs4Slave1;
		this.jobs4Slave2 = jobs4Slave2;
		this.jobs4Slave1_Lock = jobs4Slave1_Lock;
		this.jobs4Slave2_Lock = jobs4Slave2_Lock;
		this.tracker = tracker;
		this.tracker_lock = tracker_lock;
	}
	
	@Override
	public void run() {
		if(requestReader != null) {
			try {
				while(true) {
					String requestString;
					while((requestString = requestReader.readLine()) !=null) {
						char jobType = requestString.charAt(0);
						String jobName = requestString;
						int slaveALoad;
						int slaveBLoad;
						synchronized(tracker_lock) {
							slaveALoad = tracker.getSlaveALoad();
							slaveBLoad = tracker.getSlaveBLoad();
							System.out.println("Slave A Load: " + slaveALoad);
							System.out.println("Slave B Load: " + slaveBLoad);
							
						}
						if((jobType == 'a' && slaveALoad - slaveBLoad <= 8) || (jobType == 'b' && slaveBLoad - slaveALoad > 8)){
							synchronized(jobs4Slave1_Lock) {
								jobs4Slave1.add(jobName);
							} 
							int amount = jobType == 'a' ? 2: 10;
							tracker.addWorkA(amount);
							System.out.println("Adding job " +  jobName + " to the job A list");
						}else {
							synchronized(jobs4Slave2_Lock) {
								jobs4Slave2.add(jobName);
							}
							int amount = jobType == 'b' ? 2: 10;
							tracker.addWorkB(amount);
							System.out.println("Adding job " + jobName + " to the job B list");
						}	
					}
				}	
			}
			catch(IOException e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
		}
		else {
			while(true) {
				synchronized(finishedJobs_Lock) {
					if(!finishedJobs.isEmpty()) {
						//Which client are we sending it to??
						System.out.println("Sending job " + finishedJobs.get(0) + " to Client" + id);
						responseWriter.println(finishedJobs.get(0));
						finishedJobs.remove(0);
					}
				}
				try {
					sleep(250);
				}
				catch(Exception e) {
					
				}
			}
			
		}
		
			
		}
	
}
