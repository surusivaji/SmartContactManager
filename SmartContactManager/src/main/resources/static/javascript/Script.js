//side toggle bar
const toggleSidebar = () => {
	if ($('.sidebar').is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}

};

//search name by contact api
const search = () => {
	let query = $("#search-input").val();

	if (query === '') {
		$(".search-result").hide();
	} else {
		let url = `http://localhost:8000/search/${query}`;
		fetch(url)
			.then((response) => response.json())
			.then((data) => {
				console.log(data);
				if (data.length > 0) {
					let text = `<div class='list-group'>`;
					data.forEach((contact) => {
						text += `<a href='/user/viewContact/${contact.id}' class='list-group-item list-group-item-action'>${contact.name}</a>`;
					});
					text += `</div>`;
					$(".search-result").html(text).show();
				} else {
					$(".search-result").html("<p>No results found.</p>").show();
				}
			})
			.catch((error) => {
				console.error("Error fetching search results:", error);
				$(".search-result").html("<p>No record found..</p>").show();
			});
	}
};


//password hide and show
$(".toggle-password").click(function() {
	const target = $($(this).data("target"));
	const icon = $(this).find("i");

	if (target.attr("type") === "password") {
		target.attr("type", "text");
		icon.removeClass("fa-eye").addClass("fa-eye-slash");
	} else {
		target.attr("type", "password");
		icon.removeClass("fa-eye-slash").addClass("fa-eye");
	}
});

//delete contact information using sweet alert
function deleteContact(id) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this contact!",
		icon: "error",
		buttons: {
			cancel: {
				text: "No",
				value: false,
				visible: true,
				className: "alert-cancel",
				closeModal: true,
			},
			confirm: {
				text: "Yes",
				value: true,
				visible: true,
				className: "alert-confirm",
				closeModal: true
			}
		},
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/user/deleteContact/" + id;
			}
		});
}


//delete favorite information using sweet alert
function deleteFavorite(id) {
	swal({
		title: "Are you sure?",
		text: "You want to remove from favorites!",
		icon: "error",
		buttons: {
			cancel: {
				text: "No",
				value: false,
				visible: true,
				className: "alert-cancel",
				closeModal: true,
			},
			confirm: {
				text: "Yes",
				value: true,
				visible: true,
				className: "alert-confirm",
				closeModal: true
			}
		},
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/user/removeFavorite/" + id;
			}
		});
}

//logout function using sweet alert
function logoutUser() {
	$(".sidebar .item").removeClass("active");
	$("#logout-link").addClass("active");

	swal({
		title: "Are you sure?",
		text: "Do you really want to logout?",
		icon: "warning",
		buttons: {
			cancel: {
				text: "No",
				value: false,
				visible: true,
				className: "alert-cancel",
				closeModal: true,
			},
			confirm: {
				text: "Yes",
				value: true,
				visible: true,
				className: "alert-confirm",
				closeModal: true
			}
		},
		dangerMode: true,
	})
		.then((willLogout) => {
			if (willLogout) {
				window.location = "/logout";
			} else {
				$("#logout-link").removeClass("active");
				window.location = "/user/home";
			}
		});
}