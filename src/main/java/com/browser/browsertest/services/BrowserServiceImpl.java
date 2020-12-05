package com.browser.browsertest.services;


import com.browser.browsertest.constants.BrowserConstants;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class BrowserServiceImpl implements IBrowserService {

    private static final String OPEN_FIREFOX = "open -a /Applications/Firefox.app ";
    private static final String OPEN_OTHER_BROWSERS = "open ";
    private static final String CHROME_SESSION = "---/Users/vikas/Library/Application Support/Chromium/Default----";
    private static final String CHROME_SESSION_FAKE = "---/User/Chromium/Default----";
    private static final String FIREFOX_SESSION = "/---Users/vikas/Library/Application Support/Firefox/Profiles----";
    private static final String FIREFOX_SESSION_FAKE = "/---Users/viLibrary/Application Support/Firefox/Profiles----";

    @Override
    public String start(String browser, String url) {

        Runtime runtime = Runtime.getRuntime();
        try {
            if (browser.equalsIgnoreCase(BrowserConstants.FIREFOX)) {
                runtime.exec(OPEN_FIREFOX + url);
            } else {
                runtime.exec(OPEN_OTHER_BROWSERS + url);
            }
        } catch (IOException e) {
            // print error message
        }
        return "success";
    }

    @Override
    public String stop(String browser) throws IOException {

        switch (browser.toLowerCase()) {
            case BrowserConstants.FIREFOX:
                String cmds[] = {"killall", "firefox"};
                Runtime.getRuntime().exec(cmds);
                break;

            case BrowserConstants.CHROME:
                String cmds1[] = {"killall", "Google Chrome"};
                Runtime.getRuntime().exec(cmds1);
                break;

            default:
//                String cmds1[] = {"killall", "Google Chrome"};
//                Runtime.getRuntime().exec(cmds1);
//                break;

            }
        return "Success";
    }

    @Override
    public String getUrl(String browser) throws IOException {
        Runtime run = Runtime.getRuntime();
        String output = null;
        if (browser.equalsIgnoreCase("chrome")) {
            Process proc = run.exec("chrome-cli info");
            BufferedReader stdInput = new BufferedReader(
                    new InputStreamReader(proc.getInputStream())
            );

            BufferedReader stdError = new BufferedReader(
                    new InputStreamReader(proc.getErrorStream())
            );
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                if (s.contains("Url:")) output = s;
            }
        }
        return output;
    }

    @Override
    public String cleanup(String browser) throws IOException {
        if (browser.equalsIgnoreCase(BrowserConstants.CHROME)) {
            BrowserServiceImpl.deleteDirectoryJava8(CHROME_SESSION_FAKE);
        } else {
            BrowserServiceImpl.deleteDirectoryJava8(FIREFOX_SESSION_FAKE);
        }
        return "true";
    }


    public static void deleteDirectoryJava8(String dir) throws IOException {

        Path path = Paths.get(dir);

        // read java doc, Files.walk need close the resources.
        // try-with-resources to ensure that the stream's open directories are closed
        try (Stream<Path> walk = Files.walk(path)) {
            walk
                    .sorted(Comparator.reverseOrder())
                    .forEach(BrowserServiceImpl::deleteDirectoryJava8Extract);
        }

    }

    // extract method to handle exception in lambda
    public static void deleteDirectoryJava8Extract(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.err.printf("Unable to delete this path : %s%n%s", path, e);
        }
    }
}
