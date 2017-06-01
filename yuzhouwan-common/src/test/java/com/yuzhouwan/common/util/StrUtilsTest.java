package com.yuzhouwan.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuzhouwan.common.dir.DirUtils;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.yuzhouwan.common.util.StrUtils.*;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2017 yuzhouwan.com
 * All right reserved.
 * Function: String Stuff Tester
 *
 * @author Benedict Jin
 * @since 2016/3/23
 */
public class StrUtilsTest {

    @Test
    public void fillTest() throws Exception {
        assertEquals("00000010", StrUtils.fillWitchZero(10, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.0d, 8));
        assertEquals("00000010", StrUtils.fillWitchZero(10.01d, 8));
    }

    @Test
    public void mainValueTest() throws Exception {
        String mainValue = StrUtils.getMainValue("ATK000001", "ATK".length(), "0");
        assertEquals(true, mainValue != null && 1 == Integer.parseInt(mainValue));
        mainValue = StrUtils.getMainValue("ATK000040", "ATK".length(), "0");
        assertEquals(true, mainValue != null && 40 == Integer.parseInt(mainValue));
    }

    @Test
    public void cutStart() throws Exception {
        assertEquals("yuzhouwan.com", StrUtils.cutStartStr("www.yuzhouwan.com", "www."));
    }

    @Test
    public void cutMiddle() throws Exception {
        assertEquals(File.separator + "com" + File.separator + "yuzhouwan" + File.separator + "common" +
                        File.separator + "util" + File.separator + "StrUtilsTest.class",
                StrUtils.cutMiddleStr("F:" + File.separator + "如何成为 Java 高手" + File.separator + "笔记" +
                                File.separator + "Soft Engineering" + File.separator + "Git" + File.separator +
                                "[code]" + File.separator + "yuzhouwan" + File.separator + "yuzhouwan-common" +
                                File.separator + "target" + File.separator + "test-classes" + File.separator + ".." +
                                File.separator + "test-classes" + File.separator + "com" + File.separator +
                                "yuzhouwan" + File.separator + "common" + File.separator + "util" + File.separator +
                                "StrUtilsTest.class",
                        "test-classes"));
    }

    @Test
    public void cutTail() throws Exception {
        assertEquals("F:" + File.separator + "如何成为 Java 高手" + File.separator + "笔记" + File.separator +
                        "Soft Engineering" + File.separator + "Git" + File.separator + "[code]" + File.separator +
                        "yuzhouwan" + File.separator + "yuzhouwan-common" + File.separator + "target" + File.separator,
                StrUtils.cutTailStr("F:" + File.separator + "如何成为 Java 高手" + File.separator + "笔记" +
                                File.separator + "Soft Engineering" + File.separator + "Git" + File.separator +
                                "[code]" + File.separator + "yuzhouwan" + File.separator + "yuzhouwan-common" +
                                File.separator + "target" + File.separator + "test-classes" + File.separator + "",
                        "test-classes" + File.separator + ""));
    }

    @Test
    public void holderTest() throws Exception {
        assertEquals("a1b2c3", String.format("%s1b%Sc%d", "a", "2", 3));
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("a");
        linkedList.add("b");
        assertEquals("a b", String.format("%s %s", linkedList.toArray()));
    }

    @Test
    public void splitMulti() throws Exception {
        LinkedList<String> expect = new LinkedList<>();
        expect.add("ns_fac");
        expect.add("hb_scapaysettlereg_acc");
        expect.add("006b897c8c6b0cdc258566b81508efe5");
        expect.add("storeCount");
        LinkedList<String> result = StrUtils.splitMulti(
                "namespace_ns_fac_table_hb_scapaysettlereg_acc_region_006b897c8c6b0cdc258566b81508efe5_metric_storeCount",
                "namespace_", "_table_", "_region_", "_metric_");
        assert result != null;
        int size = result.size();
        assertEquals(true, expect.size() == size);
        for (int i = 0; i < size; i++) {
            assertEquals(expect.get(i), result.get(i));
        }
    }

    @Test
    public void isNumberTest() throws Exception {
        assertEquals(true, StrUtils.isNumber("0"));
        assertEquals(true, StrUtils.isNumber("1"));
        assertEquals(true, StrUtils.isNumber("100"));
        assertEquals(false, StrUtils.isNumber("-1"));
        assertEquals(false, StrUtils.isNumber("1.1"));
        assertEquals(false, StrUtils.isNumber("abc"));
    }

    @Test
    public void map2JsonTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cluster", "test");
        jsonObject.put("table", "default");
        assertEquals("{\"cluster\":\"test\",\"table\":\"default\"}", jsonObject.toJSONString());
        jsonObject.put("metric", 1L);
        assertEquals("{\"cluster\":\"test\",\"metric\":1,\"table\":\"default\"}", jsonObject.toJSONString());
        jsonObject.remove("metric");
        assertEquals("{\"cluster\":\"test\",\"table\":\"default\"}", jsonObject.toJSONString());
        jsonObject.put("metric2", 0.02d);
        assertEquals("{\"cluster\":\"test\",\"metric2\":0.02,\"table\":\"default\"}", jsonObject.toJSONString());
    }

    @Test
    public void hexTest() throws Exception {
        String yuzhouwan = "宇宙湾yuzhouwan123";
        assertEquals(yuzhouwan, hex2Str(str2Hex(yuzhouwan)));
    }

    @Test
    public void compressionTest() throws Exception {
        String mysqlKeywords = compression(new String(FileUtils.readFile(DirUtils.TEST_RESOURCES_PATH.concat("db/mysql_5.5_keywords.json"))));
        assertEquals("[\"ACCESSIBLE\",\"ADD\",\"ALL\",\"ALTER\",\"ANALYZE\",\"AND\",\"AS\",\"ASC\",\"ASENSITIVE\",\"BEFORE\",\"BETWEEN\",\"BIGINT\",\"BINARY\",\"BLOB\",\"BOTH\",\"BY\",\"CALL\",\"CASCADE\",\"CASE\",\"CHANGE\",\"CHAR\",\"CHARACTER\",\"CHECK\",\"COLLATE\",\"COLUMN\",\"CONDITION\",\"CONSTRAINT\",\"CONTINUE\",\"CONVERT\",\"CREATE\",\"CROSS\",\"CURRENT_DATE\",\"CURRENT_TIME\",\"CURRENT_TIMESTAMP\",\"CURRENT_USER\",\"CURSOR\",\"DATABASE\",\"DATABASES\",\"DAY_HOUR\",\"DAY_MICROSECOND\",\"DAY_MINUTE\",\"DAY_SECOND\",\"DEC\",\"DECIMAL\",\"DECLARE\",\"DEFAULT\",\"DELAYED\",\"DELETE\",\"DESC\",\"DESCRIBE\",\"DETERMINISTIC\",\"DISTINCT\",\"DISTINCTROW\",\"DIV\",\"DOUBLE\",\"DROP\",\"DUAL\",\"EACH\",\"ELSE\",\"ELSEIF\",\"ENCLOSED\",\"ESCAPED\",\"EXISTS\",\"EXIT\",\"EXPLAIN\",\"FALSE\",\"FETCH\",\"FLOAT\",\"FLOAT4\",\"FLOAT8\",\"FOR\",\"FORCE\",\"FOREIGN\",\"FROM\",\"FULLTEXT\",\"GRANT\",\"GROUP\",\"HAVING\",\"HIGH_PRIORITY\",\"HOUR_MICROSECOND\",\"HOUR_MINUTE\",\"HOUR_SECOND\",\"IF\",\"IGNORE\",\"IN\",\"INDEX\",\"INFILE\",\"INNER\",\"INOUT\",\"INSENSITIVE\",\"INSERT\",\"INT\",\"INT1\",\"INT2\",\"INT3\",\"INT4\",\"INT8\",\"INTEGER\",\"INTERVAL\",\"INTO\",\"IS\",\"ITERATE\",\"JOIN\",\"KEY\",\"KEYS\",\"KILL\",\"LEADING\",\"LEAVE\",\"LEFT\",\"LIKE\",\"LIMIT\",\"LINEAR\",\"LINES\",\"LOAD\",\"LOCALTIME\",\"LOCALTIMESTAMP\",\"LOCK\",\"LONG\",\"LONGBLOB\",\"LONGTEXT\",\"LOOP\",\"LOW_PRIORITY\",\"MASTER_SSL_VERIFY_SERVER_CERT\",\"MATCH\",\"MAXVALUE\",\"MEDIUMBLOB\",\"MEDIUMINT\",\"MEDIUMTEXT\",\"MIDDLEINT\",\"MINUTE_MICROSECOND\",\"MINUTE_SECOND\",\"MOD\",\"MODIFIES\",\"NATURAL\",\"NOT\",\"NO_WRITE_TO_BINLOG\",\"NULL\",\"NUMERIC\",\"ON\",\"OPTIMIZE\",\"OPTION\",\"OPTIONALLY\",\"OR\",\"ORDER\",\"OUT\",\"OUTER\",\"OUTFILE\",\"PRECISION\",\"PRIMARY\",\"PROCEDURE\",\"PURGE\",\"RANGE\",\"READ\",\"READS\",\"READ_WRITE\",\"REAL\",\"REFERENCES\",\"REGEXP\",\"RELEASE\",\"RENAME\",\"REPEAT\",\"REPLACE\",\"REQUIRE\",\"RESIGNAL\",\"RESTRICT\",\"RETURN\",\"REVOKE\",\"RIGHT\",\"RLIKE\",\"SCHEMA\",\"SCHEMAS\",\"SECOND_MICROSECOND\",\"SELECT\",\"SENSITIVE\",\"SEPARATOR\",\"SET\",\"SHOW\",\"SIGNAL\",\"SMALLINT\",\"SPATIAL\",\"SPECIFIC\",\"SQL\",\"SQLEXCEPTION\",\"SQLSTATE\",\"SQLWARNING\",\"SQL_BIG_RESULT\",\"SQL_CALC_FOUND_ROWS\",\"SQL_SMALL_RESULT\",\"SSL\",\"STARTING\",\"STRAIGHT_JOIN\",\"TABLE\",\"TERMINATED\",\"THEN\",\"TINYBLOB\",\"TINYINT\",\"TINYTEXT\",\"TO\",\"TRAILING\",\"TRIGGER\",\"TRUE\",\"UNDO\",\"UNION\",\"UNIQUE\",\"UNLOCK\",\"UNSIGNED\",\"UPDATE\",\"USAGE\",\"USE\",\"USING\",\"UTC_DATE\",\"UTC_TIME\",\"UTC_TIMESTAMP\",\"VALUES\",\"VARBINARY\",\"VARCHAR\",\"VARCHARACTER\",\"VARYING\",\"WHEN\",\"WHERE\",\"WHILE\",\"WITH\",\"WRITE\",\"XOR\",\"YEAR_MONTH\",\"ZEROFILL\",\"GENERAL\",\"IGNORE_SERVER_IDS\",\"MASTER_HEARTBEAT_PERIOD\",\"MAXVALUE\",\"RESIGNAL\",\"SIGNAL\",\"SLOW\"]",
                mysqlKeywords);
        String sqliteKeywords = compression(new String(FileUtils.readFile(DirUtils.TEST_RESOURCES_PATH.concat("db/sqlite_keywords.json"))));
        assertEquals("[\"ABORT\",\"ADD\",\"AFTER\",\"ALL\",\"ALTER\",\"ANALYZE\",\"AND\",\"AS\",\"ASC\",\"ATTACH\",\"AUTOINCREMENT\",\"BEFORE\",\"BEGIN\",\"BETWEEN\",\"BY\",\"CASCADE\",\"CASE\",\"CAST\",\"CHECK\",\"COLLATE\",\"COMMIT\",\"CONFLICT\",\"CONSTRAINT\",\"CREATE\",\"CROSS\",\"CURRENT_DATE\",\"CURRENT_TIME\",\"CURRENT_TIMESTAMP\",\"DATABASE\",\"DEFAULT\",\"DEFERRABLE\",\"DEFERRED\",\"DELETE\",\"DESC\",\"DETACH\",\"DISTINCT\",\"DROP\",\"EACH\",\"ELSE\",\"END\",\"ESCAPE\",\"EXCEPT\",\"EXCLUSIVE\",\"EXPLAIN\",\"FAIL\",\"FOR\",\"FOREIGN\",\"FROM\",\"FULL\",\"GLOB\",\"GROUP\",\"HAVING\",\"IF\",\"IGNORE\",\"IMMEDIATE\",\"IN\",\"INDEX\",\"INITIALLY\",\"INNER\",\"INSERT\",\"INSTEAD\",\"INTERSECT\",\"INTO\",\"IS\",\"ISNULL\",\"JOIN\",\"KEY\",\"LEFT\",\"LIKE\",\"LIMIT\",\"MATCH\",\"NATURAL\",\"NOT\",\"NOTNULL\",\"NULL\",\"OF\",\"OFFSET\",\"ON\",\"OR\",\"ORDER\",\"OUTER\",\"PLAN\",\"PRAGMA\",\"PRIMARY\",\"QUERY\",\"RAISE\",\"REFERENCES\",\"REINDEX\",\"RENAME\",\"REPLACE\",\"RESTRICT\",\"RIGHT\",\"ROLLBACK\",\"ROW\",\"SELECT\",\"SET\",\"TABLE\",\"TEMP\",\"TEMPORARY\",\"THEN\",\"TO\",\"TRANSACTION\",\"TRIGGER\",\"UNION\",\"UNIQUE\",\"UPDATE\",\"USING\",\"VACUUM\",\"VALUES\",\"VIEW\",\"VIRTUAL\",\"WHEN\",\"WHERE\"]",
                sqliteKeywords);
        Collection<Object> mysqlArr = Arrays.asList(JSON.parseArray(mysqlKeywords).toArray());
        Set<String> mysqlSet = mysqlArr.stream().map(Object::toString).collect(Collectors.toCollection(LinkedHashSet::new));
        JSONArray sqliteArr = JSON.parseArray(sqliteKeywords);
        Set<String> sqliteSet = sqliteArr.stream().map(Object::toString).collect(Collectors.toCollection(LinkedHashSet::new));
        Collection<String> intersection = CollectionUtils.intersection(mysqlSet, sqliteSet);
        mysqlSet.removeAll(intersection);   // in mysql, and not in sqlite
        assertEquals("[\"ACCESSIBLE\",\"ASENSITIVE\",\"BIGINT\",\"BINARY\",\"BLOB\",\"BOTH\",\"CALL\",\"CHANGE\",\"CHAR\",\"CHARACTER\",\"COLUMN\",\"CONDITION\",\"CONTINUE\",\"CONVERT\",\"CURRENT_USER\",\"CURSOR\",\"DATABASES\",\"DAY_HOUR\",\"DAY_MICROSECOND\",\"DAY_MINUTE\",\"DAY_SECOND\",\"DEC\",\"DECIMAL\",\"DECLARE\",\"DELAYED\",\"DESCRIBE\",\"DETERMINISTIC\",\"DISTINCTROW\",\"DIV\",\"DOUBLE\",\"DUAL\",\"ELSEIF\",\"ENCLOSED\",\"ESCAPED\",\"EXISTS\",\"EXIT\",\"FALSE\",\"FETCH\",\"FLOAT\",\"FLOAT4\",\"FLOAT8\",\"FORCE\",\"FULLTEXT\",\"GRANT\",\"HIGH_PRIORITY\",\"HOUR_MICROSECOND\",\"HOUR_MINUTE\",\"HOUR_SECOND\",\"INFILE\",\"INOUT\",\"INSENSITIVE\",\"INT\",\"INT1\",\"INT2\",\"INT3\",\"INT4\",\"INT8\",\"INTEGER\",\"INTERVAL\",\"ITERATE\",\"KEYS\",\"KILL\",\"LEADING\",\"LEAVE\",\"LINEAR\",\"LINES\",\"LOAD\",\"LOCALTIME\",\"LOCALTIMESTAMP\",\"LOCK\",\"LONG\",\"LONGBLOB\",\"LONGTEXT\",\"LOOP\",\"LOW_PRIORITY\",\"MASTER_SSL_VERIFY_SERVER_CERT\",\"MAXVALUE\",\"MEDIUMBLOB\",\"MEDIUMINT\",\"MEDIUMTEXT\",\"MIDDLEINT\",\"MINUTE_MICROSECOND\",\"MINUTE_SECOND\",\"MOD\",\"MODIFIES\",\"NO_WRITE_TO_BINLOG\",\"NUMERIC\",\"OPTIMIZE\",\"OPTION\",\"OPTIONALLY\",\"OUT\",\"OUTFILE\",\"PRECISION\",\"PROCEDURE\",\"PURGE\",\"RANGE\",\"READ\",\"READS\",\"READ_WRITE\",\"REAL\",\"REGEXP\",\"RELEASE\",\"REPEAT\",\"REQUIRE\",\"RESIGNAL\",\"RETURN\",\"REVOKE\",\"RLIKE\",\"SCHEMA\",\"SCHEMAS\",\"SECOND_MICROSECOND\",\"SENSITIVE\",\"SEPARATOR\",\"SHOW\",\"SIGNAL\",\"SMALLINT\",\"SPATIAL\",\"SPECIFIC\",\"SQL\",\"SQLEXCEPTION\",\"SQLSTATE\",\"SQLWARNING\",\"SQL_BIG_RESULT\",\"SQL_CALC_FOUND_ROWS\",\"SQL_SMALL_RESULT\",\"SSL\",\"STARTING\",\"STRAIGHT_JOIN\",\"TERMINATED\",\"TINYBLOB\",\"TINYINT\",\"TINYTEXT\",\"TRAILING\",\"TRUE\",\"UNDO\",\"UNLOCK\",\"UNSIGNED\",\"USAGE\",\"USE\",\"UTC_DATE\",\"UTC_TIME\",\"UTC_TIMESTAMP\",\"VARBINARY\",\"VARCHAR\",\"VARCHARACTER\",\"VARYING\",\"WHILE\",\"WITH\",\"WRITE\",\"XOR\",\"YEAR_MONTH\",\"ZEROFILL\",\"GENERAL\",\"IGNORE_SERVER_IDS\",\"MASTER_HEARTBEAT_PERIOD\",\"SLOW\"]",
                JSON.toJSONString(mysqlSet));
    }

    @Test
    public void drop() throws Exception {
        String schema = new String(FileUtils.readFile(DirUtils.TEST_RESOURCES_PATH.concat("db/superset.sql.schema.sql")));
        LinkedList<String> drops = new LinkedList<>();
        for (String s : schema.split("\n")) {
            if (s.toLowerCase().startsWith("drop table if exists")) drops.add(s);
        }
        StringBuilder stringBuilder = new StringBuilder();
        drops.forEach(stringBuilder::append);
        assertEquals("DROP TABLE IF EXISTS `ab_user` ;DROP TABLE IF EXISTS `ab_view_menu` ;DROP TABLE IF EXISTS `ab_role` ;DROP TABLE IF EXISTS `ab_permission` ;DROP TABLE IF EXISTS `ab_register_user` ;DROP TABLE IF EXISTS `ab_user_role` ;DROP TABLE IF EXISTS `ab_permission_view` ;DROP TABLE IF EXISTS `ab_permission_view_role` ;DROP TABLE IF EXISTS `alembic_version` ;DROP TABLE IF EXISTS `dashboards` ;DROP TABLE IF EXISTS `dbs` ;DROP TABLE IF EXISTS `metrics` ;DROP TABLE IF EXISTS `slices` ;DROP TABLE IF EXISTS `sql_metrics` ;DROP TABLE IF EXISTS `dashboard_slices` ;DROP TABLE IF EXISTS `logs` ;DROP TABLE IF EXISTS `url` ;DROP TABLE IF EXISTS `css_templates` ;DROP TABLE IF EXISTS `favstar` ;DROP TABLE IF EXISTS `dashboard_user` ;DROP TABLE IF EXISTS `slice_user` ;DROP TABLE IF EXISTS `clusters` ;DROP TABLE IF EXISTS `columns` ;DROP TABLE IF EXISTS `datasources` ;DROP TABLE IF EXISTS `table_columns` ;DROP TABLE IF EXISTS `tables` ;DROP TABLE IF EXISTS `access_request` ;DROP TABLE IF EXISTS `query` ;DROP TABLE IF EXISTS `energy_usage` ;DROP TABLE IF EXISTS `wb_health_population` ;DROP TABLE IF EXISTS `birth_names` ;DROP TABLE IF EXISTS `random_time_series` ;DROP TABLE IF EXISTS `long_lat` ;DROP TABLE IF EXISTS `multiformat_time_series` ;",
                stringBuilder.toString());
    }

    @Test
    public void sqlData() throws Exception {
        String dataTotal = new String(FileUtils.readFile(DirUtils.TEST_RESOURCES_PATH.concat("db/superset.sql.data.sql")));
        StringBuilder stringBuilderPrimaryKey = new StringBuilder();
        StringBuilder stringBuilderNormal = new StringBuilder();
        String lowerData;
        for (String data : dataTotal.split(";")) {
            lowerData = data.toLowerCase();
            if (lowerData.contains("alembic_version") || lowerData.contains("energy_usage") ||
                    lowerData.contains("wb_health_population") || lowerData.contains("birth_names") ||
                    lowerData.contains("random_time_series") || lowerData.contains("long_lat") ||
                    lowerData.contains("multiformat_time_series")) stringBuilderNormal.append(data).append(";");
            else stringBuilderPrimaryKey.append(lowerData).append(";");
        }
        FileUtils.writeFile(DirUtils.TEST_RESOURCES_PATH.concat("db/superset.sql.data_primary.sql"),
                stringBuilderPrimaryKey.toString().replace(";;", ";").getBytes());
        FileUtils.writeFile(DirUtils.TEST_RESOURCES_PATH.concat("db/superset.sql.data_normal.sql"),
                stringBuilderNormal.toString().replace(";;", ";").getBytes());
    }
}