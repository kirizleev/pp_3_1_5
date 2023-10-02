function getRoles(user) {
    let roles = "";
    for (const role of user.roles) {
        roles += role.name.replace("ROLE_", "") + " "
    }
    return roles;
}

async function printUsers() {
    await fetch("http://localhost:8080/api/users")
        .then(resp => resp.json())
        .then(function (data) {
            $("#usersTableBody").html("");
            for (const dataElement of data) {
                let userRow = $("<tr>")
                userRow.addClass("font-weight-normal");
                userRow.append($("<td>").text(dataElement.id));
                userRow.append($("<td>").text(dataElement.firstName));
                userRow.append($("<td>").text(dataElement.lastName));
                userRow.append($("<td>").text(dataElement.email));

                userRow.append($("<td>").text(getRoles(dataElement)));
                userRow.append($("<td>").append($("<button>").text("Edit")
                    .addClass("btn btn-info")
                    .attr("data-target", "#jssModal")
                    .attr("data-user", "edit_" + dataElement.id)
                    .attr("data-toggle", "modal")));
                userRow.append($("<td>").append($("<button>").text("Delete")
                    .addClass("btn btn-danger")
                    .attr("data-target", "#jssModal")
                    .attr("data-user", "del_" + dataElement.id)
                    .attr("data-toggle", "modal")));

                $("#usersTableBody").append(userRow);
            }
        })
}


$("#jssModal").on('show.bs.modal', async function (event) {
    let button = $(event.relatedTarget);
    let buttonData = button.data("user");
    let modal = $(this);


    let action = buttonData.split("_")[0];
    let id = buttonData.split("_")[1];

    let user = await fetch(`http://localhost:8080/api/users/${id}`)
        .then(resp => resp.json());

    let formAction;
    let isDisabled;
    let submitButtonStyle;

    if (action === "edit") {
        formAction = "Edit";
        isDisabled = "";
        submitButtonStyle = "btn-primary";
    } else {
        formAction = "Delete";
        isDisabled = "disabled";
        submitButtonStyle = "btn-danger";
    }

    let modalBody = `
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="jsModalLabel">${formAction} user</h5>
                <button type="button" class="close"
                        data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body text-center">
                <div class="form-group">
                    <label for="id" class="mb-0 font-weight-bold">ID</label>
                    <input value="${user.id}"
                           class="form-control w-50 p-3 mx-auto"
                           id="id" placeholder="ID" disabled>
                </div>
                <div class="form-group">
                    <label for="firstName"
                           class="mb-0 font-weight-bold">First name</label>
                    <input value="${user.firstName}"
                           type="text"
                           class="form-control w-50 p-3 mx-auto"
                           id="firstName"
                           placeholder="First name" ${isDisabled}>
                </div>
                <div class="form-group">
                    <label for="lastName"
                           class="mb-0 font-weight-bold">Last name</label>
                    <input value="${user.lastName}"
                           type="text"
                           class="form-control w-50 p-3 mx-auto"
                           id="lastName"
                           placeholder="Last name" ${isDisabled}>
                </div>
                <div class="form-group">
                    <label for="email"
                           class="mb-0 font-weight-bold">Email</label>
                    <input value="${user.email}"
                           type="email"
                           class="form-control w-50 p-3 mx-auto"
                           id="email"
                           placeholder="Email" ${isDisabled}>
                </div>
                <div class="form-group">
                    <label for="password"
                           class="mb-0 font-weight-bold">Password</label>
                    <input type="password"
                           class="form-control w-50 p-3 mx-auto"
                           id="password"
                           placeholder="Password" ${isDisabled}>
                </div>
                <div class="form-group">
                    <label for="role"
                           class="mb-0 font-weight-bold">Role</label>
                    <select multiple
                            id="role"
                            class="custom-select d-block w-50 p-3 mx-auto"
                            style="height: 75px" ${isDisabled}>
                        <option value="1" selected>USER
                        </option>
                        <option value="2">ADMIN
                        </option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary"
                        data-dismiss="modal">Close
                </button>
                <button id="${formAction}" type="submit" class="btn ${submitButtonStyle}">
                    ${formAction}
                </button>
            </div>
        </div>
    `

    modal.find("#appendDiv").append(modalBody);

    $("#Edit").on("click", async function (e) {
        e.preventDefault();

        let id = modal.find("#id").val();
        let firstName = modal.find("#firstName").val();
        let lastName = modal.find("#lastName").val();
        let email = modal.find("#email").val();
        let password = modal.find("#password").val();
        let roles = modal.find("#role").val();

        let data = {
            id: id,
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            roles: roles
        }

        const editFetch = await fetch(
            `http://localhost:8080/api/edit_${id}`,
            {
                method: "PUT",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json;charset=utf-8",
                    "Referer": null
                },
                body: JSON.stringify(data)
            }
        );


        if (editFetch.ok) {
            await printUsers();
            modal.modal('hide');
        }
    })

    $("#Delete").on("click", async function (e) {
        e.preventDefault();

        let id = modal.find("#id").val();

        const editFetch = await fetch(
            `http://localhost:8080/api/delete_${id}`,
            {
                method: "DELETE",
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json;charset=utf-8",
                    "Referer": null
                },
                body: JSON.stringify(id)
            }
        );


        if (editFetch.ok) {
            await printUsers();
            modal.modal('hide');
        }
    })

}).on("hidden.bs.modal", (e) => {
    let modal = $(e.target);
    modal.find("#appendDiv").html("");
})

$("#addUser").on("click", async function (e) {
    e.preventDefault();

    let form = $("#addUserForm");

    let firstName = form.find("#firstName").val();
    let lastName = form.find("#lastName").val();
    let email = form.find("#email").val();
    let password = form.find("#password").val();
    let roles = form.find("#role").val();

    let data = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password,
        roles: roles
    }

    const editFetch = await fetch(
        `http://localhost:8080/api/new`,
        {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json;charset=utf-8",
                "Referer": null
            },
            body: JSON.stringify(data)
        }
    );

    if (editFetch.ok) {
        await printUsers();
        form.trigger("reset");
        $("#new-user").removeClass("active");
        $("#users_list").addClass("active");
    }
})

async function userInfo() {
    await fetch("http://localhost:8080/api/user")
        .then(resp => resp.json())
        .then(function (user) {
            $("#nameSpan").text(user.email);
            $("#rolesSpan").text(` with roles: ${getRoles(user)}`);
        })
}

$(document).ready(printUsers(), userInfo());
