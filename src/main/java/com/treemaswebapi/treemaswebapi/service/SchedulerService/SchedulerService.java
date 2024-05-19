// package com.treemaswebapi.treemaswebapi.service.SchedulerService;

// import java.sql.Timestamp;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.List;

// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;

// import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
// import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
// import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
// import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;

// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// public class SchedulerService {
//     private final KaryawanRepository karyawanRepository;
//     private final AbsenRepository absenRepository;

//     @Scheduled(cron = "0 25 10 * * ?")
//     public void createAbsenEntries() {
//         List<KaryawanEntity> dataKaryawan = karyawanRepository.findAll();
//         LocalDate currentDate = LocalDate.now();
//         System.out.println("jumlah karyawannya segini: "+dataKaryawan.size());

//         for (KaryawanEntity karyawan : dataKaryawan) {

//             boolean entryExists = absenRepository.existsByNikAndTglAbsen(karyawan.getNik(), currentDate);

//             // Check if an entry already exists for the same nik and date
//             if (entryExists) {
//                 System.out.println("Absen entry already exists for nik: " + karyawan.getNik());
//             } else {
//                 AbsenEntity absenEntity = new AbsenEntity();
//                 absenEntity.setNik(karyawan.getNik());
//                 absenEntity.setDtmCrt(Timestamp.valueOf(LocalDateTime.now()));
//                 absenEntity.setTglAbsen(currentDate);
//                 absenRepository.save(absenEntity);
//                 System.out.println("Scheduled job has been executed for nik: " + karyawan.getNik());
//             }
//         }
//     }
// }