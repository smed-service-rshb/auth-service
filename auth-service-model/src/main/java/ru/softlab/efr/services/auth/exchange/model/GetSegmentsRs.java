package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.SimpleObjectData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GetSegmentsRs
 */
@Validated
public class GetSegmentsRs  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("segments")
    @Valid
    private List<SimpleObjectData> segments = null;


    /**
     * Создает пустой экземпляр класса
     */
    public GetSegmentsRs() {}

    /**
     * Создает экземпляр класса
     * @param segments 
     */
    public GetSegmentsRs(List<SimpleObjectData> segments) {
        this.segments = segments;
    }

    public GetSegmentsRs addSegmentsItem(SimpleObjectData segmentsItem) {
        if (this.segments == null) {
            this.segments = new ArrayList<>();
        }
        this.segments.add(segmentsItem);
        return this;
    }

    /**
    * Get segments
    * @return 
    **/
    
  @Valid


    public List<SimpleObjectData> getSegments() {
        return segments;
    }

    public void setSegments(List<SimpleObjectData> segments) {
        this.segments = segments;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetSegmentsRs getSegmentsRs = (GetSegmentsRs) o;
        return Objects.equals(this.segments, getSegmentsRs.segments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GetSegmentsRs {\n");
        
        sb.append("    segments: ").append(toIndentedString(segments)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

