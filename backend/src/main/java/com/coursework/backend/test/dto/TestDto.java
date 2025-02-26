package com.coursework.backend.test.dto;


import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestDto {
    String id;
    String name;
    Integer points;
//    Long groupId;
//    Long folderId;
}

