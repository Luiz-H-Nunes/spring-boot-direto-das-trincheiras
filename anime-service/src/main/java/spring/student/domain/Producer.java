package spring.student.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
public class Producer{
    private String name;
    private Long id;
    private LocalDateTime created;


   private static List<Producer> producers = new ArrayList<>();

    static  {
        var producer1 = Producer.builder().id(1L).name("Toei Animation").created(LocalDateTime.now()).build();
        var producer2 = new Producer("Nippon Animation",3L,LocalDateTime.now());
        var producer3 = new Producer("TMS Entertainment",2L, LocalDateTime.now());

        producers.addAll(List.of(producer1,producer2,producer3));
    }

    public static List<Producer> getProducers() {
        return producers;
    }
}


