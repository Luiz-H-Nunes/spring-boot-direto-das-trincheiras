package spring.student.repository;

import external.dependencies.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import spring.student.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodeRepository {

    private static final List<Producer> PRODUCERS = new ArrayList<>();


    private final Connection connection;
    static {
        var producer1 = Producer.builder().id(1L).name("Toei Animation").created(LocalDateTime.now()).build();
        var producer2 = Producer.builder().id(2L).name("Nippon Animation").created(LocalDateTime.now()).build();
        var producer3 = Producer.builder().id(2L).name("TMS Entertainment").created(LocalDateTime.now()).build();
        PRODUCERS.addAll(List.of(producer1,producer2,producer3));
    }

    public  List<Producer> findAll() {
        return PRODUCERS;
    }

    public  Optional<Producer> findById(Long id) {
        return  PRODUCERS.stream().filter(producer -> producer.getId().equals(id)).findFirst();

    }

    public  List<Producer> findByName(String name) {
        log.debug(connection);
        return PRODUCERS.stream().filter(producer -> producer.getName().equalsIgnoreCase(name.trim())).toList();
    }

    public  Producer save(Producer producer) {
         PRODUCERS.add(producer);
        return producer;
    }

    public  Producer delete(Producer producer) {
        PRODUCERS.remove(producer);
        return producer;
    }

    public  Producer update(Producer producer) {
        delete(producer);
        save(producer);
        return producer;
    }


}
