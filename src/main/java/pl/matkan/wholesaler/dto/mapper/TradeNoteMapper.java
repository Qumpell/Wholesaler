package pl.matkan.wholesaler.dto.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.dto.TradeNoteDto;
import pl.matkan.wholesaler.model.TradeNote;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TradeNoteMapper {
    TradeNoteMapper INSTANCE = Mappers.getMapper(TradeNoteMapper.class);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "content",target = "content")
    @Mapping(source = "user.id",target = "ownerId")
    @Mapping(source = "company.name",target = "companyName")
    @Mapping(source = "company.id",target = "companyId")
    TradeNoteDto tradeNoteToTradeNoteDto(TradeNote tradeNote);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "content",target = "content")
    TradeNote tradeNoteDtoToTradeNote(TradeNoteDto tradeNoteDto);
}
