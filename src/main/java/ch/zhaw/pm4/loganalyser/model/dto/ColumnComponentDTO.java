package ch.zhaw.pm4.loganalyser.model.dto;

import ch.zhaw.pm4.loganalyser.model.log.column.ColumnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object for a {@link ch.zhaw.pm4.loganalyser.model.log.column.ColumnComponent}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnComponentDTO {

    public static final String COLUMN_TYPE_VALIDATION_MESSAGE = "Column type is not set";
    public static final String FORMAT_VALIDATION_MESSAGE = "Format is not set";
    public static final String NAME_VALIDATION_MESSAGE = "Name cannot have only whitespaces or be empty";

    private long id;

    @NotNull(message = COLUMN_TYPE_VALIDATION_MESSAGE)
    private ColumnType columnType;

    @NotNull(message = FORMAT_VALIDATION_MESSAGE)
    private String format;

    @NotBlank(message = NAME_VALIDATION_MESSAGE)
    private String name;

}
