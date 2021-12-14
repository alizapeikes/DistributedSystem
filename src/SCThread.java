import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SCThread extends Thread{
	
	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private int id;
	private ArrayList<String> jobs1;
	private ArrayList<String> jobs2;
	private Object jobs1_Lock;
	private Object jobs2_Lock;
	private LoadTracker loadTracker;
	private Object loadTracker_Lock;
	
	public SCThread(PrintWriter responseWriter, int id, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock) {
		this.responseWriter = responseWriter;
		this.id = id;
		this.jobs1 = jobs4Slave1;
		this.jobs2 = jobs4Slave2;
		this.jobs1_Lock = jobs4Slave1_Lock;
		this.jobs2_Lock = jobs4Slave2_Lock;

	}
	public SCThread(BufferedReader requestReader, int id, ArrayList<String> jobs1, ArrayList<String>jobs2,
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
		if(responseWriter != null) {
			try {
				while(true) {
					if(id ==1) {
						synchronized(jobs1_Lock) {
							while(!jobs1.isEmpty()) {
								//Should this be here?
								System.out.println("Sending job " + jobs1.get(0) + " to slave 1.");
								responseWriter.print(jobs1.get(0));
								jobs1.remove(0);
							}
						}
					}
					else {
						synchronized(jobs2_Lock) {
							while(!jobs2.isEmpty()) {
								//Should this be here?
								System.out.println("Sending job " + jobs2.get(0) + " to slave 2.");
								responseWriter.print(jobs2.get(0));
								jobs1.remove(0);
							}
						}
					}
					sleep(1000); //to slow down infinite loop
				}
			}
			catch(Exception e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
		}
		else {
			try {
				String requestString;
				while((requestString = requestReader.readLine()) !=null) {
					if(Integer.parseInt(requestString.substring(3))%2 ==0) {
						synchronized(jobs1_Lock) {
							//Should this be here?
							System.out.println(jobs1.get(0) + " Completed.");
							jobs1.add(requestString);
						}						
					}
					else {
						synchronized(jobs2_Lock) {
							//Should this be here?
							System.out.println(jobs2.get(0) + " Completed.");
							jobs2.add(requestString);
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
	
}

