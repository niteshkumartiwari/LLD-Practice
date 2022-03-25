package model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RentalAgency {
    private Map<String, Branch> branchMap;

    public RentalAgency() {
        this.branchMap= new HashMap<>();
    }
}
