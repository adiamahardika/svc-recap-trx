package trilogi.myg.svc.scheduler.transaction.log.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "ms_regional_revass")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegionalRevass implements Serializable{
	
	private static final long serialVersionUID = 6758854600043262620L;

	@GenericGenerator(
        name = "msRegionalRevassIdSeqGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
    		@org.hibernate.annotations.Parameter(name = "sequence_name", value = "msRegionalRevassIdSeq"),
            @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
            @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
        }
    )

    @Id
    @GeneratedValue(generator = "msRegionalRevassIdSeqGenerator")
    @JsonIgnore
    private Long id;
	
	@Column(name = "regional_id")
	private String regionalId;
	
	@Column(name = "regional")
	private String regional;
	
	@Column(name = "bank_code")
	private String bankCode;
	
	@Column(name = "bank_name")
	private String bankName;
	
	@Column(name = "bank_code_api")
	private String bankCodeApi;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegionalId() {
		return regionalId;
	}

	public void setRegionalId(String regionalId) {
		this.regionalId = regionalId;
	}

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCodeApi() {
		return bankCodeApi;
	}

	public void setBankCodeApi(String bankCodeApi) {
		this.bankCodeApi = bankCodeApi;
	}
	
}
