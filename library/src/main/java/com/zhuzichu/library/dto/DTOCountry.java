package com.zhuzichu.library.dto;

import java.io.Serializable;

import me.yokeyword.indexablerv.IndexableEntity;

public class DTOCountry implements IndexableEntity,Serializable {

    /**
     * label : 中国
     * code : 86
     * ishot : true
     */

    private String label;
    private String code;
    private boolean ishot;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIshot() {
        return ishot;
    }

    public void setIshot(boolean ishot) {
        this.ishot = ishot;
    }

    @Override
    public String getFieldIndexBy() {
        return label;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.label = indexField; // 同上
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }
}
