package com.aFeng;

/**
 * 给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。
 *
 * 示例 1:
 *
 * 输入: num1 = "2", num2 = "3"
 * 输出: "6"
 * 示例 2:
 *
 * 输入: num1 = "123", num2 = "456"
 * 输出: "56088"
 * 说明：
 *
 * num1 和 num2 的长度小于110。
 * num1 和 num2 只包含数字 0-9。
 * num1 和 num2 均不以零开头，除非是数字 0 本身。
 * 不能使用任何标准库的大数类型（比如 BigInteger）或直接将输入转换为整数来处理。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/multiply-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class 字符串相乘 {

    public static void main(String[] args) {
        System.out.println(multiply("2323", "35"));
    }

    static String multiply(String num1, String num2) {
        String result = "0";
        int i = num1.length() - 1, j = num2.length() - 1;
        String longStr;
        String shortStr;
        int shortLen;
        if(i>j){
            longStr = num1;
            shortStr = num2;
            shortLen = j;
        }else {
            longStr = num2;
            shortStr = num1;
            shortLen = i;
        }
        while (shortLen>=0){
            StringBuilder string = new StringBuilder(handle(longStr, shortStr.charAt(shortLen) + "", 1));
            int count = shortStr.length()-shortLen-1;
            while (count>0){
                string.append("0");
                count--;
            }
            result = handle(result,string.toString(),0);
            shortLen--;
        }
        return result;
    }

    /**
     * @param operator 运算符,0为+ 否则为*
     * @return
     */
    static String handle(String num1,String num2,int operator){
        int i = num1.length() - 1, j = num2.length() - 1, add = 0;
        StringBuilder ans = new StringBuilder();
        while (i >= 0 || j >= 0 || add != 0) {
            int x = i >= 0 ? num1.charAt(i) - '0' : 0;
            int y = j >= 0 ? num2.charAt(j) - '0' : 0;
            int result;
            if(operator==0){
                result = x + y + add;
                i--;
                j--;
            }else{
                result = x * y + add;
                if(i>=0){
                    i--;
                }else{
                    j--;
                }
            }
            ans.append(result % 10);
            add = result / 10;
        }
        for(int index=ans.length()-1;index>=0;index--){
            if(ans.charAt(index)!='0'||ans.length()==1){
                break;
            }else{
                ans.deleteCharAt(index);
            }
        }
        ans.reverse();
        return ans.toString();
    }

}
