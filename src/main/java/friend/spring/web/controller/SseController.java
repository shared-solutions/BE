package friend.spring.web.controller;

import friend.spring.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {
    private final SseService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        return notificationService.subscribe(request);
    }

    @PostMapping("/send-data/{id}")
    public void sendData(@PathVariable Long id) {
        notificationService.notify(id, "data");
    }
}
