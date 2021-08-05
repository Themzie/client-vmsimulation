package zw.msc.thembelani.vmmigrationsimulation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import zw.msc.thembelani.vmmigrationsimulation.model.MemoryPage;
import zw.msc.thembelani.vmmigrationsimulation.service.DatabasePopulatorService;
import zw.msc.thembelani.vmmigrationsimulation.service.MemoryPageService;
import zw.msc.thembelani.vmmigrationsimulation.service.MigrationService;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;

import java.util.Date;
import java.util.List;

@SpringBootApplication
@Slf4j
public class VmMigrationSimulationApplication {

    public static void main(String[] args) {


        ApplicationContext ctx = SpringApplication.run(VmMigrationSimulationApplication.class, args);
        DatabasePopulatorService databasePopulatorService =  ctx.getBean(DatabasePopulatorService.class);
        MemoryPageService memoryPageService = ctx.getBean(MemoryPageService.class);
        //databasePopulatorService.defaultData(20000);
        MigrationService migrationService =  ctx.getBean(MigrationService.class);
        log.info("**********************Start {}****************************",new Date(System.currentTimeMillis()));
        List<MemoryPage> memoryPageList =  migrationService.filterFormMigration(0.6, MigrationStatus.DEFAULT, 5000);
        migrationService.transfer(memoryPageList);

        //int totalPagesSent = memoryPageList.size();
        MemoryPage firstMemoryPage = memoryPageService.findFirstMemoryPage();
        MemoryPage lastMemoryPage = memoryPageService.findLastMemoryPage();
        log.info("******************** First {}, Last {}",firstMemoryPage.getId(), lastMemoryPage.getId());

        log.info("********** Modified Pages (Dirty from Main ) {}",memoryPageService.findAllPagesPerStatus(MigrationStatus.MODIFIED_AFTER_TRANSFER).size());
        log.info("********************** End time {}****************************",new Date(System.currentTimeMillis()));
    }

}
