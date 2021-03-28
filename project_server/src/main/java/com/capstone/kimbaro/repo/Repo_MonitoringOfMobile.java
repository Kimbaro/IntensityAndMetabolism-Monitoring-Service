package com.capstone.kimbaro.repo;

import com.capstone.kimbaro.domain.Monitoring;
import com.capstone.kimbaro.domain.MonitoringOfMobile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Repo_MonitoringOfMobile extends ReactiveCrudRepository<MonitoringOfMobile, String> {
}
