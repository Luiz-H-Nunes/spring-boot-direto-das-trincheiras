package spring.student.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spring.student.domain.Producer;
import spring.student.mapper.ProducerMapper;
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

private final ProducerMapper MAPPER =  ProducerMapper.INSTANCE;
    @GetMapping
    public List<ProducerGetRespose> ListAllProducers(){
        return getProducers().stream().map(MAPPER::toProducerGetRespose).collect(Collectors.toList());
    }



    @GetMapping("filterList")
    public ResponseEntity<ArrayList<ProducerGetRespose>> findByName(@RequestParam(required = false) List<String> producerName) {
        return ResponseEntity.ok(getProducers().stream().filter(producer -> producerName.stream().anyMatch(hero -> producer.getName().equalsIgnoreCase(hero))).map(MAPPER::toProducerGetRespose).collect(Collectors.toCollection(ArrayList::new)));
    }

    @GetMapping("filterPath/{idproducer}")
    public ResponseEntity<ProducerGetRespose> filterProducer(@PathVariable Long idproducer) {
        return ResponseEntity.ok(getProducers().stream().
                filter(producer -> producer.getId().equals(idproducer)).
                findFirst().map(MAPPER::toProducerGetRespose).get());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,headers = "x-api-key=addProducer")
    public ResponseEntity<ProducerGetRespose> addProducer(@RequestBody ProducerPostRequest producerPostRequest,@RequestHeader HttpHeaders httpHeaders) {
        log.info("{}",httpHeaders);

        var producer = MAPPER.toProducer(producerPostRequest);

        var producerGetResponse = MAPPER.toProducerGetRespose(producer);

        Producer.getProducers().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);

    }
}
