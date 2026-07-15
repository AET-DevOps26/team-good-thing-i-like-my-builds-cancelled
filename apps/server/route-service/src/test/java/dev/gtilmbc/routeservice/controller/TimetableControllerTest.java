package dev.gtilmbc.routeservice.controller;

import dev.gtilmbc.routeservice.generated.model.Station;
import dev.gtilmbc.routeservice.generated.model.TrainConnection;
import dev.gtilmbc.routeservice.service.TimetableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimetableControllerTest {

	@Mock
	private TimetableService timetableService;

	@InjectMocks
	private TimetableController controller;

	@Test
	void getStationsShouldReturnStationsFromService() throws Exception {
		Station station = new Station("8000261").name("Munich Hbf");
		when(timetableService.findByName("Munich")).thenReturn(List.of(station));

		ResponseEntity<List<Station>> response = controller.getStations("Munich");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).containsExactly(station);
		verify(timetableService).findByName("Munich");
	}

	@Test
	void getStationsShouldReturnNoContentWhenServiceFails() throws Exception {
		when(timetableService.findByName("Munich")).thenThrow(new IOException("service unavailable"));

		ResponseEntity<List<Station>> response = controller.getStations("Munich");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isNullOrEmpty();
		verify(timetableService).findByName("Munich");
	}

	@Test
	void getConnectionsShouldDelegateToService() throws Exception {
		OffsetDateTime time = OffsetDateTime.parse("2026-07-01T09:00:00Z");
		TrainConnection connection = new TrainConnection(time, time.plusHours(1), List.of());
		when(timetableService.findConnections("8000261", "8011160", time)).thenReturn(List.of(connection));

		ResponseEntity<List<TrainConnection>> response = controller.getConnections(time, "8000261", "8011160");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).containsExactly(connection);
		verify(timetableService).findConnections("8000261", "8011160", time);
	}
}
