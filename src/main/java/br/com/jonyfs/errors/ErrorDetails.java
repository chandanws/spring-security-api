package br.com.jonyfs.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import org.springframework.validation.FieldError;

@Builder
@Value
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {

    private final Date timestamp;
    private final String message;
    private final String details;
    private final List<FieldError> fieldErrorDetails;

}
