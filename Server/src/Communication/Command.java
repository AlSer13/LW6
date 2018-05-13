package Communication;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Command extends NotificationBroadcasterSupport implements CommandMBean {
    int count;
    int valid;
    boolean lastInvalid;

    @Override
    public int getCommandCount() {
        return count;
    }

    @Override
    public int getValidCommandCount() {
        return valid;
    }

    @Override
    public void submitCommand(boolean isValid) {
        count++;
        if (isValid) {
            valid++;
            lastInvalid = false;
        } else {
            if (lastInvalid) sendNotification(new Notification("Two invalid command in a row", this,
                    count, System.currentTimeMillis(), "Don't be a looser, use valid commands"));
            lastInvalid = true;
        }
    }
}
