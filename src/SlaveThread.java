import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class SlaveThread extends Thread {

	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private char slaveType;
	private ArrayList<String> myJobs;
	private Object myJobs_Lock;
	
	public SlaveThread(PrintWriter responseWriter, char type, ArrayList<String> myJobs, Object myJobs_Lock) {
		this.responseWriter = responseWriter;
		this.slaveType = type;	
		this.myJobs = myJobs;
		this.myJobs_Lock = myJobs_Lock;
	}
	
	public SlaveThread(BufferedReader requestReader, char type, ArrayList<String> myJobs, Object myJobs_Lock) {
		this.requestReader = requestReader;
		this.slaveType = type;
		this.myJobs = myJobs;
		this.myJobs_Lock = myJobs_Lock;
	}
	
	@Override
	public void run() {
		//writer thread
		if(responseWriter != null) {
			while(true) {
				boolean empty;
				synchronized(myJobs_Lock){
					//Reading to see if there's a job to work on. 
					//In synchronized because myJobs arrayList shared with the reader thread.
					//Even if the reader gets control of the myJobs list before the next while
					//loop executes, won't cause indexOutOfBounds exception because it will only add to the list not remove.
					//The removing can only happen later on in this thread
					empty = myJobs.isEmpty();
				}	
				while(!empty) {
					String currentJob;
					synchronized(myJobs_Lock) {
						currentJob = myJobs.get(0);
						myJobs.remove(0);
					}
					try {
						if(currentJob.charAt(0) == slaveType) {
							sleep(2000);
							//Should we synchronize here?
							System.out.println("Sleeping for 2 seconds");
							System.out.println("Job " + currentJob + " completed.");
							System.out.println("Sending job " + currentJob + " to master.");
							responseWriter.println("02" + currentJob);
						}
						else {
							sleep(10000);
							//Should we synchronize here?
							System.out.println("Sleeping for 10 seconds");
							System.out.println("Job " + currentJob + " completed.");
							System.out.println("Sending job " + currentJob + " to master.");
							responseWriter.println("10" + currentJob);
						}
						
						sleep(250); // to slow down infinite loop
					}
					catch(Exception e) {
					}
					synchronized(myJobs_Lock){
						//see comment above
						empty = myJobs.isEmpty();
					}
				}
			}
		}
		
		//reader thread
		else {
			try{
				String requestString;
				while(true) {
					while((requestString = requestReader.readLine()) !=null) {
						//should this be in synchronized block???
						System.out.println("Reading job " + requestString + " from master.");
						synchronized(myJobs_Lock){
							myJobs.add(requestString);
						}
					}
					System.out.println("sleeping");
					sleep(250);
				}
			}
			catch(IOException | InterruptedException e) {
				System.out.println("error");
			}
				
		}
	}
}
