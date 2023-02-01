package com.yuzhouwan.hacker.algorithms.reverse.index;

/**
 * Copyright @ 2023 yuzhouwan.com
 * All right reserved.
 * Function: Doc Node List
 *
 * @author Benedict Jin
 * @since 2016/8/19
 */
class DocNodeList {

    //指向下一个节点
    private DocNodeList next;
    //当前node的文档id值
    private int docID;

    public DocNodeList() {
        super();
    }

    public DocNodeList(int docID) {
        super();
        this.docID = docID;
    }

    DocNodeList next() {
        return next;
    }

    public DocNodeList getNext() {
        return next;
    }

    void setNext(DocNodeList next) {
        this.next = next;
    }

    public boolean hasNext() {
        return next != null;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    //添加操作，设置插入到当前节点的下一个位置
    public void add(DocNodeList node) {
        DocNodeList temp = this.next();
        this.setNext(node);
        //重置插入node的next值，这要求在执行add()操作时要先缓存被插入节点的next引用
        node.setNext(temp);
    }
}
