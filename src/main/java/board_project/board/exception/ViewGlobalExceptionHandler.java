package board_project.board.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ViewGlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException e, Model model, HttpServletResponse response) {
        response.setStatus(e.getErrorCode().getStatus());

        model.addAttribute("errorStatus", e.getErrorCode().getStatus());
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("errorCode", e.getErrorCode().getStatus());
        model.addAttribute("errorName", e.getClass().getSimpleName());
        log.error("[{}] : {}", e.getClass().getSimpleName(), e.getMessage());
        return "error/error-page";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentValid (MethodArgumentNotValidException e, Model model, HttpServletResponse response) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        response.setStatus(400);
        model.addAttribute("errorStatus", ErrorCode.INVALID_INPUT_VALUE.getStatus());
        model.addAttribute("errorMessage", ErrorCode.INVALID_INPUT_VALUE.getMessage());
        model.addAttribute("errorCode", ErrorCode.INVALID_INPUT_VALUE.getCode());
        model.addAttribute("errorName", "Validation Error");

        log.error("[{}] : {}", e.getClass().getSimpleName(), e.getMessage());
        return "error/error-page";
    }

}
