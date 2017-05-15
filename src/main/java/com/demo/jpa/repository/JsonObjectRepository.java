package com.demo.jpa.repository;


import com.demo.jpa.entity.JsonObject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by wangbil on 5/10/2017.
 */
public interface JsonObjectRepository extends CrudRepository<JsonObject, Long> {
    List<JsonObject> findByName(String name);
}
