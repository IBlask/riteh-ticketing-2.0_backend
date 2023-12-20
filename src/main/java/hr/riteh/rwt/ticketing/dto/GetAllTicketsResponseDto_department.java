package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetAllTicketsResponseDto_department {
    private final int departmentID;
    private final String name;


    public GetAllTicketsResponseDto_department(int departmentID, String name) {
        this.departmentID = departmentID;
        this.name = name;
    }

}
