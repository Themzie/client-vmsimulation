package zw.msc.thembelani.vmmigrationsimulation.service;

import zw.msc.thembelani.vmmigrationsimulation.model.MemoryPage;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;

import java.util.List;

public interface MigrationService {
    List<MemoryPage> filterFormMigration (double limit, MigrationStatus migrationStatus, int pageSize);
    void transfer (List<MemoryPage> memoryPagesList);


}
