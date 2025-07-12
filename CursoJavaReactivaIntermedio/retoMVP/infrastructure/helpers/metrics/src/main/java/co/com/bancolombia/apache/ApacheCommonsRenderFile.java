package co.com.bancolombia.apache;

import co.com.bancolombia.usecase.uploadmovements.RenderFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Component
public class ApacheCommonsRenderFile implements RenderFile {

    public Flux<Map<String, String>> render(byte[] bytes) {
        Reader in = new InputStreamReader(new ByteArrayInputStream(bytes));
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(in);

            return Flux.fromIterable(records).map(CSVRecord::toMap);

        } catch (Exception e) {
            return Flux.error(new IllegalArgumentException("Error parsing CSV"));
        }
    }
}
