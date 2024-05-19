package com.treemaswebapi.treemaswebapi.controller.MasterData.Announcement.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementRequest {
    private String title;
    private String header;
    private String note;
    private String footer;
    private String image64;
    private String image;
}
