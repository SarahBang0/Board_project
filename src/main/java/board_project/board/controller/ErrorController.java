package board_project.board.controller;

import board_project.board.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ErrorController {

    @GetMapping("/error-403")
    public String error403(Model model) {
        model.addAttribute("errorMessage", ErrorCode.ACCESS_DENIED.getMessage());
        model.addAttribute("errorCode", ErrorCode.ACCESS_DENIED.getCode());
        model.addAttribute("errorStatus", ErrorCode.ACCESS_DENIED.getStatus());
        model.addAttribute("errorName", "SpringSecurity_AccessDenied");

        log.error("[Spring Security Error] : {}", ErrorCode.ACCESS_DENIED.getMessage());
        return "error/error-page";
    }
}
