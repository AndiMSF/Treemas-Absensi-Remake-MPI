package com.treemaswebapi.treemaswebapi.service.DetailData.CutiSakit;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.treemaswebapi.treemaswebapi.config.JwtService;
import com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request.CutiApprove;
import com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request.CutiRequest;
import com.treemaswebapi.treemaswebapi.controller.DetailData.CutiSakit.request.SakitRequest;
import com.treemaswebapi.treemaswebapi.entity.AbsenEntity.AbsenEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiAppEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiImageAppEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiImageEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.CutiPenggantiEntity;
import com.treemaswebapi.treemaswebapi.entity.CutiEntity.MasterCutiEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.repository.AbsenRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiAppRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiImageAppRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiImageRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiPenggantiRepository;
import com.treemaswebapi.treemaswebapi.repository.CutiRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;
import com.treemaswebapi.treemaswebapi.repository.LiburRepository;
import com.treemaswebapi.treemaswebapi.repository.MasterCutiRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CutiSakitService {
    
    private final CutiRepository cutiRepository;
    private final KaryawanRepository karyawanRepository;
    private final JwtService jwtService;
    private final CutiAppRepository cutiAppRepository;
    private final MasterCutiRepository masterCutiRepository;
    private final CutiImageRepository cutiImageRepository;
    private final CutiImageAppRepository cutiImageAppRepository;
    private final AbsenRepository absenRepository;
    private final CutiPenggantiRepository cutiPenggantiRepository;
    private final LiburRepository liburRepository;
    
     private static String getIndonesianDayOfWeek(DayOfWeek dayOfWeek){
        Map<String,String> indonesianDayMap = new HashMap<>();
        indonesianDayMap.put("MONDAY", "Senin");
        indonesianDayMap.put("TUESDAY", "Selasa");
        indonesianDayMap.put("WEDNESDAY", "Rabu");
        indonesianDayMap.put("THURSDAY", "Kamis");
        indonesianDayMap.put("FRIDAY", "Jumat");
        indonesianDayMap.put("SATURDAY", "Sabtu");
        indonesianDayMap.put("SUNDAY", "Minggu");

        return indonesianDayMap.get(dayOfWeek.toString());
    }

    //  ini fungsinya buat ngambil data cuti semua orang yang ngajuin cuti
    public ResponseEntity<Map<String, Object>> cutiGet() {
        
        try {
            // Disetujui atau ditolak
            List<CutiEntity> cutiList  = cutiRepository.findAllByFlgKet("cuti");
            List<Map<String, Object>> responseData = new ArrayList<>();
            // Format dtmapp to YYYY-MM-DD
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            for (CutiEntity cuti : cutiList) {
                Map<String, Object> cutiData = new HashMap<>();
                Date dtmAppDate = new Date(cuti.getDtmApp().getTime());
                String formattedDtmApp = dateFormat.format(dtmAppDate);
                cutiData.put("nik", cuti.getNik());
                cutiData.put("namaKaryawan", cuti.getNama());
                cutiData.put("tglMulai", cuti.getTglMulai());
                cutiData.put("tglSelesai", cuti.getTglSelesai());
                cutiData.put("tglMasuk", cuti.getTglKembaliKerja());
                cutiData.put("jmlCuti", cuti.getJmlCuti());
                cutiData.put("jmlCutiBersama", cuti.getJmlCutiBersama());
                cutiData.put("jmlCutiKhusus", cuti.getJmlCutiKhusus());
                cutiData.put("keperluanCuti", cuti.getKeperluanCuti());
                cutiData.put("status", cuti.getIsApproved());
                cutiData.put("noteApp", cuti.getNoteApp());
                cutiData.put("usrapp", cuti.getUsrApp());
                cutiData.put("dtmapp", formattedDtmApp);
                cutiData.put("isApproved", cuti.getIsApproved());
        
                responseData.add(cutiData);
            }

            // Menunggu
            List<CutiAppEntity> cutiAppList  = cutiAppRepository.findByFlgKetAndIsApprovedIsNull("cuti");
            List<Map<String, Object>> responseDataApp = new ArrayList<>();
            
            for (CutiAppEntity cutiApp : cutiAppList) {
                Map<String, Object> cutiDataApp = new HashMap<>();
                cutiDataApp.put("nik", cutiApp.getNik());
                cutiDataApp.put("namaKaryawan", cutiApp.getNama());
                cutiDataApp.put("tglMulai", cutiApp.getTglMulai());
                cutiDataApp.put("tglSelesai", cutiApp.getTglSelesai());
                cutiDataApp.put("tglMasuk", cutiApp.getTglKembaliKerja());
                cutiDataApp.put("jmlCuti", cutiApp.getJmlCuti());
                cutiDataApp.put("jmlCutiBersama", cutiApp.getJmlCutiBersama());
                cutiDataApp.put("jmlCutiKhusus", cutiApp.getJmlCutiKhusus());
                cutiDataApp.put("keperluanCuti", cutiApp.getKeperluanCuti());
                cutiDataApp.put("status", cutiApp.getIsApproved());
                cutiDataApp.put("noteApp", cutiApp.getNoteApp());
                cutiDataApp.put("usrapp", cutiApp.getUsrApp());
                cutiDataApp.put("dtmapp", cutiApp.getDtmApp());
                cutiDataApp.put("isApproved", cutiApp.getIsApproved());
                
                responseDataApp.add(cutiDataApp);
            }

            Map<String, Object> dataApporveOrNoOrWait = new HashMap<>();
            dataApporveOrNoOrWait.put("setujuAtauTolak", responseData);
            dataApporveOrNoOrWait.put("menunggu", responseDataApp);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", dataApporveOrNoOrWait);

           
            
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve cuti");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> sakitGet() {
        try {
            // Disetujui atau ditolak
            List<CutiEntity> sakitList  = cutiRepository.findAllByFlgKet("sakit");
            List<Map<String, Object>> responseData = new ArrayList<>();

            for (CutiEntity sakit : sakitList) {
                Map<String, Object> cutiData = new HashMap<>();
                cutiData.put("nik", sakit.getNik());
                cutiData.put("namaKaryawan", sakit.getNama());
                cutiData.put("tglMulai", sakit.getTglMulai());
                cutiData.put("tglSelesai", sakit.getTglSelesai());
                cutiData.put("tglMasuk", sakit.getTglKembaliKerja());
                cutiData.put("jmlCuti", sakit.getJmlCuti());
                cutiData.put("jmlCutiBersama", sakit.getJmlCutiBersama());
                cutiData.put("jmlCutiKhusus", sakit.getJmlCutiKhusus());
                cutiData.put("keperluanCuti", sakit.getKeperluanCuti());
                cutiData.put("status", sakit.getIsApproved());
                cutiData.put("noteApp", sakit.getNoteApp());
                cutiData.put("usrapp", sakit.getUsrApp());
                cutiData.put("dtmapp", sakit.getDtmApp());
                cutiData.put("isApproved", sakit.getIsApproved());
        
                responseData.add(cutiData);
            }

            // Menunggu
            List<CutiAppEntity> sakitAppList  = cutiAppRepository.findByFlgKet("sakit");
            List<Map<String, Object>> responseDataApp = new ArrayList<>();

            for (CutiAppEntity sakitApp : sakitAppList) {
                Map<String, Object> sakitDataApp = new HashMap<>();
                sakitDataApp.put("nik", sakitApp.getNik());
                sakitDataApp.put("namaKaryawan", sakitApp.getNama());
                sakitDataApp.put("tglMulai", sakitApp.getTglMulai());
                sakitDataApp.put("tglSelesai", sakitApp.getTglSelesai());
                sakitDataApp.put("tglMasuk", sakitApp.getTglKembaliKerja());
                sakitDataApp.put("jmlCuti", sakitApp.getJmlCuti());
                sakitDataApp.put("jmlCutiBersama", sakitApp.getJmlCutiBersama());
                sakitDataApp.put("jmlCutiKhusus", sakitApp.getJmlCutiKhusus());
                sakitDataApp.put("keperluanCuti", sakitApp.getKeperluanCuti());
                sakitDataApp.put("status", sakitApp.getIsApproved());
                sakitDataApp.put("noteApp", sakitApp.getNoteApp());
                sakitDataApp.put("usrapp", sakitApp.getUsrApp());
                sakitDataApp.put("dtmapp", sakitApp.getDtmApp());
                sakitDataApp.put("isApproved", sakitApp.getIsApproved());
                responseDataApp.add(sakitDataApp);
            }

            Map<String, Object> dataApporveOrNoOrWait = new HashMap<>();
            dataApporveOrNoOrWait.put("setujuAtauTolak", responseData);
            dataApporveOrNoOrWait.put("menunggu", responseDataApp);
        
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Retrieved");
            response.put("data", dataApporveOrNoOrWait);
            
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to retrieve cuti");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> userCutiAdd(
        @RequestHeader("Authorization") String jwtToken,
        CutiRequest request
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            MasterCutiEntity jenisCuti = masterCutiRepository.findById(request.getSelectedMasterCutiId())
                .orElseThrow(() -> new RuntimeException("MasterCuti not found for id: " + request.getSelectedMasterCutiId()));

            // Cari hakcuti dari table karyawan dan tempatkan ke tbl cuti di sisa cuti
            BigDecimal hakCuti = karyawanRepository.findByNik(userToken)
                .orElseThrow(() -> new RuntimeException("hakCuti not found"))
                .getHakCuti();

            BigDecimal jmlCuti = request.getJmlCuti();

            // Ke tbl_cuti_app
            var cutiApp = CutiAppEntity.builder()
                .nik(userToken)
                .nama(nama)
                .tglMulai(request.getTglMulai())// value ini ntar si user yang milih
                .tglSelesai(request.getTglSelesai())// value ini ntar si Rizki yang milihin
                .tglKembaliKerja(request.getTglKembaliKerja())// auto rizki
                .keperluanCuti(request.getKeperluanCuti())// si user
                .alamatCuti(request.getAlamatCuti())// si user
                .jmlCutiBersama(request.getJmlCutiBersama())//dari rizki
                .jmlCutiKhusus(request.getJmlCutiKhusus())//dari rizki, yang nentuin tanggal berapa aja yang libur tuh si Andoi
                .flgKet("cuti")
                .dtmCrt(dtmCrt)
                .usrCrt(nama)
                .jmlCuti(jmlCuti)//ambil dari table, bukan dari req
                .jenisCuti(jenisCuti)
                .sisaCuti(hakCuti)
                .build();

            cutiAppRepository.save(cutiApp);

            // kurangin angka di karyawanEntity, yang mana nantinya si SisaCuti tuh auto keganti
            BigDecimal hasilCuti = hakCuti.subtract(jmlCuti);
            KaryawanEntity dataKaryawan = karyawanRepository.findByNik(userToken).get();
            dataKaryawan.setHakCuti(hasilCuti);
            karyawanRepository.save(dataKaryawan);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Cuti Created");
            response.put("data", cutiApp);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Cuti");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> cutiApprove(
        @RequestHeader("Authorization") String jwtToken,
        CutiApprove request,
        Long id
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);

            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            Optional<CutiAppEntity> cutiApp = cutiAppRepository.findById(id);

            if(cutiApp.isPresent()) {
                var cutiAppList = cutiApp.get();
                // Orang Lain
                String nikCutiUser = cutiApp.get().getNik();
                Optional<KaryawanEntity> nikOther = karyawanRepository.findByNik(nikCutiUser);

                BigDecimal hakCuti = karyawanRepository.findByNik(nikCutiUser)
                .orElseThrow(() -> new RuntimeException("LEAVE not found"))
                .getHakCuti();

                long currentTimeMillis = System.currentTimeMillis();
                Timestamp dtmApp = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

                MasterCutiEntity jenisCuti = cutiAppList.getJenisCuti(); // Retrieve from CutiAppEntity

                CutiEntity cutiApproved = new CutiEntity(); // buat ngestore ke CutiEntity
                cutiApproved.setUsrApp(nama);
                cutiApproved.setDtmApp(dtmApp);
                cutiApproved.setNoteApp(request.getNoteApp());
                cutiApproved.setIsApproved(request.getIsApproved());
                cutiApproved.setNik(cutiAppList.getNik());
                cutiApproved.setNama(cutiAppList.getNama());
                cutiApproved.setTglMulai(cutiAppList.getTglMulai());
                cutiApproved.setTglSelesai(cutiAppList.getTglSelesai());
                cutiApproved.setTglKembaliKerja(cutiAppList.getTglKembaliKerja());
                cutiApproved.setJmlCuti(cutiAppList.getJmlCuti());
                cutiApproved.setKeperluanCuti(cutiAppList.getKeperluanCuti());
                cutiApproved.setAlamatCuti(cutiAppList.getAlamatCuti());
                cutiApproved.setFlgKet(cutiAppList.getFlgKet());
                cutiApproved.setDtmCrt(cutiAppList.getDtmCrt());
                cutiApproved.setUsrCrt(cutiAppList.getUsrCrt());
                cutiApproved.setMasterCutiEntity(jenisCuti);
                cutiApproved.setSisaCuti(cutiAppList.getSisaCuti());
                cutiApproved.setFlagApp("cuti");
                // If isApproved is "1", decrement hakCuti by 1
                String isApproved = request.getIsApproved(); //kalo diapprove harusnya ngurangin cuti pengganti dulu baru ke hak_cuti
                if ("1".equals(isApproved)) {
                    BigDecimal decrementValue = request.getJumlahCuti();
                    BigDecimal updatedHakCuti = hakCuti.subtract(decrementValue);
                    nikOther.get().setHakCuti(updatedHakCuti);
                    karyawanRepository.save(nikOther.get());
                    cutiApproved.setSisaCuti(updatedHakCuti);
                } else {
                    cutiRepository.save(cutiApproved);
                    cutiAppRepository.delete(cutiAppList);
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "Success");
                    response.put("message", "Cuti not Approved");
                    response.put("data", cutiApproved);

                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                }

                cutiRepository.save(cutiApproved);
                cutiAppRepository.delete(cutiAppList);
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Cuti Approved");
                response.put("data", cutiApproved);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Cuti Not Found");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Approve Cuti");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> userSakitAdd(
        @RequestHeader("Authorization") String jwtToken,
        SakitRequest request
    ) {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp dtmCrt = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));

            // Ke tbl_cuti_app
            var sakitApp = CutiAppEntity.builder()
                .nik(userToken)
                .nama(nama)
                .tglMulai(request.getTglMulai())
                .tglSelesai(request.getTglSelesai())
                .tglKembaliKerja(request.getTglKembaliKerja())
                .keperluanCuti(request.getKeperluanCuti())
                .flgKet("sakit")
                .dtmCrt(dtmCrt)
                .usrCrt(nama)
                .jmlCuti(request.getJmlCuti())
            .build();

            cutiAppRepository.save(sakitApp);
            
            Long sakitAppId = sakitApp.getId();
            // image sakit ke tbl cuti_image
            var sakitAppImage = CutiImageAppEntity.builder()
                .id(sakitAppId)
                .nik(userToken)
                .tglMulai(request.getTglMulai())
                .flgKet("sakit")
                .image(request.getImage() != null ? request.getImage() : null)
                .usrCrt(nama)
                .dtmCrt(dtmCrt)
            .build();

            cutiImageAppRepository.save(sakitAppImage);

            Map<String, Object> data = new HashMap<>();
            data.put("dataSakit", sakitApp);
            data.put("dataSakitImage", sakitAppImage);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Sakit Created");
            response.put("data", data);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Create Sakit");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> sakitApprove(
        @RequestHeader("Authorization") String jwtToken,
        CutiApprove request,
        Long id
    ) {
        try {
            
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);

            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            // Cari data sakit di table cutiApp
            Optional<CutiAppEntity> sakitApp = cutiAppRepository.findById(id);

            if(sakitApp.isPresent()) {
                var sakitAppList = sakitApp.get();
                long currentTimeMillis = System.currentTimeMillis();
                Timestamp dtmApp = new Timestamp(currentTimeMillis - (currentTimeMillis % 1000));
                var nikOtherUser = sakitApp.get().getNik();
                // Cari data imageApp di table imageApp
                Optional<CutiImageAppEntity> image = cutiImageAppRepository.findById(id);
                var imageDataReal = image.get();
                // Tbl Cuti entity
                CutiEntity sakitApproved = new CutiEntity();
                sakitApproved.setUsrApp(nama);
                sakitApproved.setDtmApp(dtmApp);
                sakitApproved.setNoteApp(request.getNoteApp());
                sakitApproved.setIsApproved(request.getIsApproved());
                sakitApproved.setNik(sakitAppList.getNik());
                sakitApproved.setNama(sakitAppList.getNama());
                sakitApproved.setTglMulai(sakitAppList.getTglMulai());
                sakitApproved.setTglSelesai(sakitAppList.getTglSelesai());
                sakitApproved.setTglKembaliKerja(sakitAppList.getTglKembaliKerja());
                sakitApproved.setJmlCuti(sakitAppList.getJmlCuti());
                sakitApproved.setKeperluanCuti(sakitAppList.getKeperluanCuti());
                sakitApproved.setFlgKet(sakitAppList.getFlgKet());
                sakitApproved.setDtmCrt(sakitAppList.getDtmCrt());
                sakitApproved.setUsrCrt(sakitAppList.getUsrCrt());
                sakitApproved.setFlagApp("sakit");

                String isApproved = request.getIsApproved();
                if("0".equals(isApproved)) {
                    cutiRepository.save(sakitApproved);
                    cutiAppRepository.delete(sakitAppList);
                    cutiImageAppRepository.delete(imageDataReal);
                    // delete cuti image app 
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "Success");
                    response.put("message", "Sakit not Approved");
                    response.put("data", sakitApproved);

                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                }
                // Pindahkan image dari app ke image
                // Tbl Cuti Image
                CutiImageEntity cutiApproved = new CutiImageEntity();
                cutiApproved.setNik(imageDataReal.getNik());
                cutiApproved.setTglMulai(imageDataReal.getTglMulai());
                cutiApproved.setFlgKet("sakit");
                cutiApproved.setImage(imageDataReal.getImage());
                cutiApproved.setUsrCrt(imageDataReal.getUsrCrt());
                cutiApproved.setDtmCrt(imageDataReal.getDtmCrt());
                cutiApproved.setUsrApp(nama);
                cutiApproved.setDtmapp(dtmApp);

                // Sakit Kirim isSakit = 1, set nik, nama, hari, dtmcrt, usrcrt ke tbl_absen
                
                AbsenEntity absenData = new AbsenEntity();
                absenData.setNik(cutiApproved.getNik());
                absenData.setNama(sakitAppList.getNama());
                absenData.setHari(getIndonesianDayOfWeek(LocalDate.now().getDayOfWeek()));
                absenData.setDtmCrt(imageDataReal.getDtmCrt());
                absenData.setUsrCrt(imageDataReal.getUsrCrt());
                absenData.setIsSakit("1");//gue yang nambahin ini as your request -Aliy

                // kirim image ke tbl cuti entity untuk api get sakit

                cutiImageRepository.save(cutiApproved);
                absenRepository.save(absenData);
                cutiImageAppRepository.delete(imageDataReal);
                cutiRepository.save(sakitApproved);
                cutiAppRepository.delete(sakitAppList);
                

                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Sakit Approved");
                response.put("data", sakitApproved);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Sakit Not Found");

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Approve Sakit");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getMyCuti(@RequestHeader("Authorization") String tokenWithBearer) {
        try {
            // Cari siapa yang akses api ini
            String token = tokenWithBearer.substring(7);
            String nik = jwtService.extractUsername(token);
            Optional<KaryawanEntity> userOptional = karyawanRepository.findByNik(nik);
            if (!userOptional.isPresent()) {
                // Handle case where user is not found
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User with NIK " + nik + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Optional<KaryawanEntity> user = karyawanRepository.findByNik(nik);
            String nama = user.get().getNama();

            //cari data sisaCuti
            Optional<KaryawanEntity> dataKaryawan = karyawanRepository.findByNik(nik);
            BigDecimal sisaCuti = dataKaryawan.get().getHakCuti(); 
            Long jmlCutiPengganti = cutiPenggantiRepository.countJmlCutiPerNik(nik);
            jmlCutiPengganti = jmlCutiPengganti != null ? jmlCutiPengganti : 0; 
            Long jmlCutiBersama = liburRepository.countIsCutiBersama();

            Map<String, Object> data = new HashMap<>();
            data.put("namaKaryawan", nama);
            data.put("sisaCuti", sisaCuti);
            data.put("sisaCutiPengganti", jmlCutiPengganti);
            data.put("jumlahCutiBersama", jmlCutiBersama);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "berhasil mendapatkan jumlah semua jenis cuti");
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Approve Sakit");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
}
