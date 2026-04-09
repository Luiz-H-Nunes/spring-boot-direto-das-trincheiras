package spring.student.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import spring.student.domain.Producer;
import spring.student.request.ProducerPostRequest;
import spring.student.request.ProducerPutRequest;
import spring.student.response.ProducerGetRespose;

import java.util.List;


@Mapper()
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    @Mapping(target = "created" ,expression = "java(java.time.LocalDateTime.now())")
    @Mapping(
            target = "id",
            expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(0,100000))"
    )

    Producer toProducer(ProducerPostRequest producerPostRequest);

    List<ProducerGetRespose> toListProducerGetRespose(List<Producer> producers);
    Producer toProducer(ProducerPutRequest producerPutRequest);
    ProducerGetRespose toProducerGetRespose(Producer producer);


}
