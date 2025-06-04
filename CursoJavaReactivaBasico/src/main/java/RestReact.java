import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RestReact {

	private static DatabaseService databaseService;

//    public RestReact(DatabaseService databaseService) {
//        RestReact.databaseService = databaseService;
//    }

    public static void main(String[] args)  {
		SpringApplication.run(RestReact.class, args);
//		databaseService.testConnection()
//				.doOnSuccess(aVoid -> System.out.println("Connection test successful!"))
//				.doOnError(throwable -> System.err.println("Connection test failed: " + throwable.getMessage()))
//				.subscribe();
	}
}
