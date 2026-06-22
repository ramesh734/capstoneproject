function login() {

    let username =
        document.getElementById("username").value;

    let password =
        document.getElementById("password").value;

    let role =
        document.getElementById("role").value;

    if(username === "" || password === "") {

        document.getElementById("message")
            .innerHTML =
            "Enter Username and Password";

        return;
    }

    fetch("/auth/login", {

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify({

            username:username,
            password:password,
            role:role

        })

    })

    .then(response => {

        if(!response.ok){
            throw new Error("Invalid Credentials");
        }

        return response.json();
    })

    .then(data => {

        localStorage.setItem(
            "username",
            username
        );

        localStorage.setItem(
            "password",
            password
        );
		localStorage.setItem(
			"employeeId",
			data.employeeId
		
		);

        localStorage.setItem(
            "role",
            data.role
        );

        if(data.role === "ROLE_ADMIN") {

            window.location.href =
                "admin/admin-dashboard.html";
        }
        else {

            window.location.href =
                "employee/employee-dashboard.html";
        }

    })

    .catch(error => {

        document.getElementById("message")
            .innerHTML =
            "Invalid Username / Password / Role";
    });
}