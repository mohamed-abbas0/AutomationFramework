package Configurations;

public interface Configurations {
    String webUrl = System.getProperty("webUrl");
    String webBrowser = System.getProperty("webBrowser", "Chrome");
}