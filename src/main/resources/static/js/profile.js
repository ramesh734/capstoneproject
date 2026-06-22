function getAuth() {

    let u = localStorage.getItem("username");
    let p = localStorage.getItem("password");

    return "Basic " + btoa(u + ":" + p);
}

function loadProfile() {

    let username = localStorage.getItem("username");

    document.getElementById("employeeName").innerText = username;

    fetch("/api/employees", {
        headers: {
            Authorization: getAuth()
        }
    })
    .then(res => res.json())
    .then(data => {

        let list = Array.isArray(data) ? data : [];

        let user = list.find(e =>
            e.username === username
        );

        if (user) {

            document.getElementById("name").innerText = user.employeeName;
            document.getElementById("username").innerText = user.username;
            document.getElementById("email").innerText = user.email;
            document.getElementById("department").innerText = user.department;
            document.getElementById("role").innerText = user.role;

            // Top section
            document.getElementById("nameTop").innerText = user.employeeName;
            document.getElementById("roleTop").innerText = user.role.replace("ROLE_", "");

            // Avatar initial
            document.getElementById("avatar").innerText =
                user.employeeName.charAt(0).toUpperCase();
        }
    });
}

window.onload = loadProfile;

function logout() {
    localStorage.clear();
    window.location.href = "../index.html";
}