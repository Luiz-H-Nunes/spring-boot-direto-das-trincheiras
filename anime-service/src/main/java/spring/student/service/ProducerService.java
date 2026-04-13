package spring.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spring.student.domain.Producer;
import spring.student.repository.ProducerHardCodeRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProducerService {
    private ProducerHardCodeRepository repository;


    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name) ;
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Producer not found"));
    }

    public void deleteById(Long id) {
        var  producer = findByIdOrThrowNotFound(id);
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        var producer = findByIdOrThrowNotFound(producerToUpdate.getId());
        producer.setCreated(producerToUpdate.getCreated());
        repository.update(producer);
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }
}


