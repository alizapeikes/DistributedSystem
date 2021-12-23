import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WriteToSlave extends Thread {

	private PrintWriter responseWriter;
	private int id;
	private ArrayList<String> jobsA;
	private ArrayList<String> jobsB;
	private Object jobsA_Lock;
	private Object jobsB_Lock;

	public WriteToSlave(PrintWriter responseWriter, int id, ArrayList<String> jobs4SlaveA, ArrayList<String>jobs4SlaveB,
			Object jobs4SlaveA_Lock, Object jobs4SlaveB_Lock) {
		this.responseWriter = responseWriter;
		this.id = id;
		this.jobsA = jobs4SlaveA;
		this.jobsB = jobs4SlaveB;
		this.jobsA_Lock = jobs4SlaveA_Lock;
		this.jobsB_Lock = jobs4SlaveB_Lock;

	}
	
	@Override
	public void run() {
		try {
			while(true) {
				//Checks which slave to send the job to
				if(id == 1) {
					synchronized(jobsA_Lock) {
						while(!jobsA.isEmpty()) {
							System.out.println("Sending job " + jobsA.get(0) + " to Slave A.");
							responseWriter.println(jobsA.get(0));
							jobsA.remove(0);
						}
					}
				}
				else {
					synchronized(jobsB_Lock) {
						while(!jobsB.isEmpty()) {
							System.out.println("Sending job " + jobsB.get(0) + " to Slave B.");
							responseWriter.println(jobsB.get(0));
							jobsB.remove(0);
						}
					}
				}
				sleep(250); //to slow down infinite loop
			}
		}
		catch(Exception e) {
			System.out.println("Exception caught when trying to listen on port in thread");
		}
	}
}
