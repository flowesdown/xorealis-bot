package com.ovidius.persistence.repository;

import com.ovidius.persistence.model.Guide;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GuideRepository {
    private final Map<String, Guide> storage = new ConcurrentHashMap<>();
    public void save(String name, Guide guide){storage.put(name, guide);}
    public Optional<Guide> findByName(String name) {return Optional.ofNullable(storage.get(name));}
}
