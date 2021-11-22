package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.Answer;
import ee.taltech.backoffice.game.model.dto.AnswerDto;
import ee.taltech.backoffice.game.model.mapper.AnswerMapper;
import ee.taltech.backoffice.game.repository.AnswerRepository;
import ee.taltech.backoffice.game.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final AnswerMapper answerMapper = Mappers.getMapper(AnswerMapper.class);

    private final QuestionRepository questionRepository;

    public List<AnswerDto> getAnswersForQuestion(Long id) {
        return answerMapper.toAnswerDtoList(answerRepository.findByQuestionId(id));
    }

    public void deleteAnswers(Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        answerRepository.deleteAll(answers);
    }

    public List<AnswerDto> saveAnswers(List<AnswerDto> dtos) {
        List<Answer> answers = answerMapper.toAnswerList(dtos);
        return answerMapper.toAnswerDtoList(answerRepository.saveAll(answers));
    }

    public AnswerDto getAnswer(Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Answer with id %d was not found", id)));
        return answerMapper.toDto(answer);
    }

    public AnswerDto updateAnswer(AnswerDto answer) {
        answerRepository.findById(answer.getId()).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Answer with id %d was not found", answer.getId())));
        answerRepository.save(answerMapper.toEntity(answer));
        return answer;
    }

    public void deleteAnswer(Long id) {
        Answer answerToDelete = answerRepository.findById(id).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Answer with id %d was not found", id)));
        answerRepository.delete(answerToDelete);
    }

    public AnswerDto addAnswer(AnswerDto answer, Long questionId) {
        questionRepository.findById(questionId).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Question with id %d was not found", questionId)));
        answer.setQuestionId(questionId);
        Answer savedAnswer = answerRepository.save(answerMapper.toEntity(answer));
        return answer.setId(savedAnswer.getId());
    }
}
