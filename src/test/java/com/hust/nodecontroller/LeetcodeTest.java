package com.hust.nodecontroller;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @program nodecontroller
 * @Description
 * @Author jokerwen666
 * @date 2022-01-27 21:54
 **/

public class LeetcodeTest {

    @Test
    public void test() {
        totalFruit(new int[]{0, 1, 2, 2});
        findAnagrams("cbaebabacd","abc");
    }



    public List<Integer> findAnagrams(String s, String p) {
        int success = 0;
        int[] origin = new int[26];
        int[] tokens = new int[26];
        ArrayList<Integer> ans = new ArrayList<>();

        for (int i = 0; i < p.length(); i++) {
            origin[p.charAt(i)-'a']++;
            tokens[p.charAt(i)-'a']++;
        }

        int start = 0;
        int end = 0;
        while (end < s.length()) {
            int index = s.charAt(end) - 'a';

            if (origin[index] == 0) {
                tokens[s.charAt(start)-'a'] = start != end ? tokens[s.charAt(start)-'a']+1 : 0;
                success = start != end ? success-1 : success;
                end = start != end ? end : end+1;
                start++;
            }

            else if (origin[index] != 0 && tokens[index] == 0) {
                tokens[s.charAt(start)-'a']++;
                success--;
                start++;
            }

            else if (origin[index] != 0 && tokens[index] != 0) {
                tokens[index]--;
                success++;
                if (success == p.length()) {
                    ans.add(start);
                }
                end++;
            }
        }
        return ans;
    }

    public int totalFruit(int[] fruits) {
        int[] tokens = new int[fruits.length+1];
        int type = 0;
        int start = 0;
        int ans = 0;

        for (int end = 0; end < fruits.length; end++) {
            int fruit = fruits[end];
            type = tokens[fruit] == 0 ? type+1 : type;
            tokens[fruit]++;

            while (type > 2) {
                tokens[fruits[start]]--;
                type = tokens[fruits[start]] == 0 ? type-1 : type;
                start++;
            }

            ans = end-start+1 > ans ? end-start+1 : ans;
        }

        return ans;
    }
}
