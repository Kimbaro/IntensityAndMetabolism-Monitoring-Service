package com.capstone.kimbaro.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;

@Data
@Document(collection = "Channel")
public class Channel {

    @Id
    String id;

    @Indexed
    boolean channel;

    @Indexed
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
    private LocalDateTime localDateTime;

    /**
     * @param channel 채널 사용중 여부
     */
    @Builder
    public Channel(@Nullable String id, boolean channel) {
        this.id = id;
        this.channel = channel;
        this.localDateTime = LocalDateTime.now();
    }
}
