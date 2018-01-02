package com.yuzhouwan.hacker.algorithms.reverse.index;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: Reverse Index Simple
 *
 * @author Benedict Jin
 * @since 2016/8/19
 */
class ReverseIndexSimple {

    /**
     * 两个倒排记录表的合并算法.
     *
     * @param p1 第一条倒排索引
     * @param p2 第二条倒排索引
     * @return
     */
    DocNodeList intersect(DocNodeList p1, DocNodeList p2) {
        DocNodeList answer = new DocNodeList(0);
        DocNodeList temp;
        while (p1.hasNext() && p2.hasNext()) {
            if (p1.getDocID() == p2.getDocID()) {
                temp = p1.next();
                answer.add(p1);
                p1 = temp;
                p2 = p2.next();
            } else if (p1.getDocID() < p2.getDocID()) {
                p1 = p1.next();
            } else {
                p2 = p2.next();
            }
        }
        return answer;
    }

}
