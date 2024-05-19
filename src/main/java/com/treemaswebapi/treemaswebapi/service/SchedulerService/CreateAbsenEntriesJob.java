package com.treemaswebapi.treemaswebapi.service.SchedulerService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.entity.ReimburseEntity.ReimburseAppEntity;
import com.treemaswebapi.treemaswebapi.entity.TimesheetEntity.TimesheetEntity;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.ReimburseAppRepository;
import com.treemaswebapi.treemaswebapi.repository.TimesheetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateAbsenEntriesJob implements Job {
    private final KaryawanRepository karyawanRepository;
    private final AbsenRepository absenRepository;
    private final Logger logger = LoggerFactory.getLogger(CreateAbsenEntriesJob.class);
    private final ReimburseAppRepository reimburseAppRepository;
    private final TimesheetRepository timesheetRepository;
    private final CutiRepository cutiRepository;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime jobStartTimestamp = LocalDateTime.now();
        logger.info("Job execution started at: {}", jobStartTimestamp);

        try {
            List<KaryawanEntity> dataKaryawan = karyawanRepository.findAll();
            LocalDate currentDate = LocalDate.now();
            logger.info("Number of employees: {}", dataKaryawan.size());

            for (KaryawanEntity karyawan : dataKaryawan) {
                boolean entryExists = absenRepository.existsByNikAndTglAbsen(karyawan.getNik(), currentDate);
                Timestamp dtmSekarang = Timestamp.valueOf(LocalDateTime.now());
                String nikKaryawan = karyawan.getNik();
                Optional<CutiEntity> dataCuti = cutiRepository.findByNik(nikKaryawan);
                if (entryExists) {
                    logger.info("Absen entry already exists for nik: {}", karyawan.getNik());
                } else {
                    AbsenEntity absenEntity = new AbsenEntity();
                    absenEntity.setNik(karyawan.getNik());
                    absenEntity.setNama(karyawan.getNama());
                    absenEntity.setDtmCrt(dtmSekarang);
                    absenEntity.setTglAbsen(currentDate);
                    if (dataCuti.get().getTglMulai().equals(currentDate)) {
                        if(dataCuti.get().getFlgKet().equals("cuti")){
                            absenEntity.setIsCuti("1");
                        }else if (dataCuti.get().getFlgKet().equals("sakit")) {
                            absenEntity.setIsSakit("1");
                        }
                    }
                    absenRepository.save(absenEntity);                   

                    TimesheetEntity timesheetEntity = new TimesheetEntity();
                    timesheetEntity.setTglMsk(currentDate);
                    timesheetEntity.setNik(karyawan.getNik());
                    timesheetEntity.setNama(karyawan.getNama());
                    timesheetEntity.setDtmCrt(dtmSekarang);
                    if (dataCuti.get().getTglMulai().equals(currentDate)) {
                        if(dataCuti.get().getFlgKet().equals("cuti")){
                            timesheetEntity.setFlgKet("cuti");;
                        }else if (dataCuti.get().getFlgKet().equals("sakit")) {
                            timesheetEntity.setFlgKet("sakit");
                        }
                    }
                    timesheetRepository.save(timesheetEntity);

                    ReimburseAppEntity reimburseAppEntity = new ReimburseAppEntity();
                    reimburseAppEntity.setTglAbsen(currentDate);
                    reimburseAppEntity.setNik(karyawan.getNik());
                    reimburseAppEntity.setNama(karyawan.getNama());
                    reimburseAppEntity.setDtmUpd(dtmSekarang);
                    reimburseAppEntity.setGpsLatitudeMsk(0D);
                    reimburseAppEntity.setGpsLongitudeMsk(0D);
                    reimburseAppEntity.setGpsLatitudePlg(0D);
                    reimburseAppEntity.setGpsLongitudePlg(0D);
                    if (dataCuti.get().getTglMulai().equals(currentDate)) {
                        if(dataCuti.get().getFlgKet().equals("cuti")){
                            reimburseAppEntity.setIsCuti("1");
                        }else if (dataCuti.get().getFlgKet().equals("sakit")) {
                            reimburseAppEntity.setIsSakit("1");
                        }
                    }
                    reimburseAppRepository.save(reimburseAppEntity);
                    
                    logger.info("Scheduled job has been executed for nik: {}", karyawan.getNik());
                }
            }
        } catch (Exception e) {
            logger.error("An error occurred during job execution", e);
            // Handle the exception appropriately, e.g., notify, retry, etc.
        }

        LocalDateTime jobEndTimestamp = LocalDateTime.now();
        logger.info("Job execution completed at: {}", jobEndTimestamp);
        Duration executionTime = Duration.between(jobStartTimestamp, jobEndTimestamp);
        logger.info("Total execution time: {} seconds", executionTime.getSeconds());
    }
}
