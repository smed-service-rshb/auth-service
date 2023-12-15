package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.SegmentData;

import java.util.ArrayList;
import java.util.List;

/**
 * Тело ответа на запрос полного списка сегментов
 */
public class GetSegmentRs{
    private List<SegmentData> segments = new ArrayList<>();

    public List<SegmentData> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentData> segments) {
        this.segments = segments;
    }
}
