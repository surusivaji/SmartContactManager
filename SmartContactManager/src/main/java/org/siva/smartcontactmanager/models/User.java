package org.siva.smartcontactmanager.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = {"contacts", "favorites"})
@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="Full_Name", length=40, nullable=false)
	private String  fullName;
	
	@Column(name="Email", length=40, nullable=false,  unique=true)
	private String email;
	
	@Column(name="Passowrd", length=300, nullable=false)
	private String password;
	
	@Column(name="Role", length=30, nullable=false)
	private String role;
	
	@Column(name="Enabled", nullable=false)
	private boolean enabled;
	
	@Column(name="User_Image", nullable=false)
	private String userImage;
	
	@Lob
	@Column(columnDefinition = "TEXT", nullable=false, length=2000)
	private String about;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();

}
