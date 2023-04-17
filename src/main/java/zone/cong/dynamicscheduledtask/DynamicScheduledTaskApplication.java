package zone.cong.dynamicscheduledtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 开启Schedule
public class DynamicScheduledTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicScheduledTaskApplication.class, args);
    }

}
