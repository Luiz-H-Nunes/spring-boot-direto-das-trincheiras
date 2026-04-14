package spring.student.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.student.domain.Anime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodeRepositoryTest {
    @InjectMocks
    private AnimeHardCodeRepository repository;
    @Mock
    private AnimeData  animeData;
    private final List<Anime> ANIMES = new ArrayList<>();

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

        ANIMES.addAll((anime.stream().map(a -> Anime.builder().id(counter.incrementAndGet()).name(a).created(LocalDateTime.now()).build()).collect(Collectors.toCollection(ArrayList::new))));


    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all animes")
    void findAll_ReturnsAListWithAllAnimes_WhenSuccessfull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
        var animes = repository.findAll();
        Assertions.assertThat(animes).isNotNull().hasSize(ANIMES.size());

    }



    @Test
    @Order(2)
    @DisplayName("findByid return animes with given id ")
    void findById_ReturnsAnimeWithThatsId_WhenSuccessfull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
        var animeExpected = ANIMES.getFirst();
        var anime = repository.findById(animeExpected.getId());

        Assertions.assertThat(anime).isNotNull().contains(animeExpected);}


    @Test
    @Order(3)
    @DisplayName("findByName return empty list when name is null ")
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
        var anime = repository.findByName(null);

        Assertions.assertThat(anime).isNotNull().isEmpty()
        ;}


    @Test
    @Order(4)
    @DisplayName("findByName return list animes when name is not null  ")
    void findByName_ReturnsListAnime_WhenNameIsFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
        var expectedAnime = ANIMES.getFirst();
        var anime = repository.findByName(expectedAnime.getName());

        Assertions.assertThat(anime).isNotNull().contains(expectedAnime);

    }


    @Test
    @Order(5)
    @DisplayName("save return anime and add in list  ")
    void save_CreatedAnime_WhenSuccessfull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
        var anime = Anime.builder().id(404L).name("AnimeTest").created(LocalDateTime.now()).build();
        var animeSaved = repository.save(anime);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isEqualTo(404L);
        Assertions.assertThat(animeSaved.getName()).isEqualTo("AnimeTest");
        Assertions.assertThat(ANIMES).contains(anime);

    }

    @Test
    @Order(6)
    @DisplayName("delete remove a anime  ")
    void delete_RemoveAnime_WhenSuccessfull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
        var anime = ANIMES.getFirst();
        var animeRemoved = repository.delete(anime);
        Assertions.assertThat(animeRemoved).isEqualTo(anime);
        Assertions.assertThat(ANIMES).doesNotContain(animeRemoved);

    }

    @Test
    @Order(7)
    @DisplayName("update update a anime ")
    void update_updateAnime_WhenSuccessfull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
       var animeToUpdate = ANIMES.getFirst();
       animeToUpdate.setName("New Anime");

       repository.update(animeToUpdate);

       Assertions.assertThat(ANIMES).contains(animeToUpdate);
       Assertions.assertThat(animeToUpdate.getName()).isEqualTo("New Anime");

    }





}

