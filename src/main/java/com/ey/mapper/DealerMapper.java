package com.ey.mapper;

import com.ey.dto.request.CreateDealerRequest;
import com.ey.dto.response.DealerResponse;
import com.ey.entity.Dealer;

public class DealerMapper {

    public static Dealer toEntity(CreateDealerRequest dto) {
        Dealer d = new Dealer();
        d.setName(dto.getName());
        d.setEmail(dto.getEmail());
        d.setPhone(dto.getPhone());
        d.setAddress(dto.getAddress());
        d.setActive(true);
        return d;
    }

    public static DealerResponse toResponse(Dealer d) {
        DealerResponse r = new DealerResponse();
        r.setId(d.getId());
        r.setName(d.getName());
        r.setEmail(d.getEmail());
        r.setPhone(d.getPhone());
        r.setAddress(d.getAddress());
        r.setActive(d.isActive());
        r.setCreatedAt(d.getCreatedAt());
        r.setUpdatedAt(d.getUpdatedAt());
        r.setUpdatedBy(d.getUpdatedBy() != null ? d.getUpdatedBy().getUsername() : null);
        return r;
    }
}
