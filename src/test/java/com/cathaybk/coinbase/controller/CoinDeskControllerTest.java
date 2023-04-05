package com.cathaybk.coinbase.controller;

import com.cathaybk.coinbase.config.OkHttpConfig;
import com.cathaybk.coinbase.exception.APIException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CoinDeskController.class)
@ContextConfiguration(classes = OkHttpConfig.class)
@ActiveProfiles({"test"})
public class CoinDeskControllerTest {

    @Autowired
    CoinDeskController coinDeskController;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void getBpiPrice() throws APIException {
        ResponseEntity<Map<String, Object>> data = coinDeskController.getBpiPrice();
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
        System.out.println(gson.toJson(data.getBody()));
    }
}