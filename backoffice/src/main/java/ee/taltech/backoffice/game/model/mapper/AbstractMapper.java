package ee.taltech.backoffice.game.model.mapper;


public interface AbstractMapper<S, D> {

    D toDto(S source);

    S toEntity(D dto);


}
