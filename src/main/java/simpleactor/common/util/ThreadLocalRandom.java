package simpleactor.common.util;

import java.util.Random;

/**
 * @author <a href="mailto:zhuangzhi.liu@gmail.com">Zhuangzhi Liu</a>
 *         Created on: 2015/4/28
 */
public class ThreadLocalRandom extends Random {

    private static final ThreadLocal<ThreadLocalRandom> localRandom = new ThreadLocal<ThreadLocalRandom>() {
        protected ThreadLocalRandom initialValue() {
            return new ThreadLocalRandom();
        }
    };

    public ThreadLocalRandom current() {
        return localRandom.get();
    }
}
