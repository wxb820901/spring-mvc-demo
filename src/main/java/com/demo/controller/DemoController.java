package com.demo.controller;

import com.demo.jpa.entity.JsonObject;
import com.demo.jpa.repository.JsonObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangbil on 5/10/2017.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public WebAsyncTask<ResponseEntity> get() {
        return new WebAsyncTask<ResponseEntity>(() -> {
                //service
                return new ResponseEntity<String>("hello world!", HttpStatus.OK);
            }
        );
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public WebAsyncTask<ResponseEntity> getJson() {
        return new WebAsyncTask<ResponseEntity>(() -> {
                return new ResponseEntity<JsonObject>(new JsonObject(1l, "wangbil", "hello world"), HttpStatus.OK);
            }
        );
    }

    @RequestMapping(value = "/json/{id}", method = RequestMethod.GET)
    public WebAsyncTask<ResponseEntity> getJsonByPathVariable(@PathVariable final long id) {
        return new WebAsyncTask<ResponseEntity>(() -> {
                return new ResponseEntity<JsonObject>(new JsonObject(id, "wangbil", "hello world"), HttpStatus.OK);
            }
        );
    }

/*
* json as {"id":13,"name":"hello world!","desc":"wangbil!"}
* */
    @RequestMapping(value = "/json/post", method = RequestMethod.POST)
//    @PostMapping("/json/post")
    public WebAsyncTask<ResponseEntity> postJson(final HttpServletRequest request, @RequestBody final JsonObject json) {
//        System.out.println("error======>" + json + request);
        return new WebAsyncTask<ResponseEntity>(() -> {
                return new ResponseEntity<JsonObject>(json, HttpStatus.OK);
            }
        );
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping(value = "/json/message", method = RequestMethod.POST)
    public WebAsyncTask<ResponseEntity> postJsonMessage(final HttpServletRequest request, @RequestBody final JsonObject json) {
        return new WebAsyncTask<ResponseEntity>(() -> {
            jmsTemplate.convertAndSend("mailbox", json);
            return new ResponseEntity<JsonObject>(json, HttpStatus.OK);
        }
        );
    }

    @Autowired
    private JsonObjectRepository jsonObjectRepository;
    @RequestMapping(value = "/json/all", method = RequestMethod.GET)
    public WebAsyncTask<ResponseEntity<Iterable<JsonObject>>> getAllJson() {
        return new WebAsyncTask<ResponseEntity<Iterable<JsonObject>>>(() -> {
            return new ResponseEntity<Iterable<JsonObject>>(jsonObjectRepository.findAll(), HttpStatus.OK);
        }
        );
    }

}
