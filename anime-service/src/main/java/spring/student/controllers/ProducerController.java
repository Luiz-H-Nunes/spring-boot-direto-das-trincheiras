package spring.student.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import spring.student.domain.Producer;
import spring.student.mapper.ProducerMapper;
import spring.student.repository.ProducerHardCodeRepository;
import spring.student.request.ProducerPostRequest;
import spring.student.request.ProducerPutRequest;
import spring.student.response.ProducerGetRespose;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static spring.student.repository.ProducerHardCodeRepository.findAll;


@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {

private final ProducerMapper MAPPER =  ProducerMapper.INSTANCE;
    @GetMapping
    public List<ProducerGetRespose> ListAllProducers(){
        return findAll().stream().map(MAPPER::toProducerGetRespose).collect(Collectors.toList());
    }



    @GetMapping("filterList")
    public ResponseEntity<ArrayList<ProducerGetRespose>> findByName(@RequestParam(required = false) List<String> producerName) {
        return ResponseEntity.ok(findAll().stream().
                filter(producer -> producerName.stream().
                        anyMatch(hero -> producer.getName().
                                equalsIgnoreCase(hero))).map(MAPPER::toProducerGetRespose).
                collect(Collectors.toCollection(ArrayList::new)));
    }

    @GetMapping("filterPath/{idproducer}")
    public ResponseEntity<ProducerGetRespose> filterProducer(@PathVariable Long idproducer) {
        return ResponseEntity.ok(findAll().stream().
                filter(producer -> producer.getId().equals(idproducer)).
                findFirst().map(MAPPER::toProducerGetRespose).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Producer not found")));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,headers = "x-api-key=addProducer")
    public ResponseEntity<ProducerGetRespose> addProducer(@RequestBody ProducerPostRequest producerPostRequest,@RequestHeader HttpHeaders httpHeaders) {
        log.info("{}",httpHeaders);

        var producer = MAPPER.toProducer(producerPostRequest);

        var producerGetResponse = MAPPER.toProducerGetRespose(producer);

        ProducerHardCodeRepository.findAll().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);

    }

    @DeleteMapping("filterPath/{id}")
    public ResponseEntity<Void> deletebyId(@PathVariable Long id ) {
        log.debug("Request to delete producer : {}", id);
        Producer producerToDelete = findAll().stream().
                filter(p -> p.getId().equals(id)).
                findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Producer not found"));

        findAll().remove(producerToDelete);

        return ResponseEntity.ok().build();
    }

    @PutMapping("filterPath/{id}")
    public ResponseEntity<Boolean> putById(@PathVariable Long id,@RequestBody ProducerPutRequest producerPutRequest) {
        log.debug("Request to update producer : {}", id);

        boolean exists = findAll().stream().anyMatch(producer -> producer.getId().equals(id) && !(producer.getName().equals(producerPutRequest.getName()))) ;

       if  (exists && !(producerPutRequest.getName().isBlank())){

           findAll().stream().filter(producer -> producer.getId().equals(id)).forEach(producer -> producer.setName(producerPutRequest.getName()));
           return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);

       }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

    }

}
