package javaProhibited.mock;

import com.sun.rowset.JdbcRowSetResourceBundle;

public class ComplexClass {

    private JdbcRowSetResourceBundle jrsrb;

    public ComplexClass(JdbcRowSetResourceBundle jrsrb) {

        this.jrsrb = jrsrb;
    }

    private void sayE(String e) {

        if(jrsrb == null)
            return;

        System.out.println(e + ":\t" + jrsrb.toString());
    }
}