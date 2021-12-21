import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WriteToSlave extends Thread {

	private PrintWriter responseWriter;
	private int id;
	private ArrayList<String> jobs1;
	private ArrayList<String> jobs2;
	private Object jobs2_Lock;
	private Object jobs1_Lock;

	public WriteToSlave(PrintWriter responseWriter, int id, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock) {
		this.responseWriter = responseWriter;
		this.id = id;
		this.jobs1 = jobs4Slave1;
		this.jobs2 = jobs4Slave2;
		this.jobs1_Lock = jobs4Slave1_Lock;
		this.jobs2_Lock = jobs4Slave2_Lock;

	}
	
	@Override
	public void run() {
		try {
			while(true) {
				if(id ==1) {
					synchronized(jobs1_Lock) {
						while(!jobs1.isEmpty()) {
							//Should this be here?
							System.out.println("Sending job " + jobs1.get(0) + " to Slave A.");
							responseWriter.println(jobs1.get(0));
							jobs1.remove(0);
						}
					}
				}
				else {
					synchronized(jobs2_Lock) {
						while(!jobs2.isEmpty()) {
							//Should this be here?
							System.out.println("Sending job " + jobs2.get(0) + " to Slave B.");
							responseWriter.println(jobs2.get(0));
							jobs2.remove(0);
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
