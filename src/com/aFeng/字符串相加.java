package com.aFeng;

/**
 * 给定两个字符串形式的非负整数 num1 和num2 ，计算它们的和。
 *
 * 注意：
 *
 * num1 和num2 的长度都小于 5100.
 * num1 和num2 都只包含数字 0-9.
 * num1 和num2 都不包含任何前导零。
 * 你不能使用任何內建 BigInteger 库， 也不能直接将输入的字符串转换为整数形式。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class 字符串相加 {

    public static void main(String[] args) {
        System.out.println(method("7321", "257"));
    }

    static String method(String num1, String num2){
        /**
         * 还是通过双指针的方式,分别指向两个字符串的末尾,通过char类型的运算能够隐式的把数字字符转换为数字
         * 说实话有一点鸡贼,题目只说了不能直接转换为整形,和BigInteger,因为他们一定会给出一个大于Integer.MAX_VALUE的数
         * 所以就一位一位的转换,相加,判断是否需要进位,对10取模就是个位数,对10整除就是进位的数字,当两个字符串不一样长的时候,直接当做0处理就行
         * 知道两个指针都左移到头了并且没有进位,则运算结束.因为是使用StringBuffer来存储结果,这个顺序是颠倒的,最后反转一下即可.
         */
        int i = num1.length() - 1, j = num2.length() - 1, add = 0;
        StringBuffer ans = new StringBuffer();
        while (i >= 0 || j >= 0 || add != 0) {
            int x = i >= 0 ? num1.charAt(i) - '0' : 0;
            int y = j >= 0 ? num2.charAt(j) - '0' : 0;
            int result = x + y + add;
            ans.append(result % 10);
            add = result / 10;
            i--;
            j--;
        }
        // 计算完以后的答案需要翻转过来
        ans.reverse();
        return ans.toString();

    }
}
