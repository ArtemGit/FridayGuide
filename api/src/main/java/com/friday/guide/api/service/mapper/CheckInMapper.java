package com.friday.guide.api.service.mapper;

import com.friday.guide.api.data.entity.chekIn.CheckIn;
import com.friday.guide.api.service.form.checkIn.CheckInForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CheckInMapper {
    CheckIn mapToEntity(CheckInForm form);
}
