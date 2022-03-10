package com.hust.nodecontroller;

import com.hust.nodecontroller.infostruct.IndustryInfo;
import org.junit.jupiter.api.Test;

import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * @program nodecontroller
 * @Description
 * @Author jokerwen666
 * @date 2022-01-27 21:54
 **/

public class LeetcodeTest {

    @Test
    public void test() {
        Integer a = 10;
        Integer b = 10;
        System.out.println(a == b);
        Integer c = new Integer(10);
        Integer d = 10;
        System.out.println(a == c);


        char[][] ch = new char[3][];
        ch[0] = new char[]{'A','B','C','E'};
        ch[1] = new char[]{'S','F','C','S'};
        ch[2] = new char[]{'A','D','E','E'};
        restoreIpAddresses("25525511135");
        combinationSum2(new int[]{10,1,2,7,6,1,5}, 8);
        TreeNode bst = bstFromPreorder(new int[]{8,5,1,7,10,12});
        TreeNode root = buildTree(new int[]{9,15,7,20,3}, new int[]{9,3,15,20,7});
        maxTurbulenceSize(new int[]{9,4,2,10,7,8,8,1,9});
        totalFruit(new int[]{0, 1, 2, 2});
        findAnagrams("cbaebabacd","abc");
    }

    public List<String> restoreIpAddresses(String s) {
        List<Integer> path = new ArrayList<>();
        List<String> ans = new ArrayList<>();
        dfs(s,0,0,path,ans);

        return ans;
    }

    public void dfs(String s, int depth, int begin, List<Integer> path, List<String> ans) {
        if (depth == 4) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                sb.append(path.get(i));
                if (i != path.size()-1) {
                    sb.append(".");
                }
            }
            ans.add(sb.toString());
            return;
        }

        // 让剩下的分段尽可能取到最大，剩余的长度为本段最小长度
        int l = s.length()-begin-3*(3-depth) < 1 ? 1 : s.length()-begin-3*(3-depth);
        // 让剩下的分段尽可能取到最小，剩余的长度为本段最大长度
        int r = s.length()-begin-1*(3-depth) > 3 ? 3 : s.length()-begin-1*(3-depth);

        for(int i = l; i <=r; i++) {
            // 本段长度不为1，此时不能以0开头
            if (i != 1 && s.charAt(begin)=='0') {
                return;
            }

            int partition = getVal(s, begin, i);
            // 本段长度为3的时候，不能比255大
            if (i == 3 && partition > 255) {
                return;
            }

            path.add(partition);
            dfs(s, depth+1, begin+i, path, ans);
            path.remove(path.size()-1);
        }

    }

    public int getVal(String s, int begin, int len) {
        int partitionVal = 0;
        for (int i = 0 ; i < len; i++) {
            partitionVal = partitionVal*10 + (s.charAt(begin+i)-'0');
        }
        return partitionVal;
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        dfs(candidates, 0, 0, target, path, ans);
        return ans;
    }

    public void dfs(int[] candidates, int index, int curVal, int target, List<Integer> path, List<List<Integer>>ans) {
        if (index == candidates.length) {
            return;
        }

        if (curVal == target) {
            List<Integer> pathCopy = new ArrayList<>(path);
            //Collections.sort(pathCopy);
            ans.add(pathCopy);
            return;
        }

        boolean[] isVisited = new boolean[51];

        for (int i = index; i < candidates.length; i++) {
            int num = candidates[i];
            if (num <= target-curVal && !isVisited[num]) {
                path.add(num);
                dfs(candidates, i+1, curVal+num, target, path, ans);
                path.remove(path.size()-1);
            }
            isVisited[num] = true;
        }
    }

    class Solution {
        public List<List<Integer>> permute(int[] nums) {
            List<List<Integer>> ans = new ArrayList<>();
            List<Integer> list = new ArrayList<>();
            boolean[] isVisited = new boolean[nums.length+1];
            dfs(nums, 0, isVisited, list, ans);
            return ans;
        }

        public void dfs(int[] nums, int depth, boolean[] isVisited, List<Integer> list, List<List<Integer>> ans) {
            if (depth == nums.length) {
                ans.add(new ArrayList<>(list));
                return;
            }

            for (int num : nums) {
                if (!isVisited[num]) {
                    list.add(num);
                    isVisited[num] = true;

                    dfs(nums, depth + 1, isVisited, list, ans);

                    isVisited[num] = false;
                    list.remove(list.size() - 1);
                }
            }
        }
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

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    private HashMap<Integer, Integer> inorderMap;

    public TreeNode myBuildTree(int[] postorder, int[] inorder, int postorderLeft, int postorderRight, int inorderLeft, int inorderRight) {
        if (postorderLeft > postorderRight) {
            return null;
        }

        TreeNode root = new TreeNode(postorder[postorderRight]);

        int index = inorderMap.get(postorder[postorderRight]);
        int leftChildTreeNodeCount = index - inorderLeft;
        int rightChileTreeNodeCount = inorderRight - index;

        root.left = myBuildTree(postorder, inorder, postorderLeft, postorderRight-rightChileTreeNodeCount-1, inorderLeft, index-1);
        root.right = myBuildTree(postorder, inorder, postorderRight-rightChileTreeNodeCount, postorderRight-1, index+1, inorderRight);
        return root;
    }

    public TreeNode buildTree(int[] postorder, int[] inorder) {
        inorderMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderMap.put(inorder[i],i);
        }

        return myBuildTree(postorder, inorder, 0, postorder.length-1, 0, inorder.length-1);
    }
    public TreeNode myBuildSearchTree(Queue<Integer> queue) {
        if (queue.size() == 0) {
            return null;
        }

        int rootValue = queue.poll();
        TreeNode root = new TreeNode(rootValue);

        Queue<Integer> leftTreeQueue = new LinkedList<>();
        Queue<Integer> rightTreeQueue = new LinkedList<>();
        if (queue.size() != 0) {
            int value = queue.poll();
            if (value < rootValue) {
                leftTreeQueue.offer(value);
            }
            else {
                rightTreeQueue.offer(value);
            }
        }

        root.left = myBuildSearchTree(leftTreeQueue);
        root.right = myBuildSearchTree(rightTreeQueue);
        return root;
    }

    public TreeNode bstFromPreorder(int[] preorder) {
        Queue<Integer> preorderQueue = new LinkedList<>();
        for (int i = 0; i < preorder.length; i++) {
            preorderQueue.offer(preorder[i]);
        }

        return myBuildSearchTree(preorderQueue);
    }

    public int maxTurbulenceSize(int[] arr) {
        if (arr.length == 1) {
            return 1;
        }

        else if (arr.length == 2 && arr[0] != arr[1]) {
            return 2;
        }

        else if (arr.length == 2 && arr[0] == arr[1]) {
            return 1;
        }

        int start = 0;
        int op = arr[0] > arr[1] ? 1 : arr[0] == arr[1] ? 0 : -1;
        int ans = 1;

        for (int end = 2; end < arr.length; end++) {
            if (arr[end-1] != arr[end] && op == 0) {
                ans = Math.max(ans, end-start+1);
                op = arr[end-1] > arr[end] ? 1 : -1;
            }
            else if (arr[end-1] > arr[end] && op == -1) {
                ans = Math.max(ans, end-start+1);
                op = 1;
            }

            else if (arr[end-1] < arr[end] && op == 1) {
                ans = Math.max(ans, end-start+1);
                op = -1;
            }

            else {
                start = arr[end] != arr[end-1] ? end-1 : end;
                op = arr[end-1] > arr[end] ? 1 : arr[end-1] == arr[end] ? 0 : -1;
            }
        }

        return ans;
    }

    public String serialize(TreeNode root) {
        StringBuilder s = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            root = queue.poll();
            if (root != null) {
                s.append(Integer.toString(root.val));
                queue.add(root.left);
                queue.add(root.right);
            }
            else {
                s.append("n");
            }
            s.append(" ");
        }
        return s.toString();
    }

    public int totalFruit(int[] fruits) {
        Queue<Integer> queue = new ArrayDeque<>();
        Queue<Integer> queue1 = new LinkedList<>();
        queue.size();
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
