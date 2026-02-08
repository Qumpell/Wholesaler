package pl.matkan.wholesaler.tradenote.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.matkan.wholesaler.tradenote.TradeNote;
import pl.matkan.wholesaler.tradenote.TradeNoteDetailedRequest;
import pl.matkan.wholesaler.tradenote.TradeNoteResponse;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TradeNoteMapper {

    TradeNoteMapper INSTANCE = Mappers.getMapper(TradeNoteMapper.class);


    @Mapping(source = "companyId", target = "company.id")
//    @Mapping(source = "ownerId", target = "user.id")
    TradeNote tradeNoteRequestToTradeNote(TradeNoteDetailedRequest dto);


    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "user.id", target = "ownerId")
    @Mapping(source = "user.username", target = "ownerUsername")
    TradeNoteResponse tradeNoteToTradeNoteResponse(TradeNote tradeNote);


}
