package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Внутренняя модель элемента отчета для мотивации
 */
@Validated
public class MotivationGetXlsxReport  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("startDate")
    private LocalDate startDate = null;

    @JsonProperty("endDate")
    private LocalDate endDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public MotivationGetXlsxReport() {}

    /**
     * Создает экземпляр класса
     * @param startDate Дата начала формирования отчета
     * @param endDate Дата окончания формирования отчета
     */
    public MotivationGetXlsxReport(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Дата начала формирования отчета
    * @return Дата начала формирования отчета
    **/
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Дата окончания формирования отчета
    * @return Дата окончания формирования отчета
    **/
    
  @Valid


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MotivationGetXlsxReport motivationGetXlsxReport = (MotivationGetXlsxReport) o;
        return Objects.equals(this.startDate, motivationGetXlsxReport.startDate) &&
            Objects.equals(this.endDate, motivationGetXlsxReport.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MotivationGetXlsxReport {\n");
        
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
        sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

