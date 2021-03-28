package com.capstone.kimbaro.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.annotation.Nullable;

@Data
@Document(collection = "MonitoringOfMobile")
public class MonitoringOfMobile {
    @Id
    String id;
    @Indexed
    String channel;
    @Indexed
    String rate;
    @Indexed
    String min_strength;
    @Indexed
    String max_strength;

    /**
     * @param id      Join_info에 등록된 id (모바일아이디)
     * @param channel 접속한 채널 아이디
     * @param rate    심박수
     * @Todo 채널에 참여한 모바일 사용자 id와 심박수를 명시함.
     */

    @Builder
    public MonitoringOfMobile(String id, String channel, String rate, String min_strength, String max_strength) {
        this.id = id;
        this.channel = channel;
        this.rate = rate;
        this.min_strength = min_strength;
        this.max_strength = max_strength;
    }
}
