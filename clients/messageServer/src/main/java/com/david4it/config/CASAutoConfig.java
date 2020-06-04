package com.david4it.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas30ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Arrays;

@Configuration
public class CASAutoConfig {
    @Value("${cas.server-url-prefix}")
    private String serverUrlPrefix;
    @Value("${cas.server-login-url}")
    private String serverLoginUrl;
    @Value("${cas.server-logout-url}")
    private String serverLogoutUrl;
    @Value("${cas.client-host-url}")
    private String clientHostUrl;
    @Value("${cas.client-logout-url}")
    private String clientLogoutUrl;

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        //客户端的url地址，必须和服务器中的配置文件吻合，否则就是未认证授权的服务
        serviceProperties.setService(clientHostUrl);
        return serviceProperties;
    }

    @Bean
    @Primary
    public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties sp) {
        //定义登陆页面为CAS服务器
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(serverLoginUrl);
        entryPoint.setServiceProperties(sp);
        return entryPoint;
    }

    @Bean
    public TicketValidator ticketValidator() {
        //定义验证ticket的服务器地址
        return new Cas30ProxyTicketValidator(serverUrlPrefix);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //定义内存用户，用于验证用户信息的正确性
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password("$2a$10$8GhWjgG4j2Ot15inivAAW.bmzAeu.If9J8HEV6j/jbI37.8GLwS2W").roles("USER").build());
        return manager;
    }

    @Bean
    public CasAuthenticationProvider authenticationProvider(ServiceProperties sp, TicketValidator validator, UserDetailsService userDetailsService) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setTicketValidator(validator);
        provider.setServiceProperties(sp);
        provider.setKey("david4it");
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(ServiceProperties sp, AuthenticationProvider provider) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setServiceProperties(sp);
        filter.setAuthenticationManager(new ProviderManager(Arrays.asList(provider)));
        return filter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(serverLogoutUrl, new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(clientLogoutUrl);
        return logoutFilter;
    }
}
