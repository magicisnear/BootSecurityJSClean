$(async function () {
    await getTableWithUsers(); // вызвать метод создания таблицы всех юзеров
    getDefaultModal();
    addNewUser();
})

const userFetchService = {
    head: {
        'Accept': 'application/json', 'Content-Type': 'application/json', 'Referer': null
    }, // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('api/users'),
    findOneUser: async (id) => await fetch(`api/users/${id}`),

    addNewUser: async (user) => await fetch('api/users', {
        method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)
    }),

    updateUser: async (user, id) => await fetch(`api/users/${id}`,
        { method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)
    }),

    deleteUser: async (id) => await fetch(`api/users/${id}`,
        { method: 'DELETE', headers: userFetchService.head})
}

async function getTableWithUsers() {
    let table = $('#mainTableWithUsers tbody'); // указываем id таблицы (их может быть много) и место куда будем добавлять
    table.empty(); // предварительно чистим таблицу

    await userFetchService.findAllUsers() // вызываем fetch метод findAllUsers
        .then(res => res.json()) // получаем response - ответ и переводим его в json
        .then(users => { // после перевода в json получаем юзеров наших
            users.forEach(user => { // каждого юзера по отдельности вписываем в таблицу таким образом и формируем кнопки
                let tableFilling = `$( 
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>   
                            <td>${(user.roles).map(role => role.name)}</td>   
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-primary" 
                                data-toggle="modal" data-target="#someDefaultModal"> Edit </button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#someDefaultModal"> Delete </button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling); // добавляем получившуюся колонку в таблицу
            })
        })

    // обрабатываем нажатие на любую из кнопок edit или delete
    // достаем из нее данные и отдаем модалке, которую к тому же открываем
    $("#mainTableWithUsers").find('button').on('click', (event) => { // жмём на кнопку
        let defaultModal = $('#someDefaultModal'); // начинаем формирование
        // открытия нужного модального окна, указываем нужный id модального окна

        let targetButton = $(event.target); // получаем ту кнопку которую мы нажали
        let buttonUserId = targetButton.attr('data-userid'); // получаем id из кнопки и сохраняем
        let buttonAction = targetButton.attr('data-action'); // получаем действие из кнопки и сохраняем

        defaultModal.attr('data-userid', buttonUserId); // добавляем id в модальное окно
        defaultModal.attr('data-action', buttonAction); // добавляем действие в модальное окно
        defaultModal.modal('show'); // открываем модальное окно
    })
}


// что то деалем при открытии модалки и при закрытии
// основываясь на ее дата атрибутах
async function getDefaultModal() {
    $('#someDefaultModal').modal({
        keyboard: true, backdrop: "static", show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}


// редактируем юзера из модалки редактирования, забираем данные, отправляем
async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-outline-success" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                
                <label for="id" class="col-form-label">ID</label>
                <input type="text" class="form-control" id="id" name="id" value="${user.id}" disabled><br>
                
                <label for="Username" class="col-form-label">Username</label>
                <input class="form-control" type="text" id="name" value="${user.name}"><br>
                
                <label for="lastName" class="col-form-label">LastName</label>
                <input class="form-control" type="text" id="lastName" value="${user.lastName}"><br>
                
                <label for="age" class="col-form-label">Age</label>
                <input class="form-control" type="text" id="age" value="${user.age}"><br>
                
                <label for="email" class="col-form-label">Email</label>
                <input class="form-control" type="text" id="email" value="${user.email}"><br>
                
                <label for="password" class="col-form-label">Password</label>
                <input class="form-control" type="password" id="password"><br>
                
                <label for="roles" class="col-form-label">Role</label>
                <select class="form-control selectpicker" name="roleID" id="roleID">
                 <option value="" disabled hidden>Select value</option>
                 <option value="1">ROLE_USER</option>
                 <option value="2">ROLE_ADMIN</option>
                </select>

            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#editButton").on('click', async () => {
        let id = modal.find("#id").val().trim();
        let name = modal.find("#name").val().trim();
        let lastName = modal.find("#lastName").val().trim();
        let age = modal.find("#age").val().trim();
        let email = modal.find("#email").val().trim();
        let password = modal.find("#password").val().trim();
        let roleID = modal.find("#roleID").val().trim();

        let data = {
            id: id,
            name: name,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: [{id: roleID}]
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

async function deleteUser(modal, id) {
    let preuser1 = await userFetchService.findOneUser(id);
    let user = preuser1.json();

    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-danger" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                
                <label for="id" class="col-form-label">ID</label>
                <input type="text" class="form-control" id="id" name="id" value="${user.id}" disabled><br>
                
                <label for="Username" class="col-form-label">Username</label>
                <input class="form-control" type="text" id="name" value="${user.name}" disabled><br>
                
                <label for="lastName" class="col-form-label">LastName</label>
                <input class="form-control" type="text" id="lastName" value="${user.lastName}" disabled><br>
                
                <label for="age" class="col-form-label">Age</label>
                <input class="form-control" type="text" id="age" value="${user.age}" disabled><br>
                
                 <label for="email" class="col-form-label">Email</label>
                <input class="form-control" type="text" id="email" value="${user.email}" disabled><br>
                
                <label for="password" class="col-form-label">Password</label>
                <input class="form-control" type="text" id="text" value="${user.password}" disabled><br>
    
                <label for="role" class="col-form-label">Role</label>
                <input class="form-control" type="text" id="role" value="${(user.roles).map(role => role.name)}" disabled><br>
    
           </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#deleteButton").on('click', async () => {
         const response = await userFetchService.deleteUser(id);
        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

async function addNewUser() {
    $('#addNewUserButton').click(async () => {

        let addUserForm = $('#defaultSomeForm')
        let id = addUserForm.find("#id").val().trim();
        let name = addUserForm.find("#name").val().trim();
        let lastName = addUserForm.find("#lastName").val().trim();
        let age = addUserForm.find("#age").val().trim();
        let email = addUserForm.find("#email").val().trim();
        let password = addUserForm.find("#password").val().trim();
        let roleID = addUserForm.find("#roleID").val().trim();

        let data = {
            id: id,
            name: name,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: [{id: roleID}]
        }

        const response = await userFetchService.addNewUser(data);
        if (response.ok) {
            getTableWithUsers();
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert)
        }
    })
}