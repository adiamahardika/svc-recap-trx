package trilogi.myg.svc.scheduler.transaction.log.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ms_terminal")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Terminal implements Serializable {

    private static final long serialVersionUID = 5749138602362605446L;

	@GenericGenerator(
        name = "msTerminalIdSeqGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "msTerminalIdSeq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
        }
    )

    @Id
    @GeneratedValue(generator = "msTerminalIdSeqGenerator")
    @JsonIgnore
    private Long id;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "terminal_name")
    private String terminalName;

    @Column(name = "area")
    private String area;

    @Column(name = "regional")
    private String regional;

    @Column(name = "regional_code")
    private String regionalCode;

    @Column(name = "ctp_type")
    private String ctpType;

    @Column(name = "terminal_location")
    private String terminalLocation;

    @Column(name = "kecamatan")
    private String kecamatan;

    @Column(name = "kota_kabupaten")
    private String kotaKabupaten;

    @Column(name = "kode_pos")
    private String kodePos;

    @Column(name = "pic")
    private String pic;

    @Column(name = "kontak_pic")
    private String kontakPic;

    @JsonIgnore
    @Column(name = "status")
    private String status;

    @JsonIgnore
    @Column(name = "rec_status")
    private String recStatus;

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "created_date")
    private Timestamp createdDate;

    @JsonIgnore
    @Column(name = "updated_by")
    private String updatedBy;

    @JsonIgnore
    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "regional_code", referencedColumnName = "regional_id", insertable = false, updatable = false)
    @JsonIgnore
    private RegionalRevass revass;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public String getCtpType() {
        return ctpType;
    }

    public void setCtpType(String ctpType) {
        this.ctpType = ctpType;
    }

    public String getTerminalLocation() {
        return terminalLocation;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKotaKabupaten() {
        return kotaKabupaten;
    }

    public void setKotaKabupaten(String kotaKabupaten) {
        this.kotaKabupaten = kotaKabupaten;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKontakPic() {
        return kontakPic;
    }

    public void setKontakPic(String kontakPic) {
        this.kontakPic = kontakPic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(String recStatus) {
        this.recStatus = recStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getRegionalCode() {
        return regionalCode;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode;
    }

	public RegionalRevass getRevass() {
		return revass;
	}

	public void setRevass(RegionalRevass revass) {
		this.revass = revass;
	}
    
}
