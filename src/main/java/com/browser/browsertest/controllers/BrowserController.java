package com.browser.browsertest.controllers;

import com.browser.browsertest.services.IBrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class BrowserController {

    @Autowired
    private IBrowserService browserService;

    @GetMapping("/start")
    @ResponseBody
    public String start(@RequestParam String browser, @RequestParam String url) {
        browserService.start(browser, url);
        return "Success";
    }

    @GetMapping("/stop")
    @ResponseBody
    public String stop(@RequestParam String browser) {
        try {
            browserService.stop(browser);
        } catch (IOException e) {
            return "Error";
        }
        return "Success";
    }

    @GetMapping("/geturl")
    @ResponseBody
    public String geturl(@RequestParam String browser) {
        try {
            return browserService.getUrl(browser);
        } catch (IOException e) {
            return "Error";
        }
//        return "Success";
    }

    @GetMapping("/cleanup")
    @ResponseBody
    public String cleanup(@RequestParam String browser) throws IOException {
        browserService.cleanup(browser);
        return "true";
    }
}
