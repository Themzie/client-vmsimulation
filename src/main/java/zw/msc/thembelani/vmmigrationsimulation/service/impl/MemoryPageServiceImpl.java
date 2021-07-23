package zw.msc.thembelani.vmmigrationsimulation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zw.msc.thembelani.vmmigrationsimulation.model.MemoryPage;
import zw.msc.thembelani.vmmigrationsimulation.repository.MemoryPageRepository;
import zw.msc.thembelani.vmmigrationsimulation.service.MemoryPageService;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;
import zw.msc.thembelani.vmmigrationsimulation.util.RandomNumber;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemoryPageServiceImpl implements MemoryPageService {
    private MemoryPageRepository memoryPageRepository;

    public MemoryPageServiceImpl(MemoryPageRepository memoryPageRepository) {
        this.memoryPageRepository = memoryPageRepository;
    }

    @Override
    public MemoryPage save(MemoryPage memoryPage) {
        return this.memoryPageRepository.save(memoryPage);
    }

    @Override
    public List<MemoryPage> findPagesWithAGivenProbabilityLimitAndStatus(double limit, MigrationStatus status, int pageSize) {
        Pageable pageable = PageRequest.of(0,pageSize);
        return this.memoryPageRepository.findAllByProbabilityOfModificationLessThanEqualAndStatus(limit,status,pageable);
    }

    @Override
    public List<MemoryPage> findAllPagesPerStatus(MigrationStatus modifiedAfterTransfer) {
        return this.memoryPageRepository.findAllByStatus(modifiedAfterTransfer);
    }

    @Override
    public Page<MemoryPage> findAll(Pageable pageable) {
        return memoryPageRepository.findAll(pageable);
    }

    @Override
    public MemoryPage findFirstMemoryPage() {
        return memoryPageRepository.findFirstByOrderByIdAsc();
    }

    @Override
    public MemoryPage findLastMemoryPage() {
        return memoryPageRepository.findFirstByOrderByIdDesc();
    }

    @Override
    public List<MemoryPage> modifyMemoryPages(int lowestId, int highestId) {
        List <MemoryPage> modifiedAfterTransfer = new ArrayList<>();
        double randomLimit = RandomNumber.randomBetweenLimits(lowestId,highestId);
        log.info("***************** lowest ID {}, highestId {}",lowestId,highestId);
        log.info("***************** Random Between ids {}",randomLimit);
        double randomDivisor = RandomNumber.randomBetweenLimits(1,(highestId-lowestId));
        log.info("***************** Random Divisor {}",randomDivisor);
        int limit = (int) Math.round(randomLimit/randomDivisor);

        log.info("***************** calculated loop limit {}",limit);

        Page<MemoryPage> randomMemoryPage = findAll(PageRequest.of(0,limit));
        randomMemoryPage.stream().parallel().forEach( memoryPage -> {
            if(memoryPage.getStatus().equals(MigrationStatus.SUCCESSFUL)) {
                modifiedAfterTransfer.add(memoryPage);
                memoryPage.setModified(true);
                memoryPage.setStatus(MigrationStatus.MODIFIED_AFTER_TRANSFER);
                save(memoryPage);
            }
                }
        );

  log.info(">>>>>>> TOTAL DIRTY PAGES {}",modifiedAfterTransfer.size());
        return modifiedAfterTransfer;
    }
}
