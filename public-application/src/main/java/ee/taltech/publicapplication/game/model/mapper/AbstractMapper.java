package ee.taltech.publicapplication.game.model.mapper;

public interface AbstractMapper<S, D> {

    D toDto(S source);

}
