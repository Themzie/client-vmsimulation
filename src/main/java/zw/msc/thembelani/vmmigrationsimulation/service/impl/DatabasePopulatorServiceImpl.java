package zw.msc.thembelani.vmmigrationsimulation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.msc.thembelani.vmmigrationsimulation.model.MemoryPage;
import zw.msc.thembelani.vmmigrationsimulation.service.DatabasePopulatorService;
import zw.msc.thembelani.vmmigrationsimulation.service.MemoryPageService;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;
import zw.msc.thembelani.vmmigrationsimulation.util.RandomNumber;
import zw.msc.thembelani.vmmigrationsimulation.util.Util;

@Slf4j
@Service(value = "DbPopulator")
public class DatabasePopulatorServiceImpl implements DatabasePopulatorService {
    private final MemoryPageService memoryPageService;

    public DatabasePopulatorServiceImpl(MemoryPageService memoryPageService) {
        this.memoryPageService = memoryPageService;
    }

    @Override
    public void defaultData(int limit) {

        for(int i=0; i<=limit ; i++){
            MemoryPage memoryPage = new MemoryPage();
            memoryPage.setStatus(MigrationStatus.DEFAULT);
            memoryPage.setModified(false);
            memoryPage.setPresent(true);
            memoryPage.setRound(0);
            memoryPage.setData(Util.getSampleData());
            memoryPage.setProbabilityOfModification(RandomNumber.randomBetweenLimits(0,10)/10);

            memoryPageService.save(memoryPage);
            log.info("Saving memory Page");

        }
    }
}
