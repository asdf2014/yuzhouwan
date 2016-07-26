package com.yuzhouwan.log.hadoop.reverse.index;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function: PatentMapper Tester
 *
 * @author Benedict Jin
 * @since 2016/7/26
 */
public class PatentMapperTest {

    //分割出一行中 多个 patent的信息
    private static final String PATENT_SPLIT_TOKEN = "'\\),\\('";
    //用于分割每个 patent的属性
    private static final String FIELD_SPLIT_TOKEN = "','";

    @Test
    public void test() {
        String aim = "INSERT INTO `patent` VALUES ('一种超细铜丝拉拔用润滑剂','发明专利','CN201510328622.1','2015年6月15日','2015年9月9日','CN104893809A','C10M173/00,C,C10,C10M,C10M173','C10M173/00,C10M133/16,C10M133/00,C10N30/04,C10N30/00,C10N30/06,C10N30/00,C10N30/12,C10N30/00,C10N30/18,C10N30/00,C10N40/24,C10N40/00,C,C10,C10M,C10N,C10M173,C10M133,C10N30,C10N40,C10M173/00,C10M133/16,C10M133/00,C10N30/04,C10N30/00,C10N30/06,C10N30/00,C10N30/12,C10N30/00,C10N30/18,C10N30/00,C10N40/24,C10N40/00','天津理工大学','马叙,王悦阳,丁燕红','300384 天津市西青区宾水西道391号','天津佳盟知识产权代理有限公司 12002','刘书元','天津;12','一种超细铜丝拉拔用润滑剂，其特征在于：该润滑剂由润滑基础油、乳化剂、表面活性剂、防锈剂、极压抗磨剂、稠化剂、碱保持剂、杀菌剂、消泡剂、水组成，所述的润滑剂成分质量份数为润滑基础油5‑25、乳化剂10‑30、表面活性剂1‑5、防锈剂1‑10、极压抗磨剂1‑5、稠化剂1‑5、碱保持剂5‑25、杀菌剂1‑5、消泡剂0.1、水30‑50。','公开，实质审查的生效，实质审查的生效','本发明公开一种超细铜丝拉拔用润滑剂，该润滑剂包含的成分及质量份为润滑基础油5-25、乳化剂10-30、表面活性剂1-5、防锈剂1-10、极压抗磨剂1-5、稠化剂1-5、碱保持剂5-25、杀菌剂1-5、消泡剂0.1、水30-50。该润滑剂具有良好的抗氧化、冷却、清洗、润滑、防锈、和抗硬水及铜皂分散性能，尤其适用于超细铜丝拉拔的润滑剂中。'),('一种机床轴承用润滑油及其制备方法','发明专利','CN201510324329.8','2015年6月15日','2015年9月9日','CN104893805A','C10M169/04,C10M169/00,C,C10,C10M,C10M169','C10M169/04,C10M169/00,C10N40/02,C10N40/00,C10N30/06,C10N30/00,C10N20/02,C10N20/00,C,C10,C10M,C10N,C10M169,C10N40,C10N30,C10N20,C10M169/04,C10M169/00,C10N40/02,C10N40/00,C10N30/06,C10N30/00,C10N20/02,C10N20/00','赵兰','不公告发明人','325200 浙江省温州市瑞安市飞云街道飞云西路一巷20号','','','浙江;33','一种机床轴承用润滑油，其特征在于：由以下重量份数的各组分组成：复合基础油30~50份、石蜡10~20份、改性石墨烯2~10份、聚四氟乙烯颗粒5~10份、三甲苯磷酸酯2~5份、环烷酸锌5~15份、聚异丁烯1~3份和单乙醇胺8~12份。','公开，实质审查的生效，实质审查的生效','本发明公开了一种机床轴承用润滑油及其制备方法，属于润滑油技术领域，该润滑油由以下重量份数的各组分组成：复合基础油30~50份、石蜡10~20份、改性石墨烯2~10份、聚四氟乙烯颗粒5~10份、三甲苯磷酸酯2~5份、环烷酸锌5~15份、聚异丁烯1~3份和单乙醇胺8~12份。本发明的机床轴承用润滑油，组分简单，成本低，有较好的粘度指数和高抗磨性能；使用寿命长。该润滑油的制备方法，步骤简单环保，易于实施。'),('用于特大型破碎机的高强度高抗磨铸钢件及其生产工艺','发明专利','CN201510328425.X','2015年6月15日','2015年9月16日','CN104911504A','C22C38/58,C22C38/00,C,C22,C22C,C22C38','C22C38/58,C22C38/00,C22C38/38,C22C38/00,C22C33/06,C22C33/00,C21C7/06,C21C7/00,B22C9/02,B22C9/00,B22C9/12,B22C9/00,C,B,C22,C21,B22,C22C,C21C,B22C,C22C38,C22C33,C21C7,B22C9,C22C38/58,C22C38/00,C22C38/38,C22C38/00,C22C33/06,C22C33/00,C21C7/06,C21C7/00,B22C9/02,B22C9/00,B22C9/12,B22C9/00','三明市毅君机械铸造有限公司','郑明华,刘渊毅,刘薇,黄志达,蔡建','365002 福建省三明市三元区溪口205国道割山边','泉州市博一专利事务所 35213','方传榜','福建;35','用于特大型破碎机的高强度高抗磨铸钢件，其特征在于，该铸钢件化学组成的重量百分比为：C 1.31～1.40，Si 0.50～0.90，Mn 17.5～19.0，P ≤0.040，S ≤0.020，Cr 1.00～1.40，Ni ≤0.60，Mo ≤0.50，B 0.005～0.010，Al 0.08～0.12，Ti 0.10～0.18，La 0.05～0.09 ，Ce 0.05～0.10，V 0.05～0.10，其余为Fe和不可避免的微量杂质。','公开，实质审查的生效，实质审查的生效','本发明公开了一种用于特大型破碎机的高强度高抗磨铸钢件，其特征在于，该铸钢件化学组成的重量百分比为：C 1.31～1.40，Si 0.50～0.90，Mn 17.5～19.0，P≤0.040，S≤0.020，Cr 1.00～1.40，Ni≤0.60，Mo≤0.50，B 0.005～0.010，Al 0.08～0.12，Ti 0.10～0.18，La 0.05～0.09，Ce 0.05～0.10，V 0.05～0.10，其余为Fe和不可避免的微量杂质。还公开了一种用于生产上述铸钢件的工艺，使所述铸钢件具有较好的韧性和耐磨性，使用寿命长，适用于特大型破碎机中。'),('一种高肥力吊兰颗粒肥','发明专利','CN201510335124.X','2015年6月15日','2015年9月9日','CN104892077A','C05G1/02,C05G1/00,C,C05,C05G,C05G1','C05G1/02,C05G1/00,C,C05,C05G,C05G1,C05G1/02,C05G1/00','刘自忠','刘自忠','730060 甘肃省兰州市西固区西固中路458号12室','','','甘肃;62','一种高肥力吊兰颗粒肥，其特征在于：由以下重量份的原料组成：草炭4～10份、豌豆荚5～10份、花生壳10～15份、贝壳粉10～15份、钼酸氨10～15份、膨化鸡粪颗粒50～80份、磷酸铵10～15份、磷钾复合肥10～20份、秸秆粉30～40份、过磷酸钙1～5份、氯化钾1～3份、硝酸铵2～5份。','公开，公开','本发明公开了一种高肥力吊兰颗粒肥，由以下重量份的原料制成：草炭4～10份、豌豆荚5～10份、花生壳10～15份、贝壳粉10～15份、钼酸氨10～15份、膨化鸡粪颗粒50～80份、磷酸铵10～15份、磷钾复合肥10～20份、秸秆粉30～40份、过磷酸钙1～5份、氯化钾1～3份、硝酸铵2～5份。该肥料能有效锁住盆栽土水分，改良盆栽土土质、保证肥效、能阻止害虫对吊兰的侵袭，并从氮磷钾各方面提高肥效，完全满足吊兰的养分需求，且配方中无昂贵原料，其成本低，经济效益好。";
        System.out.println(aim = aim.substring(30));
        String s = aim.split(PATENT_SPLIT_TOKEN)[0];
        System.out.println(s);
        for (String s1 : s.split(FIELD_SPLIT_TOKEN)) {
            System.out.println(s1);
        }
    }

    @Test
    public void testLastLine() {
        String aim = "一种正火高强韧性150mm特厚板及其生产方法','发明专利','CN201510344275.1','2015年6月14日','2015年10月7日','CN104962814A','C22C38/16,C22C38/00,C,C22,C22C,C22C38','C22C38/16,C22C38/00,C22C33/04,C22C33/00,C21D8/02,C21D8/00,C,C22,C21,C22C,C21D,C22C38,C22C33,C21D8,C22C38/16,C22C38/00,C22C33/04,C22C33/00,C21D8/02,C21D8/00','秦皇岛首秦金属材料有限公司','宋欣,冯路路,谌铁强,黄少帅,杨春卫,宋增强,白松莲,王根矶,白学军,刘印良,闫智平,刘海龙,周德光,赵久梁','066326 河北省秦皇岛市抚宁县杜庄乡','北京华谊知识产权代理有限公司 11207','王普玉','河北;13','一种正火高强韧性150mm特厚板，其特征在于，化学成分按重量百分比为，C：0.14％～0.19％；Si：0.20％～0.50％；Mn：1.40％～1.70％；Ni：0.30％～0.40％；Cu：0.10％～0.30％；Nb：0.035％～0.050％；V：0.030％～0.050％；Ti：0.010％～0.020％；Alt：0.015％～0.050％；P：≤0.015％；S：≤0.0050％；碳当量Ceq＝C+Mn/6+(Cr+Mo+V)/5+(Ni/+Cu)/15：0.41％～0.53％；其余为Fe及不可避免杂质。','公开，实质审查的生效，实质审查的生效','一种正火高强韧性150mm特厚板及其生产方法，属于特厚板生产技术领域。该方法主要为控制钢水冶炼、板坯浇铸、钢板轧制、钢板正火处理及冷却等步骤中的工艺参数。优点在于：通过设计合理的成分体系及优化的“控轧+正火+弱水冷”工艺，钢板综合力学性能优良，钢板厚度方向不同位置力学性能差异小且稳定，-40℃及-60℃冲击韧性良好，完全满足Q460级别150mm特厚钢板对强度、冲击韧性的要求，在工程结构用钢领域，具有较好的应用前景。');";
        System.out.println(aim.substring(0, aim.length() - 3));
    }

    @Test
    public void chineseToken() {
        KeyWordComputer kwc = new KeyWordComputer(5);
        String title = "维基解密否认斯诺登接受委内瑞拉庇护";
        String content = "有俄罗斯国会议员，9号在社交网站推特表示，美国中情局前雇员斯诺登，已经接受委内瑞拉的庇护，不过推文在发布几分钟后随即删除。俄罗斯当局拒绝发表评论，而一直协助斯诺登的维基解密否认他将投靠委内瑞拉。　　俄罗斯国会国际事务委员会主席普什科夫，在个人推特率先披露斯诺登已接受委内瑞拉的庇护建议，令外界以为斯诺登的动向终于有新进展。　　不过推文在几分钟内旋即被删除，普什科夫澄清他是看到俄罗斯国营电视台的新闻才这样说，而电视台已经作出否认，称普什科夫是误解了新闻内容。　　委内瑞拉驻莫斯科大使馆、俄罗斯总统府发言人、以及外交部都拒绝发表评论。而维基解密就否认斯诺登已正式接受委内瑞拉的庇护，说会在适当时间公布有关决定。　　斯诺登相信目前还在莫斯科谢列梅捷沃机场，已滞留两个多星期。他早前向约20个国家提交庇护申请，委内瑞拉、尼加拉瓜和玻利维亚，先后表示答应，不过斯诺登还没作出决定。　　而另一场外交风波，玻利维亚总统莫拉莱斯的专机上星期被欧洲多国以怀疑斯诺登在机上为由拒绝过境事件，涉事国家之一的西班牙突然转口风，外长马加略]号表示愿意就任何误解致歉，但强调当时当局没有关闭领空或不许专机降落。";
        Collection<Keyword> result = kwc.computeArticleTfidf(title, content);
        //System.out.println(result.iterator().next().getName());

        //[斯诺登/211.83897497289786, 维基/163.46869316143392, 委内瑞拉/101.31414008144232, 庇护/46.05172894231714, 俄罗斯/45.70875018647603]
        for (Keyword keyword : result) {
            System.out.println(keyword.getName());
        }
    }

    @Test
    public void testStrBuffer() {
        LinkedList<String> value = new LinkedList<>();
        value.add("a");
        value.add("b");
        Iterator iterator = value.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString() + (iterator.hasNext() ? " ," : ""));
        }
        System.out.println(sb.toString());
    }
}
