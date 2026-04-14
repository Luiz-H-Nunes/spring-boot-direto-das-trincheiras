package spring.student.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import spring.student.domain.Anime;
import spring.student.repository.AnimeData;
import spring.student.repository.AnimeHardCodeRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;
    @Mock
    private AnimeHardCodeRepository repository;
    private AnimeData animeData;
    private List<Anime> ANIMES = new ArrayList<>();

    @BeforeEach
    void Init()
    {
        List<String> anime = new ArrayList<>(Arrays.asList(
                "Dragon Ball Z",
                "Naruto",
                "One Piece",
                "Attack on Titan",
                "Death Note",
                "Fullmetal Alchemist: Brotherhood",
                "Bleach",
                "Demon Slayer (Kimetsu no Yaiba)",
                "My Hero Academia (Boku no Hero Academia)",
                "Neon Genesis Evangelion"
        ));
        AtomicLong counter = new AtomicLong();

        ANIMES.addAll((anime.stream().map(a -> Anime.builder().id(counter.incrementAndGet()).name(a).
                        created(LocalDateTime.now()).build()).
                collect(Collectors.toCollection(ArrayList::new))));


    }

    @Test
    @Order(1)
    @DisplayName("findAll return list with all animes when name is null ")
    void findAll_ReturnListAllAnimes_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(ANIMES);

        var animes = service.findAll(null);
        Assertions.assertThat(animes).hasSameElementsAs(ANIMES);

    }
    @Test
    @Order(2)
    @DisplayName("findAll return list with found animes when name exists")
    void findAll_ReturnListWithFoundAnimesInList_WhenNameExists() {
        var anime = ANIMES.getFirst();
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(singletonList(anime));

        var animes = service.findAll(anime.getName());

        Assertions.assertThat(animes).containsAll(singletonList(anime));


    }

    @Test
    @Order(3)
    @DisplayName("findbyId return anime when found is present")
    void findByIdOrThrowNotFound_ReturnAnime_WhenAnimeIsFound() {

        var expectedAnime = ANIMES.getFirst();

        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var animes = service.findByIdOrThrowNotFound(expectedAnime.getId());

        Assertions.assertThat(animes).isEqualTo(expectedAnime);
    }

    @Test
    @Order(4)
    @DisplayName("findbyId return throw Excpition not found if id not present")
    void findByIdOrThrowNotFound_ReturnThrow_WhenIdIsNotPresent() {

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        service.findByIdOrThrowNotFound(ArgumentMatchers.anyLong())).
                isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(5)
    @DisplayName("save return anime and add in list  ")
    void save_CreatedAnime_WhenSuccessfull() {
        var anime = Anime.builder().id(404L).name("AnimeTest").created(LocalDateTime.now()).build();
        BDDMockito.when(repository.save(anime)).thenReturn(anime);

        var savedAnime = service.save(anime);

        Assertions.assertThat(savedAnime).isEqualTo(anime);


    }

    @Test
    @Order(6)
    @DisplayName("deleteById return anime and remove of list")
    void deleteById_ReturnAnime_WhenAnimeIsDeleted() {
        var animeExpectedDeleted = ANIMES.getFirst();
        BDDMockito.when(repository.findById(animeExpectedDeleted.getId())).thenReturn(Optional.of(animeExpectedDeleted));

        Assertions.assertThatNoException().
                isThrownBy(() -> service.deleteById(animeExpectedDeleted.getId()));


    }
    @Test
    @Order(7)
    @DisplayName("deleteById return  return throw Excpition not found if id not present")
    void deleteById_ReturnThrowNotFound_WhenAnimeIsNotPresent() {
        var animeExpectedDeleted = ANIMES.getFirst();
        BDDMockito.when(repository.findById(animeExpectedDeleted.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.deleteById(animeExpectedDeleted.getId()));


    }
    @Test
    @Order(8)
    @DisplayName("update update a anime ")
    void update_updateAnime_WhenSuccessfull() {
        var animeToUpdate = ANIMES.getFirst();
        animeToUpdate.setName("New Anime");

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.when(repository.update(animeToUpdate)).thenReturn(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));

    }


}