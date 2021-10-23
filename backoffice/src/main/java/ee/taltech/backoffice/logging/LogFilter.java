package ee.taltech.backoffice.logging;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Profile("prod")
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Configuration
public class LogFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.clear();

        HttpServletRequest httpRequest = ((HttpServletRequest) request);

        // TODO generate uuid in front and retrieve it here
        MDC.put("reqId", httpRequest.getHeader("UUID"));

        // TODO pass ip from proxy
        MDC.put("ip", httpRequest.getHeader("X-TPP-IP"));

        MDC.put("reqQuery", httpRequest.getRequestURL().toString() + httpRequest.getQueryString());
        MDC.put("ua", httpRequest.getHeader("User-Agent"));
        MDC.put("host", httpRequest.getServerName());
        MDC.put("sip", httpRequest.getLocalAddr());
        MDC.put("sess", httpRequest.getSession().getId());

        chain.doFilter(request, response);

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        MDC.put("resStatus", "" + httpServletResponse.getStatus());
    }
}
