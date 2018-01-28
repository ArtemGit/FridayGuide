package com.friday.guide.api.service;

import com.friday.guide.api.data.entity.chekIn.CheckIn;
import com.friday.guide.api.data.repository.checkIn.CheckInRepository;
import com.friday.guide.api.service.form.checkIn.CheckInForm;
import com.friday.guide.api.service.mapper.CheckInMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CheckInService {

    @Autowired
    private CheckInRepository checkInRepository;
    @Autowired
    private CheckInMapper checkInMapper;

    @Transactional
    public List<CheckIn> findCheckInListHistoryForUser(Long userId) {
        if (userId != null) {
            return checkInRepository.findCheckInListHistoryForUser(userId);
        }
        return Collections.emptyList();
    }

    @Transactional
    public CheckIn save(CheckInForm checkInForm) {
        CheckIn checkIn = checkInMapper.mapToEntity(checkInForm);
        return checkInRepository.save(checkIn);
    }
}
