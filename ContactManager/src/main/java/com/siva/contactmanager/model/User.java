package com.siva.contactmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name="Users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="name", length = 40, nullable = false)
	private String name;
	@Column(name="email", length = 50, unique = true, nullable = false)
	private String email;
	@Column(name="password", length=40, nullable = false)
	private String password;
	@Column(name="role", length=40, nullable = false)
	private String role;
	@Column(name="enabled", nullable = false)
	private Boolean enabled;
	@Column(name="imageurl", nullable = false)
	private String imageUrl;
	@Column(name="about", nullable = false, length=1000)
	private String about;

}
