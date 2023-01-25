function validateForm() {
	//error fields
	let throwErrorField = [];
	//document form
	let studentForm = document.forms["studentForm"];
	let firstName = studentForm["firstName"].value.trim();
	let lastName = studentForm["lastName"].value.trim();
	let email = studentForm["email"].value.trim();
	if (firstName == "") {
		throwErrorField.push("First Name");
	} else if (lastName == "") {
		throwErrorField.push("Last Name");
	} else if (email == "") {
		throwErrorField.push("Email");
	}

	if (throwErrorField.length > 0) {
		alert("Form validation failed. Please add data for following field: " + throwErrorField);
		return false;
	}

}