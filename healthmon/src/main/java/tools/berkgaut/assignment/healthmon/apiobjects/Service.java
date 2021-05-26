package tools.berkgaut.assignment.healthmon.apiobjects;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Service {
    private String id;
    private String name;
    private String status;
    private String created;
    private String updated;
    private String url;
    private Integer timeoutMillis;
}
