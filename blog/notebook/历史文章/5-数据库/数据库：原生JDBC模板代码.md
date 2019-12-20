
```
package DbConnection;

import java.sql.*;

public class MySql {

	public static void main(String args[]){
		new MySql();
	}
	
	public MySql(){

        /**
        // MySQL使用
		String dbdriver = "com.mysql.jdbc.Driver";
		String dburl = "jdbc:mysql://localhost:3306/test";
		String dbuser = "root";
		String dbpwd = "123456";
        */
        
        /**
        // SQL Server 使用
		String dbdriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	   	String dburl = "jdbc:sqlserver://localhost;databaseName=person";
	   	String dbuser = "sa";
	   	String dbpwd = "111111";
        
        // Oracle使用
	 	String dbdriver = "sun.jdbc.odbc.JdbcOdbcDriver";
		String dburl = "jdbc:odbc:TestAccess";
	    //String dbuser = "";
	    //String dbpwd = "";
		*/
		
		Connection cn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;


		try {

			Class.forName(dbdriver); //加载驱动

			cn = DriverManager.getConnection(dburl, dbuser, dbpwd); //创建连接

	//		String sql = "select name  from user where userid=? and password=?";
			String sql = "select *  from user limit 0,2";
			pst = cn.prepareStatement(sql); //创建prepareStatement用来发送语句	

	//		pst.setString(1,"1");
	//		pst.setString(2,"123456");

			rs = pst.executeQuery(); //返回结果集	
			
			while(rs.next()){
				System.out.println(rs.getString("name"));
			}

		} catch (Exception e){
			e.printStackTrace();
		}

		try {
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
			if (cn != null)
				cn.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
	}
}

```
