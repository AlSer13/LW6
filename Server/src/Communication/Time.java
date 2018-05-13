package Communication;

import javax.management.NotificationBroadcasterSupport;

public class Time extends NotificationBroadcasterSupport implements TimeMBean {

    private int count;

    @Override
    public int getCount() {
        return count;
    }

    private long avgTime; //среднее время
    private long startTime; //время последней команды

    @Override
    public long getTime() {
        return avgTime;
    }

    Time(){
        startTime = System.currentTimeMillis();
        count = 0;
        avgTime = 0;
    }

    public void resetTime(){
        startTime = System.currentTimeMillis();
    }


    public void markTime(){
        count++;
        long endTime = System.currentTimeMillis();;
        avgTime = (avgTime*(count-1)+(endTime-startTime))/count;
        System.out.println("С предыдущей отметки: " + (endTime - startTime) + "ms");
        startTime = endTime;
    }
}
