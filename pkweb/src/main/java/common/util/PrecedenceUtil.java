package common.util;

import java.util.Arrays;

/**
 * Created by pengkai
 * @date 2016/7/12
 */
public class PrecedenceUtil {

    /**
     * 排名结果
     * （如果两个数相等则排名取前一位）
     */
    static double res = 1;

    /**
     * 运算某个数在某个数组中的排名位次
     * @param targetNum 待排名的数
     * @param allNum 数组
     * @return
     */
    public static double getPrecedence(double targetNum, double[] allNum){
        if (null != allNum){
            double numMax = 0;
            Arrays.sort(allNum);
            numMax = allNum[allNum.length - 1];
            if (numMax > targetNum) {
                res += 1;
                double[] allNum2 = Arrays.copyOf(allNum, allNum.length -1);
                if (allNum2.length > 0){
                    getPrecedence(targetNum, allNum2);
                }
            }
        }
        return res;
    }
}
