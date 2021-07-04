package br.dev.charles.challenge.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sun.istack.NotNull;

import br.dev.charles.challenge.utils.StringUtils;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable{

	private static final long serialVersionUID = 5026054760898765779L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy =  "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@Column(name = "email", nullable = false, unique = true)
	@NotNull @NotEmpty @NotBlank
	private String email;
	
	@NotNull @NotEmpty @NotBlank
	private String name;
	
	@NotNull @NotEmpty @NotBlank
	private String password;
	
	@CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
	private Date created;
	
	@LastModifiedDate
    @Column(name = "modified")
	private Date modified;
	
	@CreatedDate
	@Column(name = "last_login", nullable = false)
	private Date last_login;
	private String token;
	
	@Embedded
	@ElementCollection
	private List<UserPhone> phones = new ArrayList<>();
	
	public User() {
		
	}
	
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getLast_login() {
		return last_login;
	}
	
	public void setLast_login(Date lastLogin) {
		this.last_login = lastLogin;
	}
	
	@NotNull
	public String getToken() {
		return token;
	}

	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}
	
	public List<UserPhone> getPhones() {
		return phones;
	}
	
	public void setPhones(List<UserPhone> phones) {
		this.phones = phones;
	}
	
	@PrePersist
	@PreUpdate
	private void encapsulateToken() {
		if (token == null) {
			token = UUID.randomUUID().toString();
			password = StringUtils.sha256(password);
		}
	}
	
	public void updateToken() {
		token = UUID.randomUUID().toString();
		last_login = new Date();
	}
}
