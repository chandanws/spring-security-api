package br.com.jonyfs.errors;

import java.util.Date;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class ErrorDetails {

    private final Date timestamp;
    private final String message;
    private final String details;

}
