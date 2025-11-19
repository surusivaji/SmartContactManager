let fname = document.getElementById("name");
let secondName = document.getElementById("secondName");
let phone = document.getElementById("phone");
let email = document.getElementById("email");
let work = document.getElementById("work");
let description = document.getElementById("description");
let profile = document.getElementById("profileImage");
let myForm = document.getElementById("myForm");

// Name Validation
fname.addEventListener("input", function() {
	let fullNameValue = fname.value;
	let namePattern = /^[A-Za-z\s]+$/;
	if (fullNameValue === "" || !namePattern.test(fullNameValue)) {
		fname.style.border = "1px solid #FF0000";
	} else {
		fname.style.border = "1px solid #ced4da";
	}
});

//second name validation
secondName.addEventListener("input", function() {
	let secondNameValue = secondName.value;
	let namePattern = /^[A-Za-z\s]+$/;
	if (secondNameValue === "" || !namePattern.test(secondNameValue)) {
		secondName.style.border = "1px solid #FF0000";
	} else {
		secondName.style.border = "1px solid #ced4da";
	}
});

//mobile number validation
phone.addEventListener("input", function() {
	let mobileNumberValue = phone.value;
	let mobilePattern = /^\d{10}$/;
	if (mobileNumberValue === "") {
		phone.style.border = "1px solid #FF0000";
	}
	else if (!mobilePattern.test(mobileNumberValue)) {
		phone.style.border = "1px solid #FF0000";
	}
	else {
		phone.style.border = "1px solid #ced4da";
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

//Work Validation
work.addEventListener("input", function() {
	let workValue = work.value;
	if (workValue === "") {
		work.style.border = "1px solid #FF0000";
	}
	else {
		work.style.border = "1px solid #ced4da";
	}
})

//Description Validation
description.addEventListener("input", function() {
	let descriptionValue = description.value;
	if (descriptionValue === "") {
		description.style.border = "1px solid #FF0000";
	}
	else {
		description.style.border = "1px solid #ced4da";
	}
})

//Profile Image Validation
profile.addEventListener("input", function() {
	let profileImage = profile.files[0];
	if (!profileImage) {
		profile.style.border = "1px solid #FF0000";
	}
	else {
		profile.style.border = "1px solid #ced4da";
	}
})

myForm.addEventListener("submit", function(e) {
	e.preventDefault();
	
	let fullNameValue = fname.value;
	let secondNameValue = secondName.value;
	let mobileNumberValue = phone.value;
	let emailValue = email.value;
	let workValue = work.value;
	let descriptionValue = description.value;
	let profileImage = profile.files[0];
	
	let isValid = true;	
	 
	let namePattern = /^[A-Za-z\s]+$/;
	let mobilePattern = /^\d{10}$/;
	let emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
	
	//name validation
	if (fullNameValue === "" || !namePattern.test(fullNameValue)) {
		fname.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		fname.style.border = "1px solid #ced4da";
	}
	
	//second name validation
	if (secondNameValue === "" || !namePattern.test(secondNameValue)) {
		secondName.style.border = "1px solid #FF0000";
		isValidate = false;
	} else {
		secondName.style.border = "1px solid #ced4da";
	}
	
	//mobile number validation
	if (mobileNumberValue === "" || !mobilePattern.test(mobileNumberValue)) {
		phone.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		phone.style.border = "1px solid #ced4da";
	}

	//email validation
	if (emailValue === "" || !emailPattern.test(emailValue)) {
		email.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		email.style.border = "1px solid #ced4da";
	}
	
	//work validation
	if (workValue === "") {
		work.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		work.style.border = "1px solid #ced4da";
	}
	
	//description validation
	if (descriptionValue === "") {
		description.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		description.style.border = "1px solid #ced4da";
	}
	
	//image validation
	if (!profileImage) {
		profile.style.border = "1px solid #FF0000";
		isValid = false;
	} else {
		profile.style.border = "1px solid #ced4da";
	}
	
	if (isValid) {
		this.submit();
	}
	
		
})