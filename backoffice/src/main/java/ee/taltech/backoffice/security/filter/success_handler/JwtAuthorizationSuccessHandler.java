package ee.taltech.backoffice.security.filter.success_handler;

import ee.taltech.backoffice.game.model.Quiz;
import ee.taltech.backoffice.game.model.Room;
import ee.taltech.backoffice.game.service.QuizService;
import ee.taltech.backoffice.game.service.StatisticsService;
import ee.taltech.backoffice.security.filter.user_details.BackofficeUserDetails;
import ee.taltech.backoffice.security.properties.BackofficeSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ee.taltech.backoffice.constants.Constants.*;

@RequiredArgsConstructor
public class JwtAuthorizationSuccessHandler implements AuthenticationSuccessHandler {

    private final QuizService quizService;
    private final StatisticsService statisticsService;
    private final BackofficeSecurityProperties properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        BackofficeUserDetails userDetails = (BackofficeUserDetails) authentication.getPrincipal();
        protectQuizRelatedUris(request, userDetails.getUserId());
        protectStatisticsRelatedUris(request, userDetails.getUserId());
        protectRoomRelatedUris(request, userDetails.getUserId());
    }

    private void protectQuizRelatedUris(HttpServletRequest request, Long userId) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        if (!properties.getIgnoredQuizRelatedUris().contains(requestUri)) {
            if (properties.getQuizRelatedUris().contains(requestUri) &&
                    !method.equalsIgnoreCase(POST)) {
                Long quizId = Long.parseLong(request.getParameter(QUIZ_ID));
                Quiz quiz = quizService.getQuiz(quizId);
                checkRights(quiz.getAuthorId(), userId);
            }
        }
    }

    private void protectStatisticsRelatedUris(HttpServletRequest request, Long userId) {
        String requestUri = request.getRequestURI();
        if (properties.getStatisticsRelatedUris().contains(requestUri)) {
            Long roomId = Long.parseLong(request.getParameter(ROOM_ID));
            Room room = statisticsService.getRoom(roomId);
            checkRights(room.getAuthorId(), userId);
        }
    }

    private void protectRoomRelatedUris(HttpServletRequest request, Long userId) {
        String requestUri = request.getRequestURI();
        if (!properties.getIgnoredRoomRelatedUris().contains(requestUri)) {
            if (properties.getRoomRelatedUris().contains(requestUri)) {
                Long roomId = Long.parseLong(request.getParameter(ROOM_ID));
                Room room = statisticsService.getRoom(roomId);
                checkRights(room.getAuthorId(), userId);
            }
        }
    }

    private void checkRights(Long objectAuthorId, Long tokenUserId) {
        if (!objectAuthorId.equals(tokenUserId)) {
            throw new BadCredentialsException("You have no rights for a requested operation");
        }
    }

}
