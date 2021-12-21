import java.io.*;
import java.util.*;

public class ReadFromClient extends Thread {
	private BufferedReader requestReader;
	private ArrayList<String> jobs4Slave1;
	private ArrayList<String> jobs4Slave2;
	private Object jobs4Slave1_Lock;
	private Object jobs4Slave2_Lock;
	private LoadTracker tracker;
	private Object tracker_lock;
	
	public ReadFromClient(BufferedReader requestReader, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock, LoadTracker tracker, Object tracker_lock) {
		this.requestReader = requestReader;
		this.jobs4Slave1 = jobs4Slave1;
		this.jobs4Slave2 = jobs4Slave2;
		this.jobs4Slave1_Lock = jobs4Slave1_Lock;
		this.jobs4Slave2_Lock = jobs4Slave2_Lock;
		this.tracker = tracker;
		this.tracker_lock = tracker_lock;
	}
	
	@Override
	public void run() {
		try {
			//while(true) {
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
						synchronized(tracker_lock) {
							tracker.addWorkA(amount);
						}
						System.out.println("Adding job " +  jobName + " to the job A list");
					}else {
						synchronized(jobs4Slave2_Lock) {
							jobs4Slave2.add(jobName);
						}
						int amount = jobType == 'b' ? 2: 10;
						synchronized(tracker_lock) {
							tracker.addWorkB(amount);
						}
						System.out.println("Adding job " + jobName + " to the job B list");
					}	
				}
			//}	
		}
		catch(IOException e) {
			System.out.println("Exception caught when trying to listen on port in thread");
		}		
		
	}
	
}
