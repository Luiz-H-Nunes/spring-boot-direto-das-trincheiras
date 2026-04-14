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

import spring.student.domain.Producer;
import spring.student.repository.ProducerData;
import spring.student.repository.ProducerHardCodeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
@InjectMocks
    private ProducerService service;

@Mock
    private ProducerHardCodeRepository repository;
    private ProducerData ProducerData;
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
    @DisplayName("findAll return list with all producers when name is null ")
    void findAll_ReturnListAllProducers_WhenNameIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producers);

        var producers = service.findAll(null);
        Assertions.assertThat(producers).hasSameElementsAs(producers);

    }
    @Test
    @Order(2)
    @DisplayName("findAll return list with found producers when name exists")
    void findAll_ReturnListWithFoundProducersInList_WhenNameExists() {
        var producer = producers.getFirst();
        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(singletonList(producer));

        var producers = service.findAll(producer.getName());

        Assertions.assertThat(producers).containsAll(singletonList(producer));


    }

    @Test
    @Order(3)
    @DisplayName("findbyId return Producer when found is present")
    void findByIdOrThrowNotFound_ReturnProducer_WhenProducerIsFound() {

        var expectedProducer = producers.getFirst();

        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var producer = service.findByIdOrThrowNotFound(expectedProducer.getId());

        Assertions.assertThat(producer).isEqualTo(expectedProducer);
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
    @DisplayName("save return producer and add in list  ")
    void save_CreatedProducer_WhenSuccessfull() {
        var producer = Producer.builder().id(404L).name("ProducerTest").created(LocalDateTime.now()).build();
        BDDMockito.when(repository.save(producer)).thenReturn(producer);

        var savedProducer = service.save(producer);

        Assertions.assertThat(savedProducer).isEqualTo(producer);


    }

    @Test
    @Order(6)
    @DisplayName("deleteById return producer and remove of list")
    void deleteById_ReturnProducer_WhenProducerIsDeleted() {
        var producerExpectedDeleted = producers.getFirst();
        BDDMockito.when(repository.findById(producerExpectedDeleted.getId())).thenReturn(Optional.of(producerExpectedDeleted));

        Assertions.assertThatNoException().
                isThrownBy(() -> service.deleteById(producerExpectedDeleted.getId()));


    }
    @Test
    @Order(7)
    @DisplayName("deleteById return  return throw Excpition not found if id not present")
    void deleteById_ReturnThrowNotFound_WhenProducerIsNotPresent() {
        var producerExpectedDeleted = producers.getFirst();
        BDDMockito.when(repository.findById(producerExpectedDeleted.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.deleteById(producerExpectedDeleted.getId()));


    }
    @Test
    @Order(8)
    @DisplayName("update update a Producer ")
    void update_updateProducer_WhenSuccessfull() {
        var producerToUpdate = producers.getFirst();
        producerToUpdate.setName("New Producer");

        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
        BDDMockito.when(repository.update(producerToUpdate)).thenReturn(producerToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));

    }
}