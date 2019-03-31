package com.yuzhouwan.hacker.algorithms.tree.darts;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Double Array Trie Tree
 *
 * @author Benedict Jin
 * @since 2016/8/23
 */
public class DoubleArrayTrieTree {

    private static final int BUF_SIZE = 16384;
    private static final int UNIT_SIZE = 8; // size of int + int
    private int[] check;
    private int[] base;
    private boolean[] used;
    private int size;
    private int allocSize;
    private List<String> key;
    private int keySize;
    private int[] length;
    private int[] value;
    private int progress;
    private int nextCheckPos;
    private int error_;

    public DoubleArrayTrieTree() {
        check = null;
        base = null;
        used = null;
        size = 0;
        allocSize = 0;
        error_ = 0;
    }

    // inline _resize expanded
    private int resize(int newSize) {
        int[] base2 = new int[newSize];
        int[] check2 = new int[newSize];
        boolean[] used2 = new boolean[newSize];
        if (allocSize > 0) {
            System.arraycopy(base, 0, base2, 0, allocSize);
            System.arraycopy(check, 0, check2, 0, allocSize);
            System.arraycopy(used2, 0, used2, 0, allocSize);
        }

        base = base2;
        check = check2;
        used = used2;

        return allocSize = newSize;
    }

    private int fetch(Node parent, List<Node> siblings) {
        if (error_ < 0) return 0;
        int prev = 0;

        for (int i = parent.left; i < parent.right; i++) {
            if ((length != null ? length[i] : key.get(i).length()) < parent.depth)
                continue;

            String tmp = key.get(i);

            int cur = 0;
            if ((length != null ? length[i] : tmp.length()) != parent.depth)
                cur = (int) tmp.charAt(parent.depth) + 1;

            if (prev > cur) {
                error_ = -3;
                return 0;
            }

            if (cur != prev || siblings.size() == 0) {
                Node tmp_node = new Node();
                tmp_node.depth = parent.depth + 1;
                tmp_node.code = cur;
                tmp_node.left = i;
                if (siblings.size() != 0)
                    siblings.get(siblings.size() - 1).right = i;

                siblings.add(tmp_node);
            }
            prev = cur;
        }

        if (siblings.size() != 0)
            siblings.get(siblings.size() - 1).right = parent.right;

        return siblings.size();
    }

    private int insert(List<Node> siblings) {
        if (error_ < 0) return 0;

        int begin;
        int pos = ((siblings.get(0).code + 1 > nextCheckPos) ? siblings.get(0).code + 1 : nextCheckPos) - 1;
        int nonzero_num = 0;
        int first = 0;

        if (allocSize <= pos) resize(pos + 1);

        outer:
        while (true) {
            pos++;

            if (allocSize <= pos) resize(pos + 1);
            if (check[pos] != 0) {
                nonzero_num++;
                continue;
            } else if (first == 0) {
                nextCheckPos = pos;
                first = 1;
            }

            begin = pos - siblings.get(0).code;
            if (allocSize <= (begin + siblings.get(siblings.size() - 1).code)) {
                // progress can be zero
                double l = (1.05 > 1.0 * keySize / (progress + 1)) ? 1.05 : 1.0 * keySize / (progress + 1);
                resize((int) (allocSize * l));
            }

            if (used[begin]) continue;

            for (int i = 1; i < siblings.size(); i++)
                if (check[begin + siblings.get(i).code] != 0)
                    continue outer;

            break;
        }

        // -- Simple heuristics --
        // if the percentage of non-empty contents in check between the
        // index
        // 'next_check_pos' and 'check' is greater than some constant value
        // (e.g. 0.9),
        // new 'next_check_pos' index is written by 'check'.
        if (1.0 * nonzero_num / (pos - nextCheckPos + 1) >= 0.95) nextCheckPos = pos;

        used[begin] = true;
        size = (size > begin + siblings.get(siblings.size() - 1).code + 1) ? size
                : begin + siblings.get(siblings.size() - 1).code + 1;

        for (Node sibling : siblings) check[begin + sibling.code] = begin;

        for (Node sibling : siblings) {
            List<Node> new_siblings = new ArrayList<>();

            if (fetch(sibling, new_siblings) == 0) {
                base[begin + sibling.code] = (value != null) ? (-value[sibling.left] - 1) : (-sibling.left - 1);

                if (value != null && (-value[sibling.left] - 1) >= 0) {
                    error_ = -2;
                    return 0;
                }

                progress++;
            } else {
                int h = insert(new_siblings);
                base[begin + sibling.code] = h;
            }
        }
        return begin;
    }

    void clear() {
        check = null;
        base = null;
        used = null;
        allocSize = 0;
        size = 0;
    }

    // no deconstructor

    // set_result omitted
    // the search methods returns (the list of) the value(s) instead
    // of (the list of) the pair(s) of value(s) and length(s)

    // set_array omitted
    // array omitted

    public int getUnitSize() {
        return UNIT_SIZE;
    }

    public int getSize() {
        return size;
    }

    public int getTotalSize() {
        return size * UNIT_SIZE;
    }

    public int getNonzeroSize() {
        int result = 0;
        for (int i = 0; i < size; i++) if (check[i] != 0) result++;
        return result;
    }

    public int build(List<String> key) {
        return build(key, null, null, key.size());
    }

    public int build(List<String> key, int[] length, int[] value, int keySize) {
        if (keySize > key.size()) return 0;

        this.key = key;
        this.length = length;
        this.keySize = keySize;
        this.value = value;
        progress = 0;

        resize(65536 * 32);

        base[0] = 1;
        nextCheckPos = 0;

        Node root_node = new Node();
        root_node.left = 0;
        root_node.right = this.keySize;
        root_node.depth = 0;

        List<Node> siblings = new ArrayList<>();
        fetch(root_node, siblings);
        insert(siblings);

        used = null;
        this.key = null;

        return error_;
    }

    public void open(String fileName) throws IOException {
        File file = new File(fileName);
        size = (int) file.length() / UNIT_SIZE;
        check = new int[size];
        base = new int[size];

        DataInputStream is = null;
        try {
            is = new DataInputStream(new BufferedInputStream(new FileInputStream(file), BUF_SIZE));
            for (int i = 0; i < size; i++) {
                base[i] = is.readInt();
                check[i] = is.readInt();
            }
        } finally {
            if (is != null) is.close();
        }
    }

    public void save(String fileName) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(fileName)));
            for (int i = 0; i < size; i++) {
                out.writeInt(base[i]);
                out.writeInt(check[i]);
            }
            out.close();
        } finally {
            if (out != null) out.close();
        }
    }

    public int exactMatchSearch(String key) {
        return exactMatchSearch(key, 0, 0, 0);
    }

    public int exactMatchSearch(String key, int pos, int len, int nodePos) {
        if (len <= 0) len = key.length();
        if (nodePos <= 0) nodePos = 0;

        int result = -1;
        char[] keyChars = key.toCharArray();

        int b = base[nodePos];
        int p;

        for (int i = pos; i < len; i++) {
            p = b + (int) (keyChars[i]) + 1;
            if (b == check[p]) b = base[p];
            else return result;
        }

        p = b;
        int n = base[p];
        if (b == check[p] && n < 0) result = -n - 1;
        return result;
    }

    public List<Integer> commonPrefixSearch(String key) {
        return commonPrefixSearch(key, 0, 0, 0);
    }

    public List<Integer> commonPrefixSearch(String key, int pos, int len, int nodePos) {
        if (len <= 0) len = key.length();
        if (nodePos <= 0) nodePos = 0;

        List<Integer> result = new ArrayList<Integer>();

        char[] keyChars = key.toCharArray();

        int b = base[nodePos];
        int n;
        int p;

        for (int i = pos; i < len; i++) {
            p = b;
            n = base[p];

            if (b == check[p] && n < 0) {
                result.add(-n - 1);
            }

            p = b + (int) (keyChars[i]) + 1;
            if (b == check[p]) b = base[p];
            else return result;
        }

        p = b;
        n = base[p];

        if (b == check[p] && n < 0) result.add(-n - 1);
        return result;
    }

    // debug
    public void dump() {
        for (int i = 0; i < size; i++) {
            System.err.println("i: " + i + " [" + base[i] + ", " + check[i] + "]");
        }
    }

    /**
     * Node.
     */
    private static class Node {
        int code;
        int depth;
        int left;
        int right;
    }
}
