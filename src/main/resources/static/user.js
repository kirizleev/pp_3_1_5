$(document).ready(async function () {
    await fetch("http://localhost:8080/api/user")
        .then((resp) => resp.json())
        .then(function (data) {
            $("#userId").text(data.id);
            $("#userFirstName").text(data.firstName);
            $("#userLastName").text(data.lastName);
            $("#userEmail").text(data.email);
            $("#userRole").text(data.roles[0].name.replace("ROLE_", ""));
            $("#nameSpan").text(data.email);
            $("#rolesSpan").text(` with roles: ${data.roles[0].name.replace("ROLE_", "")}`);
        })
})

