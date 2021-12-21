import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ReadFromSlave extends Thread{
	
	private BufferedReader requestReader;
	private int id;
	private ArrayList<String> jobs1;
	private ArrayList<String> jobs2;
	private Object jobs1_Lock;
	private Object jobs2_Lock;
	private LoadTracker loadTracker;
	private Object loadTracker_Lock;
	
	
	public ReadFromSlave(BufferedReader requestReader, int id, ArrayList<String> jobs1, ArrayList<String>jobs2,
			Object jobs1_Lock, Object jobs2_Lock, LoadTracker loadTracker, Object loadTracker_Lock) {
		this.requestReader = requestReader;
		this.id = id;
		this.jobs1 = jobs1;
		this.jobs2 = jobs2;
		this.jobs1_Lock = jobs1_Lock;
		this.jobs2_Lock = jobs2_Lock;
		this.loadTracker = loadTracker;
		this.loadTracker_Lock = loadTracker_Lock;
	}
	

	@Override
	public void run() {
		try {
			String requestString;
			while((requestString = requestReader.readLine()) !=null) {
				if(Integer.parseInt(requestString.substring(3)) % 2 ==0) {
					synchronized(jobs2_Lock) {
						//Should this be here?
						System.out.println("Job " + requestString.substring(2) + " Completed.");
						jobs2.add(requestString.substring(2));
					}						
				}
				else {
					synchronized(jobs1_Lock) {
						//Should this be here?
						System.out.println("Job " + requestString.substring(2) + " Completed.");
						jobs1.add(requestString.substring(2));
					}
				}
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
			}
			
		}
		catch(IOException e) {
			System.out.println("Exception caught when trying to listen on port");
		}
			
	}
	
}
