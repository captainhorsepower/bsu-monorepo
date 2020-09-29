package edu.varabei.artsiom.cyphernotebook.clientbackend.web.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.NestedServletException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Контроллер для дефолтного /error, пробрасывает ошибку, чтобы она отловилась
 * <code>@ControllerAdvice</code>, который централизованно обработает.
 */
@Log4j2
@RestController
@RequestMapping("/error")
public class RethrowingErrorController implements ErrorController {

    /* this method seems to be never called by anything */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * Извлекает из реквеста случивлуюся ошибку и пробрасывает, чтобы о ней позаботился
     *
     * @param request request, that caused an exception somewhere in filters
     * @return anything, should never return in ideal world
     * @throws Throwable thing caused by request
     * @link GlobalExceptionHandler (<code>@ControllerAdvice</code>)
     */
    @RequestMapping // принимает любой http method
    public Object getError(HttpServletRequest request) throws Throwable {

        // rethrow any exception, that happened in filters to @ControllerAdvice
        Object val = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (val != null) {
            while (val instanceof NestedServletException) {
                val = ((NestedServletException) val).getCause();
            }

            log.debug("found error, rethrowing!");
            throw (Throwable) val;
        }

        /*
         or request uri might be wrong, thus not found.
         if so, do the same stuff that org.apache.catalina.core.StandardWrapperValve does
         before redirecting request with exceptions to /error
        */
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null && statusCode == HttpStatus.NOT_FOUND.value()) {

            // create meaningful exception
            Exception notFound = new RuntimeException("Not found");
            request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, notFound);

            throw notFound;
        }

        log.debug("Error is not found, ERROR_STATUS_CODE={}", statusCode);
        // this should never be reached, but whatever
        throw new RuntimeException("Rethrowing does not know what happened.");
    }
}