package com.poc.homesnap.homesnap.service.impl;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(ReplaceUnderscores.class)
class CoordinateConverterTest {

    @Test
    void test() {
        Map<String, String> stringStringMap = CoordinateConverter.convertToWGS84("16.869683019671115", "52.45125272770617");

        assertThat(stringStringMap.get("y")).isEqualTo("355279.65418700565");
        assertThat(stringStringMap.get("x")).isEqualTo("511619.18481781427");
    }

    @Test
    void test2() {
        Map<String, String> stringStringMap = CoordinateConverter.convertTo("355287", "511625");
        assertThat(stringStringMap.get("y")).isEqualTo("16.869785424793456");
        assertThat(stringStringMap.get("x")).isEqualTo("52.451170340413405");
    }

}