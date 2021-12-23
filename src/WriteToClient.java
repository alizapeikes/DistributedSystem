import java.io.PrintWriter;
import java.util.ArrayList;

public class WriteToClient extends Thread{
	
	private PrintWriter responseWriter;
	private int id;
	private ArrayList<String> finishedJobs;
	private Object finishedJobs_Lock;

	public WriteToClient(PrintWriter responseWriter, int id, ArrayList<String> jobs, Object jobs_lock) {
		this.responseWriter = responseWriter;
		this.id = id;
		finishedJobs = jobs;
		finishedJobs_Lock = jobs_lock;
	}
	
	@Override
	public void run() {
		while(true) {
			
			synchronized(finishedJobs_Lock) {
				if(!finishedJobs.isEmpty()) {
					System.out.println("Sending job " + finishedJobs.get(0) + " to Client" + id);
					responseWriter.println(finishedJobs.get(0));
					finishedJobs.remove(0);
				}
			}
			
			try {
				sleep(250);
			}
			catch(Exception e) {
				
			}
		}
	}

}
