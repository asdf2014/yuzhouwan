package com.yuzhouwan.bigdata.hadoop.reverse.index;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Copyright @ 2016 yuzhouwan.com
 * All right reserved.
 * Function：处理 原始 Patent 数据的 Mapper组件
 *
 * @author Benedict Jin
 * @since 2016/3/31
 */
public class PatentMapper extends Mapper<LongWritable, Text, Text, Text> {

    private static final Logger _log = LoggerFactory.getLogger(PatentMapper.class);

    //分割出一行中 多个 patent的信息
    private static final String PATENT_SPLIT_TOKEN = "'\\),\\('";
    //用于剔除无关的注释数据
    private static final String PATTERN_PREFIX = "INSERT";
    //用于分割每个 patent的属性
    private static final String FIELD_SPLIT_TOKEN = "','";

    //Ansj中文分词组件
    private static KeyWordComputer kwc = new KeyWordComputer(1);

    //输出的 Text的 Key
    private Text writeKey = new Text();
    //输出的 Text的 Value
    private Text writeValue = new Text();

    /**
     * Mapper 的核心方法，用来处理 海量的输入数据
     *
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value,
                       Mapper<LongWritable, Text, Text, Text>.Context context)
            throws IOException, InterruptedException {

        //从 input文件夹中将 海量的数据 按行读入 Mapper组件
        String text = value.toString();
        //判断 BCP文件中，是否是一行 需要 insert 的真实数据（排除注释）
        if (!text.startsWith(PATTERN_PREFIX)) {
            return;
        }

        // 手机相关专利总数、类型[1]、
        // 不同公司[11] 所拥有的专利数量及 类型来反映其侧重点

        //**整体思路**
        //[Mapper]:     手机 - 类型，公司
        //[Combiner]:   手机 - 类型，公司，1（每有一个手机专利，累计为1）
        //[Reducer]:    手机 - 类型，公司，专利总数

        //处理 原始数据，完成 Patents解析工作
        parsePatents(context, text);
    }

    /**
     * 处理 原始数据，完成 Patents解析工作
     *
     * @param context
     * @param text
     */
    private void parsePatents(Context context, String text) {
        //剔除掉 BCP前缀（INSERT INTO `patent` VALUES ('）
        String aim = text.substring(30);
        //将一行中多个 patent分割出来
        String[] patent = aim.split(PATENT_SPLIT_TOKEN);
        //遍历每一个 patent进行处理
        for (int i = 0; i < patent.length; i++) {
            if (i == patent.length - 1) {
                //针对最后一 patent，需要特殊处理一下（剔除字符： ');）
                dealWithLastPatent(patent, i);
            }
            //分割出每个 patent的属性
            String[] fields = patent[i].split(FIELD_SPLIT_TOKEN);
            //判断数据是否是完整的，避免处理异常
            if (fields.length == 17) {
                //拿到 patent的标题，例如（一种超细铜丝拉拔用润滑剂）
                String title = fields[0];
                //拿到 patent的主题内容介绍，例如（本发明公开一种超细铜丝拉拔用润滑剂，该润滑剂包含的成分...）
                String content = fields[16];

                //依据 title 和 patent 的具体描述，提取出 Rank分值 最高的一个关键字
                Collection<Keyword> result = getKeywordsByKWC(title, content);
                //剔除空缺数据
                if (result == null) {
                    continue;
                }
                //迭代提取关键字
                Iterator<Keyword> iterable = result.iterator();
                //判断迭代器里面是否存在数据
                if (!iterable.hasNext()) {
                    continue;
                }
                //拿到 patent的类型
                String type = fields[1];
                //拿到 patent的公司，剔除后面的邮编（天津佳盟知识产权代理有限公司 12002）
                String company = fields[11].split(" ")[0];
                //拿到关键字
                String keywordName = iterable.next().getName();
                //将需要传输到 Hadoop上下文的 Key、Value设置好，并完成写入
                writeKV2MapContext(context, type, company, keywordName);
            }
        }
    }

    /**
     * 将需要传输到 Hadoop上下文的 Key、Value设置好，并完成写入
     *
     * @param context
     * @param type
     * @param company
     * @param keywordName
     */
    private void writeKV2MapContext(Context context, String type, String company, String keywordName) {
        try {
            //将 keyword作为 Mapper的输出 Key
            writeKey.set(keywordName);
            //将 (type, company, 1) 作为 Mapper的输出 Key
            writeValue.set(type.concat(",").concat(company).concat(",1"));
            //写入到 Hadoop上下文中
            context.write(writeKey, writeValue);
        } catch (IOException | InterruptedException e) {
            _log.error("error: {}", e.getMessage());
        }
    }

    /**
     * 针对最后一 patent，需要特殊处理一下（剔除字符： ');）
     *
     * @param patent
     * @param i
     */
    private void dealWithLastPatent(String[] patent, int i) {
        patent[i] = patent[i].substring(0, patent[i].length() - 3);
    }

    /**
     * 依据 title 和 patent 的具体描述，提取出 Rank分值 最高的一个关键字
     * <p>
     * 接口相关文档：http://demo.nlpcn.org/demo#
     *
     * @param title
     * @param content
     * @return
     */
    private Collection<Keyword> getKeywordsByKWC(String title, String content) {
        //标题、内容 判空
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content))
            return null;
        return kwc.computeArticleTfidf(title, content);
    }
}
