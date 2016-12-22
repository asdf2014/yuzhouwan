package com.yuzhouwan.bigdata.hbase.salt;

import com.google.protobuf.ByteString;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;
import com.yuzhouwan.bigdata.hbase.util.salt.DataProtos;
import com.yuzhouwan.bigdata.hbase.util.salt.DataProtos.DataQueryResponse;
import com.yuzhouwan.common.util.ExceptionUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.Coprocessor;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.CoprocessorException;
import org.apache.hadoop.hbase.coprocessor.CoprocessorService;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.protobuf.ResponseConverter;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryEndpoint extends DataProtos.QueryDataService implements Coprocessor, CoprocessorService {

    private static final Logger _log = LoggerFactory.getLogger(QueryEndpoint.class);
    private RegionCoprocessorEnvironment env;

    public QueryEndpoint() {
    }

    @Override
    public Service getService() {
        return this;
    }

    @Override
    public void queryByStartRowAndEndRow(RpcController controller, DataProtos.DataQueryRequest request, RpcCallback<DataQueryResponse> done) {

        DataProtos.DataQueryResponse response = null;
        InternalScanner scanner = null;
        try {
            String startRow = request.getStartRow();
            String endRow = request.getEndRow();
            String regionStartKey = Bytes.toString(this.env.getRegion().getRegionInfo().getStartKey());
            String regionEndKey = Bytes.toString(this.env.getRegion().getRegionInfo().getEndKey());

            if (request.getIsSalting()) {                       // 如果加盐过则在key前添加盐值
                String startSalt = null;
                String endSalt = null;
                if (StrUtils.isNotEmpty(regionStartKey)) {
                    startSalt = regionStartKey.split("_")[0];   // 加盐的方式为盐值+"_"，所以取_前面的
                }
                if (StrUtils.isNotEmpty(regionEndKey)) {
                    endSalt = regionStartKey.split("_")[0];     //加盐的方式为盐值+"_"，所以取_前面的
                }
                if (startSalt != null) {
                    if (null != startRow) {
                        startRow = startSalt + "_" + startRow;
                        endRow = endSalt + "_" + endRow;
                    }
                }
            }
            Scan scan = new Scan();
            if (null != startRow) {
                scan.setStartRow(Bytes.toBytes(startRow));
            }
            if (null != endRow) {
                if (request.getIncluedEnd()) {
                    Filter filter = new InclusiveStopFilter(Bytes.toBytes(endRow));
                    scan.setFilter(filter);
                } else {
                    scan.setStopRow(Bytes.toBytes(endRow));
                }
            }
            scanner = this.env.getRegion().getScanner(scan);

            List<Cell> results = new ArrayList<>();
            boolean hasMore;
            DataProtos.DataQueryResponse.Builder responseBuilder = DataProtos.DataQueryResponse.newBuilder();
            do {
                hasMore = scanner.next(results);
                DataProtos.DataQueryResponse.Row.Builder rowBuilder = DataProtos.DataQueryResponse.Row.newBuilder();
                if (results.size() > 0) {
                    rowBuilder.setRowKey(ByteString.copyFrom(results.get(0).getRow()));
                    for (Cell kv : results) {
                        queryBuilder(rowBuilder, ByteString.copyFrom(kv.getFamily()), ByteString.copyFrom(kv.getQualifier()), ByteString.copyFrom(kv.getRow()), ByteString.copyFrom(kv.getValue()));
                    }
                }
                responseBuilder.addRowList(rowBuilder);
                results.clear();
            } while (hasMore);
            response = responseBuilder.build();
        } catch (IOException ignored) {
            ResponseConverter.setControllerException(controller, ignored);
        } finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (IOException e) {
                    _log.error("{}", ExceptionUtils.errorInfo(e));
                }
            }
        }
        done.run(response);
    }


    @Override
    public void queryByRowKey(RpcController controller, DataProtos.DataQueryRequest request, RpcCallback<DataQueryResponse> done) {
        DataProtos.DataQueryResponse response = null;
        try {
            String rowKey = request.getRowKey();
            String regionStartKey = Bytes.toString(this.env.getRegion().getRegionInfo().getStartKey());

            if (request.getIsSalting()) {                       // 如果加盐过则在key前添加盐值
                String startSalt = null;
                if (null != regionStartKey && !regionStartKey.isEmpty()) {
                    startSalt = regionStartKey.split("_")[0];   // 加盐的方式为盐值+"_"，所以取_前面的
                }
                if (null != startSalt) {
                    if (null != rowKey) {
                        rowKey = startSalt + "_" + rowKey;
                    }
                }
            }
            if (StrUtils.isEmpty(rowKey))
                return;
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = this.env.getRegion().get(get);

            DataProtos.DataQueryResponse.Builder responseBuilder = DataProtos.DataQueryResponse.newBuilder();
            DataProtos.DataQueryResponse.Row.Builder rowBuilder = DataProtos.DataQueryResponse.Row.newBuilder();

            if (result != null && !result.isEmpty()) {
                List<KeyValue> list = result.list();
                if (null != list && !list.isEmpty()) {
                    rowBuilder.setRowKey(ByteString.copyFrom(list.get(0).getRow()));
                    for (KeyValue kv : list) {
                        queryBuilder(rowBuilder, ByteString.copyFrom(kv.getFamily()), ByteString.copyFrom(kv.getQualifier()), ByteString.copyFrom(kv.getRow()), ByteString.copyFrom(kv.getValue()));
                    }
                }
            }
            responseBuilder.addRowList(rowBuilder);
            response = responseBuilder.build();
        } catch (IOException ignored) {
            ResponseConverter.setControllerException(controller, ignored);
        }
        done.run(response);
    }

    private void queryBuilder(DataQueryResponse.Row.Builder rowBuilder,
                              ByteString value, ByteString value2, ByteString value3, ByteString value4) {
        DataQueryResponse.Cell.Builder cellBuilder = DataQueryResponse.Cell.newBuilder();
        cellBuilder.setFamily(value);
        cellBuilder.setQualifier(value2);
        cellBuilder.setRow(value3);
        cellBuilder.setValue(value4);
        rowBuilder.addCellList(cellBuilder);
    }

    @Override
    public void start(CoprocessorEnvironment env) throws IOException {
        if (env instanceof RegionCoprocessorEnvironment) {
            this.env = (RegionCoprocessorEnvironment) env;
        } else {
            throw new CoprocessorException("Must be loaded on a table region!");
        }
    }

    @Override
    public void stop(CoprocessorEnvironment env) throws IOException {
        //nothing to do
    }
}