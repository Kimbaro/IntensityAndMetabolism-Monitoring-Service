package com.capstone.kimbaro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class Index_Controller {
    @Bean
    public RouterFunction<ServerResponse> selectRouterFuction(@Value("classpath:/static/select.html") Resource index) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(index));
    }

    @Bean
    public RouterFunction<ServerResponse> group_screentFuction(@Value("classpath:/static/group_screen/index.html") Resource index) {
        return route(GET("/group_screen/index"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(index));
    }
}
