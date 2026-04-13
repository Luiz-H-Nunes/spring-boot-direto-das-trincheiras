package spring.student.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.student.domain.Producer;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodeRepositoryTest {
    @InjectMocks
    private ProducerHardCodeRepository repository;
    @Mock
    private ProducerData producerData;
    private final List<Producer> producers = new ArrayList<>();

    @BeforeEach
    void init(){

        var producer1 = Producer.builder().id(1L).name("Toei Animation").created(LocalDateTime.now()).build();
        var producer2 = Producer.builder().id(2L).name("Nippon Animation").created(LocalDateTime.now()).build();
        var producer3 = Producer.builder().id(2L).name("TMS Entertainment").created(LocalDateTime.now()).build();
        producers.addAll(List.of(producer1,producer2,producer3));

    }
    @Test
    @Order(1)
    @DisplayName("findAll returns a list with all producers")
    void  findAll_ReturnsAListWithAllProducers_WhenSuccessfull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
        var producers = repository.findAll();
        Assertions.assertThat(producers).isNotNull().hasSize(3);
    }
    @Test
    @Order(2)
    @DisplayName("findById returns a producer with given id ")
    void  findById_ReturnsAProducerWithThatsId_WhenSuccessfull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
        var expectedProducer = producers.getFirst();
        var producer = repository.findById(expectedProducer.getId());
        Assertions.assertThat(producer).isPresent().contains(expectedProducer);
    }
    @Test
    @Order(3)
    @DisplayName("findByName returns empty list when name is null ")
    void  findByName_ReturnEmptyList_WhenNameEqualANull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
        var producers = repository.findByName(null);
        Assertions.assertThat(producers).isNotNull().isEmpty();

    }
    @Test
    @Order(4)
    @DisplayName("findByName returns a list found object when name exists ")
    void  findByName_ReturnFoundProducerInList_WhenNameIsNotNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
        var expectedProducer = producers.getFirst();
        var producer = repository.findByName(expectedProducer.getName());
        Assertions.assertThat(producer).contains(expectedProducer);

    }


}