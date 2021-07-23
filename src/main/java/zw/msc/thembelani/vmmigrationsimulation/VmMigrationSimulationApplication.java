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

import java.util.List;

@SpringBootApplication
@Slf4j
public class VmMigrationSimulationApplication {

    public static void main(String[] args) {


        ApplicationContext ctx = SpringApplication.run(VmMigrationSimulationApplication.class, args);
        DatabasePopulatorService databasePopulatorService =  ctx.getBean(DatabasePopulatorService.class);
        MemoryPageService memoryPageService = ctx.getBean(MemoryPageService.class);
        databasePopulatorService.defaultData(5000);
        MigrationService migrationService =  ctx.getBean(MigrationService.class);
        List<MemoryPage> memoryPageList =  migrationService.filterFormMigration(0.5, MigrationStatus.DEFAULT, 10000);
        migrationService.transfer(memoryPageList);

        //int totalPagesSent = memoryPageList.size();
        MemoryPage firstMemoryPage = memoryPageService.findFirstMemoryPage();
        MemoryPage lastMemoryPage = memoryPageService.findLastMemoryPage();
        log.info("******************** First {}, Last {}",firstMemoryPage.getId(), lastMemoryPage.getId());
        memoryPageService.modifyMemoryPages(firstMemoryPage.getId(),lastMemoryPage.getId());
    }

}
