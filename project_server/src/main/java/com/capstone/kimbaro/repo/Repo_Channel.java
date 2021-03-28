package com.capstone.kimbaro.repo;

import com.capstone.kimbaro.domain.Channel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repo_Channel extends ReactiveCrudRepository<Channel, String> {
}
