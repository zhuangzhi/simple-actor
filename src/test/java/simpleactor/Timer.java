package simpleactor;

public class Timer {
    long beginTime;
    long endTime;
    public Timer(){
        beginTime = System.currentTimeMillis();
    }
    public void begin() {
        beginTime = System.currentTimeMillis();
    }
    public void end () {
        endTime = System.currentTimeMillis();
    }

    public long use() {
        return endTime - beginTime;
    }

    public double useSeconds() {
        return ((double)use())/1000;
    }

    public double tps(int times) {
        return times/useSeconds();
    }

    public void print(String msg, int times) {
        System.out.println(msg + ", times:"+times+", elapsed:"+useSeconds()+"S, TPS:"+(int)tps(times));
    }
}
