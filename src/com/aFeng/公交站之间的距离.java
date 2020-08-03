package com.aFeng;

/**
 * 环形公交路线上有 n 个站，按次序从 0 到 n - 1 进行编号。我们已知每一对相邻公交站之间的距离，distance[i] 表示编号为 i 的车站和编号为 (i + 1) % n 的车站之间的距离。
 *
 * 环线上的公交车都可以按顺时针和逆时针的方向行驶。
 *
 * 返回乘客从出发点 start 到目的地 destination 之间的最短距离。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：distance = [1,2,3,4], start = 0, destination = 1
 * 输出：1
 * 解释：公交站 0 和 1 之间的距离是 1 或 9，最小值是 1。
 *  
 *
 * 示例 2：
 *
 *
 *
 * 输入：distance = [1,2,3,4], start = 0, destination = 2
 * 输出：3
 * 解释：公交站 0 和 2 之间的距离是 3 或 7，最小值是 3。
 *  
 *
 * 示例 3：
 *
 *
 *
 * 输入：distance = [1,2,3,4], start = 0, destination = 3
 * 输出：4
 * 解释：公交站 0 和 3 之间的距离是 6 或 4，最小值是 4。
 *  
 *
 * 提示：
 *
 * 1 <= n <= 10^4
 * distance.length == n
 * 0 <= start, destination < n
 * 0 <= distance[i] <= 10^4
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/distance-between-bus-stops
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */

public class 公交站之间的距离 {

    public static void main(String[] args) {
        int[] distance = {7,10,1,12,11,14,5,0};
        int start = 7, destination = 2;
        System.out.println(method(distance, start, destination));
    }

    /**
     * 这道题就比较简单了,因为是环形路线,所以只可能有两条线路,要么顺时针,要么逆时针
     * 首先计算跑玩全程的总距离,
     * 然后计算其中一种方式的距离,比如顺时针的距离,然后总距离减去顺时针的距离为逆时针的距离,两个取其小
     */
    static int method(int[] distance, int start, int destination) {
        int total = 0;
        for (int i : distance) {
            total += i;
        }
        int result = 0;
        int count = destination-start;
        if(count<0){
            count*=-1;
        }
        for (int i = 1; i <=count;i++) {
            result += distance[Math.max(start,destination)-i];
        }
        return Math.min(total-result,result);
    }
}
