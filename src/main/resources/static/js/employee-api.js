let editId = null;

/* AUTH */
function getAuth() {
    let u = localStorage.getItem("username");
    let p = localStorage.getItem("password");
    return "Basic " + btoa(u + ":" + p);
}

function showMessage(text, type) {
    let el = document.getElementById("message");
    if (!el) return;
    el.textContent = text;
    el.className = "message " + (type || "error");
    el.style.display = "block";
    setTimeout(() => { el.style.display = "none"; }, 5000);
}

function showModalMessage(text, type) {
    let el = document.getElementById("modalMessage");
    if (!el) return;
    el.textContent = text;
    el.className = "message " + (type || "error");
    el.style.display = "block";
}

/* LOAD EMPLOYEES */
function loadEmployees() {

    fetch("/api/employees", {
        headers: { Authorization: getAuth() }
    })
        .then(res => res.json())
        .then(data => {

            let table = "";

            data.forEach(emp => {

                table += `
<tr>
<td>${emp.employeeId}</td>
<td>${emp.employeeName}</td>
<td>${emp.email}</td>
<td>${emp.department}</td>
<td>${emp.username}</td>
<td>${emp.role}</td>

<td>
<button onclick="editEmployee(${emp.employeeId})">Edit</button>
<button onclick="deleteEmp(${emp.employeeId})">Delete</button>
</td>

</tr>
`;

            });

            document.querySelector("#empTable tbody").innerHTML = table;

        });

}

window.onload = loadEmployees;

/* OPEN FORM */
function openForm() {
    document.getElementById("employeeForm").style.display = "block";
    let mm = document.getElementById("modalMessage");
    if (mm) { mm.style.display = "none"; mm.textContent = ""; }
}

/* CLOSE FORM */
function closeForm() {
    document.getElementById("employeeForm").style.display = "none";
    clearForm();
}

/* CLEAR FORM */
function clearForm() {
    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("dept").value = "";
    document.getElementById("username").value = "";
    document.getElementById("password").value = "";
    document.getElementById("role").value = "ROLE_EMPLOYEE";

    editId = null;
    document.getElementById("formTitle").innerText = "Add Employee";
}

/* SAVE OR UPDATE */
function saveEmployee() {

    let emp = {
        employeeName: document.getElementById("name").value,
        email: document.getElementById("email").value,
        department: document.getElementById("dept").value,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        role: document.getElementById("role").value
    };

    let url = "/api/employees";
    let method = "POST";

    if (editId != null) {
        url = "/api/employees/" + editId;
        method = "PUT";
    }

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
            Authorization: getAuth()
        },
        body: JSON.stringify(emp)
    })
	.then(async (res) => {

	    if (!res.ok) {
	        let contentType = res.headers.get("Content-Type") || "";
	        if (contentType.includes("json")) {
	            let errJson = await res.json();
	            let msgs = Object.values(errJson);
	            throw new Error(msgs.join(" | "));
	        } else {
	            let text = await res.text();
	            throw new Error(text);
	        }
	    }

	    return res.json();
	})
        .then(() => {

            showMessage(editId ? "Employee Updated Successfully" : "Employee Added Successfully", "success");

            closeForm();
            loadEmployees();
        })
        .catch(err => {
            showModalMessage(err.message, "error");
        });
}

/* EDIT EMPLOYEE */
function editEmployee(id) {

    fetch("/api/employees/" + id, {
        headers: { Authorization: getAuth() }
    })
        .then(res => res.json())
        .then(emp => {

            editId = id;

            document.getElementById("name").value = emp.employeeName;
            document.getElementById("email").value = emp.email;
            document.getElementById("dept").value = emp.department;
            document.getElementById("username").value = emp.username;
            document.getElementById("password").value = emp.password;
            document.getElementById("role").value = emp.role;

            document.getElementById("formTitle").innerText = "Update Employee";

            openForm();

        });

}

/* DELETE */
function deleteEmp(id) {

    if (!confirm("Are you sure you want to delete this employee?")) return;

    fetch("/api/employees/" + id, {
        method: "DELETE",
        headers: { Authorization: getAuth() }
    })
        .then(async res => {
            if (!res.ok) {
                let err = await res.text();
                throw new Error(err);
            }
            showMessage("Employee deleted successfully", "success");
            loadEmployees();
        })
        .catch(err => {
            showMessage("Error: " + err.message, "error");
        });

}

/* LOGOUT */
function logout() {
    localStorage.clear();
    window.location.href = "../index.html";
}
