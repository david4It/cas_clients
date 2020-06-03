package com.david4it.config;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

public class SimpleUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {


    @Override
    public boolean matches(String url) {
        return url.contains("/logout");
    }

    /**
     * 正则表达式的规则，这个地方可以是web传递过来的
     */
    @Override
    public void setPattern(String pattern) {

    }
}
