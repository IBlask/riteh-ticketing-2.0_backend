package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetAllAgentsResponseDto {
    private final String displayDetails;
    private final String agentID;


    public GetAllAgentsResponseDto(String displayDetails, String agentID) {
        this.displayDetails = displayDetails;
        this.agentID = agentID;
    }
}
