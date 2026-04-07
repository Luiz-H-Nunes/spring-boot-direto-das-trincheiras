package spring.student.controllers;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spring.student.domain.Anime;
import spring.student.domain.Producer;
import spring.student.mapper.AnimeMapper;
import spring.student.request.AnimePostRequest;
import spring.student.response.AnimeGetResponse;
import spring.student.response.AnimePostResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static spring.student.domain.Anime.listAnime;
import static spring.student.domain.Producer.getProducers;

@Slf4j
@Getter
@RestController()
@RequestMapping("v1/animes")
public class AnimeListController {

    private final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    public static HashSet<Anime> animes;

    static{
        animes = listAnime();
    }


    @GetMapping
    public static HashSet<Anime> listAllAnime() {
        return animes;

    }

    @GetMapping("filterList")
    public HashSet<AnimeGetResponse> findByName(@RequestParam(required = false) List<String> animeName) {
       var  animeGetResponse = listAnime().stream().filter(anime -> animeName.
                stream().anyMatch(hero -> anime.getName().equalsIgnoreCase(hero))).map(anime -> MAPPER.toAnimeGetResponse(anime))
               .collect(Collectors.toCollection(HashSet::new));
        return animeGetResponse;

    }

    @GetMapping("filterPath/{idAnime}")
    public AnimeGetResponse filterAnime(@PathVariable Long idAnime) {
        var animeGetResponse = MAPPER.toAnimeGetResponse(listAnime().stream().filter(anime -> anime.getId().equals(idAnime)).findFirst().get());
        return animeGetResponse;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,headers = "x-api-key=addAnime")
    public ResponseEntity<AnimePostResponse> addAnime(@RequestBody AnimePostRequest animePostRequest, @RequestHeader HttpHeaders httpHeaders) {
        var anime = MAPPER.toAnime(animePostRequest);
        var animePostResponse = MAPPER.toAnimePostResponse(anime);
        animes.add(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);


    }
    @DeleteMapping("filterPath/{id}")
    public ResponseEntity<Void> deletebyId(@PathVariable Long id ) {

        log.debug("Request to delete anime : {}", id);


        Anime producerToDelete = listAnime().stream().
                filter(anime -> anime.getId().equals(id)).
                findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Producer not found"));

        listAnime().remove(producerToDelete);

        return ResponseEntity.ok().build();
    }


}
