import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

interface ServerStatusListener {
	void updateStatus(String str);
}

public class ConfigServer implements Runnable{
	
	private int port;
	private ServerStatusListener listener;
	ConfigServer(int port){
		listener=null;
		this.port=port;
	}
	
	public void addListener(ServerStatusListener listener){
		this.listener=listener;
	}
	
	public void start() throws IOException 
	{
		ServerSocket serverSocket=null;
		
		try{
			serverSocket=new ServerSocket(port);
		}
		catch(IOException e)
		{
			listener.updateStatus("Could not listen on port:"+port);
		}
		
		System.out.println("Waiting for connection...");
		while(true){
			try {
				Socket clientSocket=serverSocket.accept();
				Runnable r=new ClientHandler(clientSocket);
				Thread t=new Thread(r);
				t.start();
			}
			catch(IOException e)
			{
				System.err.println("Accept failed.");
				System.exit(1);
			}
			System.out.println("Connection succesful");
			System.out.println("waiting for input...");
			
			
			
			
		}
	}
	public void callUpdateStatusListener(String str){
		listener.updateStatus(str);
	}

	@Override
	public void run(){
		try {
			start();
			callUpdateStatusListener("running");
			
		} catch (IOException e) {
			e.printStackTrace();
			callUpdateStatusListener(e.getMessage());
		}
		
	}
}
