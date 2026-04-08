package spring.student.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spring.student.domain.Producer;
import spring.student.repository.ProducerHardCodeRepository;

import java.util.List;

public class ProducerService {
    private ProducerHardCodeRepository repository;

    public ProducerService(ProducerHardCodeRepository repository) {
        this.repository = repository;

    }

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name) ;
    }

    private Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Producer not found"));
    }

    private void deleteByIs(Long id) {
        var  producer = findByIdOrThrowNotFound(id);
        repository.delete(producer);
    }

    private void update(Producer producerToUpdate) {
        var producer = findByIdOrThrowNotFound(producerToUpdate.getId());
        producer.setCreated(producerToUpdate.getCreated());
        repository.update(producer);
    }
}


