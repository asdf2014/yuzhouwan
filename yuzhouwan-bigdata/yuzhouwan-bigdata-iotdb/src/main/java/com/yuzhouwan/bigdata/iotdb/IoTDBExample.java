package com.yuzhouwan.bigdata.iotdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：IoTDB Example
 *
 * @author Benedict Jin
 * @since 2020/12/20
 */
public class IoTDBExample {

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("get connection defeat");
            return;
        }
        Statement statement = connection.createStatement();

        try {
            statement.execute("SET STORAGE GROUP TO root.demo");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statement.execute("SHOW STORAGE GROUP");
        outputResult(statement.getResultSet());

        try {
            statement.execute("CREATE TIMESERIES root.demo.s0 WITH DATATYPE=INT32,ENCODING=RLE;");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        statement.execute("SHOW TIMESERIES root.demo");
        outputResult(statement.getResultSet());

        statement.execute("SHOW DEVICES");
        outputResult(statement.getResultSet());

        statement.execute("COUNT TIMESERIES root");
        outputResult(statement.getResultSet());

        statement.execute("COUNT NODES root LEVEL=3");
        outputResult(statement.getResultSet());

        statement.execute("COUNT TIMESERIES root GROUP BY LEVEL=3");
        outputResult(statement.getResultSet());

        statement.addBatch("insert into root.demo(timestamp,s0) values(1,1);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(1,1);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(2,15);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(2,17);");
        statement.addBatch("insert into root.demo(timestamp,s0) values(4,12);");
        statement.executeBatch();
        statement.clearBatch();

        String sql = "select * from root.demo";
        ResultSet resultSet = statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        sql = "select s0 from root.demo where time = 4;";
        resultSet = statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        sql = "select s0 from root.demo where time >= 2 and time < 5;";
        resultSet = statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        sql = "select count(s0) from root.demo;";
        resultSet = statement.executeQuery(sql);
        System.out.println("sql: " + sql);
        outputResult(resultSet);

        statement.execute("delete timeseries root.demo.s0");

        statement.close();
        connection.close();
    }

    public static Connection getConnection() {

        String driver = "org.apache.iotdb.jdbc.IoTDBDriver";
        String url = "jdbc:iotdb://127.0.0.1:6667/";

        String username = "root";
        String password = "root";

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void outputResult(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            System.out.println("--------------------------");
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                System.out.print(metaData.getColumnLabel(i + 1) + " ");
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; ; i++) {
                    System.out.print(resultSet.getString(i));
                    if (i < columnCount) {
                        System.out.print(", ");
                    } else {
                        System.out.println();
                        break;
                    }
                }
            }
            System.out.println("--------------------------\n");
        }
    }
}
