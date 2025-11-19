let fullName = document.getElementById("fullName");
let email = document.getElementById("email");
let password = document.getElementById("password");
let profileImage = document.getElementById("profileImage");
let about = document.getElementById("about");
let myForm = document.getElementById("myForm");

// Full Name Validation
fullName.addEventListener("input", function() {
	let fullNameValue = fullName.value;
	let namePattern = /^[A-Za-z\s]+$/;
	if (fullNameValue === "" || !namePattern.test(fullNameValue)) {
		fullName.style.border = "1px solid #FF0000";
	} else {
		fullName.style.border = "1px solid #ced4da";
	}
});

// Email Validation
email.addEventListener("input", function() {
	let emailValue = email.value;
	let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	if (emailValue === "" || !emailPattern.test(emailValue)) {
		email.style.border = "1px solid #FF0000";
	} else {
		email.style.border = "1px solid #ced4da";
	}
});

// Password Validation
password.addEventListener("input", function() {
	let passwordValue = password.value;
	let passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{6,}$/;
	if (passwordValue === "" || !passwordPattern.test(passwordValue)) {
		password.style.border = "1px solid #FF0000";
	} else {
		password.style.border = "1px solid #ced4da";
	}
});

// Profile Image Validation
profileImage.addEventListener("input", function() {
	let imageValue = profileImage.files[0];
	if (!imageValue) {
		profileImage.style.border = "1px solid #FF0000";
	} else {
		profileImage.style.border = "1px solid #ced4da";
	}
});

// About Validation
about.addEventListener("input", function() {
	let aboutValue = about.value.trim();
	if (aboutValue === "") {
		about.style.border = "1px solid #FF0000";
	} else {
		about.style.border = "1px solid #ced4da";
	}
});

myForm.addEventListener("submit", function(e) {
	e.preventDefault();
	let isValid = true;

	let fullNameValue = fullName.value;
	let namePattern = /^[A-Za-z\s]+$/;
	if (fullNameValue === "" || !namePattern.test(fullNameValue)) {
		fullName.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		fullName.style.border = "1px solid #ced4da";
	}

	let emailValue = email.value;
	let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	if (emailValue === "" || !emailPattern.test(emailValue)) {
		email.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		email.style.border = "1px solid #ced4da";
	}

	let passwordValue = password.value;
	let passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).{6,}$/;
	if (passwordValue === "" || !passwordPattern.test(passwordValue)) {
		password.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		password.style.border = "1px solid #ced4da";
	}

	let imageValue = profileImage.files[0];
	if (!imageValue) {
		profileImage.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		profileImage.style.border = "1px solid #ced4da";
	}

	let aboutValue = about.value.trim();
	if (aboutValue === "") {
		about.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		about.style.border = "1px solid #ced4da";
	}
	
	if (isValid) {
		this.submit();
	}

})