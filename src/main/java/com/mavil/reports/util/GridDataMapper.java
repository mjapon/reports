package com.mavil.reports.util;

import com.mavil.reports.vo.GenerateReportRequestVo;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
public class GridDataMapper {

    public Context setData(GenerateReportRequestVo request) {

        Context context = new Context();

        Map<String, Object> data = new HashMap<>();

        data.put("title", request.getTitle());
        data.put("columns", request.getColumns());
        data.put("data", request.getData());
        data.put("totals", request.getTotals());

        context.setVariables(data);

        return context;
    }

}
