import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ReadFromSlave extends Thread{
	
	private BufferedReader requestReader;
	private int id;
	private ArrayList<String> finishedJobs1;
	private ArrayList<String> finishedJobs2;
	private Object jobs1_Lock;
	private Object jobs2_Lock;
	private LoadTracker loadTracker;
	private Object loadTracker_Lock;
	
	
	public ReadFromSlave(BufferedReader requestReader, int id, ArrayList<String> jobs1, ArrayList<String>jobs2,
			Object jobs1_Lock, Object jobs2_Lock, LoadTracker loadTracker, Object loadTracker_Lock) {
		this.requestReader = requestReader;
		this.id = id;
		this.finishedJobs1 = jobs1;
		this.finishedJobs2 = jobs2;
		this.jobs1_Lock = jobs1_Lock;
		this.jobs2_Lock = jobs2_Lock;
		this.loadTracker = loadTracker;
		this.loadTracker_Lock = loadTracker_Lock;
	}
	

	@Override
	public void run() {
		try {
			String requestString;
			while((requestString = requestReader.readLine()) != null) {
				System.out.println("Job " + requestString.substring(2) + " Completed.");
				
				//Subtracting the work load from the tracker based on the amount
				//of time(02/10) which was concatenated with the job in the slave
				if(id ==1) {
					synchronized(loadTracker_Lock) {
						loadTracker.removeWorkA(Integer.parseInt(requestString.substring(0,2)));
					}
				}
				else {
					synchronized(loadTracker_Lock) {
						loadTracker.removeWorkB(Integer.parseInt(requestString.substring(0,2)));
					}
				}
				
				//Checking which client the job belongs to based on if
				//the job number is odd or even
				if(Integer.parseInt(requestString.substring(3)) % 2 ==0) {
					synchronized(jobs2_Lock) {
						//Adding to the finished jobs list
						finishedJobs2.add(requestString.substring(2));
					}						
				}
				else {
					synchronized(jobs1_Lock) {
						//Adding to the finished jobs list
						finishedJobs1.add(requestString.substring(2));
					}
				}
			}
			
		}catch(IOException e) {
			System.out.println("Exception caught when trying to listen on port");
		}	
	}
}
