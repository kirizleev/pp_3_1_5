function getRoles(user) {
    let roles = "";
    for (const role of user.roles) {
        roles += role.name.replace("ROLE_", "") + " "
    }
    return roles;
}

$(document).ready(async function () {
    await fetch("http://localhost:8080/api/user")
        .then((resp) => resp.json())
        .then(function (data) {
            $("#userId").text(data.id);
            $("#userFirstName").text(data.firstName);
            $("#userLastName").text(data.lastName);
            $("#userEmail").text(data.email);
            $("#userRole").text(getRoles(data));
            $("#nameSpan").text(data.email);
            $("#rolesSpan").text(` with roles: ${getRoles(data)}`);
        })
})

