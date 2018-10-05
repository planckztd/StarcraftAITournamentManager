package utility;

import java.io.*;
import java.util.Vector;

import objects.Bot;
import objects.Map;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class GameListGenerator 
{

	public static void GenerateGames(int rounds, Vector<Map> maps, Vector<Bot> bots, String TournamentType) 
	{
		try 
		{
			FileWriter fstream = new FileWriter("games.txt");
			
			BufferedWriter out = new BufferedWriter(fstream);
			
			if(TournamentType.equalsIgnoreCase("1VsAll"))
			{
				generate1VsAll(rounds, maps, bots, out);
			}
			else
			{
				generateRoundRobin(rounds, maps, bots, out);
			}
			
			out.write("");
			out.flush();
			out.close();
			
			System.out.println("Generation Complete");
			
		} 
		catch (Exception e) 
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void generateRoundRobin(int rounds, Vector<Map> maps, Vector<Bot> bots, BufferedWriter out) throws IOException 
	{
		int gameID = 0;
		int roundNum = 0;
		
		for (int i = 0; i < rounds; i++) 
		{
			for(Map m : maps)
			{
				for (int j = 0; j < bots.size(); j++) 
				{
					for (int k = j+1; k < bots.size(); k++) 
					{						
						if (roundNum % 2 == 0) 
						{
							out.write(getGameString(gameID, roundNum, bots.get(j).getName(), bots.get(k).getName(), m.getMapName()) + System.getProperty("line.separator"));
							gameID++;
						} 
						else 
						{
							out.write(getGameString(gameID, roundNum, bots.get(k).getName(), bots.get(j).getName(), m.getMapName()) + System.getProperty("line.separator"));
							gameID++;
						}
					}
				}
				roundNum++;
			}
		}
	}
	
	public static void generate1VsAll(int rounds, Vector<Map> maps, Vector<Bot> bots, BufferedWriter out) throws IOException 
	{
		int gameID = 0;
		int roundNum = 0;
		
		for (int i = 0; i < rounds; i++) 
		{
			for(Map m : maps)
			{
				for (int k = 1; k < bots.size(); k++) 
				{
					out.write(getGameString(gameID, roundNum, bots.get(0).getName(), bots.get(k).getName(), m.getMapName()) + System.getProperty("line.separator"));
					gameID++;
				}
				roundNum++;
			}
		}
	}
	
	private static String getGameString(int gameID, int roundID, String homeBot, String awayBot, String map)
	{
		JsonObject game = Json.object();
		game.add("gameID", gameID).add("roundID", roundID).add("homeBot", homeBot).add("awayBot", awayBot).add("map", map);
		return game.toString();
	}
}
