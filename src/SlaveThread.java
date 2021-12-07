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
				String currentJob;
				synchronized(myJobs_Lock) {
					currentJob = myJobs.get(0);
					myJobs.remove(0);
				}
				try {
					if(currentJob.charAt(0) == slaveType) {
						sleep(2000);
						responseWriter.print("02" + currentJob);
					}
					else {
						sleep(10000);
						responseWriter.print("10" + currentJob);
					}
					
					sleep(1000); // to slow down infinite loop
				}
				catch(Exception e) {
				}
			}
		}
		
		//reader thread
		else {
			try{
				String requestString;
				while((requestString = requestReader.readLine()) !=null) {
					synchronized(myJobs_Lock){
						myJobs.add(requestString);
					}
				}
			}
			catch(IOException e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
				
		}
	}
}

