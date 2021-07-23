package zw.msc.thembelani.vmmigrationsimulation.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomNumber {
    public static double randomBetweenLimits(int lower , int upperExclusive){
        Random r = new Random();
        int result  = r.nextInt(upperExclusive-lower) + lower;
        log.info("Estimated number {} ",result);
        return result;
    }
}
