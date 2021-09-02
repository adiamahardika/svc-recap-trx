package trilogi.myg.svc.scheduler.transaction.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "lg_payment")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LgPayment {
    @GenericGenerator(
        name = "lgPaymentIdSeqGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "lgPaymentIdSeq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
        }
    )

    @Id
    @GeneratedValue(generator = "lgPaymentIdSeqGenerator")
    @JsonIgnore
    private Long id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "no_hp")
    private String noHp;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "submit_amount")
    private Double submitAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "status")
    private String status;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "ecr")
    private String ecr;

    @Column(name = "fa_account_id")
    private String faAccountId;

    @JsonIgnore
    @Column(name = "rec_status", insertable = false)
    private String recStatus;

    @JsonIgnore
    @Column(name = "created_by", insertable=false, updatable=false, columnDefinition = "varchar default 'SYSTEM'")
    private String createdBy;

    @JsonIgnore
    @Column(name = "created_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp createdDate;

    @JsonIgnore
    @Column(name = "updated_by")
    private String updatedBy;

    @JsonIgnore
    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name="created_by", referencedColumnName = "terminal_id")
    private Terminal terminal;

    public LgPayment() {
        this.recStatus = "A";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getSubmitAmount() {
        return submitAmount;
    }

    public void setSubmitAmount(Double submitAmount) {
        this.submitAmount = submitAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getEcr() {
        return ecr;
    }

    public void setEcr(String ecr) {
        this.ecr = ecr;
    }

    public String getFaAccountId() {
        return faAccountId;
    }

    public void setFaAccountId(String faAccountId) {
        this.faAccountId = faAccountId;
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

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
