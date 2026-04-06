package spring.student.controllers.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class Producer{
    private String name;
    private Long id;


   private static List<Producer> producers = new ArrayList<>();

    static  {
        var producer1 = new Producer("Toei Animation",1L);
        var producer2 = new Producer("Nippon Animation",3L);
        var producer3 = new Producer("TMS Entertainment",2L);

        producers.addAll(List.of(producer1,producer2,producer3));
    }

    public static List<Producer> getProducers() {
        return producers;
    }
}


