// package com.treemaswebapi.treemaswebapi.service.DetailData.Reimburse;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;

// import com.treemaswebapi.treemaswebapi.entity.ReimburseEntity.ReimburseEntity;
// import com.treemaswebapi.treemaswebapi.repository.ReimburseRepository;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class ReimburseService {
    
//     private final ReimburseRepository reimburseRepository;

//     public ResponseEntity<Map<String, Object>> reimburseGet(String nik, String nama, Date tanggal, String project, BigDecimal totalJam, boolean isDataMember) {
//         try {
//             List<ReimburseEntity> reimburseList;

//             if (isDataMember) {
//                 // If it's for data member, filter based on provided parameters
//                 reimburseList = reimburseRepository.findByNikAndNamaAndTanggalAndProjectIdAndTotalJamKerja(nik, nama, tanggal, project, totalJam);
//             } else {
//                 // If it's for all members, ignore individual parameters
//                 reimburseList = reimburseRepository.findAll();
//             }

//             List<Map<String, Object>> responseData = new ArrayList<>();

//             for (ReimburseEntity reimburse : reimburseList) {
//                 Map<String, Object> reimburseData = new HashMap<>();
//                 reimburseData.put("nik", reimburse.getNik());
//                 reimburseData.put("namaKaryawan", reimburse.getNama());
//                 reimburseData.put("tanggal", reimburse.getTanggal());
//                 reimburseData.put("project", reimburse.getProjectId());
//                 reimburseData.put("totalJamKerja", reimburse.getTotalJamKerja());
//                 reimburseData.put("transport", reimburse.getTransport());
//                 reimburseData.put("uangMakan", reimburse.getUangMakan());
//                 reimburseData.put("status", reimburse.getStatus());
//                 reimburseData.put("noteApp", reimburse.getNoteApp());

//                 responseData.add(reimburseData);
//             }

//             Map<String, Object> response = new HashMap<>();
//             response.put("status", "Success");
//             response.put("message", "Retrieved");
//             response.put("data", responseData);

//             return ResponseEntity.status(HttpStatus.OK).body(response);
//         } catch (Exception e) {
//             Map<String, Object> response = new HashMap<>();
//             response.put("status", "Failed");
//             response.put("message", "Failed to retrieve reimbursement data");
//             response.put("error", e.getMessage());
//             System.out.println(e);
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//         }
//     }
// }
