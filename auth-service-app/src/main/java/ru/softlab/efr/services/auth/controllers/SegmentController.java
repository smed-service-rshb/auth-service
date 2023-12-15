package ru.softlab.efr.services.auth.controllers;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.exchange.model.GetSegmentsRs;
import ru.softlab.efr.services.auth.exchange.model.SimpleObjectData;
import ru.softlab.efr.services.auth.model.Segment;
import ru.softlab.efr.services.auth.services.SegmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер обработки запросов, связанных со справочником сегентов
 */
@Validated
@RestController
@PropertySource("classpath:ValidationMessages.properties")
public class SegmentController implements SegmentsApi {
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private SegmentService segmentService;

    @Override
    public ResponseEntity<GetSegmentsRs> getSegments() throws Exception {
        GetSegmentsRs response = fillFullResponse(segmentService.findAll());
        return ResponseEntity.ok().body(response);
    }

    private GetSegmentsRs fillFullResponse(List<Segment> segmentList) {
        GetSegmentsRs response = new GetSegmentsRs();
        response.setSegments(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(segmentList)) {

            List<SimpleObjectData> segmentDataList = segmentList.parallelStream()
                    .map(SegmentController::mapSegment2Data)
                    .collect(Collectors.toList());

            response.getSegments().addAll(segmentDataList);
        }
        return response;
    }

    private static SimpleObjectData mapSegment2Data(Segment segment) {
        SimpleObjectData segmentData = new SimpleObjectData();
        Utils.mapSimilarObjects(segment, segmentData);
        return segmentData;
    }

}
