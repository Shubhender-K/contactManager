console.log("script file");

const toggleSidebar = () => {
	if ($('.sidebar').is(":visible")) {

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");

	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
}

function deleteContact(cId) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this contact !!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/user/delete/" + cId;


			} else {
				swal("Your contact is safe!");
			}
		});
}

const search = () => {

	let query = $("#search-input").val();
	if (query == '') {
		$(".search-result").hide();
	}
	else {
		let url = `http://localhost:8080/search/${query}`;

		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {

			let text = `<div class='list-group'>`

			for (let i = 0; i < data.length; i++) {
				text += `<a href="/user/contact/${data[i].cId}" class='list-group-item list-group-item-action'>${data[i].cName}</a>`;
			}

			text += `</div>`

			$(".search-result").html(text);
			$(".search-result").show();
		})




	}


}



