package com.db;

import java.io.IOException;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;  
import java.util.Properties;  
import static util.LogRecord.logger;

public class JdbcUtil {  
    //私有静态变量，用以读取配置文件 
    private static Properties config=new Properties();  
      
    static{  
        try {  
            //配置资源文件  
            config.load(JdbcUtil.class.getClassLoader().getResourceAsStream("db.properties"));  
            logger.info("[INFO]========== start connect sql");
            //加载驱动 
            Class.forName(config.getProperty("driver"));  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }  
    }  
      
    public static  Connection getConnection(){  
        Connection connection=null;  
        try {  
            connection=DriverManager.getConnection(config.getProperty("url"),config.getProperty("username"),config.getProperty("password"));  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
          
        return connection;  
    }  
    //用以关闭连接，释放资源  
    public static void releaseConn(Connection connection, Statement statement,  
            ResultSet resultSet) {  
        if(resultSet!=null){  
            try {  
                resultSet.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
            resultSet=null;  
        }  
          
        if(statement!=null){  
            try {  
                statement.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
            statement=null;  
        }  
          
        if(connection!=null){  
            try {  
                connection.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
            connection=null;  
        }
        logger.info("[INFO]========== release connect");
    }  
      
  
}  