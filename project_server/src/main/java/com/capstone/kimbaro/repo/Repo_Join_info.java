package com.capstone.kimbaro.repo;

import com.capstone.kimbaro.domain.Join_info;
import org.reactivestreams.Publisher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface Repo_Join_info extends ReactiveCrudRepository<Join_info, String> {
    Mono<Join_info> findByChannelAndName(String channel, String name);
}
