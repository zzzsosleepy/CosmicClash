import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
	
	public SQL() {
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			String dbURL = "jdbc:sqlite:player_score.db";
			conn = DriverManager.getConnection(dbURL);
			
			if (conn != null) {
				//Do database stuff here
				conn.setAutoCommit(false);
				
				stmt = conn.createStatement();
				
				//Create the table if it does not already exist
				//Create a field for id, player name, shots fired, and player score
				String sql = "CREATE TABLE IF NOT EXISTS SCORES " + 
							 "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
							 "NAME TEXT NOT NULL, " + 
							 "SHOTS INT NOT NULL, " + 
							 "SCORE INT NOT NULL)";
				stmt.executeUpdate(sql);
				conn.commit();
				
				//Insert new data into database
				sql = "INSERT INTO SCORES (NAME, SHOTS, SCORE) VALUES " +
										  "('" + GameProperties.PLAYER_NAME + "', " + GameProperties.SHOT_COUNT + ", " + GameProperties.PLAYER_SCORE + ")";
				stmt.executeUpdate(sql);
				conn.commit();
				
				//Display all records from database
				ResultSet rs = stmt.executeQuery("SELECT * FROM SCORES");
				DisplayRecords(rs);
				rs.close();
				
//				sql = "UPDATE SCORES SET SCORE = " + GameProperties.PLAYER_SCORE + " WHERE NAME=" + GameProperties.PLAYER_NAME;
				conn.close();
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Display database records
	public static void DisplayRecords(ResultSet rs) throws SQLException{
		while(rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int shots = rs.getInt("shots");
			int score = rs.getInt("score");
			
			System.out.println("ID = " + id);
			System.out.println("Name = " + name);
			System.out.println("Shots Fired = " + shots);
			System.out.println("Score = " + score);
			System.out.println("");
		}
	}
}
