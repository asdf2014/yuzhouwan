package com.yuzhouwan.hacker.algorithms.leetcode.str;

/**
 * Copyright @ 2024 yuzhouwan.com
 * All right reserved.
 * Function: Ransom Note Solution
 *
 * @author Benedict Jin
 * @since 2016/9/19
 */
class RansomNoteSolution {

    /**
     * https://leetcode.com/problems/ransom-note
     * <p>
     * 383. Ransom Note
     * <p>
     * Given  an  arbitrary  ransom  note  string  and  another
     * string  containing  letters from  all  the  magazines,
     * write  a  function  that  will  return  true  if  the  ransom
     * note  can  be  constructed  from  the  magazines;
     * otherwise,  it  will  return  false.
     * <p>
     * Each  letter  in  the  magazine  string  can  only  be  used  once  in  your  ransom  note.
     */
    static boolean canConstruct(String ransomNote, String magazine) {

        int[] letters = new int[26];
        int a = 'a';
        for (int i = 0; i < magazine.length(); i++) {
            letters[magazine.charAt(i) - a]++;
        }
        for (int i = 0; i < ransomNote.length(); i++) {
            if (--letters[ransomNote.charAt(i) - a] < 0) {
                return false;
            }
        }
        return true;
    }
}
