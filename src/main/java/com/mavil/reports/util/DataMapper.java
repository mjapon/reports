package com.mavil.reports.util;

import com.mavil.reports.vo.GenBalanceRequestVo;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;


@Service
public class DataMapper {

    public Context setData(GenBalanceRequestVo request) {

        Context context = new Context();

        Map<String, Object> data = new HashMap<>();

        data.put("items", request.getItems());
        data.put("periodo", request.getPeriodo());
        data.put("titulo", request.getTitulo());
        data.put("resumenitems", request.getResumenItems());

        context.setVariables(data);

        return context;
    }
}