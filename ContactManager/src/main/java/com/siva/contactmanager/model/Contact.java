package com.siva.contactmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name="Contact")
public class Contact {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
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
	@Column(name="description", nullable=false, length=5000)
	private String description;
	@ManyToOne
	@JsonIgnore
	private User user;

}
