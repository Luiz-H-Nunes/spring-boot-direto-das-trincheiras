package spring.student.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import spring.student.domain.Anime;
import spring.student.request.AnimePostRequest;
import spring.student.request.AnimePutRequest;
import spring.student.response.AnimeGetResponse;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    @Mapping(target = "created" ,expression = "java(java.time.LocalDateTime.now())")
    @Mapping(
            target = "id",
            expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(0,100000))"
    )
    Anime toAnime(AnimePostRequest animePostRequest);
    List<AnimeGetResponse> toListAnimeGetResponse(List<Anime> animes);
    Anime toAnime(AnimePutRequest AnimePutRequest);
    AnimeGetResponse toAnimeGetResponse(Anime anime);

}

