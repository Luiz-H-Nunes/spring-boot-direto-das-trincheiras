package spring.student.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.student.domain.Anime;
import spring.student.domain.Producer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
@Repository
@RequiredArgsConstructor
public class AnimeHardCodeRepository {
    private final AnimeData AnimeData;


    public  List<Anime> findAll() {
        return AnimeData.getAnimes();
    }

    public  Optional<Anime> findById(Long id) {
        return  AnimeData.getAnimes().stream().filter(anime -> anime.getId().equals(id)).findFirst();

    }

    public  List<Anime> findByName(String name) {
        return AnimeData.getAnimes().stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public  Anime save(Anime anime) {
         AnimeData.getAnimes().add(anime);
        return anime;
    }

    public Anime delete(Anime anime) {
        AnimeData.getAnimes().remove(anime);
        return anime;
    }

    public Anime update(Anime anime) {
        delete(anime);
        save(anime);
        return anime;
    }


}
