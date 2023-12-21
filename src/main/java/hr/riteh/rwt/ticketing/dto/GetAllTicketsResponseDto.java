package hr.riteh.rwt.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GetAllTicketsResponseDto {
    private List<GetAllTicketsResponseDto_department> departments = new ArrayList<>();
    private List<GetAllTicketsResponseDto_ticket> tickets = new ArrayList<>();


    public void addDepartment(GetAllTicketsResponseDto_department department) {
        this.departments.add(department);
    }

    public void addTicket(GetAllTicketsResponseDto_ticket ticket) {
        this.tickets.add(ticket);
    }

}
