import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SCThread extends Thread{
	
	private PrintWriter responseWriter = null;
	private BufferedReader requestReader = null;
	private ArrayList<String> jobs4Slave1;
	private ArrayList<String> jobs4Slave2;
	private Object jobs4Slave1_Lock;
	private Object jobs4Slave2_Lock;
	
	public SCThread(PrintWriter responseWriter, int id, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock) {
		this.responseWriter = responseWriter;
		this.jobs4Slave1 = jobs4Slave1;
		this.jobs4Slave2 = jobs4Slave2;
		this.jobs4Slave1_Lock = jobs4Slave1_Lock;
		this.jobs4Slave2_Lock = jobs4Slave2_Lock;
	}
	
	public SCThread(BufferedReader requestReader, int id, ArrayList<String> jobs4Slave1, ArrayList<String>jobs4Slave2,
			Object jobs4Slave1_Lock, Object jobs4Slave2_Lock) {
		this.requestReader = requestReader;
		this.jobs4Slave1 = jobs4Slave1;
		this.jobs4Slave2 = jobs4Slave2;
		this.jobs4Slave1_Lock = jobs4Slave1_Lock;
		this.jobs4Slave2_Lock = jobs4Slave2_Lock;
	}
	

	@Override
	public void run() {
		if(requestReader != null) {
			try {
				System.out.println("hi");
			}
			catch(IOException e) {
				System.out.println("Exception caught when trying to listen on port in thread");
			}
		}
		else {
			
		}
			
	}
	
}

