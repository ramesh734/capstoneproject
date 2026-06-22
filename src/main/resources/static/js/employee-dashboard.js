function getAuth() {

    let u = localStorage.getItem("username");
    let p = localStorage.getItem("password");

    return "Basic " + btoa(u + ":" + p);
}

function loadDashboard() {

    let employeeId = localStorage.getItem("employeeId");
    let username = localStorage.getItem("username").toUpperCase();

    document.getElementById("employeeName").innerText = username;

    /* ========================
       ALLOCATIONS
    ========================= */
    fetch("/api/allocations", {
        headers: {
            Authorization: getAuth()
        }
    })
    .then(res => res.json())
    .then(data => {

        let allocations = Array.isArray(data) ? data : [];

        console.log("ALL ALLOCATIONS:", allocations);
        console.log("LOGGED EMPLOYEE ID:", employeeId);

        /* ✅ FIX: FILTER BY employeeId (NOT NAME) */
        let myAllocations = allocations.filter(a =>
            a.employeeId == employeeId
        );

        console.log("MY ALLOCATIONS:", myAllocations);

        // TOTAL ALLOCATED
        document.getElementById("assetCount").innerText =
            myAllocations.length;

        // ASSIGNED COUNT
        document.getElementById("allocCount").innerText =
            myAllocations.filter(a =>
                a.status === "ASSIGNED"
            ).length;

        let rows = "";

        myAllocations.forEach(a => {

            rows += `
            <tr>
                <td>${a.allocationId}</td>
                <td>${a.assetId}</td>
                <td>${a.status}</td>
                <td>${a.returnDate || "-"}</td>
            </tr>
            `;
        });

        document.querySelector("#allocTable tbody").innerHTML =
            rows;
    });

    /* ========================
       MAINTENANCE
    ========================= */
    fetch("/api/maintenance", {
        headers: {
            Authorization: getAuth()
        }
    })
    .then(res => res.json())
    .then(data => {

        let maintenance = Array.isArray(data) ? data : [];

        let myMaintenance = maintenance.filter(m =>
            m.employeeId == employeeId
        );

        document.getElementById("maintCount").innerText =
            myMaintenance.length;
    });
}

window.onload = loadDashboard;

function logout() {

    localStorage.clear();
    window.location.href = "../index.html";
}