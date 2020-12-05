package com.browser.browsertest.services;

import java.io.IOException;

public interface IBrowserService {
    String start(String browser, String url);
    String stop(String browser) throws IOException;
    String getUrl(String browser) throws IOException;
    String cleanup(String browser) throws IOException;
}
