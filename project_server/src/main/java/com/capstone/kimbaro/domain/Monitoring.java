package com.capstone.kimbaro.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.util.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Document(collection = "Monitoring")
public class Monitoring {
    @Id
    String id = "";
    @Indexed
    String channel = "";
    @Indexed
    String name = "";
    @Indexed
    String h_rate = "0";
    @Indexed
    String time = "0";
    @Indexed
    String kcal = "0";
    @Indexed
    List c_intensity;

    /**
     * @param id      모바일 아이디
     * @param channel 참여 채널아이디
     * @param h_rate  심박수
     */

    @Builder
    public Monitoring(@Nullable String id, String channel, String name, String h_rate, String time, String kcal,
                      List c_intensity) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.h_rate = h_rate;
        this.time = time;
        this.kcal = kcal;
        if (c_intensity == null) {
            this.c_intensity = new ArrayList(Arrays.asList(0, 0, 0, 0));
        } else {
            this.c_intensity = c_intensity;
        }
    }
}
