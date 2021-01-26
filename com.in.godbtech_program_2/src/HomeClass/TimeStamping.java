	/* 
Question : Write a program to create two threads T1 and T2

T1 will execute every minute and insert the current time in a queue Q1

T2 will be executed every 5 minutes and Read from the Q1 and insert it into the database

*/

package HomeClass;

import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class TimeStamping {

	/* SQL LIBRARY */
	private final static String Db_url = "jdbc:mysql://localhost/Test";
	private final static String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	private final static String user = "root";
	private final static String pwd = "World!1";
	private static Connection con = null;
	private static PreparedStatement pre = null;

	public static void main(String[] args) {

		List<String> timeStamps = new ArrayList<String>();

		/* TimeStamp */
		Runnable StampToQueueHandler = () -> {

			while (true) {
				try {
					timeStamps.add(java.time.LocalTime.now().toString());
					System.out.println(timeStamps);
					Thread.sleep(60000);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};
		/* Insert To DB */
		Runnable StampToDbHandler = () -> {
			while (true) {
				for (int i = 0; i <= 10; i++) {
					if (i == 5) {
						System.out.println("End");
						System.exit(0);
					} else {
						try {
							String query = null;
							int res = 0;
							Class.forName(jdbc_driver);
							System.out.println("selected database");
							con = DriverManager.getConnection(Db_url, user, pwd);
							System.out.println("Connected database successfully");
							query = "insert into timing (currenttime) values(?)";
							pre = con.prepareStatement(query);
							for (int j = 0; j < timeStamps.size(); j++) {
								pre.setString(1, timeStamps.get(j));
								res = pre.executeUpdate();
							}
							if (res == 1)
								System.out.println("suceessfully inserted.....");
							else
								System.out.println("Try again");
							con.close();
							Thread.sleep(5
									*60000);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				}
			}
		};

		Thread threadOne = new Thread(StampToQueueHandler);
		Thread threadTwo = new Thread(StampToDbHandler);

		threadOne.start();
		threadTwo.start();
	}

}
