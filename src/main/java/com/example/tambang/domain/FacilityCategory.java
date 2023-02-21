package com.example.tambang.domain;

public enum FacilityCategory {
    편의점, 음식점, 카페;

    //코드에 따라 enum 값 다르게 반환
    public FacilityCategory getFacility(String category_group_code){
        if (category_group_code == "CS2"){
            return 편의점;
        }
        else if(category_group_code == "FD6"){
            return 음식점;
        }
        else if(category_group_code == "CE7"){
            return 카페;
        }
        else
            return 편의점;
    }
}
