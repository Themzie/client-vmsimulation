package zw.msc.thembelani.vmmigrationsimulation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.msc.thembelani.vmmigrationsimulation.model.MemoryPage;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;

import java.util.List;

@Repository
public interface MemoryPageRepository extends JpaRepository<MemoryPage,Integer> {

    List<MemoryPage> findAllByProbabilityOfModificationLessThanEqualAndStatus(double probabilityOfModification,MigrationStatus migrationStatus, Pageable pageable);
    List<MemoryPage> findAllByStatus(MigrationStatus status);
    Page<MemoryPage> findAll(Pageable pageable);
    MemoryPage findFirstByOrderByIdDesc();
    MemoryPage findFirstByOrderByIdAsc();
}
