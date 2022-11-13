package com.dmdev.spring.http.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageRestController {

    private final MessageSource messageSource;

    @GetMapping
    public String getMessage(@RequestParam("key") String key,
                             @RequestParam("lang") String language) {
        return messageSource.getMessage( key, null, null, new Locale( language ));
    }
}
