import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {

	private Socket incoming;
	ClientHandler(Socket socket){
		incoming=socket;
	}
	@Override
	public void run() {
		try
		{
			try
			{
				InputStream inStream=incoming.getInputStream();
				OutputStream outStream=incoming.getOutputStream();
				
				Scanner in=new Scanner(inStream);
				PrintWriter out=new PrintWriter(outStream,true);
				
				String lastWordOfLine;
				String subStr;
				while(in.hasNextLine()){
					String line=in.nextLine();
					lastWordOfLine=line.substring(line.lastIndexOf(" ")+1);
					if(line.equals("GET CONFIG PROPERTIES"))
						sendTextFromFile(out, "config.properties.txt");						
					else if(line.equals("GET MAPLIST"))
						sendTextFromFile(out, "maps.txt");						
					else if(line.equals("GET HIGHSCORES"))
						sendTextFromFile(out, "highscores.txt");						
					else if(line.equals("GET LEVEL INFO "+lastWordOfLine)){
						if(Integer.parseInt(lastWordOfLine) <= MainWindow.getAmountOfMaps())
						sendTextFromFile(out, "map"+Integer.parseInt(lastWordOfLine)+".txt");	
					}
					else if(line.equals("GAME SCORE")){
						String name=in.nextLine();
						int score=Integer.parseInt(in.nextLine());
						if(addScore(name,score))
							out.println("GAME SCORE ACCEPTED");
						else
							out.println("GAME SCORE NOT ACCEPTED");
							
					}
					
				}
				out.close();
			}
			finally
			{
				System.out.println("closing");
				incoming.close();				
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
	}
	public static Boolean addScore(String name, int score){
	
			ArrayList<String>rank=loadHighscoresToArrayList();
			for(int i=0; i<rank.size(); i+=2){
				if(i+1<rank.size()){
					int scoreFromHighscores=Integer.parseInt(rank.get(i+1));
					if(scoreFromHighscores<score){
						rank.add(i, Integer.toString(score));
						rank.add(i, name);
						rank.remove(rank.size()-1);
						rank.remove(rank.size()-1);
						saveHighscoresToFile(rank);
						return true;					
					}
			}		
		}
		return false;
	}
	public static ArrayList<String> loadHighscoresToArrayList(){
		ArrayList<String> rank=new ArrayList<>();
		try(BufferedReader br=new BufferedReader(new FileReader("highscores.txt"))) {
			String line;
			while((line=br.readLine())!=null){
				rank.add(line);
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		return rank;
	}
	public static void saveHighscoresToFile(ArrayList<String> highscores){
		File fold = new File("highscores.txt");
		fold.delete();
		File fnew= new File("highscores.txt");
		try{
			FileWriter f2=new FileWriter(fnew, false);
			BufferedWriter bw=new BufferedWriter(f2);
			for(int i=0; i<highscores.size(); i++){
				bw.write(highscores.get(i));
				bw.newLine();
			}
			bw.close();
		}		
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void sendTextFromFile(PrintWriter out, String fileName){
		try(BufferedReader br=new BufferedReader(new FileReader(fileName))) {
			String line;
			while((line=br.readLine())!=null){
				out.println(line);
			}
			out.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

}
