package spring.student.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spring.student.mapper.ProducerMapper;
import spring.student.request.ProducerPostRequest;
import spring.student.request.ProducerPutRequest;
import spring.student.response.ProducerGetRespose;
import spring.student.service.ProducerService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    private final ProducerMapper MAPPER =  ProducerMapper.INSTANCE;
    private final ProducerService service;

    public ProducerController(ProducerService producerService) {
        this.service = producerService;
    }



    @GetMapping()
    public ResponseEntity<List<ProducerGetRespose>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received for listAll producers, param name {}", name);

        var producers = service.findAll(name);
        var producerGetResposesresponse = MAPPER.toListProducerGetRespose(producers);

        return ResponseEntity.ok(producerGetResposesresponse);
    }

    @GetMapping("{idproducer}")
    public ResponseEntity<ProducerGetRespose> findById(@PathVariable Long idproducer) {
       log.debug("Request to  findById producer, param idproducer {}", idproducer);

       var producer = service.findByIdOrThrowNotFound(idproducer);
       var producerGetResponse = MAPPER.toProducerGetRespose(producer);
       return ResponseEntity.ok(producerGetResponse);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,headers = "x-api-key=addProducer")
    public ResponseEntity<ProducerGetRespose> addProducer(@RequestBody ProducerPostRequest producerPostRequest,@RequestHeader HttpHeaders httpHeaders) {
        log.info("{}",httpHeaders);

        var producer = MAPPER.toProducer(producerPostRequest);

        var producerSaved = service.save(producer);

        var producerGetResponse = MAPPER.toProducerGetRespose(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletebyId(@PathVariable Long id ) {
        log.debug("Request to delete producer : {}", id);

        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping()
    public ResponseEntity<Void> putById(@RequestBody ProducerPutRequest producerPutRequest) {
        log.debug("Request to update producer : {}", producerPutRequest);

        var producer = MAPPER.toProducer(producerPutRequest);

        service.update(producer);


        return ResponseEntity.noContent().build();

    }

}
