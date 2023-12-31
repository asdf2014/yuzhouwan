package com.yuzhouwan.bigdata.druid.calcite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function：Calcite Druid Example
 *
 * @author Benedict Jin
 * @since 2020/7/12
 */
public class CalciteDruidExample {

    @SuppressWarnings("SqlResolve")
    public static void main(String[] args) throws Exception {
        String url = "jdbc:avatica:remote:url=http://localhost:8082/druid/v2/sql/avatica/";
        String query = "SELECT COUNT(*) as res FROM wikipedia";
        try (Connection connection = DriverManager.getConnection(url)) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getInt(1));
                }
            }
        }
    }
}
