package DataBase;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public @Data class Task {
    private int id;
    private String name;
    private String description;
    private LocalDateTime alert_time;
    private boolean alert_received;

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

