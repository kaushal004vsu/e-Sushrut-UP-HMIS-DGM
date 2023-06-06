package com.cdac.uphmis.opdLite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiagnosisDetails implements  Diagnosis{

    @SerializedName("hierachy")
    @Expose
    private String hierachy;
    @SerializedName("isPreferredTerm")
    @Expose
    private String isPreferredTerm;
    @SerializedName("conceptState")
    @Expose
    private String conceptState;
    @SerializedName("conceptFsn")
    @Expose
    private String conceptFsn;
    @SerializedName("definitionStatus")
    @Expose
    private String definitionStatus;
    @SerializedName("conceptId")
    @Expose
    private String conceptId;
    @SerializedName("typeId")
    @Expose
    private String typeId;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("activeStatus")
    @Expose
    private Integer activeStatus;
    @SerializedName("moduleId")
    @Expose
    private String moduleId;

    public String getHierachy() {
        return hierachy;
    }

    public void setHierachy(String hierachy) {
        this.hierachy = hierachy;
    }

    public String getIsPreferredTerm() {
        return isPreferredTerm;
    }

    public void setIsPreferredTerm(String isPreferredTerm) {
        this.isPreferredTerm = isPreferredTerm;
    }

    public String getConceptState() {
        return conceptState;
    }

    public void setConceptState(String conceptState) {
        this.conceptState = conceptState;
    }

    public String getConceptFsn() {
        return conceptFsn;
    }

    public void setConceptFsn(String conceptFsn) {
        this.conceptFsn = conceptFsn;
    }

    public String getDefinitionStatus() {
        return definitionStatus;
    }

    public void setDefinitionStatus(String definitionStatus) {
        this.definitionStatus = definitionStatus;
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }


    @Override
    public String toString() {
        return term;
    }
}
