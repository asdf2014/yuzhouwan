package com.yuzhouwan.bigdata.hbase.salt;

import com.yuzhouwan.bigdata.hbase.util.salt.DataProtos;
import org.apache.hbase.thirdparty.com.google.protobuf.ByteString;
import org.apache.hbase.thirdparty.com.google.protobuf.Service;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataProtosTest {

    @Test
    public void shadedRequestRoundTrip() throws Exception {
        DataProtos.DataQueryRequest request = DataProtos.DataQueryRequest.newBuilder()
                .setTableName("events")
                .setStartRow("a")
                .setEndRow("z")
                .setIncludedEnd(true)
                .setIsSalting(true)
                .build();
        assertEquals(request, DataProtos.DataQueryRequest.parseFrom(request.toByteArray()));
    }

    @Test
    public void shadedResponseRoundTrip() throws Exception {
        DataProtos.DataQueryResponse.Cell cell = DataProtos.DataQueryResponse.Cell.newBuilder()
                .setRow(ByteString.copyFromUtf8("row"))
                .setFamily(ByteString.copyFromUtf8("cf"))
                .setQualifier(ByteString.copyFromUtf8("column"))
                .setValue(ByteString.copyFromUtf8("value"))
                .build();
        DataProtos.DataQueryResponse response = DataProtos.DataQueryResponse.newBuilder()
                .addRowList(DataProtos.DataQueryResponse.Row.newBuilder()
                        .setRowKey(cell.getRow()).addCellList(cell))
                .build();
        assertEquals(response, DataProtos.DataQueryResponse.parseFrom(response.toByteArray()));
    }

    @Test
    public void endpointExposesShadedService() {
        Service service = new QueryEndpoint().getServices().iterator().next();
        assertEquals("generated.QueryDataService", service.getDescriptorForType().getFullName());
        assertEquals(2, service.getDescriptorForType().getMethods().size());
    }
}
