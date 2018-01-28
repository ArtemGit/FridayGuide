package com.friday.guide.api.data.repository.checkIn;

import com.friday.guide.api.cache.Cache;
import com.friday.guide.api.data.entity.chekIn.CheckIn;
import com.friday.guide.api.data.repository.filter.FilterRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends FilterRepository<CheckIn> {

    /*@Query("select ch from #{#entityName} ch where account.id = ?1")
    List<CheckIn> findCheckInListHistoryForUser(Long userId);*/

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = Cache.CHECK_INS, key = "#p0.id")
            }
    )
    <S extends CheckIn> S save(S entity);

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = Cache.CHECK_INS, key = "#p0.id")
            }
    )
    void delete(CheckIn entity);
}
