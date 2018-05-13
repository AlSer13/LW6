package Communication;

import javax.management.NotificationEmitter;

public interface TimeMBean extends NotificationEmitter{
    int getCount();
    long getTime();
}
