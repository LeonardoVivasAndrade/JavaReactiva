package co.com.bancolombia.model.movement;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder(toBuilder = true)
public class MovementUploadReport {
    private String boxId;
    private int total;
    private int success;
    private int failed;
    private String uploadedAt;
    private String uploadedBy;
    private String originalFileName;
    private String sizeFile;
    private String hashFile;
}