package spring.student.repository;

import external.dependencies.Connection;
import org.springframework.stereotype.Component;
import spring.student.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {
    private static final List<Producer> producers = new ArrayList<>();

    static {
        var producer1 = Producer.builder().id(1L).name("Toei Animation").created(LocalDateTime.now()).build();
        var producer2 = Producer.builder().id(2L).name("Nippon Animation").created(LocalDateTime.now()).build();
        var producer3 = Producer.builder().id(2L).name("TMS Entertainment").created(LocalDateTime.now()).build();
        producers.addAll(List.of(producer1,producer2,producer3));
    }
    public List<Producer> getProducers() {
        return producers;
    }

}
