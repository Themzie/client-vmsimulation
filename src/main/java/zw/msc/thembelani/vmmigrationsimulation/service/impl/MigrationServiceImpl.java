package zw.msc.thembelani.vmmigrationsimulation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.vm.VM;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import zw.msc.thembelani.vmmigrationsimulation.model.MemoryPage;
import zw.msc.thembelani.vmmigrationsimulation.service.MemoryPageService;
import zw.msc.thembelani.vmmigrationsimulation.service.MigrationService;
import zw.msc.thembelani.vmmigrationsimulation.util.MigrationStatus;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MigrationServiceImpl implements MigrationService {
    private final MemoryPageService memoryPageService;
    private Environment environment;


    public MigrationServiceImpl(MemoryPageService memoryPageService, Environment environment) {
        this.memoryPageService = memoryPageService;
        this.environment = environment;
    }

    @Override
    public List<MemoryPage> filterFormMigration(double limit, MigrationStatus migrationStatus, int pageSize) {
        log.info("------------------------Probability of Modification set : {} -------------------------",limit);
        log.info("------------------------Page Size : {} -------------------------",pageSize);
        return this.memoryPageService.findPagesWithAGivenProbabilityLimitAndStatus(limit,migrationStatus, pageSize);

    }

    @Override
    public void transfer(List<MemoryPage> memoryPagesList) {
        String host = environment.getRequiredProperty("server.host");
        String port = environment.getRequiredProperty("migration.server.port");
        log.info("Connected to server {} , port {}",host,port);
        Instant start = Instant.now();
            try (Socket socket = new Socket(host, Integer.parseInt(port));){

                ObjectOutputStream outputStream ;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                long totalMemorySent = memoryPagesList.stream().mapToLong(memoryPageItem -> VM.current().sizeOf(memoryPageItem)).sum();
                log.info("-----------Total Memory to be sent {} bytes",totalMemorySent);
                log.info(" -------------Number of Pages to be attempted to send -------------- {} " , memoryPagesList.size());
                ObjectOutputStream finalOutputStream = outputStream;
                log.info("Started migration  >>>>>");
                memoryPagesList.forEach(memoryPageItem -> {

                            try {
                                finalOutputStream.writeObject(memoryPageItem);
                                //update page
                                if(memoryPageItem.getStatus().equals(MigrationStatus.DEFAULT)) memoryPageItem.setStatus(MigrationStatus.TRANSFERED);
                                if(memoryPageItem.getStatus().equals(MigrationStatus.MODIFIED)) memoryPageItem.setStatus(MigrationStatus.TRANSFERED_AFTER_MODIFICATION);

                                memoryPageItem.setRound(memoryPageItem.getRound()+1);
                                MemoryPage savedMemoryPage = this.memoryPageService.save(memoryPageItem);
                                //log.info("Memory sent to server {}",savedMemoryPage.toString());


                            } catch (IOException e) {
                                log.info("An error occured {}" ,e);
                            }
                        });

                log.info("TOTAL number of pages sent {}",memoryPagesList.size());
                log.info("Total Data sent {} bytes", VM.current().sizeOf(memoryPagesList));
                log.info("-------->>>>>>>>--------Dirty Pages {} :",this.memoryPageService.findAllPagesPerStatus(MigrationStatus.MODIFIED_AFTER_TRANSFER).size());
                Instant end = Instant.now();
                //log.info("Total Time : {} in Milliseconds ", Duration.between(start,end).toMillis() );

            } catch (IOException se) {
                log.info("An error occured {}" ,se);

            }

    }
}
