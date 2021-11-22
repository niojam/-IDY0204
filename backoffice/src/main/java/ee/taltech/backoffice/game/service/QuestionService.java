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
        Question question = questionRepository.findById(id).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Question with id %d was not found", id)));
        QuestionDto questionDto = questionMapper.toDto(question);
        return questionDto.setAnswers(answerService.getAnswersForQuestion(id));
    }

    public List<QuestionDto> getQuestionsForQuiz(Long id) {
        List<QuestionDto> questions = questionMapper.toQuestionDtoList(questionRepository.findByQuizId(id));
        questions.forEach(question -> question.setAnswers(answerService.getAnswersForQuestion(question.getId())));
        return questions;
    }

    public void deleteQuestions(Long quizId) {
        List<Question> questionsToDelete = questionRepository.findByQuizId(quizId);
        questionsToDelete.forEach(question -> answerService.deleteAnswers(question.getId()));
        questionRepository.deleteAll(questionsToDelete);
    }

    public List<QuestionDto> saveQuestions(List<QuestionDto> questionDtos) {
        // check that each question has at least one correct answer
        questionDtos.forEach(question -> {
            if (!providedAtLeastOneValidAnswer(question.getAnswers())) {
                throw new BadRequest(BadRequest.Code.INVALID_ARGUMENT_EXCEPTION, "Question type does not match amount of marked answers");
            }
        });

        List<Question> questions = questionMapper.toQuestionList(questionDtos);

        // save questions (allow postgres to generate ids)
        List<Question> questionsWithIds = StreamSupport
                .stream(questionRepository.saveAll(questions).spliterator(), false)
                .collect(Collectors.toList());

        // save with nextQuestionId
        setNextQuestionId(questionsWithIds);
        questionRepository.saveAll(questionsWithIds);

        // set questionIds for answers
        IntStream.range(0, questions.size())
                .forEach(index -> {
                    Long questionId = questionsWithIds.get(index).getId();
                    questionDtos
                            .get(index)
                            .getAnswers()
                            .forEach(answer -> answer.setQuestionId(questionId));
                });

        // save answers as one request
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
        questionRepository.findById(questionDto.getId()).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Question with id %d was not found", questionDto.getId())));

        if (!providedAtLeastOneValidAnswer(questionDto.getAnswers())) {
            throw new BadRequest(BadRequest.Code.INVALID_ARGUMENT_EXCEPTION, "Question type does not match amount of marked answers");
        }

        questionRepository.save(questionMapper.toEntity(questionDto));

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
        Question questionToDelete = questionRepository.findById(id).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Question with id %d was not found", id)));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() ->
                new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION,
                        String.format("Quiz with id %d was not found", quizId)));
        if (id.equals(quiz.getFirstQuestionId())) {
            quiz.setFirstQuestionId(questionToDelete.getNextQuestionId());
            quizRepository.save(quiz);
        }

        List<Question> questions = questionRepository.findByQuizId(quizId);
        questions.remove(questionToDelete);
        setNextQuestionId(questions);
        questionRepository.saveAll(questions);
        questionRepository.delete(questionToDelete);
    }

    public QuestionDto addQuestion(QuestionDto question, Long quizId) {
        if (!providedAtLeastOneValidAnswer(question.getAnswers())) {
            throw new BadRequest(BadRequest.Code.INVALID_ARGUMENT_EXCEPTION, "Question type does not match amount of marked answers");
        }

        question.setQuizId(quizId);
        List<Question> existingQuestions = questionRepository.findByQuizId(quizId);

        Question savedQuestion = questionRepository.save(questionMapper.toEntity(question));

        question.getAnswers().forEach(answerDto -> answerDto.setQuestionId(savedQuestion.getId()));
        question.setAnswers(answerService.saveAnswers(question.getAnswers()));

        if (!existingQuestions.isEmpty()) {
            Question lastQuestion = existingQuestions.stream()
                    .filter(q -> q.getNextQuestionId() == null)
                    .findAny().orElseThrow(() ->
                            new BadRequest(BadRequest.Code.BAD_REQUEST_EXCEPTION, "Question was not found"));
            lastQuestion.setNextQuestionId(savedQuestion.getId());
            questionRepository.save(lastQuestion);
        }

        return question.setId(savedQuestion.getId());
    }

    private void setNextQuestionId(List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            Question current = questions.get(i);
            if (i != questions.size() - 1) {
                Question nextQuestion = questions.get(i + 1);
                current.setNextQuestionId(nextQuestion.getId());
            } else {
                current.setNextQuestionId(null);
            }
        }
    }

    private boolean providedAtLeastOneValidAnswer(List<AnswerDto> answers) {
        long correctAnswers = answers.stream().filter(AnswerDto::getIsCorrect).count();
        return correctAnswers >= MIN_CORRECT_ANSWERS_COUNT;
    }

}
