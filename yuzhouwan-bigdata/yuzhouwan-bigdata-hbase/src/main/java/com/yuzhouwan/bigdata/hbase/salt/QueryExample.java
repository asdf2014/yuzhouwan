package com.yuzhouwan.bigdata.hbase.salt;

import com.yuzhouwan.bigdata.hbase.util.salt.DataProtos;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.hadoop.hbase.ipc.ServerRpcController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QueryExample {

    private static final Logger _log = LoggerFactory.getLogger(QueryExample.class);
    private static Configuration conf;

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.175.55.138,10.175.55.139,10.175.55.140");
    }

    public static List<DataProtos.DataQueryResponse.Row> queryByStartRowAndStopRow(String tableName, String startRow, String stopRow, boolean isIncludeEnd, boolean isSalting) {

        final DataProtos.DataQueryRequest.Builder requestBuilder = DataProtos.DataQueryRequest.newBuilder();
        requestBuilder.setTableName(tableName);
        requestBuilder.setStartRow(startRow);
        requestBuilder.setEndRow(stopRow);
        requestBuilder.setIncluedEnd(isIncludeEnd);
        requestBuilder.setIsSalting(isSalting);
        try {
            HTable table = new HTable(HBaseConfiguration.create(conf), tableName);
            Map<byte[], List<DataProtos.DataQueryResponse.Row>> result = table.coprocessorService(DataProtos.QueryDataService.class, null, null, new Batch.Call<DataProtos.QueryDataService, List<DataProtos.DataQueryResponse.Row>>() {
                public List<DataProtos.DataQueryResponse.Row> call(DataProtos.QueryDataService counter) throws IOException {
                    ServerRpcController controller = new ServerRpcController();
                    BlockingRpcCallback<DataProtos.DataQueryResponse> rpcCallback = new BlockingRpcCallback<>();
                    counter.queryByStartRowAndEndRow(controller, requestBuilder.build(), rpcCallback);
                    DataProtos.DataQueryResponse response = rpcCallback.get();
                    if (controller.failedOnException()) {
                        throw controller.getFailedOn();
                    }
                    return response.getRowListList();
                }
            });
            List<DataProtos.DataQueryResponse.Row> results = new LinkedList<>();
            result.entrySet()
                    .stream()
                    .filter(entry -> null != entry.getValue())
                    .forEach(entry -> results.addAll(entry.getValue()));
            return results;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        List<DataProtos.DataQueryResponse.Row> rows = queryByStartRowAndStopRow("test", "00", "01", true, true);
        if (rows != null) {
            for (DataProtos.DataQueryResponse.Row row : rows) {
                _log.debug("Row: {}", row.getRowKey().toStringUtf8());
            }
        }
    }
}
