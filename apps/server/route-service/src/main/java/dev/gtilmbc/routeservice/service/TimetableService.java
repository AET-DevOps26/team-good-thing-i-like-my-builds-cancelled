package dev.gtilmbc.routeservice.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gtilmbc.routeservice.generated.model.Station;
import dev.gtilmbc.routeservice.model.VerbindungResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

@Service
public class TimetableService {

    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper mapper = new ObjectMapper();
    DateFormatter dateFormatter = new DateFormatter();

    public TimetableService() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        dateFormatter.setPattern("yyyy-MM-dd'T'hh:mm:ss");
    }

    public List<Station> findByName(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.bahn.de/web/api/reiseloesung/orte?suchbegriff=" + name + "&typ=ALL&limit=10"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        var stations = mapper.readValue(response.body(), Station[].class);

        return List.of(stations);
    }

    public void findConnections(Station from, Station to, Date time) throws IOException, InterruptedException {
        String timeString = dateFormatter.print(time, Locale.GERMAN);
        String requestBody = "{\"abfahrtsHalt\":\"" + from.getId() + "\",\"anfrageZeitpunkt\":\"" + timeString + "\",\"ankunftsHalt\":\"" + to.getId() + "\",\"ankunftSuche\":\"ABFAHRT\",\"klasse\":\"KLASSE_2\",\"maxUmstiege\":0,\"produktgattungen\":[\"ICE\",\"EC_IC\",\"IR\",\"REGIONAL\",\"SBAHN\",\"UBAHN\",\"TRAM\"],\"reisende\":[{\"typ\":\"ERWACHSENER\",\"ermaessigungen\":[{\"art\":\"KEINE_ERMAESSIGUNG\",\"klasse\":\"KLASSENLOS\"}],\"alter\":[],\"anzahl\":1}],\"schnelleVerbindungen\":true,\"autonomeReservierungOnly\":false,\"bikeCarriage\":false,\"reservierungsKontingenteVorhanden\":false,\"nurDeutschlandTicketVerbindungen\":false,\"deutschlandTicketVorhanden\":false}";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.bahn.de/web/api/angebote/fahrplan"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        var connections = mapper.readValue(ungzip(response), VerbindungResponse.class).verbindungen();

        System.out.println(connections);
    }

    private String ungzip(HttpResponse<byte[]> response) throws IOException {
        byte[] data = response.body();

        boolean gzip = response.headers()
            .firstValue("Content-Encoding")
            .map(v -> v.equalsIgnoreCase("gzip"))
            .orElse(false);

        InputStream is = gzip ? new GZIPInputStream(new ByteArrayInputStream(data)) : new ByteArrayInputStream(data);

        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
}
