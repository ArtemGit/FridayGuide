package com.friday.guide.web.controller;

import com.friday.guide.api.data.entity.chekIn.CheckIn;
import com.friday.guide.api.service.CheckInService;
import com.friday.guide.api.service.form.checkIn.CheckInForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RestCheckInController.ROOT_MAPPING)
public class RestCheckInController {

    public final static String ROOT_MAPPING = "/com/friday/guide/api/check-in";

    @Autowired
    private CheckInService checkInService;

   /* @ResponseBody
    @RequestMapping(value = "/user/{userId}/history", method = RequestMethod.GET)
    public ResponseEntity<List<CheckIn>> getUSerCheckInListHistory(@PathVariable Long userId) {
        return new ResponseEntity<>(checkInService.findCheckInListHistoryForUser(userId), HttpStatus.OK);
    }*/


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void saveGeolocation(@RequestBody CheckInForm checkInForm) {
        checkInService.save(checkInForm);
    }

}
