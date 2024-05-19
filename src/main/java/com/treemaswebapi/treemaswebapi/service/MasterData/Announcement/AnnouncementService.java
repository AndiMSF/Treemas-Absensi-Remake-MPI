package com.treemaswebapi.treemaswebapi.service.MasterData.Announcement;

import java.text.SimpleDateFormat;
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
import com.treemaswebapi.treemaswebapi.controller.MasterData.Announcement.request.AnnouncementRequest;
import com.treemaswebapi.treemaswebapi.entity.AnnouncementEntity.AnnouncementEntity;
import com.treemaswebapi.treemaswebapi.entity.KaryawanEntity.KaryawanEntity;
import com.treemaswebapi.treemaswebapi.repository.AnnouncementRepository;
import com.treemaswebapi.treemaswebapi.repository.KaryawanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
   
    private final AnnouncementRepository announcementRepository;
    private final KaryawanRepository karyawanRepository;
    private final JwtService jwtService;

    public ResponseEntity<Map<String, Object>> announcementAdd(
        // @RequestPart(value = "image", required = false) MultipartFile image,
        AnnouncementRequest request,
        @RequestHeader("Authorization") String jwtToken
    )  {
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();

            // Pisahkan data gambar dari string request.getImage64()
            String imageData = null;

            // Periksa apakah string request.getImage64() dimulai dengan "data:image/"
            if (request.getImage64().startsWith("data:image/")) {
                // Temukan posisi tanda koma pertama
                int commaIndex = request.getImage64().indexOf(",");
                if (commaIndex != -1) {
                    // Pisahkan data setelah koma
                    imageData = request.getImage64().substring(commaIndex + 1);
                }
            } else {
                imageData = "";
            }

            // Kirim ke tbl_announcement
            var announcement = AnnouncementEntity.builder()
                .title(request.getTitle())
                .header(request.getHeader())
                .note(request.getNote())
                .image64(imageData)
                .image(request.getImage())
                .footer(request.getFooter())
                .usrCrt(nama)
            .build();
            announcementRepository.save(announcement);
            Map<String, Object> data = new HashMap<>();
            data.put("user", announcement);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Announcement Created");
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed!");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> announcementGet(){
        try {
            List<AnnouncementEntity> announcements = announcementRepository.findAll(); // Fetch all records from the AnnouncementEntity table
        
            int totalAnnouncements = announcements.size();

            List<Map<String, Object>> formattedAnnouncements = new ArrayList<>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (AnnouncementEntity announcement : announcements) {
                Map<String, Object> formattedAnnouncement = new HashMap<>();
                formattedAnnouncement.put("id", announcement.getId());
                
                Date tglUploadDate = new Date(announcement.getTglUpload().getTime());
                String formattedTglUpload = dateFormat.format(tglUploadDate);
                
                formattedAnnouncement.put("tgl_upload", formattedTglUpload);
                formattedAnnouncement.put("title", announcement.getTitle());
                formattedAnnouncement.put("header", announcement.getHeader());
                formattedAnnouncement.put("note", announcement.getNote());
                formattedAnnouncement.put("footer", announcement.getFooter());
               
                
                // Tambahan properti lain yang ingin Anda tampilkan
                formattedAnnouncement.put("usrCrt", announcement.getUsrCrt());

                formattedAnnouncements.add(formattedAnnouncement);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Announcements Retrieved");
            response.put("jumlah_announcement", totalAnnouncements);
            response.put("data", formattedAnnouncements);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed!");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> announcementGetId(Long id){
        try {
            Optional<AnnouncementEntity> announcements = announcementRepository.findById(id); // Fetch all records from the AnnouncementEntity table
    

            List<Map<String, Object>> formattedAnnouncements = new ArrayList<>();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            if(announcements.isPresent()) {
                AnnouncementEntity announcement = announcements.get();
                Map<String, Object> formattedAnnouncement = new HashMap<>();
                formattedAnnouncement.put("id", announcement.getId());
                
                Date tglUploadDate = new Date(announcement.getTglUpload().getTime());
                String formattedTglUpload = dateFormat.format(tglUploadDate);
                
                formattedAnnouncement.put("tgl_upload", formattedTglUpload);
                formattedAnnouncement.put("title", announcement.getTitle());
                formattedAnnouncement.put("header", announcement.getHeader());
                formattedAnnouncement.put("note", announcement.getNote());
                formattedAnnouncement.put("footer", announcement.getFooter());
                formattedAnnouncement.put("image64", announcement.getImage64());
                formattedAnnouncement.put("image", announcement.getImage());
                
                // Tambahan properti lain yang ingin Anda tampilkan
                formattedAnnouncement.put("usrCrt", announcement.getUsrCrt());

                formattedAnnouncements.add(formattedAnnouncement);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "Success");
            response.put("message", "Announcements Retrieved");
            response.put("data", formattedAnnouncements);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed!");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> announcementUpdate(
        Long id,  
        AnnouncementRequest request,
        @RequestHeader("Authorization") String jwtToken
){
        try {
            // Cari siapa yang akses api ini
            String token = jwtToken.substring(7);
            String userToken = jwtService.extractUsername(token);
            
            Optional<KaryawanEntity> user = karyawanRepository.findByNik(userToken);
            String nama = user.get().getNama();
            Optional<AnnouncementEntity> announcementOptional = announcementRepository.findById(id);
            if (announcementOptional.isPresent()) {
                AnnouncementEntity announcement = announcementOptional.get();
                // Perbarui properti-properti yang sesuai dengan data baru
                announcement.setTitle(request.getTitle());
                announcement.setHeader(request.getHeader());
                announcement.setNote(request.getNote());
                announcement.setFooter(request.getFooter());
                announcement.setUsrCrt(nama);
                
                String imageData = null;

                // Periksa apakah string request.getImage64() dimulai dengan "data:image/"
                if (request.getImage64().startsWith("data:image/")) {
                    // Temukan posisi tanda koma pertama
                    int commaIndex = request.getImage64().indexOf(",");
                    if (commaIndex != -1) {
                        // Pisahkan data setelah koma
                        imageData = request.getImage64().substring(commaIndex + 1);
                    }
                }

                if (imageData != null) {
                    // Sekarang, 'imageData' berisi data gambar dalam format base64
                    // Anda dapat menggunakannya untuk menyimpan atau memproses gambar.

                    announcement.setImage64(imageData);
                }
                announcement.setImage(request.getImage()); 

            announcementRepository.save(announcement);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Announcement updated successfully");
                response.put("data", announcement);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Announcement not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed to update announcement");
            response.put("error", e.getMessage());
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, String>> announcementDelete(
        Long id
    ) {
        try {
            // Cari Announcement berdasarkan ID
            Optional<AnnouncementEntity> announcementOptional = announcementRepository.findById(id);
            if (announcementOptional.isPresent()) {
                announcementRepository.deleteById(id);

                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                response.put("message", "Announcement deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "Failed");
                response.put("message", "Announcement not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "Failed");
            response.put("message", "Failed To Delete Announcement");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
