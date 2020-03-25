package Enity;

import lombok.Data;

import java.time.LocalDateTime;

public @Data class Task {
    private int id = -1;
    private int listId = -1;
    private String name = "";
    private String description = "";
    private LocalDateTime alert_time;
    private boolean alert_received = false;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", alert_time=" + alert_time +
                ", alert_received=" + alert_received +
                '}';
    }
}

