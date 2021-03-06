PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE ab_user (
	id INTEGER NOT NULL, 
	first_name VARCHAR(64) NOT NULL, 
	last_name VARCHAR(64) NOT NULL, 
	username VARCHAR(64) NOT NULL, 
	password VARCHAR(256), 
	active BOOLEAN, 
	email VARCHAR(64) NOT NULL, 
	last_login DATETIME, 
	login_count INTEGER, 
	fail_login_count INTEGER, 
	created_on DATETIME, 
	changed_on DATETIME, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, 
	PRIMARY KEY (id), 
	UNIQUE (username), 
	CHECK (active IN (0, 1)), 
	UNIQUE (email), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "ab_user" VALUES(1,'admin','user','admin','pbkdf2:sha1:1000$sZRobnh7$3217dfa97fd183b69f207a7829a61bbd1300591d',1,'admin@fab.org','2016-12-23 11:02:29.474892',7,0,'2016-11-17 20:32:25.452827','2016-11-17 20:32:25.452864',NULL,NULL);
INSERT INTO "ab_user" VALUES(2,'superset','jinjy','superset','pbkdf2:sha1:1000$qyOiYsI7$669dd91df94162c2c84b9f0153cc776c11a27c49',1,'16110505@yuzhouwan.com','2017-01-22 14:06:02.424812',12,0,'2016-12-14 17:53:40.937795','2016-12-14 18:54:43.259585',NULL,1);
CREATE TABLE ab_view_menu (
	id INTEGER NOT NULL, 
	name VARCHAR(100) NOT NULL, 
	PRIMARY KEY (id), 
	UNIQUE (name)
);
INSERT INTO "ab_view_menu" VALUES(1,'MyIndexView');
INSERT INTO "ab_view_menu" VALUES(2,'UtilView');
CREATE TABLE ab_role (
	id INTEGER NOT NULL, 
	name VARCHAR(64) NOT NULL, 
	PRIMARY KEY (id), 
	UNIQUE (name)
);
INSERT INTO "ab_role" VALUES(1,'Admin');
INSERT INTO "ab_role" VALUES(2,'Public');
INSERT INTO "ab_role" VALUES(3,'Alpha');
INSERT INTO "ab_role" VALUES(4,'Gamma');
INSERT INTO "ab_role" VALUES(5,'sql_lab');
INSERT INTO "ab_role" VALUES(6,'granter');
CREATE TABLE ab_permission (
	id INTEGER NOT NULL, 
	name VARCHAR(100) NOT NULL, 
	PRIMARY KEY (id), 
	UNIQUE (name)
);
INSERT INTO "ab_permission" VALUES(1,'can_this_form_post');
INSERT INTO "ab_permission" VALUES(2,'can_this_form_get');
CREATE TABLE ab_register_user (
	id INTEGER NOT NULL, 
	first_name VARCHAR(64) NOT NULL, 
	last_name VARCHAR(64) NOT NULL, 
	username VARCHAR(64) NOT NULL, 
	password VARCHAR(256), 
	email VARCHAR(64) NOT NULL, 
	registration_date DATETIME, 
	registration_hash VARCHAR(256), 
	PRIMARY KEY (id), 
	UNIQUE (username)
);
CREATE TABLE ab_user_role (
	id INTEGER NOT NULL, 
	user_id INTEGER, 
	role_id INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(user_id) REFERENCES ab_user (id), 
	FOREIGN KEY(role_id) REFERENCES ab_role (id)
);
INSERT INTO "ab_user_role" VALUES(1,1,1);
INSERT INTO "ab_user_role" VALUES(2,2,1);
CREATE TABLE ab_permission_view (
	id INTEGER NOT NULL, 
	permission_id INTEGER, 
	view_menu_id INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(permission_id) REFERENCES ab_permission (id), 
	FOREIGN KEY(view_menu_id) REFERENCES ab_view_menu (id)
);
INSERT INTO "ab_permission_view" VALUES(1,1,4);
INSERT INTO "ab_permission_view" VALUES(2,2,4);
CREATE TABLE ab_permission_view_role (
	id INTEGER NOT NULL, 
	permission_view_id INTEGER, 
	role_id INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(permission_view_id) REFERENCES ab_permission_view (id), 
	FOREIGN KEY(role_id) REFERENCES ab_role (id)
);
INSERT INTO "ab_permission_view_role" VALUES(1,1,1);
INSERT INTO "ab_permission_view_role" VALUES(2,2,1);
CREATE TABLE alembic_version (
	version_num VARCHAR(32) NOT NULL
);
INSERT INTO "alembic_version" VALUES('e46f2d27a08e');
CREATE TABLE dashboards (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	dashboard_title VARCHAR(500), 
	position_json TEXT, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, css TEXT, description TEXT, slug VARCHAR(255), json_metadata TEXT, 
	PRIMARY KEY (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "dashboards" VALUES('2016-11-21 19:40:51.531836','2016-12-16 17:08:37.126564',1,'World''s Bank Data','[
    {
        "size_y": 2, 
        "size_x": 2, 
        "col": 1, 
        "slice_id": "75", 
        "row": 0
    }, 
    {
        "size_y": 2, 
        "size_x": 2, 
        "col": 1, 
        "slice_id": "76", 
        "row": 2
    }, 
    {
        "size_y": 7, 
        "size_x": 3, 
        "col": 10, 
        "slice_id": "77", 
        "row": 0
    }, 
    {
        "size_y": 3, 
        "size_x": 6, 
        "col": 1, 
        "slice_id": "78", 
        "row": 4
    }, 
    {
        "size_y": 4, 
        "size_x": 7, 
        "col": 3, 
        "slice_id": "79", 
        "row": 0
    }, 
    {
        "size_y": 4, 
        "size_x": 8, 
        "col": 5, 
        "slice_id": "80", 
        "row": 7
    }, 
    {
        "size_y": 3, 
        "size_x": 3, 
        "col": 7, 
        "slice_id": "81", 
        "row": 4
    }, 
    {
        "size_y": 4, 
        "size_x": 4, 
        "col": 1, 
        "slice_id": "82", 
        "row": 7
    }, 
    {
        "size_y": 4, 
        "size_x": 4, 
        "col": 9, 
        "slice_id": "83", 
        "row": 11
    }, 
    {
        "size_y": 4, 
        "size_x": 8, 
        "col": 1, 
        "slice_id": "84", 
        "row": 11
    }
]',NULL,NULL,NULL,NULL,'world_health',NULL);
CREATE TABLE dbs (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	database_name VARCHAR(250), 
	sqlalchemy_uri VARCHAR(1024), 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, password BLOB, cache_timeout INTEGER, extra TEXT, select_as_create_table_as BOOLEAN, allow_ctas BOOLEAN, expose_in_sqllab BOOLEAN, force_ctas_schema VARCHAR(250), allow_run_async BOOLEAN, allow_run_sync BOOLEAN, allow_dml BOOLEAN, perm VARCHAR(1000), 
	PRIMARY KEY (id), 
	UNIQUE (database_name), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "dbs" VALUES('2016-11-21 19:40:05.271468','2016-12-16 17:07:38.795402',1,'main','sqlite:////root/.superset/superset.db',NULL,NULL,NULL,NULL,'{
    "metadata_params": {},
    "engine_params": {}
}
',0,0,1,NULL,0,1,0,'[main].(id:1)');
CREATE TABLE metrics (
	id INTEGER NOT NULL, 
	metric_name VARCHAR(512), 
	verbose_name VARCHAR(1024), 
	metric_type VARCHAR(32), 
	datasource_name VARCHAR(255), 
	json TEXT, 
	description TEXT, changed_by_fk INTEGER, changed_on DATETIME, created_by_fk INTEGER, created_on DATETIME, is_restricted BOOLEAN, d3format VARCHAR(128), 
	PRIMARY KEY (id), 
	FOREIGN KEY(datasource_name) REFERENCES datasources (datasource_name), 
	FOREIGN KEY(datasource_name) REFERENCES datasources (datasource_name)
);
INSERT INTO "metrics" VALUES(1,'count','COUNT(*)','count','copa_fi_sum_td','{"type": "count", "name": "count"}',NULL,NULL,'2016-11-21 19:51:28.943189',NULL,'2016-11-21 19:51:28.943154',0,NULL);
INSERT INTO "metrics" VALUES(2,'count','COUNT(*)','count','druid_metrics','{"type": "count", "name": "count"}',NULL,NULL,'2016-11-21 19:51:33.740316',NULL,'2016-11-21 19:51:33.740290',0,NULL);
CREATE TABLE slices (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	slice_name VARCHAR(250), 
	druid_datasource_id INTEGER, 
	table_id INTEGER, 
	datasource_type VARCHAR(200), 
	datasource_name VARCHAR(2000), 
	viz_type VARCHAR(250), 
	params TEXT, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, description TEXT, cache_timeout INTEGER, perm VARCHAR(2000), datasource_id INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(druid_datasource_id) REFERENCES datasources (id), 
	FOREIGN KEY(table_id) REFERENCES tables (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "slices" VALUES('2016-12-06 15:48:41.467559','2016-12-06 15:48:41.467593',35,'region top 10',NULL,NULL,'druid','hbase_metrics','line','{
    "add_to_dash": "false", 
    "collapsed_fieldsets": "", 
    "contribution": "false", 
    "datasource_id": "15", 
    "datasource_name": "hbase_metrics", 
    "datasource_type": "druid", 
    "druid_time_origin": "", 
    "flt_col_0": "", 
    "flt_col_1": "clusterName", 
    "flt_col_2": "", 
    "flt_eq_0": "", 
    "flt_eq_1": "test", 
    "flt_eq_2": "", 
    "flt_op_0": "in", 
    "flt_op_1": "in", 
    "flt_op_2": "in", 
    "goto_dash": "false", 
    "granularity": "1 minute", 
    "having_col_0": "", 
    "having_eq_0": "", 
    "having_op_0": "==", 
    "limit": "10", 
    "line_interpolation": "linear", 
    "metrics": [
        "count"
    ], 
    "new_dashboard_name": "", 
    "new_slice_name": "region top 10", 
    "num_period_compare": "", 
    "period_ratio_type": "growth", 
    "rdo_save": "saveas", 
    "resample_fillmethod": "", 
    "resample_how": "", 
    "resample_rule": "", 
    "rich_tooltip": "y", 
    "rolling_periods": "", 
    "rolling_type": "sum", 
    "save_to_dashboard_id": "", 
    "show_brush": "false", 
    "show_legend": "y", 
    "show_markers": "false", 
    "since": "1 day ago", 
    "slice_name": "region top 10", 
    "time_compare": "", 
    "timeseries_limit_metric": "", 
    "until": "now", 
    "userid": "1", 
    "viz_type": "line", 
    "x_axis_format": "smart_date", 
    "x_axis_label": "", 
    "x_axis_showminmax": "y", 
    "y_axis_format": ".3s", 
    "y_axis_label": "", 
    "y_axis_zero": "false", 
    "y_log_scale": "false"
}',1,1,NULL,NULL,'[druid cluster].[hbase_metrics](id:15)',15);
CREATE TABLE sql_metrics (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	metric_name VARCHAR(512), 
	verbose_name VARCHAR(1024), 
	metric_type VARCHAR(32), 
	table_id INTEGER, 
	expression TEXT, 
	description TEXT, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, is_restricted BOOLEAN, d3format VARCHAR(128), 
	PRIMARY KEY (id), 
	FOREIGN KEY(table_id) REFERENCES tables (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "sql_metrics" VALUES('2016-11-21 19:40:05.367107','2016-11-21 19:40:05.367130',1,'sum__value','sum__value','sum',1,'SUM(value)',NULL,NULL,NULL,0,NULL);
INSERT INTO "sql_metrics" VALUES('2016-11-21 19:40:05.378120','2016-11-21 19:40:05.378142',2,'avg__value','avg__value','avg',1,'AVG(value)',NULL,NULL,NULL,0,NULL);
CREATE TABLE dashboard_slices (
	id INTEGER NOT NULL, 
	dashboard_id INTEGER, 
	slice_id INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(dashboard_id) REFERENCES dashboards (id), 
	FOREIGN KEY(slice_id) REFERENCES slices (id)
);
INSERT INTO "dashboard_slices" VALUES(51,1,75);
INSERT INTO "dashboard_slices" VALUES(52,1,76);
CREATE TABLE logs (
	id INTEGER NOT NULL, 
	action VARCHAR(512), 
	user_id INTEGER, 
	json TEXT, 
	dttm DATETIME, dashboard_id INTEGER, slice_id INTEGER, dt DATE, 
	PRIMARY KEY (id), 
	FOREIGN KEY(user_id) REFERENCES ab_user (id)
);
INSERT INTO "logs" VALUES(1,'explore',1,'{"datasource_id": "11", "datasource_type": "druid"}','2016-11-21 11:58:50',NULL,0,'2016-11-21');
INSERT INTO "logs" VALUES(2,'explore',1,'{"datasource_id": "4", "datasource_type": "druid"}','2016-11-21 11:59:01',NULL,0,'2016-11-21');
CREATE TABLE url (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	url TEXT, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "url" VALUES('2016-12-08 20:31:01.432592','2016-12-08 20:31:01.432639',1,'//superset/explore/druid/15/?goto_dash=false&new_dashboard_name=&slice_name=region+top+10&y_axis_zero=false&y_axis_label=&resample_fillmethod=&save_to_dashboard_id=&line_interpolation=linear&add_to_dash=false&show_legend=y&rolling_periods=&x_axis_label=&rolling_type=None&new_slice_name=region+top+10&flt_op_0=in&viz_type=line&show_markers=false&since=12+hours+ago&y_log_scale=false&time_compare=&having_col_0=&json=false&until=now&having_eq_0=&rdo_save=saveas&having_op_0=%3D%3D&collapsed_fieldsets=&resample_rule=&datasource_id=15&period_ratio_type=growth&show_brush=false&y_axis_format=.3s&x_axis_showminmax=y&metrics=incReadRequestCount&granularity=one+day&flt_eq_0=&flt_col_0=&timeseries_limit_metric=incReadRequestCount&resample_how=&slice_id=36&num_period_compare=&userid=1&datasource_type=druid&druid_time_origin=&rich_tooltip=y&limit=50&x_axis_format=smart_date&datasource_name=hbase_metrics&contribution=false&groupby=tableName',1,1);
INSERT INTO "url" VALUES('2016-12-11 18:07:23.456765','2016-12-11 18:07:23.456797',2,'//superset/sqllab?title=Untitled%20Query&sql=SELECT%20*%0AFROM%0AWHERE',1,1);
INSERT INTO "url" VALUES('2016-12-14 18:55:04.523695','2016-12-14 18:55:04.523848',3,'//superset/sqllab?title=Untitled%20Query&sql=SELECT%20*%0AFROM%0AWHERE',1,1);
INSERT INTO "url" VALUES('2017-01-16 17:38:33.633559','2017-01-16 17:38:33.633615',4,'//superset/explore/druid/15/?viz_type=line&granularity=1+hour&druid_time_origin=&since=28+days+ago&until=now&metrics=ReadQps&groupby=nameSpaceName&groupby=tableName&limit=10&timeseries_limit_metric=incReadRequestCount&show_brush=false&show_legend=y&show_legend=false&rich_tooltip=y&rich_tooltip=false&y_axis_zero=false&y_log_scale=false&contribution=false&show_markers=false&x_axis_showminmax=y&x_axis_showminmax=false&line_interpolation=linear&x_axis_format=smart_date&y_axis_format=.3s&x_axis_label=&y_axis_label=&rolling_type=None&rolling_periods=&time_compare=&num_period_compare=&period_ratio_type=growth&resample_how=&resample_rule=&resample_fillmethod=&flt_col_0=&flt_op_0=in&flt_eq_0=&having_col_0=&having_op_0=%3D%3D&having_eq_0=&slice_id=36&slice_name=region+top+10&collapsed_fieldsets=&action=&userid=2&goto_dash=false&datasource_name=hbase_metrics&datasource_id=15&datasource_type=druid&previous_viz_type=line&rdo_save=overwrite&new_slice_name=&add_to_dash=false&save_to_dashboard_id=&new_dashboard_name=',2,2);
CREATE TABLE css_templates (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	template_name VARCHAR(250), 
	css TEXT, 
	changed_by_fk INTEGER, 
	created_by_fk INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "css_templates" VALUES('2016-11-21 19:40:05.061464','2016-11-21 19:40:05.061509',1,'Flat','.gridster div.widget {
    transition: background-color 0.5s ease;
    background-color: #FAFAFA;
    border: 1px solid #CCC;
    box-shadow: none;
    border-radius: 0px;
}
.gridster div.widget:hover {
    border: 1px solid #000;
    background-color: #EAEAEA;
}
.navbar {
    transition: opacity 0.5s ease;
    opacity: 0.05;
}
.navbar:hover {
    opacity: 1;
}
.chart-header .header{
    font-weight: normal;
    font-size: 12px;
}
/*
var bnbColors = [
    //rausch    hackb      kazan      babu      lima        beach     tirol
    ''#ff5a5f'', ''#7b0051'', ''#007A87'', ''#00d1c1'', ''#8ce071'', ''#ffb400'', ''#b4a76c'',
    ''#ff8083'', ''#cc0086'', ''#00a1b3'', ''#00ffeb'', ''#bbedab'', ''#ffd266'', ''#cbc29a'',
    ''#ff3339'', ''#ff1ab1'', ''#005c66'', ''#00b3a5'', ''#55d12e'', ''#b37e00'', ''#988b4e'',
 ];
*/
',NULL,NULL);
CREATE TABLE favstar (
	id INTEGER NOT NULL, 
	user_id INTEGER, 
	class_name VARCHAR(50), 
	obj_id INTEGER, 
	dttm DATETIME, 
	PRIMARY KEY (id), 
	FOREIGN KEY(user_id) REFERENCES ab_user (id)
);
CREATE TABLE dashboard_user (
	id INTEGER NOT NULL, 
	user_id INTEGER, 
	dashboard_id INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(dashboard_id) REFERENCES dashboards (id), 
	FOREIGN KEY(user_id) REFERENCES ab_user (id)
);
CREATE TABLE slice_user (
	id INTEGER NOT NULL, 
	user_id INTEGER, 
	slice_id INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(slice_id) REFERENCES slices (id), 
	FOREIGN KEY(user_id) REFERENCES ab_user (id)
);
INSERT INTO "slice_user" VALUES(1,1,35);
INSERT INTO "slice_user" VALUES(2,1,36);
INSERT INTO "slice_user" VALUES(3,1,37);
CREATE TABLE "clusters" (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	cluster_name VARCHAR(250), 
	coordinator_host VARCHAR(255), 
	coordinator_port INTEGER, 
	coordinator_endpoint VARCHAR(255), 
	broker_host VARCHAR(255), 
	broker_port INTEGER, 
	broker_endpoint VARCHAR(255), 
	metadata_last_refreshed DATETIME, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, cache_timeout INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id), 
	UNIQUE (cluster_name), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "clusters" VALUES('2016-11-21 19:47:27.652240','2016-12-08 19:08:49.691924',1,'druid cluster','10.37.2.142',8081,'druid/coordinator/v1/metadata','10.37.2.144',8082,'druid/v2','2016-12-08 19:08:49.690187',1,NULL,10000000);
CREATE TABLE "columns" (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	datasource_name VARCHAR(255), 
	column_name VARCHAR(255), 
	is_active BOOLEAN, 
	type VARCHAR(32), 
	groupby BOOLEAN, 
	count_distinct BOOLEAN, 
	sum BOOLEAN, 
	max BOOLEAN, 
	min BOOLEAN, 
	filterable BOOLEAN, 
	description TEXT, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, avg BOOLEAN, dimension_spec_json TEXT, 
	PRIMARY KEY (id), 
	CHECK (is_active IN (0, 1)), 
	CHECK (groupby IN (0, 1)), 
	CHECK (count_distinct IN (0, 1)), 
	CHECK (sum IN (0, 1)), 
	CHECK (max IN (0, 1)), 
	CHECK (min IN (0, 1)), 
	CHECK (filterable IN (0, 1)), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "columns" VALUES('2016-11-21 19:51:28.932124','2016-11-21 19:51:28.932176',1,'copa_fi_sum_td','rate_sum_quantity',1,'LONG',0,0,0,0,0,0,NULL,NULL,NULL,0,NULL);
INSERT INTO "columns" VALUES('2016-11-21 19:51:28.948495','2016-11-21 19:51:28.948517',2,'copa_fi_sum_td','rate_sum_yytz_amnt',1,'LONG',0,0,0,0,0,0,NULL,NULL,NULL,0,NULL);
INSERT INTO "columns" VALUES('2016-12-06 18:36:23.295766','2016-12-06 18:36:23.295811',949,'hbase_metrics','mutateCount_min',1,'LONG',0,0,0,0,0,0,NULL,NULL,NULL,0,NULL);
CREATE TABLE "datasources" (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	datasource_name VARCHAR(255), 
	is_featured BOOLEAN, 
	is_hidden BOOLEAN, 
	description TEXT, 
	default_endpoint TEXT, 
	user_id INTEGER, 
	cluster_name VARCHAR(250), 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, 
	"offset" INTEGER, 
	cache_timeout INTEGER, perm VARCHAR(1000), 
	PRIMARY KEY (id), 
	CHECK (is_featured IN (0, 1)), 
	CHECK (is_hidden IN (0, 1)), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(cluster_name) REFERENCES clusters (cluster_name), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(user_id) REFERENCES ab_user (id), 
	UNIQUE (datasource_name)
);
INSERT INTO "datasources" VALUES('2016-11-21 19:51:28.697894','2016-12-16 17:09:43.429787',1,'copa_fi_sum_td',0,0,NULL,NULL,NULL,'druid cluster',NULL,NULL,0,NULL,'[druid cluster].[copa_fi_sum_td](id:1)');
INSERT INTO "datasources" VALUES('2016-11-21 19:51:32.139774','2016-12-16 17:09:43.490323',2,'druid_metrics',0,0,NULL,NULL,NULL,'druid cluster',NULL,NULL,0,NULL,'[druid cluster].[druid_metrics](id:2)');
CREATE TABLE "table_columns" (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	table_id INTEGER, 
	column_name VARCHAR(255), 
	is_dttm BOOLEAN, 
	is_active BOOLEAN, 
	type VARCHAR(32), 
	groupby BOOLEAN, 
	count_distinct BOOLEAN, 
	sum BOOLEAN, 
	max BOOLEAN, 
	min BOOLEAN, 
	filterable BOOLEAN, 
	description TEXT, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, 
	expression TEXT, 
	verbose_name VARCHAR(1024), python_date_format VARCHAR(255), database_expression VARCHAR(255), avg BOOLEAN, 
	PRIMARY KEY (id), 
	CHECK (is_dttm IN (0, 1)), 
	CHECK (is_active IN (0, 1)), 
	CHECK (groupby IN (0, 1)), 
	CHECK (count_distinct IN (0, 1)), 
	CHECK (sum IN (0, 1)), 
	CHECK (max IN (0, 1)), 
	CHECK (min IN (0, 1)), 
	CHECK (filterable IN (0, 1)), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(table_id) REFERENCES tables (id)
);
INSERT INTO "table_columns" VALUES('2016-11-21 19:40:05.315624','2016-11-21 19:40:05.315670',1,1,'source',0,1,'VARCHAR(255)',1,0,0,0,0,1,'',NULL,NULL,'',NULL,NULL,NULL,0);
INSERT INTO "table_columns" VALUES('2016-11-21 19:40:05.335575','2016-11-21 19:40:05.335603',2,1,'target',0,1,'VARCHAR(255)',1,0,0,0,0,1,'',NULL,NULL,'',NULL,NULL,NULL,0);
CREATE TABLE "tables" (
	created_on DATETIME NOT NULL, 
	changed_on DATETIME NOT NULL, 
	id INTEGER NOT NULL, 
	table_name VARCHAR(250), 
	main_dttm_col VARCHAR(250), 
	default_endpoint TEXT, 
	database_id INTEGER NOT NULL, 
	created_by_fk INTEGER, 
	changed_by_fk INTEGER, 
	"offset" INTEGER, 
	description TEXT, 
	is_featured BOOLEAN, 
	user_id INTEGER, 
	cache_timeout INTEGER, 
	schema VARCHAR(255), sql TEXT, params TEXT, perm VARCHAR(1000), 
	PRIMARY KEY (id), 
	CHECK (is_featured IN (0, 1)), 
	CONSTRAINT user_id FOREIGN KEY(user_id) REFERENCES ab_user (id), 
	UNIQUE (table_name), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(database_id) REFERENCES dbs (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id)
);
INSERT INTO "tables" VALUES('2016-11-21 19:40:05.283385','2016-12-16 17:07:38.825622',1,'energy_usage',NULL,NULL,1,NULL,NULL,0,'Energy consumption',1,NULL,NULL,NULL,NULL,NULL,'[main].[energy_usage](id:1)');
INSERT INTO "tables" VALUES('2016-11-21 19:40:55.026520','2016-12-16 17:08:41.387319',3,'birth_names','ds',NULL,1,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,'[main].[birth_names](id:3)');
INSERT INTO "tables" VALUES('2016-11-21 19:40:56.639055','2016-12-16 17:08:43.683564',4,'random_time_series','ds',NULL,1,NULL,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,'[main].[random_time_series](id:4)');
CREATE TABLE access_request (
	created_on DATETIME, 
	changed_on DATETIME, 
	id INTEGER NOT NULL, 
	datasource_type VARCHAR(200), 
	datasource_id INTEGER, 
	changed_by_fk INTEGER, 
	created_by_fk INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(changed_by_fk) REFERENCES ab_user (id), 
	FOREIGN KEY(created_by_fk) REFERENCES ab_user (id)
);
CREATE TABLE "query" (
	id INTEGER NOT NULL, 
	client_id VARCHAR(11) NOT NULL, 
	database_id INTEGER NOT NULL, 
	tmp_table_name VARCHAR(256), 
	tab_name VARCHAR(256), 
	sql_editor_id VARCHAR(256), 
	user_id INTEGER, 
	status VARCHAR(16), 
	schema VARCHAR(256), 
	sql TEXT, 
	select_sql TEXT, 
	executed_sql TEXT, 
	"limit" INTEGER, 
	limit_used BOOLEAN, 
	select_as_cta BOOLEAN, 
	select_as_cta_used BOOLEAN, 
	progress INTEGER, 
	rows INTEGER, 
	error_message TEXT, 
	start_time NUMERIC(20, 6), 
	changed_on DATETIME, 
	end_time NUMERIC(20, 6), results_key VARCHAR(64), 
	PRIMARY KEY (id), 
	CHECK (limit_used IN (0, 1)), 
	CHECK (select_as_cta IN (0, 1)), 
	CHECK (select_as_cta_used IN (0, 1)), 
	CONSTRAINT client_id UNIQUE (client_id), 
	FOREIGN KEY(database_id) REFERENCES dbs (id), 
	FOREIGN KEY(user_id) REFERENCES ab_user (id)
);
CREATE TABLE energy_usage (
	source VARCHAR(255), 
	target VARCHAR(255), 
	value FLOAT
);
INSERT INTO "energy_usage" VALUES('Agricultural Energy Use','Carbon Dioxide',1.4);
INSERT INTO "energy_usage" VALUES('Agriculture','Agriculture Soils',5.2);
CREATE TABLE wb_health_population (
	"NY_GNP_PCAP_CD" FLOAT, 
	"SE_ADT_1524_LT_FM_ZS" FLOAT, 
	"SE_ADT_1524_LT_MA_ZS" FLOAT, 
	"SE_ADT_1524_LT_ZS" FLOAT, 
	"SE_ADT_LITR_FE_ZS" FLOAT, 
	"SE_ADT_LITR_MA_ZS" FLOAT, 
	"SE_ADT_LITR_ZS" FLOAT, 
	"SE_ENR_ORPH" FLOAT, 
	"SE_PRM_CMPT_FE_ZS" FLOAT, 
	"SE_PRM_CMPT_MA_ZS" FLOAT, 
	"SE_PRM_CMPT_ZS" FLOAT, 
	"SE_PRM_ENRR" FLOAT, 
	"SE_PRM_ENRR_FE" FLOAT, 
	"SE_PRM_ENRR_MA" FLOAT, 
	"SE_PRM_NENR" FLOAT, 
	"SE_PRM_NENR_FE" FLOAT, 
	"SE_PRM_NENR_MA" FLOAT, 
	"SE_SEC_ENRR" FLOAT, 
	"SE_SEC_ENRR_FE" FLOAT, 
	"SE_SEC_ENRR_MA" FLOAT, 
	"SE_SEC_NENR" FLOAT, 
	"SE_SEC_NENR_FE" FLOAT, 
	"SE_SEC_NENR_MA" FLOAT, 
	"SE_TER_ENRR" FLOAT, 
	"SE_TER_ENRR_FE" FLOAT, 
	"SE_XPD_TOTL_GD_ZS" FLOAT, 
	"SH_ANM_CHLD_ZS" FLOAT, 
	"SH_ANM_NPRG_ZS" FLOAT, 
	"SH_CON_1524_FE_ZS" FLOAT, 
	"SH_CON_1524_MA_ZS" FLOAT, 
	"SH_CON_AIDS_FE_ZS" FLOAT, 
	"SH_CON_AIDS_MA_ZS" FLOAT, 
	"SH_DTH_COMM_ZS" FLOAT, 
	"SH_DTH_IMRT" FLOAT, 
	"SH_DTH_INJR_ZS" FLOAT, 
	"SH_DTH_MORT" FLOAT, 
	"SH_DTH_NCOM_ZS" FLOAT, 
	"SH_DTH_NMRT" FLOAT, 
	"SH_DYN_AIDS" FLOAT, 
	"SH_DYN_AIDS_DH" FLOAT, 
	"SH_DYN_AIDS_FE_ZS" FLOAT, 
	"SH_DYN_AIDS_ZS" FLOAT, 
	"SH_DYN_MORT" FLOAT, 
	"SH_DYN_MORT_FE" FLOAT, 
	"SH_DYN_MORT_MA" FLOAT, 
	"SH_DYN_NMRT" FLOAT, 
	"SH_FPL_SATI_ZS" FLOAT, 
	"SH_H2O_SAFE_RU_ZS" FLOAT, 
	"SH_H2O_SAFE_UR_ZS" FLOAT, 
	"SH_H2O_SAFE_ZS" FLOAT, 
	"SH_HIV_0014" FLOAT, 
	"SH_HIV_1524_FE_ZS" FLOAT, 
	"SH_HIV_1524_KW_FE_ZS" FLOAT, 
	"SH_HIV_1524_KW_MA_ZS" FLOAT, 
	"SH_HIV_1524_MA_ZS" FLOAT, 
	"SH_HIV_ARTC_ZS" FLOAT, 
	"SH_HIV_KNOW_FE_ZS" FLOAT, 
	"SH_HIV_KNOW_MA_ZS" FLOAT, 
	"SH_HIV_ORPH" FLOAT, 
	"SH_HIV_TOTL" FLOAT, 
	"SH_IMM_HEPB" FLOAT, 
	"SH_IMM_HIB3" FLOAT, 
	"SH_IMM_IBCG" FLOAT, 
	"SH_IMM_IDPT" FLOAT, 
	"SH_IMM_MEAS" FLOAT, 
	"SH_IMM_POL3" FLOAT, 
	"SH_MED_BEDS_ZS" FLOAT, 
	"SH_MED_CMHW_P3" FLOAT, 
	"SH_MED_NUMW_P3" FLOAT, 
	"SH_MED_PHYS_ZS" FLOAT, 
	"SH_MLR_NETS_ZS" FLOAT, 
	"SH_MLR_PREG_ZS" FLOAT, 
	"SH_MLR_SPF2_ZS" FLOAT, 
	"SH_MLR_TRET_ZS" FLOAT, 
	"SH_MMR_DTHS" FLOAT, 
	"SH_MMR_LEVE" FLOAT, 
	"SH_MMR_RISK" FLOAT, 
	"SH_MMR_RISK_ZS" FLOAT, 
	"SH_MMR_WAGE_ZS" FLOAT, 
	"SH_PRG_ANEM" FLOAT, 
	"SH_PRG_ARTC_ZS" FLOAT, 
	"SH_PRG_SYPH_ZS" FLOAT, 
	"SH_PRV_SMOK_FE" FLOAT, 
	"SH_PRV_SMOK_MA" FLOAT, 
	"SH_STA_ACSN" FLOAT, 
	"SH_STA_ACSN_RU" FLOAT, 
	"SH_STA_ACSN_UR" FLOAT, 
	"SH_STA_ANV4_ZS" FLOAT, 
	"SH_STA_ANVC_ZS" FLOAT, 
	"SH_STA_ARIC_ZS" FLOAT, 
	"SH_STA_BFED_ZS" FLOAT, 
	"SH_STA_BRTC_ZS" FLOAT, 
	"SH_STA_BRTW_ZS" FLOAT, 
	"SH_STA_DIAB_ZS" FLOAT, 
	"SH_STA_IYCF_ZS" FLOAT, 
	"SH_STA_MALN_FE_ZS" FLOAT, 
	"SH_STA_MALN_MA_ZS" FLOAT, 
	"SH_STA_MALN_ZS" FLOAT, 
	"SH_STA_MALR" FLOAT, 
	"SH_STA_MMRT" FLOAT, 
	"SH_STA_MMRT_NE" FLOAT, 
	"SH_STA_ORCF_ZS" FLOAT, 
	"SH_STA_ORTH" FLOAT, 
	"SH_STA_OW15_FE_ZS" FLOAT, 
	"SH_STA_OW15_MA_ZS" FLOAT, 
	"SH_STA_OW15_ZS" FLOAT, 
	"SH_STA_OWGH_FE_ZS" FLOAT, 
	"SH_STA_OWGH_MA_ZS" FLOAT, 
	"SH_STA_OWGH_ZS" FLOAT, 
	"SH_STA_PNVC_ZS" FLOAT, 
	"SH_STA_STNT_FE_ZS" FLOAT, 
	"SH_STA_STNT_MA_ZS" FLOAT, 
	"SH_STA_STNT_ZS" FLOAT, 
	"SH_STA_WAST_FE_ZS" FLOAT, 
	"SH_STA_WAST_MA_ZS" FLOAT, 
	"SH_STA_WAST_ZS" FLOAT, 
	"SH_SVR_WAST_FE_ZS" FLOAT, 
	"SH_SVR_WAST_MA_ZS" FLOAT, 
	"SH_SVR_WAST_ZS" FLOAT, 
	"SH_TBS_CURE_ZS" FLOAT, 
	"SH_TBS_DTEC_ZS" FLOAT, 
	"SH_TBS_INCD" FLOAT, 
	"SH_TBS_MORT" FLOAT, 
	"SH_TBS_PREV" FLOAT, 
	"SH_VAC_TTNS_ZS" FLOAT, 
	"SH_XPD_EXTR_ZS" FLOAT, 
	"SH_XPD_OOPC_TO_ZS" FLOAT, 
	"SH_XPD_OOPC_ZS" FLOAT, 
	"SH_XPD_PCAP" FLOAT, 
	"SH_XPD_PCAP_PP_KD" FLOAT, 
	"SH_XPD_PRIV" FLOAT, 
	"SH_XPD_PRIV_ZS" FLOAT, 
	"SH_XPD_PUBL" FLOAT, 
	"SH_XPD_PUBL_GX_ZS" FLOAT, 
	"SH_XPD_PUBL_ZS" FLOAT, 
	"SH_XPD_TOTL_CD" FLOAT, 
	"SH_XPD_TOTL_ZS" FLOAT, 
	"SI_POV_NAHC" FLOAT, 
	"SI_POV_RUHC" FLOAT, 
	"SI_POV_URHC" FLOAT, 
	"SL_EMP_INSV_FE_ZS" FLOAT, 
	"SL_TLF_TOTL_FE_ZS" FLOAT, 
	"SL_TLF_TOTL_IN" FLOAT, 
	"SL_UEM_TOTL_FE_ZS" FLOAT, 
	"SL_UEM_TOTL_MA_ZS" FLOAT, 
	"SL_UEM_TOTL_ZS" FLOAT, 
	"SM_POP_NETM" FLOAT, 
	"SN_ITK_DEFC" FLOAT, 
	"SN_ITK_DEFC_ZS" FLOAT, 
	"SN_ITK_SALT_ZS" FLOAT, 
	"SN_ITK_VITA_ZS" FLOAT, 
	"SP_ADO_TFRT" FLOAT, 
	"SP_DYN_AMRT_FE" FLOAT, 
	"SP_DYN_AMRT_MA" FLOAT, 
	"SP_DYN_CBRT_IN" FLOAT, 
	"SP_DYN_CDRT_IN" FLOAT, 
	"SP_DYN_CONU_ZS" FLOAT, 
	"SP_DYN_IMRT_FE_IN" FLOAT, 
	"SP_DYN_IMRT_IN" FLOAT, 
	"SP_DYN_IMRT_MA_IN" FLOAT, 
	"SP_DYN_LE00_FE_IN" FLOAT, 
	"SP_DYN_LE00_IN" FLOAT, 
	"SP_DYN_LE00_MA_IN" FLOAT, 
	"SP_DYN_SMAM_FE" FLOAT, 
	"SP_DYN_SMAM_MA" FLOAT, 
	"SP_DYN_TFRT_IN" FLOAT, 
	"SP_DYN_TO65_FE_ZS" FLOAT, 
	"SP_DYN_TO65_MA_ZS" FLOAT, 
	"SP_DYN_WFRT" FLOAT, 
	"SP_HOU_FEMA_ZS" FLOAT, 
	"SP_MTR_1519_ZS" FLOAT, 
	"SP_POP_0004_FE" FLOAT, 
	"SP_POP_0004_FE_5Y" FLOAT, 
	"SP_POP_0004_MA" FLOAT, 
	"SP_POP_0004_MA_5Y" FLOAT, 
	"SP_POP_0014_FE_ZS" FLOAT, 
	"SP_POP_0014_MA_ZS" FLOAT, 
	"SP_POP_0014_TO" FLOAT, 
	"SP_POP_0014_TO_ZS" FLOAT, 
	"SP_POP_0509_FE" FLOAT, 
	"SP_POP_0509_FE_5Y" FLOAT, 
	"SP_POP_0509_MA" FLOAT, 
	"SP_POP_0509_MA_5Y" FLOAT, 
	"SP_POP_1014_FE" FLOAT, 
	"SP_POP_1014_FE_5Y" FLOAT, 
	"SP_POP_1014_MA" FLOAT, 
	"SP_POP_1014_MA_5Y" FLOAT, 
	"SP_POP_1519_FE" FLOAT, 
	"SP_POP_1519_FE_5Y" FLOAT, 
	"SP_POP_1519_MA" FLOAT, 
	"SP_POP_1519_MA_5Y" FLOAT, 
	"SP_POP_1564_FE_ZS" FLOAT, 
	"SP_POP_1564_MA_ZS" FLOAT, 
	"SP_POP_1564_TO" FLOAT, 
	"SP_POP_1564_TO_ZS" FLOAT, 
	"SP_POP_2024_FE" FLOAT, 
	"SP_POP_2024_FE_5Y" FLOAT, 
	"SP_POP_2024_MA" FLOAT, 
	"SP_POP_2024_MA_5Y" FLOAT, 
	"SP_POP_2529_FE" FLOAT, 
	"SP_POP_2529_FE_5Y" FLOAT, 
	"SP_POP_2529_MA" FLOAT, 
	"SP_POP_2529_MA_5Y" FLOAT, 
	"SP_POP_3034_FE" FLOAT, 
	"SP_POP_3034_FE_5Y" FLOAT, 
	"SP_POP_3034_MA" FLOAT, 
	"SP_POP_3034_MA_5Y" FLOAT, 
	"SP_POP_3539_FE" FLOAT, 
	"SP_POP_3539_FE_5Y" FLOAT, 
	"SP_POP_3539_MA" FLOAT, 
	"SP_POP_3539_MA_5Y" FLOAT, 
	"SP_POP_4044_FE" FLOAT, 
	"SP_POP_4044_FE_5Y" FLOAT, 
	"SP_POP_4044_MA" FLOAT, 
	"SP_POP_4044_MA_5Y" FLOAT, 
	"SP_POP_4549_FE" FLOAT, 
	"SP_POP_4549_FE_5Y" FLOAT, 
	"SP_POP_4549_MA" FLOAT, 
	"SP_POP_4549_MA_5Y" FLOAT, 
	"SP_POP_5054_FE" FLOAT, 
	"SP_POP_5054_FE_5Y" FLOAT, 
	"SP_POP_5054_MA" FLOAT, 
	"SP_POP_5054_MA_5Y" FLOAT, 
	"SP_POP_5559_FE" FLOAT, 
	"SP_POP_5559_FE_5Y" FLOAT, 
	"SP_POP_5559_MA" FLOAT, 
	"SP_POP_5559_MA_5Y" FLOAT, 
	"SP_POP_6064_FE" FLOAT, 
	"SP_POP_6064_FE_5Y" FLOAT, 
	"SP_POP_6064_MA" FLOAT, 
	"SP_POP_6064_MA_5Y" FLOAT, 
	"SP_POP_6569_FE" FLOAT, 
	"SP_POP_6569_FE_5Y" FLOAT, 
	"SP_POP_6569_MA" FLOAT, 
	"SP_POP_6569_MA_5Y" FLOAT, 
	"SP_POP_65UP_FE_ZS" FLOAT, 
	"SP_POP_65UP_MA_ZS" FLOAT, 
	"SP_POP_65UP_TO" FLOAT, 
	"SP_POP_65UP_TO_ZS" FLOAT, 
	"SP_POP_7074_FE" FLOAT, 
	"SP_POP_7074_FE_5Y" FLOAT, 
	"SP_POP_7074_MA" FLOAT, 
	"SP_POP_7074_MA_5Y" FLOAT, 
	"SP_POP_7579_FE" FLOAT, 
	"SP_POP_7579_FE_5Y" FLOAT, 
	"SP_POP_7579_MA" FLOAT, 
	"SP_POP_7579_MA_5Y" FLOAT, 
	"SP_POP_80UP_FE" FLOAT, 
	"SP_POP_80UP_FE_5Y" FLOAT, 
	"SP_POP_80UP_MA" FLOAT, 
	"SP_POP_80UP_MA_5Y" FLOAT, 
	"SP_POP_AG00_FE_IN" FLOAT, 
	"SP_POP_AG00_MA_IN" FLOAT, 
	"SP_POP_AG01_FE_IN" FLOAT, 
	"SP_POP_AG01_MA_IN" FLOAT, 
	"SP_POP_AG02_FE_IN" FLOAT, 
	"SP_POP_AG02_MA_IN" FLOAT, 
	"SP_POP_AG03_FE_IN" FLOAT, 
	"SP_POP_AG03_MA_IN" FLOAT, 
	"SP_POP_AG04_FE_IN" FLOAT, 
	"SP_POP_AG04_MA_IN" FLOAT, 
	"SP_POP_AG05_FE_IN" FLOAT, 
	"SP_POP_AG05_MA_IN" FLOAT, 
	"SP_POP_AG06_FE_IN" FLOAT, 
	"SP_POP_AG06_MA_IN" FLOAT, 
	"SP_POP_AG07_FE_IN" FLOAT, 
	"SP_POP_AG07_MA_IN" FLOAT, 
	"SP_POP_AG08_FE_IN" FLOAT, 
	"SP_POP_AG08_MA_IN" FLOAT, 
	"SP_POP_AG09_FE_IN" FLOAT, 
	"SP_POP_AG09_MA_IN" FLOAT, 
	"SP_POP_AG10_FE_IN" FLOAT, 
	"SP_POP_AG10_MA_IN" FLOAT, 
	"SP_POP_AG11_FE_IN" FLOAT, 
	"SP_POP_AG11_MA_IN" FLOAT, 
	"SP_POP_AG12_FE_IN" FLOAT, 
	"SP_POP_AG12_MA_IN" FLOAT, 
	"SP_POP_AG13_FE_IN" FLOAT, 
	"SP_POP_AG13_MA_IN" FLOAT, 
	"SP_POP_AG14_FE_IN" FLOAT, 
	"SP_POP_AG14_MA_IN" FLOAT, 
	"SP_POP_AG15_FE_IN" FLOAT, 
	"SP_POP_AG15_MA_IN" FLOAT, 
	"SP_POP_AG16_FE_IN" FLOAT, 
	"SP_POP_AG16_MA_IN" FLOAT, 
	"SP_POP_AG17_FE_IN" FLOAT, 
	"SP_POP_AG17_MA_IN" FLOAT, 
	"SP_POP_AG18_FE_IN" FLOAT, 
	"SP_POP_AG18_MA_IN" FLOAT, 
	"SP_POP_AG19_FE_IN" FLOAT, 
	"SP_POP_AG19_MA_IN" FLOAT, 
	"SP_POP_AG20_FE_IN" FLOAT, 
	"SP_POP_AG20_MA_IN" FLOAT, 
	"SP_POP_AG21_FE_IN" FLOAT, 
	"SP_POP_AG21_MA_IN" FLOAT, 
	"SP_POP_AG22_FE_IN" FLOAT, 
	"SP_POP_AG22_MA_IN" FLOAT, 
	"SP_POP_AG23_FE_IN" FLOAT, 
	"SP_POP_AG23_MA_IN" FLOAT, 
	"SP_POP_AG24_FE_IN" FLOAT, 
	"SP_POP_AG24_MA_IN" FLOAT, 
	"SP_POP_AG25_FE_IN" FLOAT, 
	"SP_POP_AG25_MA_IN" FLOAT, 
	"SP_POP_BRTH_MF" FLOAT, 
	"SP_POP_DPND" FLOAT, 
	"SP_POP_DPND_OL" FLOAT, 
	"SP_POP_DPND_YG" FLOAT, 
	"SP_POP_GROW" FLOAT, 
	"SP_POP_TOTL" FLOAT, 
	"SP_POP_TOTL_FE_IN" FLOAT, 
	"SP_POP_TOTL_FE_ZS" FLOAT, 
	"SP_POP_TOTL_MA_IN" FLOAT, 
	"SP_POP_TOTL_MA_ZS" FLOAT, 
	"SP_REG_BRTH_RU_ZS" FLOAT, 
	"SP_REG_BRTH_UR_ZS" FLOAT, 
	"SP_REG_BRTH_ZS" FLOAT, 
	"SP_REG_DTHS_ZS" FLOAT, 
	"SP_RUR_TOTL" FLOAT, 
	"SP_RUR_TOTL_ZG" FLOAT, 
	"SP_RUR_TOTL_ZS" FLOAT, 
	"SP_URB_GROW" FLOAT, 
	"SP_URB_TOTL" FLOAT, 
	"SP_URB_TOTL_IN_ZS" FLOAT, 
	"SP_UWT_TFRT" FLOAT, 
	country_code VARCHAR(3), 
	country_name VARCHAR(255), 
	region VARCHAR(255), 
	year DATETIME
);
INSERT INTO "wb_health_population" VALUES(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.1706269979,NULL,NULL,0.0348442495,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,145.321,565.0536,608.188,53.73,33.39,NULL,NULL,NULL,NULL,31.893,31.5800487805,31.282,NULL,NULL,7.671,19.85901,17.28853,NULL,NULL,NULL,758332.0,17.4526590995,779707.0,16.7689274585,43.4675541072,40.9586355115,3793159.0,42.1706091513,584754.0,13.457841973,600733.0,12.9197866621,545614.0,12.5570530347,524019.0,11.2699213908,445978.0,10.2639767277,476117.0,10.239707268,53.8683522513,56.1163237387,4949871.0,55.0304048131,382499.0,8.8030369982,415921.0,8.9450897292,325664.0,7.4950058457,361143.0,7.7669955113,275406.0,6.3383412964,319223.0,6.8654344903,231432.0,5.3263000911,267880.0,5.7612157998,194046.0,4.465878649,222625.0,4.7879299217,162562.0,3.7412889981,183177.0,3.9395334723,134240.0,3.0894713101,147801.0,3.1787123205,107417.0,2.4721524115,122305.0,2.6303774018,81379.0,1.8728999236,93056.0,2.0013278239,56627.0,1.303244129,67283.0,1.4470355482,2.6640936415,2.9250407498,251763.0,2.7989860356,34653.0,0.7975227153,40746.0,0.8763121509,16990.0,0.3910169663,19681.0,0.4232734364,7487.0,0.1723098309,8296.0,0.1784196143,178556.0,177635.0,162320.0,165324.0,149031.0,154536.0,138378.0,145153.0,130047.0,137059.0,123728.0,130134.0,119108.0,124264.0,115875.0,119328.0,113718.0,115212.0,112325.0,111795.0,111850.0,109013.0,112447.0,106800.0,111473.0,104781.0,107682.0,102732.0,102162.0,100693.0,97200.0,98951.0,92279.0,97482.0,88099.0,95699.0,85217.0,93345.0,83183.0,90640.0,80987.0,88065.0,78832.0,85538.0,76635.0,83075.0,74256.0,80738.0,71789.0,78505.0,69468.0,76259.0,NULL,81.7177255731,5.086253763,76.6314718101,1.8136768792,8994793.0,4345080.0,48.306614727,4649713.0,51.693385273,NULL,NULL,NULL,NULL,8255331.0,1.5112289351,91.779,5.2538074226,739462.0,8.221,NULL,'AFG','Afghanistan','South Asia','1960-01-01 00:00:00.000000');
INSERT INTO "wb_health_population" VALUES(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,105341.0,NULL,157255.0,NULL,NULL,NULL,NULL,NULL,NULL,356.5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,145.321,558.3968,601.95,53.726,32.773,NULL,NULL,240.5,NULL,32.433,32.0959756098,31.775,NULL,NULL,7.671,20.47498,17.81978,NULL,NULL,NULL,792360.0,17.8639346062,806891.0,17.0611128308,43.6640364655,41.357431869,3892696.0,42.4737518883,594810.0,13.4101253763,618851.0,13.0851462422,549561.0,12.3899764831,530223.0,11.211172796,458676.0,10.3409536946,479934.0,10.1478491213,53.6575006048,55.7122063274,5014856.0,54.7177969971,387812.0,8.743308859,421542.0,8.9131935106,330878.0,7.4597190098,364754.0,7.7124532923,279733.0,6.3066434691,321953.0,6.8074578341,234987.0,5.2978348242,271973.0,5.7506677357,196832.0,4.4376217583,225358.0,4.7650280711,164570.0,3.7102677043,185178.0,3.9154517175,135748.0,3.0604692248,148725.0,3.1446800197,108527.0,2.4467656507,122108.0,2.58188326,82231.0,1.85391641,93337.0,1.973541765,57370.0,1.2934195673,67311.0,1.4232412628,2.6784629297,2.9303618037,257393.0,2.8084511145,35343.0,0.7968158928,41505.0,0.8775924977,17738.0,0.3999072039,20491.0,0.4332670249,8353.0,0.1883202657,9282.0,0.1962610183,182674.0,181588.0,171491.0,171802.0,157159.0,160585.0,145319.0,150745.0,135717.0,142171.0,128098.0,134751.0,122173.0,128386.0,117648.0,122980.0,114460.0,118355.0,112431.0,114379.0,111194.0,110973.0,110715.0,108144.0,111188.0,105815.0,110123.0,103692.0,106341.0,101599.0,100884.0,99551.0,95945.0,97762.0,91026.0,96220.0,86848.0,94385.0,83973.0,92016.0,81947.0,89322.0,79760.0,86753.0,77613.0,84232.0,75428.0,81779.0,73064.0,79456.0,70616.0,77239.0,NULL,82.7558956827,5.1326099892,77.6232856935,1.874002583,9164945.0,4435529.0,48.3966788671,4729416.0,51.6033211329,NULL,NULL,NULL,NULL,8385191.0,1.5608000308,91.492,5.3055590565,779754.0,8.508,NULL,'AFG','Afghanistan','South Asia','1961-01-01 00:00:00.000000');
CREATE TABLE birth_names (
	ds DATETIME, 
	gender VARCHAR(16), 
	name VARCHAR(255), 
	num BIGINT, 
	state VARCHAR(10), 
	sum_boys BIGINT, 
	sum_girls BIGINT
);
INSERT INTO "birth_names" VALUES('1965-01-01 00:00:00.000000','boy','Aaron',369,'CA',369,0);
INSERT INTO "birth_names" VALUES('1965-01-01 00:00:00.000000','girl','Amy',494,'CA',0,494);
CREATE TABLE random_time_series (
	ds DATETIME
);
INSERT INTO "random_time_series" VALUES('2017-07-19 13:23:33.000000');
INSERT INTO "random_time_series" VALUES('2016-11-23 00:48:29.000000');
CREATE TABLE long_lat (
	"LON" FLOAT, 
	"LAT" FLOAT, 
	"NUMBER" TEXT, 
	"STREET" TEXT, 
	"UNIT" TEXT, 
	"CITY" FLOAT, 
	"DISTRICT" FLOAT, 
	"REGION" FLOAT, 
	"POSTCODE" BIGINT, 
	"ID" FLOAT, 
	date DATE, 
	occupancy FLOAT, 
	radius_miles FLOAT
);
INSERT INTO "long_lat" VALUES(-122.3912672,37.7690928,'1550','04th Street',NULL,NULL,NULL,NULL,94158,NULL,'2016-12-16',4.0,2.86484850892037);
INSERT INTO "long_lat" VALUES(-122.3908502,37.7694259,'1505','04th Street',NULL,NULL,NULL,NULL,94158,NULL,'2016-12-16',4.0,2.05121870620017);
CREATE TABLE multiformat_time_series (
	ds DATE, 
	ds2 DATETIME, 
	epoch_ms BIGINT, 
	epoch_s BIGINT, 
	string0 VARCHAR(100), 
	string1 VARCHAR(100), 
	string2 VARCHAR(100), 
	string3 VARCHAR(100)
);
INSERT INTO "multiformat_time_series" VALUES('2017-07-19','2017-07-19 13:23:33.000000',1500470613000,1500470613,'2017-07-19 06:23:33.000000','2017-07-19^06:23:33','20170719-062333','2017/07/1906:23:33.000000');
INSERT INTO "multiformat_time_series" VALUES('2016-11-23','2016-11-23 00:48:29.000000',1479862109000,1479862109,'2016-11-22 16:48:29.000000','2016-11-22^16:48:29','20161122-164829','2016/11/2216:48:29.000000');
CREATE INDEX ti_user_id_changed_on ON "query" (user_id, changed_on);
COMMIT;
