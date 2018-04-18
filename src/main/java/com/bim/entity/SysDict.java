package com.bim.entity;

import org.hibernate.validator.constraints.NotBlank;

public class SysDict {
    private Integer id;
    
    @NotBlank(message = "{dicCode}")
    private String code;
    
    @NotBlank(message = "{dicParentCode}")
    private String parentCode;

    @NotBlank(message = "{dicName}")
    private String showName;

    private Byte isDelete;

    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    public SysDict(){}

    /**
     * 
     * @param code
     * @param parentCode
     * @param showName
     * @param isDelete
     * @param memo
     */
	public SysDict(Integer id,String code, String parentCode, String showName, Byte isDelete, String memo) {
		super();
		this.code = code;
		this.parentCode = parentCode;
		this.showName = showName;
		this.isDelete = isDelete;
		this.memo = memo;
	}
    
    
}