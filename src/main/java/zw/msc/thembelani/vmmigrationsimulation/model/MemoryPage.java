package zw.msc.thembelani.vmmigrationsimulation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryPage  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private boolean present;
    private int round;
    private boolean modified;
    private double probabilityOfModification;
    private String data;
    @Enumerated(EnumType.STRING)
    private MigrationStatus status;
}
