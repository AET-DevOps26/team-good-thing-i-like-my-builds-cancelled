package dev.gtilmbc.routeservice;

import dev.gtilmbc.routeservice.generated.model.Station;
import dev.gtilmbc.routeservice.service.TimetableService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class RouteServiceApplication {

	public static void main(String[] args) {
        var service = new TimetableService();

        try {
            System.out.println(service.findConnections("A=1@O=Rosenheim@X=12119203@Y=47850021@U=80@L=8000320@i=U×008020174@", "A=1@O=Raubling@X=12110151@Y=47788508@U=80@L=8004955@i=U×008020182@", LocalDateTime.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

		SpringApplication.run(RouteServiceApplication.class, args);
	}

}
