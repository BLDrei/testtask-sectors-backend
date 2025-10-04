package com.bldrei.sectors.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cache")
@SuppressWarnings("unused")
public class CacheController {
  private final CacheManager cacheManager;

  @DeleteMapping("clear-all")
  public void clearAllCaches() {
    cacheManager
      .getCacheNames()
      .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
  }
}
