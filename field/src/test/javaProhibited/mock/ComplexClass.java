package javaProhibited.mock;

import sun.jdbc.odbc.JdbcOdbcBatchUpdateException;
import java.sql.BatchUpdateException;

public class ComplexClass {

    private BatchUpdateException batchE;

    public ComplexClass(JdbcOdbcBatchUpdateException odbcE) {

        this.batchE = odbcE;
    }

    private void sayE(String e) {

        if(batchE == null)
            return;

        System.out.println(e + ":\t" + batchE.getMessage());
    }
}