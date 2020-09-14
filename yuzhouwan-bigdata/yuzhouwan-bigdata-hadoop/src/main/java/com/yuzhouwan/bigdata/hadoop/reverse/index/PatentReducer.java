package com.yuzhouwan.bigdata.hadoop.reverse.index;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：处理 Mapper端传输过来的 Reducer组件
 *
 * @author Benedict Jin
 * @since 2016/3/31
 */
public class PatentReducer extends Reducer<Text, Text, Text, Text> {

    private static final Logger LOG = LoggerFactory.getLogger(PatentReducer.class);

    /**
     * Reducer 的核心方法，用来处理 海量的 Combiner端输入数据.
     *
     * @param key
     * @param value
     * @param context
     */
    @Override
    protected void reduce(Text key, Iterable<Text> value,
                          Reducer<Text, Text, Text, Text>.Context context) {

        try {
            //手机 - 类型，公司
            //手机 - 类型，公司，1（每有一个手机专利，累计为1）
            //手机 - 类型，公司，专利总数

            //利用 StringBuffer 将 key相同的 [类型，公司，专利总数] 拼装到一起
            StringBuffer strBuffer = new StringBuffer();
            //得到 Value的迭代器
            Iterator iterator = value.iterator();
            //如果迭代器中还有数据，则一直将数据取出处理
            while (iterator.hasNext()) {
                //将相同的专利 keyword 的 信息[类型，公司，专利总数] 拼装起来
                appendPatent(strBuffer, iterator);
            }
            try {
                //写入到 Hadoop上下文中
                context.write(key, new Text(strBuffer.toString()));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
    }

    /**
     * 将相同的专利 keyword 的 信息[类型，公司，专利总数] 拼装起来.
     *
     * @param strBuffer
     * @param iterator
     */
    private void appendPatent(StringBuffer strBuffer, Iterator iterator) {
        strBuffer.append(iterator.next().toString()).append(iterator.hasNext() ? " ;" : "");
    }
}
