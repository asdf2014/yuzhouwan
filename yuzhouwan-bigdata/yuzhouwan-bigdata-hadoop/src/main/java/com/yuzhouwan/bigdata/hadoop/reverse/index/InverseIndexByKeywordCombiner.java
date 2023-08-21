package com.yuzhouwan.bigdata.hadoop.reverse.index;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function：倒排索引的 Combiner组件
 *
 * @author Benedict Jin
 * @since 2016/3/31
 */
public class InverseIndexByKeywordCombiner extends Reducer<Text, Text, Text, Text> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InverseIndexByKeywordCombiner.class);

    //输出的 Text的 Key
    private Text writeKey = new Text();
    //输出的 Text的 Value
    private Text writeValue = new Text();

    /**
     * Combiner 的核心方法，用来处理 海量的 Mapper端输入数据.
     *
     * @param key
     * @param values
     * @param context
     */
    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) {

        //**整体思路**
        //[Mapper]:     手机 - 类型，公司
        //[Combiner]:   手机 - 类型，公司，1（每有一个手机专利，累计为1）
        //[Reducer]:    手机 - 类型，公司，专利总数

        //拿到关键字
        String keyword = key.toString();

        //计数器，用来累计 专利的总数
        int counter = 0;
        //用来获取 Key中的 [类型，公司]
        String prefix = "";
        //标识是否获取到 [类型，公司]
        boolean isGot = false;
        for (Text val : values) {
            //只需要获取一次 [类型，公司]
            if (!isGot) {
                //分割，抽取 [类型，公司]
                int index = val.toString().lastIndexOf(',');
                prefix = val.toString().substring(0, index + 1);
                isGot = true;
            }
            //得到最后一个数值，并进行累计
            counter += Integer.parseInt(val.toString().split(",")[2]);
        }
        try {
            //将 keyword作为 Mapper的输出 Key
            writeKey.set(keyword);
            if (isGot) {
                //将 [类型，公司，专利总数] 作为输出 Value
                writeValue.set(prefix + counter);
            }
            //写入到 Hadoop上下文中
            context.write(writeKey, writeValue);
        } catch (IOException | InterruptedException e) {
            LOGGER.error("", e);
        }
    }
}
