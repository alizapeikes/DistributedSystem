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
		if(responseWriter != null) {
			
		}
		
		else {
			try{
				String requestString;
				System.out.println("Hi from else in MC thread");
				while((requestString = requestReader.readLine()) !=null) {
					System.out.println(requestString);
				}
			}
			catch(IOException e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
				
		}
	}
}

