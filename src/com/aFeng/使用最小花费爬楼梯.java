package com.aFeng;

/**
 * @author ykf
 * @version 2020/12/21
 *
数组的每个索引作为一个阶梯，第 i个阶梯对应着一个非负数的体力花费值 cost[i](索引从0开始)。

每当你爬上一个阶梯你都要花费对应的体力花费值，然后你可以选择继续爬一个阶梯或者爬两个阶梯。

您需要找到达到楼层顶部的最低花费。在开始时，你可以选择从索引为 0 或 1 的元素作为初始阶梯。

示例 1:

输入: cost = [10, 15, 20]
输出: 15
解释: 最低花费是从cost[1]开始，然后走两步即可到阶梯顶，一共花费15。
示例 2:

输入: cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
输出: 6
解释: 最低花费方式是从cost[0]开始，逐个经过那些1，跳过cost[3]，一共花费6。
注意：

cost 的长度将会在 [2, 1000]。
每一个 cost[i] 将会是一个Integer类型，范围为 [0, 999]。
 */
public class 使用最小花费爬楼梯 {

    public static void main(String[] args) {
        int[] cost = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
        System.out.println(minCostClimbingStairs(cost));
    }

    /**
     * 动态规划简单题
     * @param cost 数组
     * @return 结果
     */
    public static int minCostClimbingStairs(int[] cost) {
        //最短数组是两个,直接返回
        if(cost.length<3)
            return Integer.min(cost[0],cost[1]);
        int[][] dp = new int[2][cost.length];
        //直接写下转移方程 不踩cost[i]时
        //dp[0][i] = dp[1][i-1]
        //踩下cost[i]时
        //dp[1][i] = Integer.min(dp[1][i-1],dp[0][i-1]) + cost[i]
        dp[0][0] = 0;
        dp[1][0] = cost[0];
        for (int i = 1; i < cost.length; i++) {
            dp[0][i] = dp[1][i-1];
            dp[1][i] = Integer.min(dp[1][i-1],dp[0][i-1]) + cost[i];
        }
        return Integer.min(dp[0][cost.length-1],dp[1][cost.length-1]);
    }
}
