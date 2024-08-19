package pl.matkan.wholesaler.tradenote.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TradeNoteResponseMapper {
    TradeNoteResponseMapper INSTANCE = Mappers.getMapper(TradeNoteResponseMapper.class);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "content",target = "content")
    @Mapping(source = "ownerId",target = "ownerId")
    @Mapping(source = "companyName",target = "companyName")
//    @Mapping(source = "company.id",target = "companyId")
    TradeNoteResponse tradeNoteToTradeNoteResponse(TradeNote tradeNote);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "content",target = "content")
    @Mapping(source = "ownerId",target = "ownerId")
    @Mapping(source = "companyName",target = "companyName")
    TradeNote tradeNoteResponseToTradeNote(TradeNoteResponse tradeNoteResponse);
}
