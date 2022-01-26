package com.hust.nodecontroller;

import com.hust.nodecontroller.utils.HashUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author Zhang Bowen
 * @Description
 * @ClassName HashTest
 * @date 2020.10.20 21:12
 */
public class HashTest {

    @Test
    public void test(){
        HashMap<Integer,Integer> map  = new HashMap<>();

        HashSet<Integer> set = new HashSet<>();
        ArrayList<Integer> list = new ArrayList<>();
        lengthOfLongestSubstring("abcabcbb");

        String[] tokens = new String[] {"4","13","5","/","+"};
        minWindow("ADOBECODEBANC","ABC");

        Stack<Integer> numStack = new Stack<>();
        for (String token : tokens) {
            if (isNumber(token)) {
                numStack.push(Integer.parseInt(token));
            }
            else {
                int opNum1 = numStack.pop();
                int opNum2 = numStack.pop();
                switch (token.charAt(0)) {
                    case '+' :
                        numStack.push(opNum1 + opNum2);

                    case '-' :
                        numStack.push(opNum1 - opNum2);
                    case '*' :
                        numStack.push(opNum1 * opNum2);
                    case '/' :
                        numStack.push(opNum1 / opNum2);
                }
            }
        }


        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        for (int k : set) {
            System.out.println(k);
        }

        String data = "http://222.180.148.30:8666/Query/086.001.000001/01.02.06.20201119.600057";

        String data_ = " http://222.180.148.30:8666/Query/086.001.000001/01.02.06.20201119.600057";

        String dataHash = Integer.toHexString(HashUtil.apHash(data));

        String url = "python \"" + "/root/hust/OID.py" + "\" " + "123456";

        String dataHash_ = Integer.toHexString(HashUtil.apHash(data_));

    }

    public boolean isNumber(String token) {
        return !("+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token));
    }

    public int minSubArrayLen(int target, int[] nums) {
        if (nums.length == 1) {
            return nums[0] >= target ? 1: 0;
        }

        int[] prefix = new int[nums.length+1];

        for (int i = 1; i <= nums.length; i++) {
            if (nums[i-1] >= target) {
                return 1;
            }
            prefix[i] = prefix[i-1] + nums[i-1];
        }

        if (prefix[nums.length] < target) {
            return 0;
        }

        int ans = Integer.MAX_VALUE;

        for (int i = 0; i < nums.length; i++) {
            int t = target + prefix[i];
            int index = binarySearch(prefix, t);
            if (index != -1) {
                ans = Math.min(index-i, ans);
            }
        }

        return ans == Integer.MAX_VALUE ? 0 : ans;
    }

    public String minWindow(String s, String t) {
        String str = "";
        String ans = "";
        int ansLength = Integer.MAX_VALUE;
        int count = t.length();
        HashMap<Character, Integer> map2 = new HashMap<>();
        HashMap<Character, Integer> map1 = new HashMap<>();

        for (int i = 0; i < t.length(); i++) {
            char ch = t.charAt(i);
            // map1用于保存动态字符串str中出现t中字符的个数
            map1.put(ch,0);

            // map2用于保存t中字符出现的个数
            int value = map2.getOrDefault(ch, 0);
            map2.put(ch,value+1);
        }

        for (int end = 0; end < s.length(); end++) {
            char addChar = s.charAt(end);

            if (map2.containsKey(addChar)) {
                int value = map1.getOrDefault(addChar, 0);
                map1.put(addChar, value+1);
                count = value+1 > map2.get(addChar) ? count : count-1;
            }

            str = str + addChar;
            while(count == 0) {
                if (str.length() < ansLength) {
                    ans = str;
                    ansLength = ans.length();
                }

                char delChar = str.charAt(0);
                if (map2.containsKey(delChar)) {
                    int value = map1.get(delChar);
                    map1.put(delChar,value-1);
                    count = value-1 >= map2.get(delChar) ? count : count+1;
                }

                str = str.substring(1);
            }
        }
        return ans;
    }

    public int lengthOfLongestSubstring(String s) {
        int ans = Integer.MIN_VALUE;
        StringBuilder str = new StringBuilder();
        HashSet<Character> set = new HashSet<>();
        for (int end = 0; end < s.length(); end++) {
            char ch = s.charAt(end);
            s.contains("a");
            while (str.toString().indexOf(ch) != -1 && str.length() != 0) {
                str = new StringBuilder(str.substring(1));
            }
            str.append(s.charAt(end));
            ans = Math.max(ans, str.length());
        }
        return ans;
    }

    public int binarySearch(int[] array, int target) {
        int length = array.length;
        int l = 0;
        int r = length-1;
        int m = ((r-l) / 2) + l;
        int pos = -1;

        while (l != r) {
            if (array[m] >= target) {
                pos = m;
                r = m -1;
                m = ((r-l) / 2) + l;
            }
            else if (array[m] < target) {
                l = m+1;
                m = ((r-l) / 2) + l;
            }
        }
        if (array[l] >= target && pos == -1) {
            pos = l;
        }
        else if (array[l] >= target && pos != -1) {
            pos = Math.min(l,pos);
        }
        return pos;
    }
}
