package com.capstone.kimbaro.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;

@Data
@Document(collection = "Join_info")
public class Join_info {
    @Id
    String id;

    @Indexed
    String channel;
    @Indexed
    String gender;
    @Indexed
    String age;
    @Indexed
    String name;
    @Indexed
    String weight;
    @Indexed
    String h_rest;
    @Indexed
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
    private LocalDateTime join_time;

    /**
     * @param id      모바일 아이디
     * @param channel 참여 채널 정보 명시
     * @param gender  성별
     * @param age     나이
     * @param weight  몸무게
     * @param name    이름(가명,닉네임)
     * @param h_rest  안정시 심박수
     */
    public Join_info(@Nullable String id, String channel, String gender, String age, String name, String weight,
                     String h_rest) {
        this.id = id;
        this.channel = channel;
        this.gender = gender;
        this.age = age;
        this.name = name;
        this.weight = weight;
        this.h_rest = h_rest;
        this.join_time = LocalDateTime.now();
    }
}
