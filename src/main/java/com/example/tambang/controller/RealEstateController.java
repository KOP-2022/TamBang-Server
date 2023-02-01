package com.example.tambang.controller;

import com.example.tambang.domain.RealEstate;
import com.example.tambang.service.RealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RealEstateController {
    //Spring container에 의해 자동으로 DI된다.
    private final RealEstateService realEstateService;

    //@RequestPart 어노테이션을 활용해 여러 유형의 request body를 한 번에 매핑할 수 있다.
    @PostMapping("/real-estate")
    public Object enroll(@RequestPart RealEstateForm form, @RequestPart MultipartFile file){
        RealEstate realEstate = new RealEstate();
        String ownerId = form.getMemberId();

        realEstate.createRealEstate(form.getSigungu(),
                form.getRoadName(), form.getBuildType(),
                form.getFloor(), form.getArea(),
                form.getDealType(), form.getPrice(),
                form.getDeposit(), form.getMonthlyPay(), form.getDescription(),
                ""
                );
        //service layer로 전달한다.
        System.out.println("realEstate = " + realEstate);

        Long registerId = realEstateService.register(realEstate, ownerId);

        ResponseVO responseBody = new ResponseVO();

        if(true){
            responseBody.getData().put("real_estate_id", registerId);
            responseBody.setSuccess(true);
        }
        else{
            responseBody.setSuccess(false);
        }

        return responseBody;
    }
}
