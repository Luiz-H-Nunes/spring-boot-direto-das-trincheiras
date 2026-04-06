package spring.student.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spring.student.domain.Producer;
import spring.student.request.ProducerPostRequest;
import spring.student.response.ProducerGetRespose;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static spring.student.domain.Producer.getProducers;


@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {


    @GetMapping
    public List<Producer> ListAllProducers(){
        return getProducers();
    }



    @GetMapping("filterList")
    public ArrayList<Producer> findByName(@RequestParam(required = false) List<String> producerName) {
        return getProducers().stream().filter(producer -> producerName.stream().anyMatch(hero -> producer.getName().equalsIgnoreCase(hero))).collect(Collectors.toCollection(ArrayList::new));
    }

    @GetMapping("filterPath/{idproducer}")
    public Producer filterProducer(@PathVariable Long idproducer) {
        return getProducers().stream().
                filter(producer -> producer.getId().equals(idproducer)).
                findFirst().get();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,headers = "x-api-key=addProducer")
    public ResponseEntity<ProducerGetRespose> addProducer(@RequestBody ProducerPostRequest producerPostRequest,@RequestHeader HttpHeaders httpHeaders) {
        log.info("{}",httpHeaders);
        var producer = Producer.builder().id(getProducers().stream().mapToLong(Producer::getId).max().orElse(0)+1).
                name(producerPostRequest.getName()).created(LocalDateTime.now()).build();

        Producer.getProducers().add(producer);

        var producerGetResponse = ProducerGetRespose.builder().Id(producer.getId()).name(producerPostRequest.getName()).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);

    }
}
