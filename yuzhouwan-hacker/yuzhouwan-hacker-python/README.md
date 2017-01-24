# convert SQLite into MySQL
## generate
    sqlite3 sqlite_database.db .dump > sqlite_database.sql
    python sqlite2mysql.py sqlite_database.sql sql_database

## import schema & data
    run com.yuzhouwan.common.util.StrUtilsTest.sqlData
    mysql> source ~/sqlite_database.sql.database.sql
    mysql> source ~/sqlite_database.sql.schema.sql
    mysql> source ~/sqlite_database.sql.data_normal.sql
    mysql> source ~/sqlite_database.sql.data_primary.sql   (run until all throw depulication error)
