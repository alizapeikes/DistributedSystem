

import java.net.*;
import java.io.*;
import java.util.*;

public class CCThread implements Runnable {
	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private ArrayList<Integer> jobs4Slave1;
	private ArrayList<Integer> jobs4Slave2;
	private Object jobs4Slave1_Lock;
	private Object jobs4Slave2_Lock;
	private int id;
	
	public CCThread(PrintWriter responseWriter, int id) {
		this.responseWriter = responseWriter;
		this.id = id;
		
	}
	
	public CCThread(BufferedReader requestReader, int id, ArrayList<Integer> jobs4Slave1, ArrayList<Integer>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock) {
		this.requestReader = requestReader;
		this.id = id;
		this.jobs4Slave1 = jobs4Slave1;
		this.jobs4Slave2 = jobs4Slave2;
		this.jobs4Slave1_Lock = jobs4Slave1_Lock;
		this.jobs4Slave2_Lock = jobs4Slave2_Lock;
	}
	
	@Override
	public void run() {
			try {
				if(requestReader != null) {
					String requestString;
					while((requestString = requestReader.readLine()) !=null) {
						char jobType = requestString.charAt(0);
						int jobID = Integer.parseInt(requestString.substring(1));
						if(jobType == 'a') {
							synchronized(jobs4Slave1_Lock) {
								jobs4Slave1.add(jobID);
							}
							System.out.println("Adding job a to the job a list");
						}else {
							synchronized(jobs4Slave2_Lock) {
								jobs4Slave2.add(jobID);
							}
							System.out.println("Adding job b to the job b list");
						}	
					}
				}
				else {
				}
			} catch(IOException e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
		
			
		}
	
}
