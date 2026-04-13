package spring.student.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spring.student.domain.Anime;
import spring.student.repository.AnimeHardCodeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeHardCodeRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name) ;
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Anime not found"));
    }

    public void deleteById(Long id) {
        var  anime = findByIdOrThrowNotFound(id);
        repository.delete(anime);
    }

    public void update(Anime animeToUpdate) {
        var anime = findByIdOrThrowNotFound(animeToUpdate.getId());
        anime.setCreated(animeToUpdate.getCreated());
        repository.update(anime);
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }
}


