package ru.practicum.shareit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.user.UserClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public UserClient userClient() {
        String apiPrefix = "/users";
        return new UserClient(getRestTemplate(serverUrl, apiPrefix));
    }

    @Bean
    public RequestClient requestClient() {
        String apiPrefix = "/requests";
        return new RequestClient(getRestTemplate(serverUrl, apiPrefix));
    }

    @Bean
    public ItemClient itemClient() {
        String apiPrefix = "/items";
        return new ItemClient(getRestTemplate(serverUrl, apiPrefix));
    }

    @Bean
    public BookingClient bookingClient() {
        String apiPrefix = "/bookings";
        return new BookingClient(getRestTemplate(serverUrl, apiPrefix));
    }

    private RestTemplate getRestTemplate(String serverUrl, String apiPrefix) {
        return restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + apiPrefix))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }
}