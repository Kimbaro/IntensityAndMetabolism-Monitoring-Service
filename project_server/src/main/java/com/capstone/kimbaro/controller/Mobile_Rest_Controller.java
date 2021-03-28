package com.capstone.kimbaro.controller;

import com.capstone.kimbaro.domain.Channel;
import com.capstone.kimbaro.domain.Join_info;
import com.capstone.kimbaro.domain.Monitoring;
import com.capstone.kimbaro.repo.Repo_Channel;
import com.capstone.kimbaro.repo.Repo_Join_info;
import com.capstone.kimbaro.repo.Repo_Monitoring;
import com.capstone.kimbaro.repo.Repo_MonitoringOfMobile;
import com.capstone.kimbaro.service.Intensity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@CrossOrigin("*")
public class Mobile_Rest_Controller {

    Repo_Channel repo_channel;
    Repo_Join_info repo_join_info;
    Repo_Monitoring repo_monitoring;
    Repo_MonitoringOfMobile repo_monitoringOfMobile;

    @Autowired
    public Mobile_Rest_Controller(Repo_Channel repo_channel, Repo_Join_info repo_join_info,
                                  Repo_Monitoring repo_monitoring, Repo_MonitoringOfMobile repo_monitoringOfMobile) {
        this.repo_channel = repo_channel;
        this.repo_join_info = repo_join_info;
        this.repo_monitoring = repo_monitoring;
        this.repo_monitoringOfMobile = repo_monitoringOfMobile;
    }

    Logger log = LogManager.getLogger(Mobile_Rest_Controller.class);

    @GetMapping(value = "/test")
    @ResponseBody
    public Channel test() {
        log.info("SYSTEM[테스트]:");
        return Channel.builder().channel(true).build();
        //return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/c_ch") //create channel
    public Mono c_ch() {
        Mono<Channel> channel = Mono.just(new Channel(null, true)).flatMap(repo_channel::save);
//        channel.subscribe(o -> {
//            log.info("SYSTEM[채널생성]:" + o);
//        });
        return channel;
    }

    @GetMapping(value = "/search_ch")
    public Mono<Channel> search_ch(@RequestParam("channel") String channel) {
        log.info("SYSTEM[채널검색]:" + channel);
        return repo_channel.findById(channel).switchIfEmpty(Mono.error(new IllegalStateException("채널을 찾을 수 없음")));
    }


    @GetMapping(value = "/join_ch", produces = {"application/json"})
    @ResponseBody
    //join channel
    //모바일 아이디 생성 시점.
    public Mono join_ch(@ModelAttribute Join_info join_info) { //사용자 신체정보 등록 및 참여 채널아이디와 모바일 아이디 페어
        // return Mono.just(join_info).flatMap(repo_join_info::save);
        Mono<Join_info> join_ch = Mono.just(join_info).flatMap(join_info1 -> repo_join_info.save(join_info1));
        return join_ch.flatMap(join_info1 -> {
            log.info("SYSTEM[참여자개인정보]:" + join_info1);
            return repo_join_info.findByChannelAndName(join_info1.getChannel(), join_info1.getName()).flatMap(o -> {
                return repo_monitoring.save(Monitoring.builder().id(o.getId()).channel(o.getChannel()).h_rate("0").name(join_info.getName()).time("0").build());
            });
        });
    }

    /**
     * mobile_update는 모바일에서의 측정 정보를 매개변수로 받는다.
     * repo_join_info.findById 는 사용자의 신체정보가 저장된다. flatMap을 통해 해당되는 데이터를 카르보넨을 통해 가공한다.
     * repo_monitoring.findById 는 신체정보 입력 후 심박수 측정 시 실시간 데이터가 저장된다.
     * 1. switchIfEmpty : 저장된 실시간 데이터가 없는 경우 초기화한다.
     * 2. 만약 있는 경우 매개변수로 받은 신체정보와 심박수 정보를 토대로 카르보넨 범위 계산하여 time을 증가시킨다. 또한 메타볼리즘 공식으로 칼로리를 계산한다.
     * 3. 이후 repo_monitoring.save를 통해 저장한다.
     * 4. 저장된 일련의 데이터들은 web을 통해 모니터랑 구현한다.
     *
     * @return
     */
    @GetMapping(value = "/mobile/update", produces = {"application/json"})
    public Mono<Monitoring> mobile_update(@RequestParam HashMap<String, String> data) {
        log.info("SYSTEM[모바일정보응답]:" + data);

//        data.put("channel",UserConfig.channel);
//        data.put("mobile_id",UserConfig.mobile_id);
//        data.put("rate",UserConfig.RATE);
//        data.put("min_strength",UserConfig.min_strength);
//        data.put("max_strength",UserConfig.max_strength);
        return repo_join_info.findById(data.get("mobile_id")).flatMap(join_info -> {
            int gender = join_info.getGender().equals("남") ? 0 : 1;
            int min_carvonen = new Intensity().c_intensity(Integer.parseInt(join_info.getAge()),
                    gender, Integer.parseInt(join_info.getH_rest()), Integer.parseInt(data.get("min_strength")));
            int max_carvonen = new Intensity().c_intensity(Integer.parseInt(join_info.getAge()),
                    gender, Integer.parseInt(join_info.getH_rest()), Integer.parseInt(data.get("max_strength")));
            int min_var = Integer.parseInt(data.get("min_strength"));
            int max_var = Integer.parseInt(data.get("max_strength"));


            List list = Arrays.asList(min_carvonen, max_carvonen, min_var, max_var);
            String mobile_id = join_info.getId();
            String channel = join_info.getChannel();
            String rate = data.get("rate");
            String name = join_info.getName();
//            repo_monitoring.save(Monitoring.builder().id(o.getId()).channel(o.getChannel()).h_rate("0").name
//            (join_info.getName()).time("0").build());
            return repo_monitoring.findById(mobile_id).flatMap(monitoring -> {
                monitoring.setH_rate(rate);
                monitoring.setC_intensity(list);
                int temp_time = Integer.parseInt(monitoring.getTime());
                if (Integer.parseInt(rate) >= min_carvonen && Integer.parseInt(rate) <= max_carvonen) {
                    temp_time++; //운동시간 증가
                }
                monitoring.setTime(temp_time + "");
                //메타볼리즘
                String kcal = new Intensity().m_intensity(max_carvonen,
                        Double.parseDouble(join_info.getWeight()), temp_time).toString();
                monitoring.setKcal(kcal);
                log.info("SYSTEM[실시간업데이트정보]:" + monitoring);
                return repo_monitoring.save(monitoring);
            });
        });
        //return ResponseEntity.ok().build();

//        repo_join_info.findById(data.get("id")).flatMap(join_info -> {
//
//            int gender = join_info.getGender().equals("남") ? 0 : 1;
//            int min_carvonen = new Intensity().c_intensity(Integer.parseInt(join_info.getAge()),
//                    gender, Integer.parseInt(join_info.getH_rest()), Integer.parseInt(data.get("min_strength")));
//            int max_carvonen = new Intensity().c_intensity(Integer.parseInt(join_info.getAge()),
//                    gender, Integer.parseInt(join_info.getH_rest()), Integer.parseInt(data.get("max_strength")));
//            String id = join_info.getId();
//            String channel = join_info.getChannel();
//            String rate = data.get("rate");
//            String name = join_info.getName();
//
//            List list = Arrays.asList(min_carvonen, max_carvonen);
//            repo_monitoring.findById(id).switchIfEmpty(repo_monitoring.save(Monitoring.builder().id(id).channel
//            (channel).h_rate(rate).c_intensity(list).kcal("0").name(join_info.getName()).time("0").build()))
//                    .flatMap(monitoring -> {
//                        //심박수가 카르보넨 범위내 있다면
//                        int temp_time = Integer.parseInt(monitoring.getTime());
//                        if (Integer.parseInt(rate) >= min_carvonen && Integer.parseInt(rate) <= max_carvonen) {
//                            temp_time++; //운동시간 증가
//                        }
//                        //메타볼리즘
//                        String kcal = new Intensity().m_intensity(min_carvonen,
//                                Double.parseDouble(join_info.getWeight()), temp_time).toString();
//
//                        return repo_monitoring.save(Monitoring.builder().id(id).channel(channel).h_rate(rate).name
//                        (name).time(temp_time + "").kcal(kcal).c_intensity(list).build());
//                    });
//            return repo_join_info.save(join_info);
//            //return repo_monitoring.save(Monitoring.builder().id(id).channel(channel).h_rate(rate).c_intensity(list)
//            // .kcal("0").m_intensity("0").name(join_info.getName()).time("0").build());
//        }).subscribe();
//        return repo_monitoringOfMobile.save(MonitoringOfMobile.builder().id(data.get("id")).channel(data.get(
//                "channel")).rate(data.get("rate")).min_strength(data.get("min_strength")).max_strength(data.get(
//                "max_strength")).build());
    }

    @GetMapping(value = "/web/update", produces = {"application/json"})
    @ResponseBody
    public Flux<Monitoring> web_update(@RequestParam("channel") String channel) {

//        repo_join_info.findAllByChannel(channel).collectList().subscribe(join_infos -> {
//
//        });

        //repo_channel.findById(channel).switchIfEmpty(Mono.error(new IllegalStateException("채널을 찾을 수 없음")));
        log.info("SYSTEM[모니터링요청채널]:" + channel);
        log.info("SYSTEM[모니터링요청데이터]:" + channel);
        return repo_monitoring.findAllByChannel(channel);
    }
}
