package au.chipomho.robot.filters;

import au.chipomho.robot.helpers.RobotCommandConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@SuppressWarnings({"PMD.AtLeastOneConstructor"})
public class TransactionHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        String transactionId = request.getHeader(RobotCommandConstants.X_TRANSACTION_ID);
        if (isBlank(transactionId)){
            transactionId = java.util.UUID.randomUUID().toString().toLowerCase(Locale.getDefault());
            if (log.isDebugEnabled()) {
                log.debug(" {} Adding Transaction  {}", request.getRequestURI(), transactionId);
            }
        }
        response.setHeader(RobotCommandConstants.X_TRANSACTION_ID, transactionId);
        MDC.put(RobotCommandConstants.MDC_TRANSACTION_ID, transactionId);
        filterChain.doFilter(request, response);
    }

}
