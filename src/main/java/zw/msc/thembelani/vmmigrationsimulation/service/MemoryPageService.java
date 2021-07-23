package zw.msc.thembelani.vmmigrationsimulation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import zw.msc.thembelani.vmmigrationsimulation.model.MemoryPage;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;

import java.util.List;


public interface MemoryPageService {
    MemoryPage save(MemoryPage memoryPage);
    List<MemoryPage> findPagesWithAGivenProbabilityLimitAndStatus(double limit, MigrationStatus status, int pageSize);
    List<MemoryPage> findAllPagesPerStatus(MigrationStatus modifiedAfterTransfer);
    Page<MemoryPage> findAll(Pageable pageable);
    MemoryPage findFirstMemoryPage ();
    MemoryPage findLastMemoryPage();
    List<MemoryPage> modifyMemoryPages(int lowestId, int highestId );
}