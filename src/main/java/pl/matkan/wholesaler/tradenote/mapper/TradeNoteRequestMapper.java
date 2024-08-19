package pl.matkan.wholesaler.tradenote.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteRequest;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TradeNoteRequestMapper {
    TradeNoteRequestMapper INSTANCE = Mappers.getMapper(TradeNoteRequestMapper.class);

    @Mapping(source = "content",target = "content")
    @Mapping(source = "companyName",target = "companyName")
    @Mapping(source = "ownerId",target = "ownerId")
    TradeNoteRequest tradeNoteToTradeNoteRequest(TradeNote tradeNote);

    @Mapping(source = "content",target = "content")
    @Mapping(source = "companyName",target = "companyName")
    @Mapping(source = "ownerId",target = "ownerId")
    TradeNote tradeNoteRequestToTradeNote(TradeNoteRequest tradeNoteRequest);
}
