package com.capstone.kimbaro.service;

import java.time.LocalDateTime;

public class Intensity {

    //최대심박수 (남자 : 214-age. 여자: 209-age)
    //적정심박수 = ((최대심박수 - 안정시심박수)*%X/100)+안정시심박수.
    //카르보넨
    public Integer c_intensity(int age, int gender, int rhr, int intensity) {
        if (intensity != 0) {
            if (gender == 0) {//남
                int HR_MAX = 214 - age;
                int HR_REST = rhr;
                int temp = HR_MAX - HR_REST;
                double intensity_temp = intensity / 100.0;
                double result = (temp * intensity_temp) + HR_REST;
                return (int) result;
            } else if (gender == 1) {//여
                int HR_MAX = 209 - age;
                int HR_REST = rhr;
                int temp = HR_MAX - HR_REST;
                double intensity_temp = intensity / 100.0;
                double result = (temp * intensity_temp) + HR_REST;
                return (int) result;
            }
        }
        return 0;
    }

    //메타볼리즘
    //운동강도에 따라 mets 할당
    // 1mets == 3.5ml
    // 1ml == 0.005kg
    //
    // ex) 6mets == 21ml
    // 21ml * 70kg(몸무게) == 1470ml
    // 1470kg * 0.005 kcal = 7.35kcal/min
    // 7.35kcal/min * 20min = 147kcal

    /**
     * @param intensity 운동강도
     * @param weight    몸무게
     * @param time      초 단위의 적정 운동시간
     *                  </p>
     * @Todo 운동시작시간과 현재시간을 비교하여 1분마다 소모칼로리계산, intenstiry에 따른 적정 mets 할당 min_rate <= h_rate 인 경우 칼로리 계산할것
     * @Todo kcal는 적정운동시간/min 으로 정수로만(소수점은 포함하지 않음)계산한다.
     */
    public Integer m_intensity(int intensity, double weight, int time) {
        double mets = 0.0;
        if (intensity <= 60) {
            mets = 2.0;
        } else if (intensity <= 70) {
            mets = 6.0;
        } else if (intensity <= 80) {
            mets = 8.0;
        } else if (intensity <= 90) {
            mets = 10.0;
        } else {
            mets = 15.0;
        }
        mets *= 3.5;
        mets *= weight;
        mets *= 0.005;
        mets *= (time / 60);

        return (int) mets; //kcal
    }

}
