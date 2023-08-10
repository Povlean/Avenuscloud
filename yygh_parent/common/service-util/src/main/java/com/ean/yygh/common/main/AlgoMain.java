package com.ean.yygh.common.main;

import java.util.List;

/**
 *
 */
public class AlgoMain {

    public static String reverseLeftWords(String s, int n) {
        // 输入: s = "abcdefg", k = 2
        // 输出: "cdefgab"
        int startIndex = 0;
        int endIndex = n;
        String str = s.substring(startIndex, endIndex);
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i < s.length();i++) {
            char c = s.charAt(i);
            sb.append(c);
        }
        sb = sb.delete(startIndex, endIndex);
        sb.append(str);
        return null;
    }

    public static int[] reversePrint(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head.next != null) {
            int val = head.val;
            sb.append(val);
            head = head.next;
        }
        String str = sb.reverse().toString();
        int[] res = new int[str.length()];
        for (int i = 0;i < str.length();i++) {
            char c = str.charAt(i);
            res[i] = c;
            System.out.printf("res[%d] = %d", i, res[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        // reverseLeftWords("abcdefg", 2);
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        head.next = node1;
        node1.next = node2;
        System.out.println(head);
        int[] arr = reversePrint(head);
        for (int i = 0;i < arr.length;i++) {
            System.out.printf("arr[%d] = %d", i, arr[i]);
        }
    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
