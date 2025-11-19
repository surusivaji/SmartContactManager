package org.siva.smartcontactmanager.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Contacts")
public class Contact {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name", length=40, nullable=false)
	private String name;
	
	@Column(name="secondname", length=40, nullable=false)
	private String secondName;
	
	@Column(name="work", length=50, nullable=false)
	private String work;
	
	@Column(name="email", length=50, nullable=false)
	private String email;
	
	@Column(name="phone", length=10, nullable=false)
	private String phone;
	
	@Column(name="Image", nullable=false)
	private String image;
	
	@Lob
	@Column(columnDefinition = "TEXT", nullable=false, length=2000)
	private String description;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

}
