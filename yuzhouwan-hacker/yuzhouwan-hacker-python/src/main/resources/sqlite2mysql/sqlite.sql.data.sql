INSERT INTO ab_user VALUES(1,'admin','user','admin','pbkdf2:sha1:1000$sZRobnh7$3217dfa97fd183b69f207a7829a61bbd1300591d',1,'admin@fab.org','2016-12-23 11:02:29.474892',7,0,'2016-11-17 20:32:25.452827','2016-11-17 20:32:25.452864',NULL,NULL);
INSERT INTO ab_user VALUES(2,'superset','jinjy','superset','pbkdf2:sha1:1000$qyOiYsI7$669dd91df94162c2c84b9f0153cc776c11a27c49',1,'16110505@yuzhouwan.com','2017-01-22 14:06:02.424812',12,0,'2016-12-14 17:53:40.937795','2016-12-14 18:54:43.259585',NULL,1);
INSERT INTO ab_view_menu VALUES(1,'MyIndexView');
INSERT INTO ab_view_menu VALUES(2,'UtilView');
INSERT INTO ab_role VALUES(1,'Admin');
INSERT INTO ab_role VALUES(2,'Public');
INSERT INTO ab_role VALUES(3,'Alpha');
INSERT INTO ab_role VALUES(4,'Gamma');
INSERT INTO ab_role VALUES(5,'sql_lab');
INSERT INTO ab_role VALUES(6,'granter');
INSERT INTO ab_permission VALUES(1,'can_this_form_post');
INSERT INTO ab_permission VALUES(2,'can_this_form_get');
INSERT INTO ab_user_role VALUES(1,1,1);
INSERT INTO ab_user_role VALUES(2,2,1);
INSERT INTO ab_permission_view VALUES(1,1,4);
INSERT INTO ab_permission_view VALUES(2,2,4);
INSERT INTO ab_permission_view_role VALUES(1,1,1);
INSERT INTO ab_permission_view_role VALUES(2,2,1);
INSERT INTO alembic_version VALUES('e46f2d27a08e');
INSERT INTO dashboards VALUES('2016-11-21 19:40:51.531836','2016-12-16 17:08:37.126564',1,'World''s Bank Data','[
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
INSERT INTO dbs VALUES('2016-11-21 19:40:05.271468','2016-12-16 17:07:38.795402',1,'main','sqlite:////root/.superset/superset.db',NULL,NULL,NULL,NULL,'{
    "metadata_params": {},
    "engine_params": {}
}
',0,0,1,NULL,0,1,0,'[main].(id:1)');
INSERT INTO metrics VALUES(1,'count','COUNT(*)','count','copa_fi_sum_td','{"type": "count", "name": "count"}',NULL,NULL,'2016-11-21 19:51:28.943189',NULL,'2016-11-21 19:51:28.943154',0,NULL);
INSERT INTO metrics VALUES(2,'count','COUNT(*)','count','druid_metrics','{"type": "count", "name": "count"}',NULL,NULL,'2016-11-21 19:51:33.740316',NULL,'2016-11-21 19:51:33.740290',0,NULL);
INSERT INTO slices VALUES('2016-12-06 15:48:41.467559','2016-12-06 15:48:41.467593',35,'region top 10',NULL,NULL,'druid','hbase_metrics','line','{
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
INSERT INTO sql_metrics VALUES('2016-11-21 19:40:05.367107','2016-11-21 19:40:05.367130',1,'sum__value','sum__value','sum',1,'SUM(value)',NULL,NULL,NULL,0,NULL);
INSERT INTO sql_metrics VALUES('2016-11-21 19:40:05.378120','2016-11-21 19:40:05.378142',2,'avg__value','avg__value','avg',1,'AVG(value)',NULL,NULL,NULL,0,NULL);
INSERT INTO dashboard_slices VALUES(51,1,75);
INSERT INTO dashboard_slices VALUES(52,1,76);
INSERT INTO logs VALUES(1,'explore',1,'{"datasource_id": "11", "datasource_type": "druid"}','2016-11-21 11:58:50',NULL,0,'2016-11-21');
INSERT INTO logs VALUES(2,'explore',1,'{"datasource_id": "4", "datasource_type": "druid"}','2016-11-21 11:59:01',NULL,0,'2016-11-21');
INSERT INTO url VALUES('2016-12-08 20:31:01.432592','2016-12-08 20:31:01.432639',1,'//superset/explore/druid/15/?goto_dash=false&new_dashboard_name=&slice_name=region+top+10&y_axis_zero=false&y_axis_label=&resample_fillmethod=&save_to_dashboard_id=&line_interpolation=linear&add_to_dash=false&show_legend=y&rolling_periods=&x_axis_label=&rolling_type=None&new_slice_name=region+top+10&flt_op_0=in&viz_type=line&show_markers=false&since=12+hours+ago&y_log_scale=false&time_compare=&having_col_0=&json=false&until=now&having_eq_0=&rdo_save=saveas&having_op_0=%3D%3D&collapsed_fieldsets=&resample_rule=&datasource_id=15&period_ratio_type=growth&show_brush=false&y_axis_format=.3s&x_axis_showminmax=y&metrics=incReadRequestCount&granularity=one+day&flt_eq_0=&flt_col_0=&timeseries_limit_metric=incReadRequestCount&resample_how=&slice_id=36&num_period_compare=&userid=1&datasource_type=druid&druid_time_origin=&rich_tooltip=y&limit=50&x_axis_format=smart_date&datasource_name=hbase_metrics&contribution=false&groupby=tableName',1,1);
INSERT INTO url VALUES('2016-12-11 18:07:23.456765','2016-12-11 18:07:23.456797',2,'//superset/sqllab?title=Untitled%20Query&sql=SELECT%20*%0AFROM%0AWHERE',1,1);
INSERT INTO url VALUES('2016-12-14 18:55:04.523695','2016-12-14 18:55:04.523848',3,'//superset/sqllab?title=Untitled%20Query&sql=SELECT%20*%0AFROM%0AWHERE',1,1);
INSERT INTO url VALUES('2017-01-16 17:38:33.633559','2017-01-16 17:38:33.633615',4,'//superset/explore/druid/15/?viz_type=line&granularity=1+hour&druid_time_origin=&since=28+days+ago&until=now&metrics=ReadQps&groupby=nameSpaceName&groupby=tableName&limit=10&timeseries_limit_metric=incReadRequestCount&show_brush=false&show_legend=y&show_legend=false&rich_tooltip=y&rich_tooltip=false&y_axis_zero=false&y_log_scale=false&contribution=false&show_markers=false&x_axis_showminmax=y&x_axis_showminmax=false&line_interpolation=linear&x_axis_format=smart_date&y_axis_format=.3s&x_axis_label=&y_axis_label=&rolling_type=None&rolling_periods=&time_compare=&num_period_compare=&period_ratio_type=growth&resample_how=&resample_rule=&resample_fillmethod=&flt_col_0=&flt_op_0=in&flt_eq_0=&having_col_0=&having_op_0=%3D%3D&having_eq_0=&slice_id=36&slice_name=region+top+10&collapsed_fieldsets=&action=&userid=2&goto_dash=false&datasource_name=hbase_metrics&datasource_id=15&datasource_type=druid&previous_viz_type=line&rdo_save=overwrite&new_slice_name=&add_to_dash=false&save_to_dashboard_id=&new_dashboard_name=',2,2);
INSERT INTO css_templates VALUES('2016-11-21 19:40:05.061464','2016-11-21 19:40:05.061509',1,'Flat','.gridster div.widget {
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
INSERT INTO slice_user VALUES(1,1,35);
INSERT INTO slice_user VALUES(2,1,36);
INSERT INTO slice_user VALUES(3,1,37);
INSERT INTO clusters VALUES('2016-11-21 19:47:27.652240','2016-12-08 19:08:49.691924',1,'druid cluster','10.37.2.142',8081,'druid/coordinator/v1/metadata','10.37.2.144',8082,'druid/v2','2016-12-08 19:08:49.690187',1,NULL,10000000);
INSERT INTO columns VALUES('2016-11-21 19:51:28.932124','2016-11-21 19:51:28.932176',1,'copa_fi_sum_td','rate_sum_quantity',1,'LONG',0,0,0,0,0,0,NULL,NULL,NULL,0,NULL);
INSERT INTO columns VALUES('2016-11-21 19:51:28.948495','2016-11-21 19:51:28.948517',2,'copa_fi_sum_td','rate_sum_yytz_amnt',1,'LONG',0,0,0,0,0,0,NULL,NULL,NULL,0,NULL);
INSERT INTO columns VALUES('2016-12-06 18:36:23.295766','2016-12-06 18:36:23.295811',949,'hbase_metrics','mutateCount_min',1,'LONG',0,0,0,0,0,0,NULL,NULL,NULL,0,NULL);
INSERT INTO datasources VALUES('2016-11-21 19:51:28.697894','2016-12-16 17:09:43.429787',1,'copa_fi_sum_td',0,0,NULL,NULL,NULL,'druid cluster',NULL,NULL,0,NULL,'[druid cluster].[copa_fi_sum_td](id:1)');
INSERT INTO datasources VALUES('2016-11-21 19:51:32.139774','2016-12-16 17:09:43.490323',2,'druid_metrics',0,0,NULL,NULL,NULL,'druid cluster',NULL,NULL,0,NULL,'[druid cluster].[druid_metrics](id:2)');
INSERT INTO table_columns VALUES('2016-11-21 19:40:05.315624','2016-11-21 19:40:05.315670',1,1,'source',0,1,'VARCHAR(255)',1,0,0,0,0,1,'',NULL,NULL,'',NULL,NULL,NULL,0);
INSERT INTO table_columns VALUES('2016-11-21 19:40:05.335575','2016-11-21 19:40:05.335603',2,1,'target',0,1,'VARCHAR(255)',1,0,0,0,0,1,'',NULL,NULL,'',NULL,NULL,NULL,0);
INSERT INTO tables VALUES('2016-11-21 19:40:05.283385','2016-12-16 17:07:38.825622',1,'energy_usage',NULL,NULL,1,NULL,NULL,0,'Energy consumption',1,NULL,NULL,NULL,NULL,NULL,'[main].[energy_usage](id:1)');
INSERT INTO tables VALUES('2016-11-21 19:40:55.026520','2016-12-16 17:08:41.387319',3,'birth_names','ds',NULL,1,NULL,NULL,0,NULL,1,NULL,NULL,NULL,NULL,NULL,'[main].[birth_names](id:3)');
INSERT INTO tables VALUES('2016-11-21 19:40:56.639055','2016-12-16 17:08:43.683564',4,'random_time_series','ds',NULL,1,NULL,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,'[main].[random_time_series](id:4)');
INSERT INTO energy_usage VALUES('Agricultural Energy Use','Carbon Dioxide',1.4);
INSERT INTO energy_usage VALUES('Agriculture','Agriculture Soils',5.2);
INSERT INTO wb_health_population VALUES(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0.1706269979,NULL,NULL,0.0348442495,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,145.321,565.0536,608.188,53.73,33.39,NULL,NULL,NULL,NULL,31.893,31.5800487805,31.282,NULL,NULL,7.671,19.85901,17.28853,NULL,NULL,NULL,758332.0,17.4526590995,779707.0,16.7689274585,43.4675541072,40.9586355115,3793159.0,42.1706091513,584754.0,13.457841973,600733.0,12.9197866621,545614.0,12.5570530347,524019.0,11.2699213908,445978.0,10.2639767277,476117.0,10.239707268,53.8683522513,56.1163237387,4949871.0,55.0304048131,382499.0,8.8030369982,415921.0,8.9450897292,325664.0,7.4950058457,361143.0,7.7669955113,275406.0,6.3383412964,319223.0,6.8654344903,231432.0,5.3263000911,267880.0,5.7612157998,194046.0,4.465878649,222625.0,4.7879299217,162562.0,3.7412889981,183177.0,3.9395334723,134240.0,3.0894713101,147801.0,3.1787123205,107417.0,2.4721524115,122305.0,2.6303774018,81379.0,1.8728999236,93056.0,2.0013278239,56627.0,1.303244129,67283.0,1.4470355482,2.6640936415,2.9250407498,251763.0,2.7989860356,34653.0,0.7975227153,40746.0,0.8763121509,16990.0,0.3910169663,19681.0,0.4232734364,7487.0,0.1723098309,8296.0,0.1784196143,178556.0,177635.0,162320.0,165324.0,149031.0,154536.0,138378.0,145153.0,130047.0,137059.0,123728.0,130134.0,119108.0,124264.0,115875.0,119328.0,113718.0,115212.0,112325.0,111795.0,111850.0,109013.0,112447.0,106800.0,111473.0,104781.0,107682.0,102732.0,102162.0,100693.0,97200.0,98951.0,92279.0,97482.0,88099.0,95699.0,85217.0,93345.0,83183.0,90640.0,80987.0,88065.0,78832.0,85538.0,76635.0,83075.0,74256.0,80738.0,71789.0,78505.0,69468.0,76259.0,NULL,81.7177255731,5.086253763,76.6314718101,1.8136768792,8994793.0,4345080.0,48.306614727,4649713.0,51.693385273,NULL,NULL,NULL,NULL,8255331.0,1.5112289351,91.779,5.2538074226,739462.0,8.221,NULL,'AFG','Afghanistan','South Asia','1960-01-01 00:00:00.000000');
INSERT INTO wb_health_population VALUES(NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,105341.0,NULL,157255.0,NULL,NULL,NULL,NULL,NULL,NULL,356.5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,145.321,558.3968,601.95,53.726,32.773,NULL,NULL,240.5,NULL,32.433,32.0959756098,31.775,NULL,NULL,7.671,20.47498,17.81978,NULL,NULL,NULL,792360.0,17.8639346062,806891.0,17.0611128308,43.6640364655,41.357431869,3892696.0,42.4737518883,594810.0,13.4101253763,618851.0,13.0851462422,549561.0,12.3899764831,530223.0,11.211172796,458676.0,10.3409536946,479934.0,10.1478491213,53.6575006048,55.7122063274,5014856.0,54.7177969971,387812.0,8.743308859,421542.0,8.9131935106,330878.0,7.4597190098,364754.0,7.7124532923,279733.0,6.3066434691,321953.0,6.8074578341,234987.0,5.2978348242,271973.0,5.7506677357,196832.0,4.4376217583,225358.0,4.7650280711,164570.0,3.7102677043,185178.0,3.9154517175,135748.0,3.0604692248,148725.0,3.1446800197,108527.0,2.4467656507,122108.0,2.58188326,82231.0,1.85391641,93337.0,1.973541765,57370.0,1.2934195673,67311.0,1.4232412628,2.6784629297,2.9303618037,257393.0,2.8084511145,35343.0,0.7968158928,41505.0,0.8775924977,17738.0,0.3999072039,20491.0,0.4332670249,8353.0,0.1883202657,9282.0,0.1962610183,182674.0,181588.0,171491.0,171802.0,157159.0,160585.0,145319.0,150745.0,135717.0,142171.0,128098.0,134751.0,122173.0,128386.0,117648.0,122980.0,114460.0,118355.0,112431.0,114379.0,111194.0,110973.0,110715.0,108144.0,111188.0,105815.0,110123.0,103692.0,106341.0,101599.0,100884.0,99551.0,95945.0,97762.0,91026.0,96220.0,86848.0,94385.0,83973.0,92016.0,81947.0,89322.0,79760.0,86753.0,77613.0,84232.0,75428.0,81779.0,73064.0,79456.0,70616.0,77239.0,NULL,82.7558956827,5.1326099892,77.6232856935,1.874002583,9164945.0,4435529.0,48.3966788671,4729416.0,51.6033211329,NULL,NULL,NULL,NULL,8385191.0,1.5608000308,91.492,5.3055590565,779754.0,8.508,NULL,'AFG','Afghanistan','South Asia','1961-01-01 00:00:00.000000');
INSERT INTO birth_names VALUES('1965-01-01 00:00:00.000000','boy','Aaron',369,'CA',369,0);
INSERT INTO birth_names VALUES('1965-01-01 00:00:00.000000','girl','Amy',494,'CA',0,494);
INSERT INTO random_time_series VALUES('2017-07-19 13:23:33.000000');
INSERT INTO random_time_series VALUES('2016-11-23 00:48:29.000000');
INSERT INTO long_lat VALUES(-122.3912672,37.7690928,'1550','04th Street',NULL,NULL,NULL,NULL,94158,NULL,'2016-12-16',4.0,2.86484850892037);
INSERT INTO long_lat VALUES(-122.3908502,37.7694259,'1505','04th Street',NULL,NULL,NULL,NULL,94158,NULL,'2016-12-16',4.0,2.05121870620017);
INSERT INTO multiformat_time_series VALUES('2017-07-19','2017-07-19 13:23:33.000000',1500470613000,1500470613,'2017-07-19 06:23:33.000000','2017-07-19^06:23:33','20170719-062333','2017/07/1906:23:33.000000');
INSERT INTO multiformat_time_series VALUES('2016-11-23','2016-11-23 00:48:29.000000',1479862109000,1479862109,'2016-11-22 16:48:29.000000','2016-11-22^16:48:29','20161122-164829','2016/11/2216:48:29.000000');
