package spring.student.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import spring.student.mapper.AnimeMapper;
import spring.student.request.AnimePostRequest;

import spring.student.request.AnimePutRequest;
import spring.student.response.AnimeGetResponse;
import spring.student.service.AnimeService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/animes")
public class AnimeController {

    private final AnimeMapper MAPPER;
    private final AnimeService service;



    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received for listAll anime, param name {}", name);

        var anime = service.findAll(name);
        var animeGetResponse = MAPPER.toListAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("{idAnime}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long idAnime) {
        log.debug("Request to  findById anime, param idAnime {}", idAnime);

        var anime = service.findByIdOrThrowNotFound(idAnime);
        var animeGetResponse = MAPPER.toAnimeGetResponse(anime);
        return ResponseEntity.ok(animeGetResponse);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,headers = "x-api-key=addAnime")
    public ResponseEntity<AnimeGetResponse> addAnime(@RequestBody AnimePostRequest animePostRequestPostRequest, @RequestHeader HttpHeaders httpHeaders) {
        log.info("{}",httpHeaders);


        var anime = MAPPER.toAnime(animePostRequestPostRequest);


        var animeSaved = service.save(anime);

        var animeGetResponse = MAPPER.toAnimeGetResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animeGetResponse);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletebyId(@PathVariable Long id ) {
        log.debug("Request to delete anime : {}", id);

        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping()
    public ResponseEntity<Void> putById(@RequestBody AnimePutRequest animePutRequest) {
        log.debug("Request to update anime : {}", animePutRequest);

        var anime = MAPPER.toAnime(animePutRequest);

        service.update(anime);


        return ResponseEntity.noContent().build();

    }


}
