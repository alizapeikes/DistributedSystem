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
					//In a synchronized block, because myJobs ArrayList is shared with the reader thread.
					empty = myJobs.isEmpty();
				}	
				while(!empty) {
					String currentJob;
					synchronized(myJobs_Lock) {
						currentJob = myJobs.get(0);
						myJobs.remove(0);
					}
					try {
						//If job is optimal for the slave type, slave sleeps for 2 seconds,
						//otherwise sleeps for 10 seconds.
						if(currentJob.charAt(0) == slaveType) {
							System.out.println("Sleeping for 2 seconds");
							sleep(2000); 
							System.out.println("Job " + currentJob + " completed.");
							System.out.println("Sending job " + currentJob + " to master.");
							//Concatenating the number of seconds the slave slept for the job which will
							//be used for calculations in master.
							responseWriter.println("02" + currentJob);
						}
						else {
							System.out.println("Sleeping for 10 seconds");
							sleep(10000);
							System.out.println("Job " + currentJob + " completed.");
							System.out.println("Sending job " + currentJob + " to master.");
							//Concatenating the number of seconds the slave slept for the job which will
							//be used for calculations in master.
							responseWriter.println("10" + currentJob);
						}
						
						sleep(250); // to slow down infinite loop
					}
					catch(Exception e) {}
					
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
				//while(true) {
					while((requestString = requestReader.readLine()) !=null) {
						System.out.println("Reading job " + requestString + " from master.");
						
						synchronized(myJobs_Lock){
							//Adding the job to the slaves list of jobs.
							myJobs.add(requestString);
						}
					}
					System.out.println("sleeping");
					sleep(250);
				//}
			}
			catch(IOException | InterruptedException e) {
				System.out.println("error");
			}	
		}
	}
}
