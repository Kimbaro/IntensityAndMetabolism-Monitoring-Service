package com.capstone.kimbaro.repo;

import com.capstone.kimbaro.domain.Monitoring;
import org.reactivestreams.Publisher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface Repo_Monitoring extends ReactiveCrudRepository<Monitoring, String> {

    Flux<Monitoring> findAllByChannel(String channel);
}
