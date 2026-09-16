package com.uniportal.service;

import com.uniportal.dao.NoticeDAO;
import com.uniportal.model.Notice;
import java.util.List;

public class NoticeService {
    private NoticeDAO noticeDAO;

    public NoticeService() {
        this.noticeDAO = new NoticeDAO();
    }

    public List<Notice> getAllNotices() {
        return noticeDAO.getAllNotices();
    }

    public List<Notice> getPublishedNotices(Integer deptId) {
        return noticeDAO.getPublishedNotices(deptId);
    }

    public void addNotice(Notice notice) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(notice.getTitle(), "Title");
        com.uniportal.util.ValidationUtil.requireNonEmpty(notice.getDescription(), "Description");
        if (notice.getPublishDate() == null) {
            throw new IllegalArgumentException("Publish Date cannot be empty.");
        }
        if (!noticeDAO.addNotice(notice)) {
            throw new RuntimeException("Failed to add notice.");
        }
    }

    public void updateNotice(Notice notice) {
        com.uniportal.util.ValidationUtil.requireNonEmpty(notice.getTitle(), "Title");
        com.uniportal.util.ValidationUtil.requireNonEmpty(notice.getDescription(), "Description");
        if (notice.getPublishDate() == null) {
            throw new IllegalArgumentException("Publish Date cannot be empty.");
        }
        if (!noticeDAO.updateNotice(notice)) {
            throw new RuntimeException("Failed to update notice.");
        }
    }

    public void deleteNotice(int id) {
        if (!noticeDAO.deleteNotice(id)) {
            throw new RuntimeException("Failed to delete notice.");
        }
    }
}
