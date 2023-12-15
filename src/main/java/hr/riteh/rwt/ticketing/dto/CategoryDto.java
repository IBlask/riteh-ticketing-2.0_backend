package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CategoryDto {
    private final int id;
    private final String name;


    public CategoryDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
