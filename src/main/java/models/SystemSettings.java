package models;

import java.io.Serializable;

public class SystemSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int settingId;
    private String settingKey;
    private String settingValue;
    
    public SystemSettings() {}
    
    public SystemSettings(String settingKey, String settingValue) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }
    
    public SystemSettings(int settingId, String settingKey, String settingValue) {
        this.settingId = settingId;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
    }
    
    // Getters and Setters
    public int getSettingId() { return settingId; }
    public void setSettingId(int settingId) { this.settingId = settingId; }
    
    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }
    
    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
    
    @Override
    public String toString() {
        return "SystemSettings{" +
                "settingId=" + settingId +
                ", settingKey='" + settingKey + '\'' +
                ", settingValue='" + settingValue + '\'' +
                '}';
    }
}
