package DataBase;

import lombok.Data;

public @Data class Tasks {
    private int id;
    private String name;
    private String description;
    private String alert_time;
    private boolean alert_received;
}

