const toggleSidebar = ()=> {

    if ($('.sidebar').is(":visible")) {
        //close sidebar
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        //open sidebar
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }

};

const search = () => {
    let query = $("#search-input").val();

    if (query === '') {
        $(".search-result").hide();
    } else {
        let url = `http://localhost:8080/search/${query}`;
        fetch(url)
            .then((response) => response.json())
            .then((data) => {
                if (data.length > 0) {
                    let text = `<div class='list-group'>`;
                    data.forEach((contact) => {
                        text += `<a href='/user/viewcontact/${contact.id}' class='list-group-item list-group-item-action'>${contact.name}</a>`;
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
