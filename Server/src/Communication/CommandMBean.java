package Communication;

import javax.management.NotificationEmitter;

public interface CommandMBean extends NotificationEmitter {
    int getCommandCount();
    int getValidCommandCount();
    void submitCommand(boolean isValid);
}
