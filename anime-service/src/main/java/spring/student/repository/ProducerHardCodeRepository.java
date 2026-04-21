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

    private final ProducerData producerData;


//    private final Connection connection;


    public  List<Producer> findAll() {
        return producerData.getProducers();
    }

    public  Optional<Producer> findById(Long id) {
        return  producerData.getProducers().stream().filter(producer -> producer.getId().equals(id)).findFirst();

    }

    public  List<Producer> findByName(String name) {
      //  log.debug(connection);
        return producerData.getProducers().stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    public  Producer save(Producer producer) {
         producerData.getProducers().add(producer);
        return producer;
    }

    public  Producer delete(Producer producer) {
        producerData.getProducers().remove(producer);
        return producer;
    }

    public  Producer update(Producer producer) {
        delete(producer);
        save(producer);
        return producer;
    }


}
