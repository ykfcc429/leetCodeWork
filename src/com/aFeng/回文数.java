package com.aFeng;

/**
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * 示例 1:
 *
 * 输入: 121
 * 输出: true
 * 示例 2:
 *
 * 输入: -121
 * 输出: false
 * 解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
 * 示例 3:
 *
 * 输入: 10
 * 输出: false
 * 解释: 从右向左读, 为 01 。因此它不是一个回文数。
 * 进阶:
 *
 * 你能不将整数转为字符串来解决这个问题吗？
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/palindrome-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class 回文数 {

    public static void main(String[] args) {
        System.out.println(isPalindrome(121));
    }

    public static boolean isPalindrome(int x) {
        /*
        一个负数肯定不是回文数,或者个位数为0的数也不是回文数,因为没有数字是0开头,除非这个数就是0
         */
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        /*
         新建一个int变量来存储反转后的数字,由于回文数的特性,我们其实只需要转换一半的数字
         */
        int revertedNumber = 0;
        /*
        示例: 原数字例如 12321(下文称x)
        第一次迭代 revertedNumber(下文称rn)应该为12321的最后一位数1, 也就是x对10取余
        原数字就去掉最后一位,直接除以10,int是整形数字,不会保留小数,此时结果为:
             x = 1232
             rn = 1
        第二次迭代 x 依旧取最后一位,对10取余,而rn需要乘以10再加上这个余数
            x = 123
            rn = 1*10 + 2
            rn = 12
        继续第三次迭代
            x = 12
            rn = 123
        那么我们究竟需要迭代多少次呢?
        迭代的过程中rn会越来越大,x越来越小,当x已经小于rn的时候就没有必要再迭代了,再想想回文数的特性,是不是这样?
        如果是一个偶个数的x,那么在某一次迭代中x会直接等于rn,而奇个数的x需要多一次迭代才能使x小于rn
        所以开始迭代之前只需要观察x是否大于rn就可以开始迭代,如果x小于等于rn就代表反转结束
         */
        while (x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        /*
        相对于偶个数的x,如果是回文数x会与rn完全相同
        对于奇个数的x,需要去掉rn的个位数,因为奇个数的数字最中间的数不管是多少都并不影响它是否为一个回文数
         */
        return x == revertedNumber || x == revertedNumber / 10;
    }
}
