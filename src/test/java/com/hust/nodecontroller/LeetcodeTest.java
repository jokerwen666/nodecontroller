package com.hust.nodecontroller;

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
        TreeNode bst = bstFromPreorder(new int[]{8,5,1,7,10,12});
        TreeNode root = buildTree(new int[]{9,15,7,20,3}, new int[]{9,3,15,20,7});
        maxTurbulenceSize(new int[]{9,4,2,10,7,8,8,1,9});
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
