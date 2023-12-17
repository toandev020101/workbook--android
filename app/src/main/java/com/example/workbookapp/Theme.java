package com.example.workbookapp;

import java.util.Date;
import java.util.Objects;

public class Theme {
    private int id;
    private String color;
    private Date createdAt;
    private Date updatedAt;

    public Theme() {
    }

    public Theme(int id, String color, Date createdAt, Date updatedAt) {
        this.id = id;
        this.color = color;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Theme other = (Theme) obj;
        return id == other.id && color.equals(other.color); // So sánh theo các trường dữ liệu cụ thể của Theme
    }

    // Ghi đè phương thức hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, color); // Sử dụng các trường dữ liệu cụ thể để tạo hashCode
    }
}
