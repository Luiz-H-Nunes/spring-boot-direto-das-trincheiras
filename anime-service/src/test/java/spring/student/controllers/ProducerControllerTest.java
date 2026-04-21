package spring.student.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;
import spring.student.domain.Anime;
import spring.student.domain.Producer;
import spring.student.mapper.ProducerMapperImpl;
import spring.student.repository.ProducerData;
import spring.student.repository.ProducerHardCodeRepository;
import spring.student.request.ProducerPostRequest;
import spring.student.service.ProducerService;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "spring.student")
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodeRepository.class, ProducerData.class})

class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProducerData producerData;
    @SpyBean
    private ProducerHardCodeRepository producerHardCodeRepository;
    private final List<Producer> producers = new ArrayList<>();
    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init(){
        String dateTime = "2026-04-15T19:58:51.13";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
        LocalDateTime date = LocalDateTime.parse(dateTime, formatter);


        var producer1 = Producer.builder().id(1L).name("Toei Animation").created(date).build();
        var producer2 = Producer.builder().id(2L).name("Nippon Animation").created(date).build();
        var producer3 = Producer.builder().id(3L).name("TMS Entertainment").created(date).build();
        producers.addAll(List.of(producer1,producer2,producer3));

    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:" + fileName);
        return new String(Files.readAllBytes(file.getFile().toPath()));
    }


    @Test
    @Order(1)
    @DisplayName(" GET v1/producers return list with all animes when name is null ")
    void findAll_ReturnListAllAnimes_WhenNameIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
    @Test
    @Order(2)
    @DisplayName("GET v1/producers?param=Toei Animation return list with found producers when name exists")
    void findAll_ReturnListWithFoundProducersInList_WhenNameExists() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-Toei Animation-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers?name=Toei Animation"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/producers?param=x returns empty list when name is not found")
    void findAll_ReturnEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-xl-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name","x"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET v1/producers/1 findbyId return Producer when found is present")
    void findByIdOrThrowNotFound_ReturnProducer_WhenProducerIsFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-by-id-1-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}",1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @Test
    @Order(4)
    @DisplayName("findbyId return throw ResponseStatusException 404 not found if id not present")
    void findByIdOrThrowNotFound_ReturnThrow_WhenIdIsNotPresent() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}",99L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound()).
        andExpect(MockMvcResultMatchers.status().reason("Producer not found"));


    }
    @Test
    @Order(5)
    @DisplayName("POST v1/producers created Producer  ")
    void save_CreatedProducer_WhenSuccessfull() throws Exception {

        var request = readResourceFile("producer/post-request-producer-200.json");
        var response = readResourceFile("producer/post-reponse-producer-201.json");
        var producerResponse = Producer.builder().id(4L).name("Aniplex")
                .created(LocalDateTime.parse("2026-04-15T19:58:51.13",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS")));

        BDDMockito.when(producerHardCodeRepository.save(ArgumentMatchers.any())).thenReturn(producerResponse.build());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/producers").content(request)
                .header("x-api-key","addProducer")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(7)
    @DisplayName("PUT v1/producers update a Producer ")
    void update_updateProducer_WhenSuccessfull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
    var request = readResourceFile("producer/put-request-producer-204.json");

    mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers").content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is(204));



    }

    @Test
    @Order(8)
    @DisplayName("PUT v1/producers return throw ResponseStatusException 404 when producer is not found ")
    void update_updateProducer_WhenProducerIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var request = readResourceFile("producer/put-request-producer-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers").content(request).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404));


    }

    @Test
    @Order(9)
    @DisplayName("DELETE v1/producers return ResponseStatusException if id is not found")
    void deleteById_ReturnThrowNotFound_WhenProducerIsNotPresent() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producers);

    mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}",404L))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());


    }

  @Test
    @Order(10)
  @DisplayName("DELETE v1/producers remove Producer")
   void deleteById_Return200_WhenSucessfull() throws Exception {
    BDDMockito.when(producerData.getProducers()).thenReturn(producers);


      mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", producers.getFirst().getId()))
              .andDo(MockMvcResultHandlers.print())
              .andExpect(MockMvcResultMatchers.status().isOk());

  }

}