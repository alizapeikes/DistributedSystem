import java.io.*;
import java.util.*;

public class ReadFromClient extends Thread {
	private BufferedReader requestReader;
	private ArrayList<String> jobs4SlaveA;
	private ArrayList<String> jobs4SlaveB;
	private Object jobs4SlaveA_Lock;
	private Object jobs4SlaveB_Lock;
	private LoadTracker tracker;
	private Object tracker_lock;
	
	public ReadFromClient(BufferedReader requestReader, ArrayList<String> jobs4SlaveA, ArrayList<String>jobs4SlaveB,
			Object jobs4SlaveA_Lock, Object jobs4SlaveB_Lock, LoadTracker tracker, Object tracker_lock) {
		this.requestReader = requestReader;
		this.jobs4SlaveA = jobs4SlaveA;
		this.jobs4SlaveB = jobs4SlaveB;
		this.jobs4SlaveA_Lock = jobs4SlaveA_Lock;
		this.jobs4SlaveB_Lock = jobs4SlaveB_Lock;
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
					
					//Reading tracker information to determine which slave to send the job to.
					synchronized(tracker_lock) {
						slaveALoad = tracker.getSlaveALoad();
						slaveBLoad = tracker.getSlaveBLoad();
					}

					//Algorithm determines which slave to send the job to
					if((jobType == 'a' && slaveALoad - slaveBLoad <= 8) || (jobType == 'b' && slaveBLoad - slaveALoad > 8)){
						synchronized(jobs4SlaveA_Lock) {
							jobs4SlaveA.add(jobName);
						} 
						
						//Adding the correct 'work' to the tracker.
						int amount = jobType == 'a' ? 2: 10;
						synchronized(tracker_lock) {
							tracker.addWorkA(amount);
						}
						
						System.out.println("Adding job " +  jobName + " to the job A list");
					}else {
						synchronized(jobs4SlaveB_Lock) {
							jobs4SlaveB.add(jobName);
						}
						
						//Adding the correct 'work' to the tracker.
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
