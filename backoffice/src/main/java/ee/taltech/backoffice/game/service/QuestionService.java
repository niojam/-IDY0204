package ee.taltech.backoffice.game.service;

import ee.taltech.backoffice.game.controller.errorHandling.BadRequest;
import ee.taltech.backoffice.game.model.Question;
import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.dto.AnswerDto;
import ee.taltech.backoffice.game.model.dto.QuestionDto;
import ee.taltech.backoffice.game.model.mapper.QuestionMapper;
import ee.taltech.backoffice.game.repository.QuestionRepository;
import ee.taltech.backoffice.game.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {


    public static final int MIN_CORRECT_ANSWERS_COUNT = 1;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);
    private final AnswerService answerService;

    public QuestionDto getQuestion(Long id) {
        return null;
    }

    public List<QuestionDto> getQuestionsForQuiz(Long id) {
        return null;
    }


    public List<QuestionDto> saveQuestions(List<QuestionDto> questionDtos) {
        List<Question> questions = questionMapper.toQuestionList(questionDtos);

        questionDtos.forEach(question -> {
            if (!providedAtLeastOneValidAnswer(question.getAnswers())) {
                throw new BadRequest(BadRequest.Code.INVALID_ARGUMENT_EXCEPTION, "Question type does not match amount of marked answers");
            }
        });

        List<Question> questionsWithIds = StreamSupport
                .stream(questionRepository.saveAll(questions).spliterator(), false)
                .collect(Collectors.toList());

        questionRepository.saveAll(questionsWithIds);
        IntStream.range(0, questions.size())
                .forEach(index -> {
                    Long questionId = questionsWithIds.get(index).getId();
                    questionDtos
                            .get(index)
                            .getAnswers()
                            .forEach(answer -> answer.setQuestionId(questionId));
                });
        List<AnswerDto> answerDtos = questionDtos.stream().map(QuestionDto::getAnswers)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<AnswerDto> savedAnswers = answerService.saveAnswers(answerDtos);

        List<QuestionDto> savedQuestionDtos = questionMapper.toQuestionDtoList(questionsWithIds);

        savedQuestionDtos.forEach(questionDto -> {
            questionDto.setAnswers(savedAnswers.stream()
                    .filter(answerDto -> questionDto.getId().equals(answerDto.getQuestionId()))
                    .collect(Collectors.toList()));
        });

        return savedQuestionDtos;

    }

    public QuestionDto editQuestion(QuestionDto questionDto) {
        questionRepository.save(questionMapper.toEntity(questionDto));

        if (!providedAtLeastOneValidAnswer(questionDto.getAnswers())) {
            throw new BadRequest(BadRequest.Code.INVALID_ARGUMENT_EXCEPTION, "Question type does not match amount of marked answers");
        }
        answerService.getAnswersForQuestion(questionDto.getId()).forEach(answerDto -> {
            boolean isAnswerDeleted = questionDto.getAnswers()
                    .stream()
                    .noneMatch(answer -> answerDto.getId().equals(answer.getId()));
            if (isAnswerDeleted) {
                answerService.deleteAnswer(answerDto.getId());
            }
        });
        questionDto.getAnswers().forEach(answerDto -> answerDto.setQuestionId(questionDto.getId()));

        questionDto.setAnswers(answerService.saveAnswers(questionDto.getAnswers()));
        return questionDto;
    }

    public void deleteQuestion(Long id, Long quizId) {
    }

    public void deleteQuestions(Long quizId) {
        List<Question> questionsToDelete = questionRepository.findByQuizId(quizId);
        questionsToDelete.forEach(question -> answerService.deleteAnswers(question.getId()));
        questionRepository.deleteAll(questionsToDelete);
    }

    public QuestionDto addQuestion(QuestionDto question, Long quizId) {
        return null;
    }

    private boolean providedAtLeastOneValidAnswer(List<AnswerDto> answers) {
        long correctAnswers = answers.stream().filter(AnswerDto::getIsCorrect).count();
        return correctAnswers >= MIN_CORRECT_ANSWERS_COUNT;
    }

}
