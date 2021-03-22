package com.aFeng;

/**
 * @author ykf
 * @version 2021/3/19
 */
public class 设计停车系统 {

}

class ParkingSystem{

    private int[] nums;

    public ParkingSystem(int big, int medium, int small) {
        nums = new int[]{big,medium,small};
    }

    public boolean addCar(int carType) {
        nums[carType-1]--;
        return nums[carType-1]>-1;
    }
}