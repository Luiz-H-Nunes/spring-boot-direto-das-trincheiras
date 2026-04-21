package spring.student.controllers;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import spring.student.domain.Anime;
import spring.student.repository.AnimeData;
import spring.student.repository.AnimeHardCodeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "spring.student")
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeData animeData;
    @SpyBean
    private AnimeHardCodeRepository animeHardCodeRepository;
    private final List<Anime> ANIMES = new ArrayList<>();
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init(){
        String dateTime = "2026-04-15T19:58:51.13";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
        LocalDateTime date = LocalDateTime.parse(dateTime, formatter);

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

        ANIMES.addAll((anime.stream().map(a -> Anime.builder().id(counter.incrementAndGet()).name(a).created(date).build()).collect(Collectors.toCollection(ArrayList::new))));


    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:" + fileName);
        return new String(Files.readAllBytes(file.getFile().toPath()));
    }


    @Test
    @Order(1)
    @DisplayName(" GET v1/animes return list with all animes when name is null ")
    void findAll_ReturnListAllAnimes_WhenNameIsNull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);

        var response = readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @Order(2)
    @DisplayName("GET v1/animes?param=Toei Animation return list with found animes when name exists")
    void findAll_ReturnListWithFoundAnimesInList_WhenNameExists() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);

        var response = readResourceFile("anime/get-anime-DBZ-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes?name=Dragon Ball Z"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/animes?param=x returns empty list when name is not found")
    void findAll_ReturnEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);

        var response = readResourceFile("anime/get-anime-xl-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name","x"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/animes/1 findbyId return anime when found is present")
    void findByIdOrThrowNotFound_ReturnAnime_WhenAnimeIsFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);

        var response = readResourceFile("anime/get-anime-by-id-1-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}",1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @Test
    @Order(4)
    @DisplayName("findbyId return throw ResponseStatusException 404 not found if id not present")
    void findByIdOrThrowNotFound_ReturnThrow_WhenIdIsNotPresent() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}",99L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound()).
                andExpect(MockMvcResultMatchers.status().reason("Anime not found"));


    }
    @Test
    @Order(5)
    @DisplayName("POST v1/animes created Anime  ")
    void save_CreatedAnime_WhenSuccessfull() throws Exception {

        var request = readResourceFile("anime/post-request-anime-200.json");
        var response = readResourceFile("anime/post-reponse-anime-201.json");
        var animeResponse = Anime.builder().id(404L).name("Aniplex")
                .created(LocalDateTime.parse("2026-04-15T19:58:51.13",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS")));

        BDDMockito.when(animeHardCodeRepository.save(ArgumentMatchers.any())).thenReturn(animeResponse.build());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes").content(request)
                        .header("x-api-key","addAnime")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(7)
    @DisplayName("PUT v1/animes update a Anime ")
    void update_updateAnime_WhenSuccessfull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);
        var request = readResourceFile("anime/put-request-anime-204.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes").content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(204));



    }

    @Test
    @Order(8)
    @DisplayName("PUT v1/animes return throw ResponseStatusException 404 when anime is not found ")
    void update_updateAnime_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);

        var request = readResourceFile("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes").content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404));


    }

    @Test
    @Order(9)
    @DisplayName("DELETE v1/animes return ResponseStatusException if id is not found")
    void deleteById_ReturnThrowNotFound_WhenAnimeIsNotPresent() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}",404L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());


    }

    @Test
    @Order(10)
    @DisplayName("DELETE v1/animes remove Anime")
    void deleteById_Return200_WhenSucessfull() throws Exception {
        BDDMockito.when(animeData.getAnimes()).thenReturn(ANIMES);


        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", ANIMES.getFirst().getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}